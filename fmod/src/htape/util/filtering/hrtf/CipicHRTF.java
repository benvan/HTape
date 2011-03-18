package htape.util.filtering.hrtf;

public class CipicHRTF implements IHRTF {

    HRIR[][] hrirs;
    private double maxEl=230.625;
    private double minEl=-45;

    public CipicHRTF(HRIR[][] positions) {
        this.hrirs = positions;
    }

    public HRIR get(double azimuth, double elevation) {

        double bitswitch;
        int azIndex = 0, elIndex = 0;

        //constrain coords
        if (elevation > maxEl){
        	double dMax = elevation - maxEl;
        	double dMin = (elevation - 360) - minEl;
        	elevation = (dMax < Math.abs(dMin)) ? dMax : (dMin < 0) ? minEl : 360 - elevation; 
        }
        
        azimuth = Math.min(azimuth, 80);
        azimuth = Math.max(azimuth, -80);

        //get azimuth
        int lower = 0;
        int upper = hrirs.length -1;
        boolean foundUpper = false;
        for (int i = 0; i < hrirs.length; i++) {
            double az = hrirs[i][0].getAzimuth();
            lower = (az < azimuth) ? i : lower;
            if (az > azimuth && !foundUpper) {
                upper = i;
                foundUpper = true;

            }
        }
        if (lower == upper){
                azIndex = lower;
        }else{
            bitswitch = (azimuth - hrirs[lower][0].getAzimuth()) / (double)(hrirs[upper][0].getAzimuth() - hrirs[lower][0].getAzimuth());
            azIndex = lower + (int)Math.round(bitswitch);
        }


        //get elevation
        lower = 0;
        upper = hrirs[elIndex].length -1;
        foundUpper = false;
        for (int i = 0; i < hrirs[azIndex].length; i++) {
            double el = hrirs[azIndex][i].getElevation();
            lower = (el < elevation) ? i : lower;
            if (el > elevation && !foundUpper){
                upper = i;
                foundUpper = true;
            }
        }
        if (lower == upper){
                elIndex = lower;
        }else{
            bitswitch = (elevation - hrirs[azIndex][lower].getElevation()) / (hrirs[azIndex][upper].getElevation() - hrirs[azIndex][lower].getElevation());
            elIndex = lower + (int)Math.round(bitswitch);
        }

        if (azIndex < 0 || elIndex < 0 || elIndex > hrirs[azIndex].length || azIndex > hrirs.length) {
            System.out.println("GOD DAMMIT!!!!");

        }

        return hrirs[azIndex][elIndex];
    }

    public HRIR[][] getHrirs(){
        return hrirs;
    }

}
