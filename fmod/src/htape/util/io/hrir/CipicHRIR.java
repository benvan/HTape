package htape.util.io.hrir;

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
public class CipicHRIR implements IHRIRReader {


    public HRIR read(DataInputStream in, double azimuth, double elevation) throws IOException {
        float[] coeffs = new float[400];
        for (int i = 0; i < 400; i++) {
            coeffs[i] = in.readFloat();
        }
        return new HRIR(azimuth, elevation, coeffs);
    }
}
