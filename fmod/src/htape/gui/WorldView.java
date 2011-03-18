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
	private double[] cameraBinding;

    public WorldView(final World w) {
        this.w = w;
        this.canvas = new WorldViewCanvas(w);
        this.frame = new JFrame();

        frame.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent keyEvent) {

            }

            public void keyPressed(KeyEvent keyEvent) {
                Camera camera = w.getCamera();
                Point pos = camera.getPos();
                double f = camera.getFocalLength();
                int pow = (int)Math.ceil(Math.log10(f));
                int step = (int)Math.pow(10, pow-1);
                int cur = (int)((f - (f%step))/step);
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_Q:
                        camera.setFocalLength(step*(cur+1));
                        break;
                    case KeyEvent.VK_E:
                        camera.setFocalLength(step*(cur-1));
                        break;
                    case KeyEvent.VK_W:
                        pos.setZ(pos.getZ() + 50);
                        break;
                    case KeyEvent.VK_S:
                        pos.setZ(pos.getZ() - 50);
                        break;
                    case KeyEvent.VK_A:
                        pos.setX(pos.getX() - 50);
                        break;
                    case KeyEvent.VK_D:
                        pos.setX(pos.getX() + 50);
                        break;
                    case KeyEvent.VK_LEFT:
                        camera.transform(Matrix.rotY(-Math.PI / 32));
                        break;
                    case KeyEvent.VK_RIGHT:
                        camera.transform(Matrix.rotY(Math.PI / 32));
                        break;
                    case KeyEvent.VK_UP:
                        camera.transform(Matrix.rotX(Math.PI / 64));
                        break;
                    case KeyEvent.VK_DOWN:
                        camera.transform(Matrix.rotX(-Math.PI / 64));
                        break;
                    case KeyEvent.VK_SPACE:
                    	w.play(getActiveSource());
                }
            }

            public void keyReleased(KeyEvent keyEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        
        init();
    }

    protected PointSource getActiveSource() {
		// TODO Auto-generated method stub
		for (PointSource s : w.getSources()){
			return s;
		}
		return null;
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
