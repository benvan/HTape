package htape.util;

public class HRTF implements IHRTF {

    HRIR[][] hrirs;

    public HRTF(HRIR[][] positions) {
        this.hrirs = positions;
    }

    public HRIR get(int azimuth, int elevation) {


        int azIndex = 0;
        int elIndex = 0;

        //find elevation

        double min = hrirs[0][0].getElevation();
        int minIndex = 0;

        double max;

        for (int i = 1; i < hrirs.length; i++) {
            if (hrirs[i][0].getElevation() < elevation ){
                min = hrirs[i][0].getElevation();
                minIndex = i;
            }
            if ((max = hrirs[i][0].getElevation()) > elevation ){
                //found boundary
                int offset = (int) Math.round((elevation-min)/(max-min));
                elIndex = minIndex+offset;
            }
        }

        //find azimuth
        double posAz = (double)(((azimuth % 360) + 360) % 360) / 360;
        System.out.println(posAz);
        azIndex = (int) Math.round((posAz * hrirs[elIndex].length));
        System.out.println(posAz * hrirs[elIndex].length);

        return hrirs[elIndex][azIndex];
    }
}
