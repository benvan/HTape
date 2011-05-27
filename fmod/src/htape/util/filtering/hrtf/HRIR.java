package htape.util.filtering.hrtf;

import htape.util.filtering.IFilter;

/**
 * Created by IntelliJ IDEA.
 * User: ben
 * Date: 1/23/11
 * Time: 7:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class HRIR implements IFilter{

    double azimuth;
    double elevation;
    float[] coefficients;

    public HRIR(double azimuth, double elevation, float[] coefficients) {
        this.coefficients = coefficients;
        this.azimuth = azimuth;
        this.elevation = elevation;
    }

    public float getLeft(int c) {
        return coefficients[c];
    }

    public float getRight(int c){
        return coefficients[c+(coefficients.length / 2)];
    }

    public double getAzimuth(){
        return azimuth;
    }

    public double getElevation(){
        return elevation;
    }

    public int getTaps() {
        return coefficients.length/2;
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", azimuth, elevation);
    }
}
