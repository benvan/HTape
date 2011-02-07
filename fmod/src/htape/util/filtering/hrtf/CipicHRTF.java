package htape.util.filtering.hrtf;

public class CipicHRTF implements IHRTF {

    private HRIR[][] positions;

    public CipicHRTF(HRIR[][] positions) {
        this.positions = positions;
    }

    public HRIR get(int azimuth, int elevation) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public HRIR[][] getHrirs() {
        return new HRIR[0][];  //To change body of implemented methods use File | Settings | File Templates.
    }
}
