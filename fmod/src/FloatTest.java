import com.sun.org.apache.bcel.internal.generic.D2F;
import htape.util.HRTF;
import htape.util.HRTFFactory;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: ben
 * Date: 1/27/11
 * Time: 10:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class FloatTest {

    public static void main(String[] args) {
        try {

            HRTFFactory fac = new HRTFFactory();

            HRTF str = fac.fallbackWTF(new File("/home/ben/project/resources/hrtfs/listen/18.hrtf"));
            HRTF bin = fac.listenBinary(new File("/home/ben/18.hrtf_bin"));

            for (int i = 0; i < 512; i++) {
                if (str.get(0,0).getLeft(i) != bin.get(0,0).getLeft(i)){
                    System.out.println(str.get(0,0).getLeft(i) / bin.get(0,0).getLeft(i));

                }

                if (str.get(0, 0).getRight(i) != bin.get(0, 0).getRight(i)) {
                    System.out.println(str.get(0,0).getRight(i) / bin.get(0,0).getRight(i));
                }

            }

            System.out.println("ready to begin");





        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
