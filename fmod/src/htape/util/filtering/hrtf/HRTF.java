package htape.util.filtering.hrtf;

public class HRTF implements IHRTF {

    HRIR[][] hrirs;
    private int maxEl=0, minEl=0;

    public HRTF(HRIR[][] positions) {
        this.hrirs = positions;
        for (int i = 0; i < hrirs.length; i++) {
            int el = positions[i][0].getElevation();
            maxEl = Math.max(el, maxEl);
            minEl = Math.min(el, minEl);
        }
        System.out.println(maxEl);
        System.out.println(minEl);
    }

    public HRIR get(int azimuth, int elevation) {

        azimuth = ((azimuth % 360) + 360) % 360;

        double bitswitch;
        int azIndex = 0, elIndex = 0;

        //constrain coords
        elevation = Math.min(elevation, maxEl);
        elevation = Math.max(elevation, minEl);
        azimuth = Math.min(azimuth, 360);
        azimuth = Math.max(azimuth, 0);

        //get elevation
        int lower = 0;
        int upper = hrirs.length -1;
        boolean foundUpper = false;
        for (int i = 0; i < hrirs.length; i++) {
            int el = hrirs[i][0].getElevation();
            lower = (el < elevation) ? i : lower;
            if (el > elevation && !foundUpper){
                upper = i;
                foundUpper = true;
            }
        }
        if (lower == upper){
                elIndex = lower;
        }else{
            bitswitch = (double)(elevation - hrirs[lower][0].getElevation()) / (double)(hrirs[upper][0].getElevation() - hrirs[lower][0].getElevation());
            elIndex = lower + (int)Math.round(bitswitch);
        }


        //get azimuth
        lower = 0;
        upper = hrirs[elIndex].length -1;
        foundUpper = false;
        for (int i = 0; i < hrirs[elIndex].length; i++) {
            int az = hrirs[elIndex][i].getAzimuth();
            lower = (az < azimuth) ? i : lower;
            if (az > azimuth && !foundUpper) {
                upper = i;
                foundUpper = true;
            }
        }
        if (lower == upper){
                azIndex = lower;
        }else{
            bitswitch = (double)(azimuth - hrirs[elIndex][lower].getAzimuth()) / (double)(hrirs[elIndex][upper].getAzimuth() - hrirs[elIndex][lower].getAzimuth());
            azIndex = lower + (int)Math.round(bitswitch);
        }


        if (azIndex < 0 || elIndex < 0 || azIndex > hrirs[elIndex].length || elIndex > hrirs.length) {
            System.out.println("GOD DAMMIT!!!!");

        }

        return hrirs[elIndex][azIndex];
    }

    public HRIR[][] getHrirs(){
        return hrirs;
    }

    public HRIR get(double azimuth, double elevation) {
        int az = (int) (azimuth * 360 - 180);
        int el = (int) (elevation * 135 - 45);
        return get(az,el);
    }

}
