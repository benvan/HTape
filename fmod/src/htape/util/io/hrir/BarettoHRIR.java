package htape.util.io.hrir;

import htape.util.filtering.hrtf.DelayedHRIR;
import htape.util.filtering.hrtf.HRIR;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: ben
 * Date: 1/27/11
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class BarettoHRIR implements IHRIRReader {


    public HRIR read(DataInputStream in, int azimuth, int elevation) throws IOException {
        float[] coeffs = new float[512];
        int delay;
        for (int i = 0; i < 512; i++) {
            coeffs[i] = in.readFloat();
        }
        delay = (int)in.readFloat();
        return new DelayedHRIR(azimuth, elevation, coeffs, delay);
    }
}
