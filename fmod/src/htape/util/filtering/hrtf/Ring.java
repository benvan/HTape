package htape.util.filtering.hrtf;

import com.sun.java.swing.plaf.gtk.GTKConstants;

/**
 * Created by IntelliJ IDEA.
 * User: ben
 * Date: 2/4/11
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class Ring {

    HRTF.Orientation orientation;
    int[] positions;
    HRIR[] hrirs;

    public Ring(HRTF.Orientation orientation, HRIR[] hrirs) {
        this.orientation = orientation;
        this.hrirs = hrirs;
        positions = new int[positions.length];

        for (int i = 0; i < hrirs.length; i++) {
            if (orientation == HRTF.Orientation.HORIZONTAL) {
                //fixed azimuth
                positions[i] = hrirs[i].getElevation();
            }else{
                //fixed elevation
                positions[i] = hrirs[i].getAzimuth();
            }
        }

    }

    public int[] getPositions(){
        return positions;
    }
}
