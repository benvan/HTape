import htape.util.filtering.hrtf.HRTF;
import htape.util.filtering.hrtf.HRTFFactory;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: ben
 * Date: 1/27/11
 * Time: 10:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class FloatTest {

    public static void main(String[] args) {

        int[] cipic_azimuths = new int[]{-80, -65, -55, -45, -40, -35, -30, -25, -20, -15, -10, -5, 0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 55, 65, 80};

        for (int i = 0; i < cipic_azimuths.length; i++) {
            int azimuth = cipic_azimuths[i];
            System.out.print(azimuth);
            System.out.print(": ");
            for (double j = -45; j < 231; j += 5.625){
                System.out.print(" " + j);
            }
            System.out.println();
        }

    }

}
