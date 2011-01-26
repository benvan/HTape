/*===============================================================================================
 Custom DSP Example
 Copyright (c), Firelight Technologies Pty, Ltd 2004-2010.

 This example shows how to add a user created DSP callback to process audio data.
 A read callback is generated at runtime, and can be added anywhere in the DSP network.

===============================================================================================*/

package org.jouvieje.fmodex.examples;

import static org.jouvieje.fmodex.defines.FMOD_INITFLAGS.FMOD_INIT_NORMAL;
import static org.jouvieje.fmodex.defines.FMOD_MODE.FMOD_LOOP_NORMAL;
import static org.jouvieje.fmodex.defines.FMOD_MODE.FMOD_OPENMEMORY;
import static org.jouvieje.fmodex.defines.FMOD_MODE.FMOD_SOFTWARE;
import static org.jouvieje.fmodex.defines.VERSIONS.FMOD_VERSION;
import static org.jouvieje.fmodex.defines.VERSIONS.NATIVEFMODEX_JAR_VERSION;
import static org.jouvieje.fmodex.defines.VERSIONS.NATIVEFMODEX_LIBRARY_VERSION;
import static org.jouvieje.fmodex.enumerations.FMOD_CHANNELINDEX.FMOD_CHANNEL_FREE;
import static org.jouvieje.fmodex.enumerations.FMOD_RESULT.FMOD_OK;
import static org.jouvieje.fmodex.utils.BufferUtils.newByteBuffer;
import static org.jouvieje.fmodex.utils.BufferUtils.SIZEOF_INT;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Scanner;

import javax.swing.JPanel;
import javax.swing.text.Highlighter;

import htape.util.HRIR;
import htape.util.HRTF;
import htape.util.HRTFFactory;
import htape.util.HistoryBuffer;
import org.jouvieje.fmodex.Channel;
import org.jouvieje.fmodex.DSP;
import org.jouvieje.fmodex.FmodEx;
import org.jouvieje.fmodex.Init;
import org.jouvieje.fmodex.Sound;
import org.jouvieje.fmodex.System;
import org.jouvieje.fmodex.callbacks.FMOD_DSP_READCALLBACK;

import org.jouvieje.fmodex.enumerations.FMOD_RESULT;
import org.jouvieje.fmodex.examples.utils.ConsoleGUI;
import org.jouvieje.fmodex.examples.utils.FmodExExampleFrame;
import org.jouvieje.fmodex.examples.utils.Medias;
import org.jouvieje.fmodex.exceptions.InitException;
import org.jouvieje.fmodex.utils.BufferUtils;
import org.jouvieje.fmodex.structures.FMOD_CREATESOUNDEXINFO;
import org.jouvieje.fmodex.structures.FMOD_DSP_DESCRIPTION;
import org.jouvieje.fmodex.structures.FMOD_DSP_STATE;

/**
 * Based on FMOD Ex C++ example. Ported to Java with NativeFmodEx by J�r�me JOUVIE (Jouvieje.
 * 
 * @author J�r�me JOUVIE (Jouvieje)
 * @site   http://jerome.jouvie.free.fr/
 * @mail   jerome.jouvie@gmail.com
 */
public class DspCustom extends ConsoleGUI {
	private static final long serialVersionUID = 1L;
    private static FmodExExampleFrame frame;

    public static void main(String[] args) {
        frame = new FmodExExampleFrame(new DspCustom());
    }

	private boolean init = false;
	private boolean deinit = false;


    private HRTF hrtf;
    private HRIR hrir;
    private int az = 0;
    private int el = 0;

	public DspCustom() {
		super();

        File f = new File("/home/ben/project/resources/hrtfs/listen/21/hrtf_compensated");
        try {

            /*

            Scanner s = new Scanner(f);
            int[] azimuths = new int[]{180,150,120,90,60,30,0,-30,-60,-90,-120,-150};
            int[] elevations = new int[]{54,36,18,0,-18,-36};
            HRIR[][] hrirs = new HRIR[azimuths.length][elevations.length];

            for (int i = 0; i < azimuths.length; i++) {
                for (int j = 0; j < elevations.length; j++) {
                    hrirs[i][j] = createHRIR(azimuths[i], elevations[j],s);
                }
            }*/

            //hrtf = new HRTF(hrirs);
            HRTFFactory fac = new HRTFFactory();
            hrtf = fac.listen(f);
            hrir = hrtf.get(az,el);

        } catch (FileNotFoundException e) {
            java.lang.System.out.println("eeeerrrrrrrrgh.");
        }

		initialize();
	}



    public JPanel getPanel() {
		return this;
	}

	public String getTitle() {
		return "FMOD Ex DspCustom example.";
	}

	private void errorCheck(FMOD_RESULT result) {
		if(result != FMOD_RESULT.FMOD_OK) {
			printfExit("FMOD error! (%d) %s\n", result.asInt(), FmodEx.FMOD_ErrorString(result));
		}
	}

	private boolean active = false;

	private System system = new System();
	private Sound sound = new Sound();
	private DSP mydsp = new DSP();

	private FMOD_DSP_READCALLBACK myDSPCallback = new FMOD_DSP_READCALLBACK(){
		ByteBuffer nameBuffer = newByteBuffer(256);

        HistoryBuffer lhist = new HistoryBuffer(512, 0);
        HistoryBuffer rhist = new HistoryBuffer(512, 0);

        int pos = 0;

		public FMOD_RESULT FMOD_DSP_READCALLBACK(FMOD_DSP_STATE dsp_state, FloatBuffer inbuffer, FloatBuffer outbuffer,
				int length, int inchannels, int outchannels) {
			DSP thisdsp = dsp_state.getInstance();

			/*
			 * This redundant call just shows using the instance parameter of FMOD_DSP_STATE and using it to 
			 * call a DSP information function. 
			 */
			thisdsp.getInfo(nameBuffer, null, null, null, null);
			String name = BufferUtils.toString(nameBuffer);

			/*
			 * This loop assumes inchannels = outchannels, which it will be if the DSP is created with '0' 
			 * as the number of channels in FMOD_DSP_DESCRIPTION.  
			 * Specifying an actual channel count will mean you have to take care of any number of channels coming in,
			 * but outputting the number of channels specified.  Generally it is best to keep the channel 
			 * count at 0 for maximum compatibility.
			 */


			for(int sample = 0; sample < length; sample++) {
				/*
				 * Feel free to unroll this.
				 */

                lhist.add(inbuffer.get());
                rhist.add(inbuffer.get());

                float l=0,r=0;

                for (int i = 0; i < lhist.length(); i++) {
                    l += lhist.get(i) * hrir.getLeft(i);
                    r += rhist.get(i) * hrir.getRight(i);
                }

                outbuffer.put(l);
                outbuffer.put(r);

			}
			inbuffer.rewind();
			outbuffer.rewind();

			return FMOD_OK;
		}
	};

	public void init() {
		/*
		 * NativeFmodEx Init
		 */
		try {
			Init.loadLibraries();
		}
		catch(InitException e) {
			printfExit("NativeFmodEx error! %s\n", e.getMessage());
			return;
		}

		/*
		 * Checking NativeFmodEx version
		 */
		if(NATIVEFMODEX_LIBRARY_VERSION != NATIVEFMODEX_JAR_VERSION) {
			printfExit("Error!  NativeFmodEx library version (%08x) is different to jar version (%08x)\n",
					NATIVEFMODEX_LIBRARY_VERSION, NATIVEFMODEX_JAR_VERSION);
			return;
		}

		/*==================================================*/

		init = true;
	}

	public void run() {
		if(!init) return;

		ByteBuffer soundBuffer;
		FMOD_CREATESOUNDEXINFO exinfo;
		Channel channel = new Channel();
		int version;

		ByteBuffer buffer = newByteBuffer(SIZEOF_INT);

		/*
		 * Create a System object and initialize.
		 */
		errorCheck(FmodEx.System_Create(system));

		errorCheck(system.getVersion(buffer.asIntBuffer()));
		version = buffer.getInt(0);

		if(version < FMOD_VERSION) {
			printfExit("Error!  You are using an old version of FMOD %08x.  This program requires %08x\n", version,
					FMOD_VERSION);
			return;
		}

		errorCheck(system.init(32, FMOD_INIT_NORMAL, null));

//		soundBuffer = Medias.loadMediaIntoMemory("/home/ben/.local/share/Trash/files/Viva La Vida/06_Yes.mp3");
//		exinfo = FMOD_CREATESOUNDEXINFO.allocate();
//		exinfo.setLength(soundBuffer.capacity());
		errorCheck(system.createStream("/home/ben/play/sample.wav", FMOD_SOFTWARE, null, sound));
//		exinfo.release();

		printf("===============================================================================\n");
		printf("Custom DSP example. Copyright (c) Firelight Technologies 2004-2009.\n");
		printf("===============================================================================\n");
		printf("Press 'f' to activate, deactivate user filter\n");
		printf("Press 'e' to quit\n");
		printf("\n");

		errorCheck(system.playSound(FMOD_CHANNEL_FREE, sound, false, channel));
		
		/*
		 * Create the DSP effects.
		 */
		{
			FMOD_DSP_DESCRIPTION dspdesc = FMOD_DSP_DESCRIPTION.allocate();

			dspdesc.setName("My first DSP unit");
			dspdesc.setChannels(0); // 0 = whatever comes in, else specify. 
			dspdesc.setRead(myDSPCallback);

			errorCheck(system.createDSP(dspdesc, mydsp));
		}

		/*
		 * Inactive by default.
		 */
		mydsp.setBypass(false);

		errorCheck(system.addDSP(mydsp, null));

		/*
		 * Main loop.
		 */



		boolean exit = false;
        boolean changed = true;
		do {
			switch(getKey()) {
				case 'f':
				case 'F':
					mydsp.setBypass(active);
					active = !active;
					break;
                case 'a':
                case 'A':
                    az -= 15;
                    changed = true;
                    break;
                case 's':
                case 'S':
                    el -= 15;
                    changed = true;
                    break;
                case 'd':
                case 'D':
                    az += 15;
                    changed = true;
                    break;
                case 'w':
                case 'W':
                    el += 15;
                    changed = true;
                    break;
				case 'e':
				case 'E':
					exit = true;
					break;
			}

            if (changed){
                hrir = hrtf.get(az, el);

                //this.print("delay: " + hrir.getDelay());
                this.print("Azimuth: " + hrir.getAzimuth() + " , Elevation: " + hrir.getElevation() + "\n");
                errorCheck(system.playSound(FMOD_CHANNEL_FREE, sound, false, channel));
                changed = false;
            }

			system.update();

			try {
				Thread.sleep(10);
			}
			catch(InterruptedException e) {}
		}
		while(!exit && !deinit);

		stop();
	}

	public boolean isRunning() { return deinit; }
	public void stop() {
		if(!init || deinit) return;
		deinit = true;

		print("\n");

		/*
		 * Shut down
		 */
		if(!sound.isNull()) {
			errorCheck(sound.release());
		}
		if(!mydsp.isNull()) {
			errorCheck(mydsp.release());
		}

		if(!system.isNull()) {
			errorCheck(system.close());
			errorCheck(system.release());
		}
	}
}