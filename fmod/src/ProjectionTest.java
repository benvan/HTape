import htape.geometry.Matrix;
import htape.geometry.Point;


public class ProjectionTest {
	
	public static void main(String args[]){
		
        //double az = Math.toRadians(90);
        double el = Math.toRadians(90);
        //Point p = new Point(Math.cos(az), 0, Math.sin(az));
        Point p = new Point(0,1,1);

        double c = Math.cos(el), s = Math.sin(el);

        Matrix m = Matrix.rotX(el);
        System.out.print("rotX: ");
        System.out.println(m.mult(p).toPoint().toString());
        
        m = Matrix.rotY(el);
        System.out.print("rotY: ");
        System.out.println(m.mult(p).toPoint().toString());
        
        m = Matrix.rotZ(el);
        System.out.print("rotZ: ");
        System.out.println(m.mult(p).toPoint().toString());
        
	}

}
