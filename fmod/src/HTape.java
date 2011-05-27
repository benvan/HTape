import htape.Player;
import htape.util.CIPICLocationPicker;
import htape.util.LocationPicker;
import htape.util.ResourcePool;
import htape.util.filtering.DistanceFilter;
import htape.util.filtering.IFilter;
import htape.util.filtering.hrtf.HRTFFactory;
import htape.util.filtering.hrtf.IHRTF;
import htape.util.io.UnrecognisedHRTFException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: ben
 * Date: 2/17/11
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class HTape {

    JFrame frame;

    CIPICLocationPicker loc;
    ResourcePool resources;
    Player player;

    public HTape() {

        frame = new JFrame();
        loc = new CIPICLocationPicker();
        resources = new ResourcePool();
        player = new Player();
        init();
    }

    private void init() {

        resources.audioFiles().add("/home/ben/windows/ben/Music/Radiohead/In Rainbows/01_15_Step.mp3");
        resources.audioFiles().add("/home/ben/play/sample.wav");
        resources.audioFiles().add("media/walking-in-snow-1.wav");
        player.loadAudioFile(resources.audioFiles().get());

        try {
            resources.hrtfs().add(new HRTFFactory().fromFile("/home/ben/subject_008.hrtf.cipic.bin"));
        } catch (UnrecognisedHRTFException e) {
            System.err.println("Failed to recognise HRTF format.");
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Failed to read HRTF file.");
            System.exit(0);
        }

        loc.represent(resources.hrtfs().get());

        loc.getActionMap().put("playSound", new HTapeAction(resources, player) {
            public void actionPerformed(ActionEvent actionEvent) {
                player.play();
            }
        });
        loc.getActionMap().put("nextSong", new HTapeAction(resources, player) {
            public void actionPerformed(ActionEvent actionEvent) {
                player.loadAudioFile(resources.audioFiles().next());
            }
        });
        loc.getActionMap().put("previousSong", new HTapeAction(resources, player) {
            public void actionPerformed(ActionEvent actionEvent) {
                player.loadAudioFile(resources.audioFiles().previous());
            }
        });
        loc.getActionMap().put("nextHRTF", new HTapeAction(resources, player) {
            public void actionPerformed(ActionEvent actionEvent) {
                IHRTF hrtf = resources.hrtfs().next();
                loc.represent(hrtf);
            }
        });
        loc.getActionMap().put("previousHRTF", new HTapeAction(resources, player) {
            public void actionPerformed(ActionEvent actionEvent) {
                IHRTF hrtf = resources.hrtfs().previous();
                loc.represent(hrtf);
            }
        });

        loc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "playSound");
        loc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), "nextSong");
        loc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, KeyEvent.CTRL_DOWN_MASK), "previousSong");
        loc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), "nextHRTF");
        loc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, KeyEvent.CTRL_DOWN_MASK), "previousHRTF");

        frame.setPreferredSize(new Dimension(1100, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(loc);
    }

    public void run(){
        frame.pack();
        frame.setVisible(true);
        
        final DistanceFilter filt = new DistanceFilter();

        Timer t = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                loc.repaint();
                IHRTF hrtf = resources.hrtfs().get();
                if (hrtf == null) return;
                filt.wrap(hrtf.get(loc.getAzimuth(), loc.getElevation()));
                player.setFilter(filt);
            }
        });
        t.start();
    }

    public static void main(String[] args) {

        HTape htape = new HTape();
        htape.run();

    }

    private abstract class HTapeAction extends AbstractAction{
        private ResourcePool pool;
        private Player player;

        private HTapeAction(ResourcePool pool, Player player) {
            this.pool = pool;
            this.player = player;
        }
    }
}
