import htape.util.CIPICLocationPicker;
import htape.util.LocationPicker;
import htape.util.filtering.IFilter;
import htape.util.filtering.hrtf.HRIR;
import htape.util.filtering.hrtf.HRTF;
import htape.util.filtering.hrtf.HRTFFactory;
import htape.util.filtering.hrtf.IHRTF;
import htape.util.io.UnrecognisedHRTFException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: ben
 * Date: 1/27/11
 * Time: 10:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class FloatTest {

    private static IHRTF hrtf;

    public static void main(String[] args) throws IOException, UnrecognisedHRTFException {

        hrtf = new HRTFFactory().fromFile("/home/ben/subject_008.hrtf.cipic.bin");

        final CIPICLocationPicker l = new CIPICLocationPicker();

        JFrame frame = new JFrame();

        frame.setPreferredSize(new Dimension(1100, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(l);

        l.represent(hrtf);

        frame.pack();
        frame.setVisible(true);


        l.getActionMap().put("closest", new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                closest(l.getAzimuth(), l.getElevation());
            }
        });

        l.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "closest");

        Timer t = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                l.repaint();



                //IFilter filter = hrtf.get(loc.getAzimuth(), loc.getElevation());
            }
        });
        t.start();





    }

    public static HRIR[] closest(double az, double el) {



        double dist[] = new double[1250];

        az += 90;

        double sinA = Math.sin(Math.PI * az/180), sinB = Math.sin(Math.PI * el/180), sinC, sinD;
        double cosA = Math.cos(Math.PI * az/180), cosB = Math.cos(Math.PI * el/180), cosC, cosD;


        System.out.print("          ");
        for (int j = 0; j < hrtf.getHrirs()[0].length; j++){
            System.out.print(String.format("%+06.2f ,", hrtf.getHrirs()[0][j].getElevation()));
        }
        System.out.println();


        double sml = 180;
        double smlel = 0, smlaz = 0;

        for (int i = 0; i < hrtf.getHrirs().length; i++) {
            HRIR[] hrirs = hrtf.getHrirs()[i];

            for (int j = 0; j < hrirs.length; j++) {
                HRIR hrir = hrirs[j];
                double _az = hrir.getAzimuth()+90, _el = hrir.getElevation();
                sinC = Math.sin(Math.PI * _az/180);
                sinD = Math.sin(Math.PI * _el/180);
                cosC = Math.cos(Math.PI * _az/180);
                cosD = Math.cos(Math.PI * _el/180);

                double x = Math.acos((sinA * cosB * sinC * cosD) + (sinA * sinB * sinC * sinD) + (cosA * cosC));
                if (x < sml) {
                    sml = x;
                    smlel = _el;
                    smlaz = _az-90;
                }
                dist[i*25 + j] = x;
                if (j == 0){
                    System.out.print(String.format("%06.2f  : ", _az -90));
                }
                System.out.print(String.format(" %05.2f ,", x * 180 / Math.PI));
            }
            System.out.println();
        }

        System.out.println("\n\n\n\n\n\n\n\n");

        System.out.println("" + sml + " : " + smlel + " " + smlaz );

        return null;




    }

}
