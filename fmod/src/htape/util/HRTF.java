package htape.util;

/**
 * Created by IntelliJ IDEA.
 * User: ben
 * Date: 1/23/11
 * Time: 7:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class HRTF {

    HRIR[][] hrirs;

    public HRTF(HRIR[][] positions) {
        this.hrirs = positions;
    }

    public HRIR get(int azimuth, int elevation) {
        return hrirs[azimuth][elevation];
    }
}
