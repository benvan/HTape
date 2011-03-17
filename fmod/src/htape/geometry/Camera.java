package htape.geometry;

/**
 * Created by ben, on 3/16/11 at 10:53 PM
 */
public class Camera {

    Point u,v,w;
    Point c;

    public Camera() {
        c = new Point();
        u = new Point();
        v = new Point();
        w = new Point();

        u.x = 1;
        v.y = 1;
        w.z = 1;

    }

    public Point getU() {
        return u;
    }

    public Point getV() {
        return v;
    }

    public Point getW() {
        return w;
    }

    public void setPos(Point c) {
        this.c = c;
    }

    public Point getPos() {
        return c;
    }
}
