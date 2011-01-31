package htape.util;

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
public class LocationPicker extends JPanel {

    private boolean dragging = false;
    private Point mouseVector;

    private Point2D.Double origin;
    private Point2D.Double upperBound, lowerBound;
    private Point2D.Double location;
    private static final int PADDING = 10;

    public LocationPicker() {
        super();

        mouseVector = new Point();
        origin = new Point2D.Double();
        location = new Point2D.Double(0, 0);
        upperBound = new Point2D.Double(180, 90);
        lowerBound = new Point2D.Double(-180, -45);

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



    public void paintComponent(Graphics g) {

        int w = getWidth() - 2*PADDING;
        int h = getHeight() - 2*PADDING;
        int o_x = w / 2;
        int o_y = (int) ((upperBound.getY() / (upperBound.getY() - lowerBound.getY())) * h);

        origin.setLocation(o_x,o_y);
        location.setLocation(
                lowerBound.getX() + (mouseVector.getX() + o_x) * (upperBound.getX() - lowerBound.getX()) / getWidth(),
                upperBound.getY() - (mouseVector.getY() + o_y) * (upperBound.getY() - lowerBound.getY()) / getHeight()
        );

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());


        g.translate(PADDING, PADDING);

        //draw dragline
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(o_x, o_y, (int) mouseVector.getX() + o_x, (int) mouseVector.getY() + o_y);
        g.setColor(Color.LIGHT_GRAY);
        int diameter = 10;
        g.fillOval((int) (mouseVector.getX() + o_x - diameter / 2), (int) mouseVector.getY() + o_y - diameter / 2, diameter, diameter);
        g.setColor(Color.DARK_GRAY);
        g.drawOval((int) (mouseVector.getX() + o_x - diameter/2), (int) mouseVector.getY() + o_y - diameter/2, diameter, diameter);
        g.drawString(String.format("(%.0f , %.0f)", location.getX(),location.getY()), (int)mouseVector.getX()+o_x , (int)mouseVector.getY()+o_y - 10);

        //draw axes
        g.setColor(Color.DARK_GRAY);
        g.drawLine(0,o_y, w, o_y);
        int x_marks = 24;
        for (int i = 0; i <= x_marks; i++) {
            int markx = (int) (((double)i/(x_marks)) * w);
            g.drawLine(markx, o_y - 3, markx, o_y + 3);
            g.drawString(String.valueOf(-180 + i *15), markx - 5, o_y + 12);
        }

        g.drawLine(o_x,0, o_x, h);
        int y_marks = 10;
        for (int i = 0; i < y_marks; i++) {
            int marky = (int) (((double)i/(y_marks-1)) * h);
            g.drawLine(o_x - 3, marky, o_x + 3, marky);
            g.drawString(String.valueOf(90 - i * 15), o_x + 8, marky);
        }

        g.translate(-PADDING,-PADDING);

    }


    public double getDragX() {
        return mouseVector.getX() / (getWidth()/2);
    }

    public double getDragY() {
        return 15*(mouseVector.getY() - origin.getY()) / (getHeight() - 20);
    }

    public int getElevation() {
        return (int)location.getY();
    }

    public int getAzimuth() {
        return (int)location.getX();
    }
}
