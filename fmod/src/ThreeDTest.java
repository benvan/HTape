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
import htape.World;
import htape.geometry.Matrix;
import htape.geometry.Point;
import htape.geometry.PointSource;
import htape.gui.HRTFTest;
import htape.gui.WorldView;
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
    private static String wav_location = "media/s";
	
	public static void main(String[] args) throws IOException {

		
		
        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            final DatagramSocket s = new DatagramSocket(29129);
            s.setSoTimeout(0);
            final byte[] bs = new byte[64];
            final DatagramPacket packet = new DatagramPacket(bs, bs.length);
            final float[] pos = new float[6];
            

            	
            Thread t = new Thread(new Runnable(){

				@Override
				public void run() {

					while(true){
					
						try {
							s.receive(packet);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            	
		            	ByteBuffer b = ByteBuffer.wrap(packet.getData());
		            	b.order(ByteOrder.BIG_ENDIAN);
		            	
		            	
		            	
		            	String str = new String(bs);
		            	int i = 0;
		            	try{
		            	for (String  st : str.trim().split(" ")){
		            		pos[i++] = Float.parseFloat(st);
//		            		System.out.print(st);
//		            		System.out.print(" ");
		            		
		            	}
		            	//System.out.println();
		            	}
		            	catch(NumberFormatException e){
		            		System.err.println("******** Some numerical error **********");
		            	}
	
					}
				}
            });
            
            t.start();
            
            
            
            World world = new World();
            PointSource p = world.createSource();
            world.bind(p, "media/sample.wav");
            world.move(p, new Point(0,1,0));
            

            double ang = 0.5;
            Matrix m = new Matrix(new double[][]{
                {1,0,0,0},
                {0,Math.cos(ang),-Math.sin(ang),0},
                {0,Math.sin(ang),Math.cos(ang),0},
                {0,0,0,1}
            });

            m = m.mult(new Matrix(new double[][]{
                {Math.cos(ang),-Math.sin(ang),0,0},
                {Math.sin(ang),Math.cos(ang),0,0},
                {0,0,1,0},
                {0,0,0,1}
            }));

            //world.getCamera().transform(m);
            world.getCamera().setFocalLength(1000);
            
            world.getCamera().bind(pos);



            try {
                world.setHRTF(new HRTFFactory().fromFile(hrtf_location));
            } catch (UnrecognisedHRTFException e) {
                System.err.println("Failed to recognise HRTF format.");
                System.exit(0);
            } catch (IOException e) {
                System.err.println("Failed to read HRTF file.");
                System.exit(0);
            }

            WorldView view = new WorldView(world);
            

            view.run();
            	            	
            
        
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
