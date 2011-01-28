package htape.util;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: ben
 * Date: 1/27/11
 * Time: 12:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThreeDTest extends JFrame {

    Label infoLabel;
    LocationPicker drag;

    public static void main(String[] args) {
        new htape.util.ThreeDTest();
    }

    private void info(String message){
        infoLabel.setText(message);
    }

    public ThreeDTest() {
        super("Using JPanels with Borders");
        setBounds(100,300,700,500);


        addWindowListener(new ExitListener());

        Container content = getContentPane();
        content.setBackground(Color.lightGray);
        JPanel infoRegion = new JPanel(new GridLayout(3, 1));
        infoRegion.setPreferredSize(new Dimension(100,500));
        content.add(infoRegion, BorderLayout.EAST);



        infoLabel = new Label();

        infoRegion.add(infoLabel);


        drag = new LocationPicker();
        content.add(drag, BorderLayout.WEST);

        pack();
        setVisible(true);
    }

    public double getElevation() {
        return drag.getDragX();
    }

    public double getAzimuth() {
        return drag.getDragY();
    }


}
