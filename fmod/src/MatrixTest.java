import htape.World;
import htape.geometry.Matrix;
import htape.geometry.Point;
import htape.geometry.PointSource;

/**
 * Created by ben, on 3/11/11 at 1:07 PM
 */
public class MatrixTest {

    public static void main(String[] args) {

        World w = new World();
        PointSource p = w.createSource();
        w.bind(p, "media/sample.wav");
        w.move(p, new Point(5,5,0));

        w.update();


    }


}
