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

        for (int i = 0; i < positions.length; i++) {
            Point[] positionRow = positions[i];
            projections[i] = new Point[positionRow.length];
            for (int j = 0; j < positionRow.length; j++) {
                Point pos = positionRow[j];

                Point coord = m.mult(pos).toPoint();
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
                    g.drawLine((int) (prev.getX()), (int) (prev.getY()), (int) (point.getX()), (int) (point.getY()));
                    g.drawRect((int)prev.getX()-1, (int) prev.getY()-1, 2,2);

                    if (i + 1 < projections.length) {
                        Point neighbour = projections[i+1][j];
                        g.drawLine((int)(neighbour.getX()), (int)(neighbour.getY()), (int)(point.getX()), (int)(point.getY()));
                    }
                }


                prev = point;
            }
            g.drawRect((int)prev.getX()-1, (int) prev.getY()-1, 2,2);
        }

        //draw point sources
        g.setColor(Color.red);
        for (PointSource pointSource : world.getSources()) {
            Point p = m.mult(pointSource.getPosition()).toPoint();
            g.drawRect((int)p.getX()-3, (int) p.getY()-3, 6,6);
        }






    }

    private void createPositions() {
        HRIR[][] hrirs = world.getHRTF().getHrirs();
        positions = new Point[hrirs.length][];

        int scale = 200;
        Matrix resize = new Matrix(new double[][]{
            {scale,0,0,0},
            {0,scale,0,0},
            {0,0,scale,0},
            {0,0,0,1}
        });

        for (int i = 0; i < hrirs.length; i++) {
            HRIR[] hrir = hrirs[i];
            positions[i] = new Point[hrir.length];
            for (int j = 0; j < hrir.length; j++) {
                HRIR hrir1 = hrir[j];

                double az = Math.toRadians(90 - hrir1.getAzimuth());
                double el = Math.toRadians(hrir1.getElevation());
                Point p = new Point(Math.cos(az), 0, Math.sin(az));



                Matrix m = new Matrix(new double[][]{
                    {1,0,0,0},
                    {0,Math.cos(el),-Math.sin(el),0},
                    {0,Math.sin(el),Math.cos(el),0},
                    {0,0,0,1},
                }).mult(resize);



                positions[i][j] = m.mult(p).toPoint();

            }
        }
    }


}
