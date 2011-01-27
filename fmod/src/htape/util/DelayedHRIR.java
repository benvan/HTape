package htape.util;

/**
 * Created by IntelliJ IDEA.
 * User: ben
 * Date: 1/23/11
 * Time: 7:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class DelayedHRIR extends HRIR {

    int delay;

    public DelayedHRIR(int azimuth, int elevation, float[] coefficients, int delay) {
        super(azimuth, elevation, coefficients);
        this.delay = delay;
        processDelay();
    }

    private void processDelay() {

        int length = coefficients.length;
        int mid = length / 2;

        float[] delayedCoeffs = new float[length + (Math.abs(delay)*2)];

        int ldelay = (delay > 0) ? 0 : -delay;
        int rdelay = (delay > 0) ? delay : 0;

        System.arraycopy(coefficients, 0, delayedCoeffs, ldelay, mid);
        System.arraycopy(coefficients, mid,delayedCoeffs, mid + ldelay+rdelay, mid);

        this.coefficients = delayedCoeffs;

    }

    public int getDelay(){
        return delay;
    }

}
