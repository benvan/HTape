package htape;

import htape.util.StereoHistoryBuffer;
import htape.util.filtering.IFilter;
import org.jouvieje.fmodex.*;
import org.jouvieje.fmodex.System;
import org.jouvieje.fmodex.callbacks.FMOD_DSP_READCALLBACK;
import org.jouvieje.fmodex.enumerations.FMOD_RESULT;
import org.jouvieje.fmodex.exceptions.InitException;
import org.jouvieje.fmodex.structures.FMOD_DSP_DESCRIPTION;

import static org.jouvieje.fmodex.defines.FMOD_INITFLAGS.FMOD_INIT_NORMAL;
import static org.jouvieje.fmodex.defines.FMOD_MODE.FMOD_SOFTWARE;
import static org.jouvieje.fmodex.defines.VERSIONS.*;
import static org.jouvieje.fmodex.enumerations.FMOD_CHANNELINDEX.FMOD_CHANNEL_FREE;


public class Player {
    private static final long serialVersionUID = 1L;

    private boolean init = false;
    private boolean deinit = false;

    private System system = new System();
    private Sound sound = new Sound();
    private DSP mydsp = new DSP();
    private FMOD_DSP_READCALLBACK myDSPCallback;

    private Channel channel;
    private IFilter filter;
    private StereoHistoryBuffer hist = new StereoHistoryBuffer();

    public Player() {
        myDSPCallback = new HistoricalDSPCallback();
        init();
        run();
    }

    public void init() {
        try {
            Init.loadLibraries();
        } catch (InitException e) {
            java.lang.System.err.println(String.format("NativeFmodEx error! %s\n", e.getMessage()));
            java.lang.System.exit(1);
        }

        if (NATIVEFMODEX_LIBRARY_VERSION != NATIVEFMODEX_JAR_VERSION) {
            printfExit("Error!  NativeFmodEx library version (%08x) is different to jar version (%08x)\n",
                    NATIVEFMODEX_LIBRARY_VERSION, NATIVEFMODEX_JAR_VERSION);
            return;
        }

        init = true;
    }

    public void setFilter(IFilter filter) {
        this.filter = filter;
        hist.resize(filter.getTaps());
        mydsp.setBypass(false);
    }




    public void run() {

        channel = new Channel();

        //Create a System object and initialize.
        errorCheck(FmodEx.System_Create(system));
        errorCheck(system.init(32, FMOD_INIT_NORMAL, null));

        {
            FMOD_DSP_DESCRIPTION dspdesc = FMOD_DSP_DESCRIPTION.allocate();
            dspdesc.setChannels(0); // 0 = whatever comes in, else specify.
            dspdesc.setRead(myDSPCallback);
            errorCheck(system.createDSP(dspdesc, mydsp));
        }

        mydsp.setBypass(true);
        errorCheck(system.addDSP(mydsp, null));
    }

    public void play() {
        errorCheck(system.playSound(FMOD_CHANNEL_FREE, sound, false, channel));
    }

    public boolean isRunning() {
        return deinit;
    }

    public void stop() {
        if (!init || deinit) return;
        deinit = true;


        //shutdown
        if (!sound.isNull()) {
            errorCheck(sound.release());
        }
        if (!mydsp.isNull()) {
            errorCheck(mydsp.release());
        }

        if (!system.isNull()) {
            errorCheck(system.close());
            errorCheck(system.release());
        }
    }

    public void loadAudioFile(String s) {
        java.lang.System.out.println("Loading: " + s);
        errorCheck(system.createStream(s, FMOD_SOFTWARE, null, sound));
    }

    private void printfExit(String s, Object... args) {
        java.lang.System.out.print(String.format(s, args));
        java.lang.System.exit(1);
    }

    private void errorCheck(FMOD_RESULT result) {
        if (result != FMOD_RESULT.FMOD_OK) {
            printfExit("FMOD error! (%d) %s\n", result.asInt(), FmodEx.FMOD_ErrorString(result));
        }
    }

}
