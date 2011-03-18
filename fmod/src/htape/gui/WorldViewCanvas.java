package htape.gui;

import htape.World;
import htape.geometry.*;
import htape.geometry.Point;
import htape.util.filtering.hrtf.HRIR;
import htape.util.filtering.hrtf.HRTF;
import htape.util.filtering.hrtf.IHRTF;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

/**
 * Created by ben, on 3/17/11 at 12:45 PM
 */
public class WorldViewCanvas extends JPanel {

    private World world;
    private Point[][] positions;
    private IHRTF hrtf = null;

    public WorldViewCanvas(World world) {
        this.world = world;
    }



    @Override
    protected void paintComponent(Graphics g) {

        if (hrtf != world.getHRTF()) {
            hrtf = world.getHRTF();
            createPositions();
        }



        int width = getWidth();
        int height = getHeight();
        int o_x = width / 2;
        int o_y = height / 2;

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.LIGHT_GRAY);

        Point[][] projections = new Point[positions.length][];
        Camera camera = world.getCamera();
        double f = camera.getFocalLength();

        htape.geometry.Point u = camera.getU(), v = camera.getV(), w = camera.getW(), c = camera.getPos();

        Matrix m = new Matrix(new double[][]{
                {u.getX(),  u.getY(),   u.getZ(),   -c.dot(u)},
                {v.getX(),  v.getY(),   v.getZ(),   -c.dot(v)},
                {w.getX(),  w.getY(),   w.getZ(),   -c.dot(w)},
                {0,0,0,1},
        });

        drawOrigin(g, f, m, o_x, o_y);



        for (int i = 0; i < positions.length; i++) {
            Point[] positionRow = positions[i];
            projections[i] = new Point[positionRow.length];
            for (int j = 0; j < positionRow.length; j++) {
                Point pos = positionRow[j];

                Point coord = pos.dup();
                coord.setX(f*coord.getX() / coord.getZ());
                coord.setY(f*coord.getY() / coord.getZ());
                projections[i][j] = new Matrix(new double[][]{
                    {1,0,0,o_x},
                    {0,1,0,o_y},
                    {0,0,1,0},
                    {0,0,0,1}
                }).mult(coord).toPoint();

            }
        }

        //draw projections on z-plane
        for (int i = 0; i < projections.length; i++) {
            Point[] projectionRow = projections[i];
            Point prev = projectionRow[projectionRow.length-1];
            for (int j = 0; j < projectionRow.length; j++) {
                Point point = projectionRow[j];

                if (prev.getZ() > 0){
                    if (point.getZ() > 0) {
                        g.drawLine((int) (prev.getX()), (int) (prev.getY()), (int) (point.getX()), (int) (point.getY()));
                        if (i + 1 < projections.length) {
                            Point neighbour = projections[i+1][j];
                            if (neighbour.getZ() > 0) {
                                g.drawLine((int)(neighbour.getX()), (int)(neighbour.getY()), (int)(point.getX()), (int)(point.getY()));
                            }
                        }
                    }
                    g.drawRect((int)prev.getX()-1, (int) prev.getY()-1, 2,2);


                }


                prev = point;
            }
            g.drawRect((int)prev.getX()-1, (int) prev.getY()-1, 2,2);
        }

        //draw point sources
        g.setColor(Color.red);
        for (PointSource pointSource : world.getSources()) {

            Point coord = m.mult(pointSource.getPosition()).toPoint();
            coord.setX(f*coord.getX() / coord.getZ());
            coord.setY(f*coord.getY() / coord.getZ());
            Point p = new Matrix(new double[][]{
                {1,0,0,o_x},
                {0,1,0,o_y},
                {0,0,1,0},
                {0,0,0,1}
            }).mult(coord).toPoint();
            
            g.drawRect((int)p.getX()-3, (int) p.getY()-3, 6,6);

            //draw line to hrtf found
            coord = m.mult(getPosition(world.hrir)).toPoint();
            coord.setX(f*coord.getX() / coord.getZ());
            coord.setY(f*coord.getY() / coord.getZ());
            Point hrir = new Matrix(new double[][]{
                {1,0,0,o_x},
                {0,1,0,o_y},
                {0,0,1,0},
                {0,0,0,1}
            }).mult(coord).toPoint();

            g.drawRect((int)hrir.getX()-3, (int) hrir.getY()-3, 6,6);
            g.drawLine((int) p.getX(), (int) p.getY(), (int) hrir.getX(), (int) hrir.getY());



        }



        g.drawString("x: " + camera.getPos().getX(), width - 100, 10);
        g.drawString("y: " + camera.getPos().getY(), width - 100, 20);
        g.drawString("z: " + camera.getPos().getZ(), width - 100, 30);







    }

    private void drawOrigin(Graphics g, double f, Matrix m, int o_x, int o_y) {
        g.setColor(Color.green);
        int originSize = 100;
        Point o_a = m.mult(new Point(-originSize,0,0)).toPoint()
                ,o_b = m.mult(new Point(originSize,0,0)).toPoint()
                ,o_c = m.mult(new Point(0,0,-originSize)).toPoint()
                ,o_d = m.mult(new Point(0,0,originSize)).toPoint();

        Matrix centre = new Matrix(new double[][]{
                    {1,0,0,o_x},
                    {0,1,0,o_y},
                    {0,0,1,0},
                    {0,0,0,1}
                });

        o_a.setX(f*o_a.getX() / o_a.getZ());
        o_a.setY(f*o_a.getY() / o_a.getZ());

        o_b.setX(f*o_b.getX() / o_b.getZ());
        o_b.setY(f*o_b.getY() / o_b.getZ());

        o_c.setX(f*o_c.getX() / o_c.getZ());
        o_c.setY(f*o_c.getY() / o_c.getZ());

        o_d.setX(f*o_d.getX() / o_d.getZ());
        o_d.setY(f*o_d.getY() / o_d.getZ());

        o_a = centre.mult(o_a).toPoint();
        o_b = centre.mult(o_b).toPoint();
        o_c = centre.mult(o_c).toPoint();
        o_d = centre.mult(o_d).toPoint();

        g.drawLine((int) o_a.getX(), (int)o_a.getY(), (int)o_b.getX(), (int)o_b.getY());
        g.drawLine((int) o_c.getX(), (int)o_c.getY(), (int)o_d.getX(), (int)o_d.getY());

        g.setColor(Color.black);

    }

    private void createPositions() {
        HRIR[][] hrirs = world.getHRTF().getHrirs();
        positions = new Point[hrirs.length][];



        for (int i = 0; i < hrirs.length; i++) {
            HRIR[] hrir = hrirs[i];
            positions[i] = new Point[hrir.length];
            for (int j = 0; j < hrir.length; j++) {
                HRIR hrir1 = hrir[j];

                positions[i][j] = getPosition(hrir1);

            }
        }
    }

    private Point getPosition(HRIR hrir1) {
        if (hrir1 == null) {
            return new Point();
        }
        int scale = 1000;
        Matrix resize = new Matrix(new double[][]{
                {scale, 0, 0, 0},
                {0, scale, 0, 0},
                {0, 0, scale, 0},
                {0, 0, 0, 1}
        });

        double az = Math.toRadians(90 - hrir1.getAzimuth());
        double el = Math.toRadians(hrir1.getElevation());
        Point p = new Point(Math.cos(az), 0, Math.sin(az));


        Matrix m = new Matrix(new double[][]{
                {1, 0, 0, 0},
                {0, Math.cos(el), -Math.sin(el), 0},
                {0, Math.sin(el), Math.cos(el), 0},
                {0, 0, 0, 1},
        }).mult(resize);


        return m.mult(p).toPoint();
    }


}
