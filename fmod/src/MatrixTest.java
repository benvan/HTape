import htape.World;
import htape.geometry.Matrix;
import htape.geometry.Point;
import htape.geometry.PointSource;
import htape.gui.WorldView;
import htape.util.filtering.hrtf.HRTFFactory;
import htape.util.io.UnrecognisedHRTFException;

import java.io.IOException;

/**
 * Created by ben, on 3/11/11 at 1:07 PM
 */
public class MatrixTest {

    public static void main(String[] args) throws InterruptedException {

        World world = new World();
        PointSource p = world.createSource();
        world.bind(p, "media/sample.wav");
        world.move(p, new Point(-300, -200, 0));
        

        double ang = 0.5;
        Matrix m = new Matrix(new double[][]{
            {1,0,0,0},
            {0,Math.cos(ang),-Math.sin(ang),0},
            {0,Math.sin(ang),Math.cos(ang),0},
            {0,0,0,1}
        });

        m = m.mult(new Matrix(new double[][]{
            {Math.cos(ang),-Math.sin(ang),0,0},
            {Math.sin(ang),Math.cos(ang),0,0},
            {0,0,1,0},
            {0,0,0,1}
        }));

        //world.getCamera().transform(m);
        world.getCamera().setPos(new Point(0,0,-1000));
        world.getCamera().setFocalLength(1000);



        try {
            world.setHRTF(new HRTFFactory().fromFile("/home/ben/subject_008.hrtf.cipic.bin"));
        } catch (UnrecognisedHRTFException e) {
            System.err.println("Failed to recognise HRTF format.");
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Failed to read HRTF file.");
            System.exit(0);
        }

        WorldView view = new WorldView(world);
        

        view.run();





    }


}
