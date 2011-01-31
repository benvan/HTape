import htape.util.ExitListener;
import htape.util.LocationPicker;
import htape.util.io.UnrecognisedHRTFException;
import org.jouvieje.fmodex.examples.HRTFTest;

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

    public static void main(String[] args) {

        final int[] hrtfIndex = {18};
        final LocationPicker loc = new LocationPicker();
        final HRTFTest player = new HRTFTest();

        //player.loadWav("/home/ben/play/sample.wav");
        player.loadWav("/home/ben/play/sample.wav");
        try {
            player.loadHRTF("/home/ben/subject_008.hrtf.cipic.bin");
        }catch (IOException e) {
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

        frame.setPreferredSize(new Dimension(700, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(loc);

        frame.addKeyListener(new KeyListener() {

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
                            player.loadHRTF(String.format("/home/ben/project/resources/hrtfs/listen/%02d.hrtf_bin", ++hrtfIndex[0]));
                            player.play();
                            break;
                        case KeyEvent.VK_PAGE_DOWN:
                            hrtfIndex[0] = Math.min(59, hrtfIndex[0]);
                            hrtfIndex[0] = Math.max(2, hrtfIndex[0]);
                            player.loadHRTF(String.format("/home/ben/project/resources/hrtfs/listen/%02d.hrtf_bin", --hrtfIndex[0]));
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
        });

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

}
