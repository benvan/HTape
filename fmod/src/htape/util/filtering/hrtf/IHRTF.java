package htape.util.filtering.hrtf;

/**
 * Created by IntelliJ IDEA.
 * User: ben
 * Date: 1/26/11
 * Time: 7:03 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IHRTF {
    HRIR get(double azimuth, double elevation);
    HRIR[][] getHrirs();
}
