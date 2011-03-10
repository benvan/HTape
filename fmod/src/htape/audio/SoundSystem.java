package htape.audio;

import htape.geometry.SourceTag;
import org.jouvieje.fmodex.*;
import org.jouvieje.fmodex.System;
import org.jouvieje.fmodex.enumerations.FMOD_RESULT;
import org.jouvieje.fmodex.exceptions.InitException;

import java.util.ArrayList;
import java.util.Collection;

import static org.jouvieje.fmodex.defines.FMOD_INITFLAGS.FMOD_INIT_NORMAL;
import static org.jouvieje.fmodex.defines.FMOD_MODE.FMOD_SOFTWARE;
import static org.jouvieje.fmodex.defines.VERSIONS.NATIVEFMODEX_JAR_VERSION;
import static org.jouvieje.fmodex.defines.VERSIONS.NATIVEFMODEX_LIBRARY_VERSION;
import static org.jouvieje.fmodex.enumerations.FMOD_CHANNELINDEX.FMOD_CHANNEL_FREE;

/**
 * Created by ben, on 3/10/11 at 1:42 PM
 */
public class SoundSystem {

    Collection<ISource> sources;
    org.jouvieje.fmodex.System fmodSystem;
    org.jouvieje.fmodex.Channel fmodChannel;
    org.jouvieje.fmodex.DSP fmodDSP;

    public SoundSystem() {
        sources = new ArrayList<ISource>();
        fmodSystem = new System();
        fmodChannel = new Channel();
        fmodDSP = new DSP();



        init();
    }

    private void init() {
        try {
			Init.loadLibraries();
		}
		catch(InitException e) {
			printfExit("NativeFmodEx error! %s\n", e.getMessage());
		}

		if(NATIVEFMODEX_LIBRARY_VERSION != NATIVEFMODEX_JAR_VERSION) {
			printfExit("Error!  NativeFmodEx library version (%08x) is different to jar version (%08x)\n",
                    NATIVEFMODEX_LIBRARY_VERSION, NATIVEFMODEX_JAR_VERSION);
		}

        //Create a System object and initialize.
		errorCheck(FmodEx.System_Create(fmodSystem));
		errorCheck(fmodSystem.init(32, FMOD_INIT_NORMAL, null));

    }

    public ISource createSource() {
        ISource source = new SoundSource();
        sources.add(source);
        return source;
    }

    public void play(ISource source) {
        Sound s = source.getSound();
        fmodSystem.playSound(FMOD_CHANNEL_FREE, s, false, fmodChannel);
    }

    public void bind(ISource source, String file) {
        Sound s = source.getSound();
        fmodSystem.createStream(file, FMOD_SOFTWARE, null, s);
    }

    private void printfExit(String s, Object... args) {
        java.lang.System.out.print(String.format(s, args));
        java.lang.System.exit(1);
    }

    private void errorCheck(FMOD_RESULT result) {
		if(result != FMOD_RESULT.FMOD_OK) {
			printfExit("FMOD error! (%d) %s\n", result.asInt(), FmodEx.FMOD_ErrorString(result));
		}
	}
}
