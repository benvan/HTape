package htape.geometry;

/**
 * Created by ben, on 3/10/11 at 1:20 PM
 */
public class Point {

    double x,y,z;

    public Point() {
        this(0, 0, 0);
    }

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double dot(Point v) {
        return x*v.x + y*v.y + z*v.z;
    }

    public Point dup() {
        return new Point(x,y,z);
    }
}
