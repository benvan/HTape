import htape.gui.HRTFTest;
import htape.util.LocationPicker;
import htape.util.io.UnrecognisedHRTFException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: ben
 * Date: 1/26/11
 * Time: 8:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class testmain {

//    private static String hrtf_location = "/home/ben/project/resources/hrtfs/listen/38.hrtf.listen.bin";
    private static String hrtf_location = "/home/ben/subject_008.hrtf.cipic.bin";
    private static String hrtf_directory = "/home/ben/project/resources/hrtfs/listen/";
    private static String wav_location = "/home/ben/play/sample.wav";

    public static void main(String[] args) {

        if (args.length > 0) {
            if (args.length != 2) {
                System.out.println("Usage: testmain hrtf_file wav_file");
                System.exit(0);
            }
            hrtf_location = args[0];
            wav_location = args[1];
        }

        final int[] hrtfIndex = {18};
        final LocationPicker loc = new LocationPicker();
        final HRTFTest player = new HRTFTest();

        player.loadWav(wav_location);
        try {
            player.loadHRTF(hrtf_location);
            loc.represent(player.getHRTF());
        } catch (IOException e) {
            java.lang.System.err.println(e.getMessage());
        } catch (UnrecognisedHRTFException e) {
            java.lang.System.err.println(e.getMessage());
        }


        //38 and 31 are current favourites

        /*File hrtfDir = new File("/home/ben/project/resources/hrtfs/listen");
        for (File hrtf : hrtfDir.listFiles()) {
            if (hrtf.isFile() && hrtf.getName().contains(".hrtf_bin")){
                player.loadHRTF(hrtf.getAbsolutePath());
            }
        }*/


        JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(1100, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(loc);

        loc.getActionMap().put("foo", new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        loc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A,  0), "foo");


        /*frame.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent keyEvent) {
            }

            public void keyPressed(KeyEvent keyEvent) {

                try {
                    switch (keyEvent.getKeyCode()) {
                        case KeyEvent.VK_SPACE:
                            player.play();
                            break;
                        case KeyEvent.VK_PAGE_UP:

                            hrtfIndex[0] = Math.min(59, hrtfIndex[0]);
                            hrtfIndex[0] = Math.max(2, hrtfIndex[0]);
                            player.loadHRTF(String.format("%s/%02d.hrtf_bin", hrtf_directory, ++hrtfIndex[0]));
                            player.play();
                            break;
                        case KeyEvent.VK_PAGE_DOWN:
                            hrtfIndex[0] = Math.min(59, hrtfIndex[0]);
                            hrtfIndex[0] = Math.max(2, hrtfIndex[0]);
                            player.loadHRTF(String.format("%s/%02d.hrtf_bin", hrtf_directory, --hrtfIndex[0]));
                            player.play();
                            break;
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } catch (UnrecognisedHRTFException e) {
                    System.out.println(e.getMessage());
                }

            }

            public void keyReleased(KeyEvent keyEvent) {
            }
        });*/

        frame.pack();
        frame.setVisible(true);


        Timer t = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {

                loc.repaint();
                player.update(-loc.getAzimuth(), loc.getElevation());

            }
        });

        t.start();


    }

    private static void nextTrack() {

    }

}
