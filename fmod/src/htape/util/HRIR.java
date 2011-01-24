package htape.util;

/**
 * Created by IntelliJ IDEA.
 * User: ben
 * Date: 1/23/11
 * Time: 7:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class HRIR {

    int azimuth;
    int elevation;
    float[] coefficients;
    int delay;

    public HRIR(int azimuth, int elevation, float[] coefficients, int delay) {
        this.coefficients = coefficients;
        this.delay = delay;
        this.azimuth = azimuth;
        this.elevation = elevation;
    }

    public float getLeft(int c){
        return coefficients[c];
    }

    public float getRight(int c){
        return coefficients[c+(coefficients.length / 2)];
    }

    public int getDelay(){
        return delay;
    }

    public int getAzimuth(){
        return azimuth;
    }

    public int getElevation(){
        return elevation;
    }

}