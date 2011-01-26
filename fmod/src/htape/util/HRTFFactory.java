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
public class HRTFFactory {

    public HRTF baretto(File f) throws FileNotFoundException {

        Scanner s = new Scanner(f);
        int[] azimuths = new int[]{180,150,120,90,60,30,0,-30,-60,-90,-120,-150};
        int[] elevations = new int[]{54,36,18,0,-18,-36};
        HRIR[][] hrirs = new HRIR[azimuths.length][elevations.length];

        for (int i = 0; i < azimuths.length; i++) {
            for (int j = 0; j < elevations.length; j++) {
                hrirs[i][j] = createBarettoHRIR(azimuths[i], elevations[j],s);
            }
        }

        return new HRTF(hrirs);

    }

    public HRTF listen(File f) throws FileNotFoundException {

        Scanner s = new Scanner(f);
        int[][] positions = new int[][]{
                {-45,24},
                {-30,24},
                {-15,24},
                {0,24},
                {15,24},
                {30,24},
                {45,24},
                {60,12},
                {75,6},
                {90,1}
        };
        HRIR[][] hrirs = new HRIR[10][];

        for (int i = 0; i < positions.length; i++) {
            HRIR[] ring = new HRIR[positions[i][1]];
            for (int j = 0; j < ring.length; j++) {
                ring[j] = createListenHRIR((360 / positions[i][1]) * j, positions[i][0], s);
            }
            hrirs[i] = ring;
        }

        return new HRTF(hrirs);

    }


    private HRIR createListenHRIR(int azimuth, int elevation, Scanner s) {
        float[] coeffs = new float[1024];
        int delay;
        for (int i = 0; i < 1024; i++) {
            coeffs[i] = s.nextFloat();
        }
        return new HRIR(azimuth, elevation, coeffs);
    }


    private HRIR createBarettoHRIR(int azimuth, int elevation, Scanner s) {
        float[] coeffs = new float[512];
        int delay;
        for (int i = 0; i < 512; i++) {
            coeffs[i] = s.nextFloat();
        }
        delay = (int)s.nextFloat();
        return new DelayedHRIR(azimuth, elevation, coeffs, delay);
    }

}
