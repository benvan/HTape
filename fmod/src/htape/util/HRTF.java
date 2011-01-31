package htape.util;

public class HRTF implements IHRTF {

    HRIR[][] hrirs;
    private int maxEl=0, minEl=0;

    public HRTF(HRIR[][] positions) {
        this.hrirs = positions;
        for (int i = 0; i < positions.length; i++) {
            int el = positions[i][0].getElevation();
            if (el>maxEl){
                maxEl = el;
            }
            if (el < minEl) {
                minEl = el;
            }
        }
    }

    public HRIR get(int azimuth, int elevation) {


        int azIndex;
        int elIndex = 0;

        //find elevation
        elevation = Math.min(elevation, maxEl);
        elevation = Math.max(elevation, minEl);

        double max;
        double min = hrirs[0][0].getElevation();
        int minIndex = 0;



        for (int i = 0; i < hrirs.length; i++) {
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

        azIndex = (int) Math.round((posAz * (hrirs[elIndex].length-1)));

        return hrirs[elIndex][azIndex];
    }

    public HRIR get(double azimuth, double elevation) {
        int az = (int) (azimuth * 360 - 180);
        int el = (int) (elevation * 135 - 45);
        return get(az,el);
    }
}
