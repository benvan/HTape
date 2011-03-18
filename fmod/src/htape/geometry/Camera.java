package htape.geometry;

/**
 * Created by ben, on 3/16/11 at 10:53 PM
 */
public class Camera {

    Point u,v,w;
    Point c;
    double f = -300; //focal length

    double fov = 10;
    double nearPlane = 10;
    double farPlane = 100000;
	
    private float[] binding;

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

    public void transform(Matrix m) {
        u = m.mult(u).toPoint();
        v = m.mult(v).toPoint();
        w = m.mult(w).toPoint();
    }

    public double getFocalLength(){
        return f;
    }

    public void setFocalLength(double focalLength) {
        this.f = focalLength;
    }

    public String info() {
        return String.format("x: %.2f\ny: %.2f\n z:%.2f", c.getX(), c.getY(), c.getZ());
    }

	public void alignToBinding() {
		if (binding == null) return;
		
		int scale = 20;
    	double zOffset = 0.7;
    	
    	c.setX(binding[0]*scale);
    	c.setY(binding[1]*scale);
    	c.setZ(zOffset-binding[2]*scale);
	}

	public void bind(float[] pos) {
		binding = pos;
	}
}
