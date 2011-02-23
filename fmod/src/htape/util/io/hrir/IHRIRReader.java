package htape.util.io.hrir;

import htape.util.filtering.hrtf.HRIR;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: ben
 * Date: 1/27/11
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IHRIRReader {

    HRIR read(DataInputStream in, double azimuth, double elevation ) throws IOException;

}
