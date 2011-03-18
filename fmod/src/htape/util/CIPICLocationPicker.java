package htape.util;

import htape.util.filtering.hrtf.HRIR;
import htape.util.filtering.hrtf.IHRTF;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

/**
* Created by IntelliJ IDEA.
* User: ben
* Date: 1/27/11
* Time: 4:38 PM
* To change this template use File | Settings | File Templates.
*/
public class CIPICLocationPicker extends JPanel {

    private boolean dragging = false;
    private Point mouseVector;

    private Point2D.Double origin;
    private Point2D.Double upperBound, lowerBound;
    private Point2D.Double location;
    private static final int PADDING = 10;
    private int[] azimuths, elevations;
    private HRIR[][] positions;
    private IHRTF hrtf;

    public CIPICLocationPicker() {
        super();

        mouseVector = new Point();
        origin = new Point2D.Double();
        location = new Point2D.Double(0, 0);
        upperBound = new Point2D.Double(80, 230.625);
        lowerBound = new Point2D.Double(-80, -45);

        addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(MouseEvent mouseEvent) {
                mouseVector.setLocation(
                        mouseEvent.getX() - origin.getX() - PADDING,
                        mouseEvent.getY() - origin.getY() - PADDING
                );
            }

            public void mouseMoved(MouseEvent mouseEvent) {
            }
        });
    }


    public void represent(IHRTF hrtf) {

        positions = hrtf.getHrirs();
        this.hrtf = hrtf;
    }


    public void paintComponent(Graphics g) {

        if (this.hrtf == null) {
            return;
        }

        int w = getWidth() - 2 * PADDING;
        int h = getHeight() - 2 * PADDING;
        int o_x = w / 2;
        int o_y = (int) ((upperBound.getY() / (upperBound.getY() - lowerBound.getY())) * h);

        origin.setLocation(o_x, o_y);
        location.setLocation(
                lowerBound.getX() + (mouseVector.getX() + o_x) * (upperBound.getX() - lowerBound.getX()) / w,
                upperBound.getY() - (mouseVector.getY() + o_y) * (upperBound.getY() - lowerBound.getY()) / h
        );

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());


        g.translate(PADDING, PADDING);

        //draw dragline
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(o_x, o_y, (int) mouseVector.getX() + o_x, (int) mouseVector.getY() + o_y);
        g.setColor(Color.RED);

        double az = (hrtf.get(location.getX(), location.getY()).getAzimuth());
        double el = (hrtf.get(location.getX(), location.getY()).getElevation());
        double hSpan = upperBound.getX() - lowerBound.getX();
        double vSpan = upperBound.getY() - lowerBound.getY();
        double mark_x = (int) (w * (az / hSpan + 0.5));
        double mark_y = h - (h * (((hrtf.get(location.getX(), location.getY()).getElevation()) - lowerBound.getY()) / vSpan));

        g.drawLine((int) mouseVector.getX() + o_x, (int) mouseVector.getY() + o_y, (int)mark_x, (int)mark_y);
        g.drawOval((int)mark_x - 3, (int)mark_y - 3, 6, 6);
        g.setColor(Color.LIGHT_GRAY);
        int diameter = 10;
        g.fillOval((int) (mouseVector.getX() + o_x - diameter / 2), (int) mouseVector.getY() + o_y - diameter / 2, diameter, diameter);

        g.setColor(Color.DARK_GRAY);
        g.drawOval((int) (mouseVector.getX() + o_x - diameter / 2), (int) mouseVector.getY() + o_y - diameter / 2, diameter, diameter);
        g.drawString(String.format("(%d , %d)", (int) location.getX(), (int) location.getY()), (int) mouseVector.getX() + o_x, (int) mouseVector.getY() + o_y - 10);
        g.drawString(String.format("(%d , %d)", (int) az, (int) el), (int) mouseVector.getX() + o_x, (int) mouseVector.getY() + o_y - 30);


        //draw axes
        g.setColor(Color.DARK_GRAY);
        g.drawLine(0, o_y, w, o_y);
        g.drawLine(o_x, 0, o_x, h);

        g.setFont(new Font("Serif", Font.ITALIC, 9));


        int count = 0;
        for (int i = 0; i < positions.length; i++) {
            HRIR[] row = positions[i];
            for (int j = 0; j < row.length; j++) {
                count++;
                HRIR hrir = row[j];
                az = hrir.getAzimuth();
                mark_x = w * (az / hSpan + 0.5);
                mark_y = h - (h * (((hrir.getElevation()) - lowerBound.getY()) / (upperBound.getY() - lowerBound.getY())));
                g.drawLine((int)mark_x - 2, (int)mark_y, (int)mark_x + 2, (int)mark_y);
                g.drawLine((int)mark_x, (int)mark_y + 2, (int)mark_x, (int)mark_y - 2);
                //g.drawString(String.format("(%d,%d)", (int)positions[i][j].getAzimuth(), (int)positions[i][j].getElevation()), (int) mark_x + 5, (int) mark_y);


            }

        }

        /*int spread_x = azimuths[azimuths.length-1] - azimuths[0];
        for (int i = 0; i < azimuths.length; i++) {
            double grad = (double) w / (double)spread_x;
            int markx = o_x + (int)(grad*azimuths[i]);
            g.drawLine(markx, o_y - 3, markx, o_y + 3);
            g.drawString(String.valueOf(-180 + i *15), markx - 5, o_y + 12);
        }

        g.drawLine(o_x,0, o_x, h);
        int y_marks = 10;
        for (int i = 0; i < y_marks; i++) {
            int marky = (int) (((double)i/(y_marks-1)) * h);
            g.drawLine(o_x - 3, marky, o_x + 3, marky);
            g.drawString(String.valueOf(90 - i * 15), o_x + 8, marky);
        }*/

        g.translate(-PADDING, -PADDING);

    }


    public double getDragX() {
        return mouseVector.getX() / (getWidth()/2);
    }

    public double getDragY() {
        return 15*(mouseVector.getY() - origin.getY()) / (getHeight() - 20);
    }

    public double getElevation() {
        return location.getY();
    }

    public double getAzimuth() {
        return location.getX();
    }
}
