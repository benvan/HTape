package htape.audio;

import htape.audio.EmptyFilter;
import htape.util.HistoryBuffer;
import htape.util.StereoHistoryBuffer;
import htape.util.filtering.IFilter;
import org.jouvieje.fmodex.callbacks.FMOD_DSP_READCALLBACK;
import org.jouvieje.fmodex.enumerations.FMOD_RESULT;
import org.jouvieje.fmodex.structures.FMOD_DSP_STATE;

import java.nio.FloatBuffer;

import static org.jouvieje.fmodex.enumerations.FMOD_RESULT.FMOD_OK;

/**
* Created by ben, on 3/10/11 at 7:43 PM
*/
public class HistoricalDSPCallback implements FMOD_DSP_READCALLBACK {

    int pos = 0;
    StereoHistoryBuffer hist;
    IFilter filter;

    public HistoricalDSPCallback(int initialSize, IFilter filter) {
        this(filter);
        hist.resize(initialSize);
    }

    public HistoricalDSPCallback(IFilter filter) {
        hist = new StereoHistoryBuffer();
        this.filter = filter;
    }

    public HistoricalDSPCallback() {
        this(new EmptyFilter());
    }

    public FMOD_RESULT FMOD_DSP_READCALLBACK(FMOD_DSP_STATE dsp_state, FloatBuffer inbuffer, FloatBuffer outbuffer,
                                             int length, int inchannels, int outchannels) {

        for (int sample = 0; sample < length; sample++) {

            hist.getLeft().add(inbuffer.get());
            hist.getRight().add(inbuffer.get());

            float l = 0, r = 0;
            int limit = Math.min(hist.length(), filter.getTaps());

            for (int i = 0; i < limit; i++) {
                l += hist.getLeft().get(i) * filter.getLeft(i);
                r += hist.getRight().get(i) * filter.getRight(i);
            }

            outbuffer.put(l);
            outbuffer.put(r);

        }
        inbuffer.rewind();
        outbuffer.rewind();

        return FMOD_OK;
    }
}
