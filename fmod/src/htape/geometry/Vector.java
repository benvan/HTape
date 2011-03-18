package htape.geometry;

/**
 * Created by ben, on 3/11/11 at 12:54 PM
 */
public class Vector {

    double v[];

    public Vector(int size) {
        v = new double[size];
        for (int i = 0; i < v.length; i++) {
            v[i] = 0;
        }
    }

    public int size(){
        return v.length;
    }

    public double[] elements(){
        return v;
    }

}
