package htape.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: ben
 * Date: 1/24/11
 * Time: 6:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListenHRTFFactory {

    public  HRTF createFromFile(File f) throws FileNotFoundException {

        Scanner s = new Scanner(f);
        int[] azimuths = new int[]{180,150,120,90,60,30,0,-30,-60,-90,-120,-150};
        int[] elevations = new int[]{54,36,18,0,-18,-36};
        HRIR[][] hrirs = new HRIR[azimuths.length][elevations.length];

        for (int i = 0; i < azimuths.length; i++) {
            for (int j = 0; j < elevations.length; j++) {
                hrirs[i][j] = createHRIR(azimuths[i], elevations[j],s);
            }
        }

        return new HRTF(hrirs);

    }

    private HRIR createHRIR(int azimuth, int elevation, Scanner s) {
        float[] coeffs = new float[512];
        int delay;
        for (int i = 0; i < 512; i++) {
            coeffs[i] = s.nextFloat();
        }
        return new HRIR(azimuth, elevation, coeffs);
    }

}
