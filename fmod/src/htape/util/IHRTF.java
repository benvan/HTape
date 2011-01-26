package htape.util;

/**
 * Created by IntelliJ IDEA.
 * User: ben
 * Date: 1/26/11
 * Time: 7:03 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IHRTF {
    HRIR get(int azimuth, int elevation);
}
