package htape.gui;

import htape.util.ExtendedHistoryBuffer;
import htape.util.filtering.hrtf.HRIR;
import htape.util.filtering.hrtf.HRTF;
import htape.util.filtering.hrtf.HRTFFactory;
import htape.util.filtering.hrtf.IHRTF;
import htape.util.io.UnrecognisedHRTFException;
import org.jouvieje.fmodex.*;
import org.jouvieje.fmodex.System;
import org.jouvieje.fmodex.callbacks.FMOD_DSP_READCALLBACK;
import org.jouvieje.fmodex.enumerations.FMOD_RESULT;
import org.jouvieje.fmodex.exceptions.InitException;
import org.jouvieje.fmodex.structures.FMOD_CREATESOUNDEXINFO;
import org.jouvieje.fmodex.structures.FMOD_DSP_DESCRIPTION;
import org.jouvieje.fmodex.structures.FMOD_DSP_STATE;
import org.jouvieje.fmodex.utils.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;

import static org.jouvieje.fmodex.defines.FMOD_INITFLAGS.FMOD_INIT_NORMAL;
import static org.jouvieje.fmodex.defines.FMOD_MODE.FMOD_SOFTWARE;
import static org.jouvieje.fmodex.defines.VERSIONS.*;
import static org.jouvieje.fmodex.enumerations.FMOD_CHANNELINDEX.FMOD_CHANNEL_FREE;
import static org.jouvieje.fmodex.enumerations.FMOD_RESULT.FMOD_ERR_DSP_NOTFOUND;
import static org.jouvieje.fmodex.enumerations.FMOD_RESULT.FMOD_OK;
import static org.jouvieje.fmodex.utils.BufferUtils.SIZEOF_INT;
import static org.jouvieje.fmodex.utils.BufferUtils.newByteBuffer;


public class HRTFTest {
	private static final long serialVersionUID = 1L;

	private boolean init = false;
	private boolean deinit = false;

    private IHRTF hrtf;
    private HRIR hrir;
    private int elevation;
    private int azimuth;
    private Channel channel;
    private HashMap<String, IHRTF> hrtfMap;

    public HRTFTest() {
        hrtfMap = new HashMap<String, IHRTF>();
        init();
        run();
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
    }

    public void setAzimuth(int azimuth) {
        this.azimuth = azimuth;
    }

    public void update(int az, int el) {
        setAzimuth(az);
        setElevation(el);
        hrir = hrtf.get(az, el);
    }







	private void errorCheck(FMOD_RESULT result) {
		if(result != FMOD_RESULT.FMOD_OK) {
			printfExit("FMOD error! (%d) %s\n", result.asInt(), FmodEx.FMOD_ErrorString(result));
		}
	}

    private void printfExit(String s, int nativefmodexLibraryVersion, String nativefmodexJarVersion) {
        //To change body of created methods use File | Settings | File Templates.
    }

    private boolean active = false;

	private System system = new System();
	private Sound sound = new Sound();
	private DSP mydsp = new DSP();

	private FMOD_DSP_READCALLBACK myDSPCallback = new FMOD_DSP_READCALLBACK(){
		ByteBuffer nameBuffer = newByteBuffer(256);

        ExtendedHistoryBuffer lhist = new ExtendedHistoryBuffer(512, 0);
        ExtendedHistoryBuffer rhist = new ExtendedHistoryBuffer(512, 0);

        int pos = 0;

		public FMOD_RESULT FMOD_DSP_READCALLBACK(FMOD_DSP_STATE dsp_state, FloatBuffer inbuffer, FloatBuffer outbuffer,
				int length, int inchannels, int outchannels) {

            if (hrir == null) {
                return FMOD_ERR_DSP_NOTFOUND;
            }

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


            for (int sample = 0; sample < length; sample++) {

                lhist.add(inbuffer.get());
                rhist.add(inbuffer.get());

                float l = 0, r = 0;
                int limit = Math.min(lhist.length(), hrir.getTaps());

                for (int i = 0; i < limit; i++) {
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
		try {
			Init.loadLibraries();
		}
		catch(InitException e) {
			java.lang.System.err.println(String.format("NativeFmodEx error! %s\n", e.getMessage()));
			java.lang.System.exit(1);
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

		ByteBuffer soundBuffer;
		FMOD_CREATESOUNDEXINFO exinfo;
        channel = new Channel();
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
		errorCheck(system.createStream("/home/ben/thermo.wav", FMOD_SOFTWARE, null, sound));
//		exinfo.release();

		printf("===============================================================================\n");
		printf("Custom DSP example. Copyright (c) Firelight Technologies 2004-2009.\n");
		printf("===============================================================================\n");
		printf("Press 'f' to activate, deactivate user filter\n");
		printf("Press 'e' to quit\n");
		printf("\n");

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

        mydsp.setBypass(false);
		errorCheck(system.addDSP(mydsp, null));


	}

    public void play() {
        errorCheck(system.playSound(FMOD_CHANNEL_FREE, sound, false, channel));
    }

    private void printf(String s) {
        //To change body of created methods use File | Settings | File Templates.
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

    private void print(String s) {
        //To change body of created methods use File | Settings | File Templates.
    }

    public void loadHRTF(String s) throws IOException, UnrecognisedHRTFException {

        java.lang.System.out.println("Loading " + s);

        if (hrtfMap.containsKey(s)) {
            java.lang.System.out.println("Using cached version");
            hrtf = hrtfMap.get(s);
        }else{
            HRTFFactory fac = new HRTFFactory();
            hrtf = fac.fromFile(s);
            hrtfMap.put(s, hrtf);

            hrir = hrtf.get(azimuth, elevation);
        }
    }

    public void loadWav(String s) {
        errorCheck(system.createStream(s, FMOD_SOFTWARE, null, sound));
    }

    public IHRTF getHRTF() {
        return hrtf;
    }

    private void printfExit(String s, Object... args) {
        java.lang.System.out.print(String.format(s, args));
        java.lang.System.exit(1);
    }
}
