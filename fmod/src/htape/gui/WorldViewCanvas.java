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
    private Matrix cameraProjection;
    private Matrix screenProjection;

    private ExtendedGraphics eg;

    public WorldViewCanvas(World world) {
        this.world = world;
        this.eg = new ExtendedGraphics();
    }

    protected void paintComponent(Graphics g) {
        eg.setGraphics(g, getWidth(), getHeight());

        updateHRTFModel();
        updateProjectionMatrices();

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.LIGHT_GRAY);

        drawHRTF(g);
        drawOrigin(g);
        for (PointSource pointSource : world.getSources()) {
            drawPointSource(g, pointSource);
        }

        drawInfo(g);
    }

    private void drawHRTF(Graphics g) {
        g.setFont(new Font("Monospaced", Font.PLAIN, 10));
        Point[][] projections = new Point[positions.length][];
        for (int i = 0; i < positions.length; i++) {
            Point[] positionRow = positions[i];
            projections[i] = new Point[positionRow.length];
            for (int j = 0; j < positionRow.length; j++) {
                Point pos = positionRow[j];
                projections[i][j] = toScreenSpace(pos);
            }
        }

        //draw projections on z-plane
        for (int i = 0; i < projections.length; i++) {
            Point[] projectionRow = projections[i];
            Point prev = projectionRow[projectionRow.length - 1];
            for (int j = 0; j < projectionRow.length; j++) {
                Point point = projectionRow[j];

                if (prev.getZ() > 0) {
                    if (point.getZ() > 0) {
                        eg.line(prev, point);
                        if (i + 1 < projections.length) {
                            Point neighbour = projections[i + 1][j];
                            if (neighbour.getZ() > 0) {
                                eg.line(neighbour, point);
                            }
                        }
                    }
                    eg.square(prev, 2);
                    if (world.getHRTF().getHrirs()[i][j].getAzimuth() == 0) {
                        g.drawString(String.format("%+.2f", world.getHRTF().getHrirs()[i][j].getElevation()), (int) point.getX(), (int) (getHeight() - point.getY()));
                    }
                    if (world.getHRTF().getHrirs()[i][j].getElevation() == 0) {
                        g.drawString(String.format("%+.2f", world.getHRTF().getHrirs()[i][j].getAzimuth()), (int) point.getX(), (int) (getHeight() - point.getY()));
                    }

                }
                prev = point;
            }
            eg.square(prev, 2);
        }
    }

    private void drawPointSource(Graphics g, PointSource pointSource) {
        g.setColor(Color.red);
        Point source = pointSource.getPosition().dup();
        Point p = toScreenSpace(source);

        if (p.getZ() <= 0) {
            return;
        }

        Point radius = applyPerspective(toCameraSpace(source).addX(0.1));
        double r = Math.abs(radius.getX() - p.getX());

        eg.circF(p, r);
        g.setColor(Color.WHITE);
        eg.circ(p, r);
        g.setColor(Color.red);

        //draw line to hrtf found
        Point hrir = toScreenSpace(getPosition(world.hrir));

        eg.square(hrir, 6);
        eg.line(p, hrir);
        g.setFont(new Font("Monospaced", Font.PLAIN, 10));
        g.drawString(String.valueOf(world.hrir.toString()), (int) hrir.getX(), (int) (getHeight() - hrir.getY()));
    }


    private void updateProjectionMatrices() {
        Camera camera = world.getCamera();
        double f = camera.getFocalLength();

        htape.geometry.Point u = camera.getU(), v = camera.getV(), w = camera.getW(), c = camera.getPos();

        cameraProjection = new Matrix(new double[][]{
                {u.getX(), u.getY(), u.getZ(), -c.dot(u)},
                {v.getX(), v.getY(), v.getZ(), -c.dot(v)},
                {w.getX(), w.getY(), w.getZ(), -c.dot(w)},
                {0, 0, 0, 1},
        });

        screenProjection = new Matrix(new double[][]{
                {1, 0, 0, getWidth() / 2},
                {0, 1, 0, getHeight() / 2},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
    }


    private void updateHRTFModel() {
        if (hrtf != world.getHRTF()) {
            hrtf = world.getHRTF();
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
    }

    private void drawInfo(Graphics g) {
        Camera camera = world.getCamera();
        int w = 110;
        int h = 100;
        int tr = getWidth(); //top-right

        g.setColor(Color.black);
        g.fillRect(tr - w + 1, 1, w - 2, h - 2);
        g.setColor(Color.white);
        g.drawRect(tr - w, 0, w, h);

        g.setFont(new Font("Monospaced", Font.PLAIN, 12));

        int textStart = tr - 100;
        g.drawString(String.format("x: %+.2f", camera.getPos().getX()), textStart, 10);
        g.drawString(String.format("y: %+.2f", camera.getPos().getY()), textStart, 20);
        g.drawString(String.format("z: %+.2f", camera.getPos().getZ()), textStart, 30);
        g.drawString("az: " + world.hrir.getAzimuth(), textStart, 50);
        g.drawString("el: " + world.hrir.getElevation(), textStart, 60);
        g.drawString(String.format("focal: %+.2f", camera.getFocalLength()), textStart, 80);
    }


    private Point toScreenSpace(Point position) {
        Point coord = toCameraSpace(position);
        return applyPerspective(coord);
    }

    private Point applyPerspective(Point coord) {
        double focalLength = world.getCamera().getFocalLength();

        coord.setX(focalLength * coord.getX() / Math.abs(coord.getZ()));
        coord.setY(focalLength * coord.getY() / Math.abs(coord.getZ()));

        Point p = screenProjection.mult(coord).toPoint();

        return p;
    }

    private Point toCameraSpace(Point position) {
        return cameraProjection.mult(position).toPoint();
    }


    private void drawOrigin(Graphics g) {
        g.setColor(Color.green);

        int originSize = 1;
        Point endPoint = new Point(-originSize, 0, 0);
        Point origin = toScreenSpace(new Point());
        if (origin.getZ() <= 0) {
            return;
        }

        Matrix rotY = Matrix.rotY(Math.toRadians(90));

        for (int i = 0; i < 4; i++) {
            endPoint = rotY.mult(endPoint).toPoint();
            Point screenEndPoint = toScreenSpace(endPoint);
            eg.line(origin, screenEndPoint);
        }

        endPoint = toScreenSpace(new Point(0, originSize, 0));
        eg.line(origin, endPoint);

        endPoint = toScreenSpace(new Point(0, -originSize, 0));
        eg.line(origin, endPoint);

        Point cameraSpaceOrigin = toCameraSpace(new Point());
        g.drawString(cameraSpaceOrigin.toString(),
                (int) origin.getX(), (int) origin.getY()
        );
        g.setColor(Color.WHITE);

    }

    private Point getPosition(HRIR hrir1) {
        if (hrir1 == null) {
            return new Point();
        }
        int scale = 150;
        Matrix resize = new Matrix(new double[][]{
                {scale, 0, 0, 0},
                {0, scale, 0, 0},
                {0, 0, scale, 0},
                {0, 0, 0, 1}
        });

        double az = Math.toRadians(90 - hrir1.getAzimuth());
        double el = Math.toRadians(hrir1.getElevation());
        Point p = new Point(Math.cos(az), 0, -Math.sin(az));


        Matrix m = new Matrix(new double[][]{
                {1, 0, 0, 0},
                {0, Math.cos(el), -Math.sin(el), 0},
                {0, Math.sin(el), Math.cos(el), 0},
                {0, 0, 0, 1},
        }).mult(resize);


        return m.mult(p).toPoint();
    }


}
