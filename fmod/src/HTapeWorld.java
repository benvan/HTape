import htape.World;
import htape.audio.ISource;
import htape.geometry.Point;
import htape.geometry.PointSource;
import htape.geometry.SourceTag;

/**
 * Created by ben, on 3/10/11 at 2:22 PM
 */
public class HTapeWorld {

    public static void main(String[] args) {
        World w = new World();
        PointSource p = w.createSource();

        w.bind(p, "media/sample.wav");
        w.move(p, new Point(10, 0, 0));
        w.play(p);


    }


}
