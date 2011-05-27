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
	private float zx=0,zy=0,zz=0;
	public float px=0,py=0,pz=0;

    public Camera() {
        c = new Point();
        resetOrientiation();

    }

	private void resetOrientiation() {
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
        return String.format(
        		"pos: %.2f %.2f %.2f\n\n" +
        		"raw: %.2f %.2f %.2f\n" +
        		"rad: %.2f %.2f %.2f",
        		c.getX(), c.getY(), c.getZ(),
        		binding[3], binding[4], binding[5],
        		Math.toRadians(binding[3] - zx), Math.toRadians(binding[4] - zy), Math.toRadians(binding[5] - zz)
        		);
    }

	public void alignToBinding() {
		if (binding == null) return;
		
		int scale = 40;
    	double zOffset = 0.4;
    	
    	c.setX(binding[0]*scale +px);
    	c.setY(binding[1]*scale +py);
    	c.setZ(binding[2]*scale +pz);
    	
    	resetOrientiation();
    	transform(
			Matrix.rotZ(Math.toRadians(binding[5] - zz))
			.mult(Matrix.rotY(Math.toRadians(binding[4] - zy)))
			.mult(Matrix.rotX(Math.toRadians(binding[3] - zx)))
		);
    	
	}

	public void bind(float[] pos) {
		binding = pos;
	}
	
	public void zero(){
		zx = binding[3];
		zy = binding[4];
		zz = binding[5];
		
	}

    public void translate(double x, double y, double z) {
        
        c = new Matrix(new double[][]{
                {u.getX(), v.getX(), w.getX(), x},
                {u.getY(), v.getY(), w.getY(), y},
                {u.getZ(), v.getZ(), w.getZ(), z},
                {0,0,0,1}
        }).mult(c).toPoint();
    }
}
