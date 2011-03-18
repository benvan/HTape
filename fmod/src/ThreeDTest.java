import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


import htape.Player;
import htape.gui.HRTFTest;
import htape.util.CIPICLocationPicker;
import htape.util.LocationPicker;
import htape.util.ResourcePool;
import htape.util.filtering.IFilter;
import htape.util.filtering.hrtf.HRTFFactory;
import htape.util.filtering.hrtf.IHRTF;
import htape.util.io.UnrecognisedHRTFException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;



public class ThreeDTest {
	
	private static String hrtf_location = "C:\\Ben\\workspace\\HTape\\fmod\\resources\\hrtfs\\subject_008.hrtf.cipic.bin";//"/home/ben/subject_008.hrtf.cipic.bin";
    private static String hrtf_directory = "c:\\temp\\";
    private static String wav_location = "media/sample.wav";
	
	public static void main(String[] args) throws IOException {

		
		
        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            final DatagramSocket s = new DatagramSocket(29129);
            s.setSoTimeout(0);
            final byte[] bs = new byte[64];
            final DatagramPacket p = new DatagramPacket(bs, bs.length);
            final float[] pos = new float[6];
            

            	
            Thread t = new Thread(new Runnable(){

				@Override
				public void run() {

					while(true){
					
						try {
							s.receive(p);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            	
		            	ByteBuffer b = ByteBuffer.wrap(p.getData());
		            	b.order(ByteOrder.BIG_ENDIAN);
		            	
		            	
		            	
		            	String str = new String(bs);
		            	int i = 0;
		            	try{
		            	for (String  st : str.trim().split(" ")){
		            		pos[i++] = Float.parseFloat(st);
		            		System.out.print(st);
		            		System.out.print(" ");
		            		
		            	}
		            	System.out.println();
		            	}
		            	catch(NumberFormatException e){
		            		System.err.println("******** Some numerical error **********");
		            	}
	
					}
				}
            });
            
            t.start();
            
            
            
            final int[] hrtfIndex = {18};
            final CIPICLocationPicker loc = new CIPICLocationPicker();
            final Player player = new Player();
            
            final ResourcePool pool = new ResourcePool();
            

            player.loadAudioFile(wav_location);
            try {
                pool.hrtfs().add(new HRTFFactory().fromFile(hrtf_location));
            } catch (UnrecognisedHRTFException e) {
                System.err.println("Failed to recognise HRTF format.");
                System.exit(0);
            } catch (IOException e) {
                System.err.println("Failed to read HRTF file.");
                System.exit(0);
            }
            
            loc.represent(pool.hrtfs().get());

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

            frame.add(loc);

            frame.addKeyListener(new KeyListener() {

                public void keyTyped(KeyEvent keyEvent) {
                }

                public void keyPressed(KeyEvent keyEvent) {
                        switch (keyEvent.getKeyCode()) {
                            case KeyEvent.VK_SPACE:
                                player.play();
                                break;
                        }
                }

                public void keyReleased(KeyEvent keyEvent) {
                }
            });

            frame.pack();
            frame.setVisible(true);



            Timer timer = new Timer(10, new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {

                	loc.repaint();
                    IHRTF hrtf = pool.hrtfs().get();
                    if (hrtf == null) return;
                    IFilter filter = hrtf.get(loc.getAzimuth() + pos[4] , loc.getElevation() - pos[3]);
                    loc.highlight(filter);
                    player.setFilter(filter);
                }
            });

            timer.start();
            	            	
            
        
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
