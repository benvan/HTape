package htape.gui;

import htape.World;
import htape.geometry.*;
import htape.geometry.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by ben, on 3/17/11 at 12:40 PM
 */
public class WorldView {

    World w;
    JFrame frame;
    private WorldViewCanvas canvas;

    public WorldView(final World w) {
        this.w = w;
        this.canvas = new WorldViewCanvas(w);
        this.frame = new JFrame();

        frame.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent keyEvent) {

            }

            public void keyPressed(KeyEvent keyEvent) {
                Point pos = w.getCamera().getPos();
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        pos.setZ(pos.getZ() + 50);
                        break;
                    case KeyEvent.VK_DOWN:
                        pos.setZ(pos.getZ() - 50);
                        break;
                    case KeyEvent.VK_LEFT:
                        w.getCamera().transform(Matrix.rotY(-Math.PI/32));
                        break;
                    case KeyEvent.VK_RIGHT:
                        w.getCamera().transform(Matrix.rotY(Math.PI/32));
                        break;
                }
            }

            public void keyReleased(KeyEvent keyEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        
        init();
    }

    private void init() {
        frame.setPreferredSize(new Dimension(1100, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(canvas);
    }

    public void run(){
        frame.pack();
        frame.setVisible(true);

        Timer t = new Timer(25, new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                w.update();
                canvas.repaint();
            }
        });
        t.start();
    }


}
