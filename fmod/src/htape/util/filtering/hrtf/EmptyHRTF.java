package htape.util.filtering.hrtf;

import htape.audio.EmptyFilter;
import htape.util.filtering.IFilter;

/**
 * Created by ben, on 3/10/11 at 8:00 PM
 */
public class EmptyHRTF implements IHRTF {

    HRIR hrir;

    public EmptyHRTF() {
        hrir = new HRIR(0,0, new float[]{});
    }

    public HRIR get(double azimuth, double elevation) {
        return hrir;
    }

    public HRIR[][] getHrirs() {
        return new HRIR[0][];
    }
}
