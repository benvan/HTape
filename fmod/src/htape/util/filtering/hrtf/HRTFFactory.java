package htape.util.filtering.hrtf;

import htape.util.io.UnrecognisedHRTFException;
import htape.util.io.hrir.*;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: ben
 * Date: 1/24/11
 * Time: 6:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class HRTFFactory {


    private final static int[][] baretto_positions;
    private final static int[][] listen_positions;
    private final static int[][] cipic_positions;

    static {
        baretto_positions = new int[][]{{-36, 12}, {-18, 12}, {0, 12}, {18, 12}, {36, 12}, {54, 12}};
        listen_positions = new int[][]{{-45, 24}, {-30, 24}, {-15, 24}, {0, 24}, {15, 24}, {30, 24}, {45, 24}, {60, 12}, {75, 6}, {90, 1}};

        int[] cipic_azimuths = new int[]{-80, -65, -55, -45, -40, -35, -30, -25, -20, -15, -10, -5, 0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 55, 65, 80};
        cipic_positions = new int[cipic_azimuths.length][2];
        for (int i = 0; i < cipic_azimuths.length; i++) {
            cipic_positions[i] = new int[]{cipic_azimuths[i], 50};
        }

    }

    private HRIR[][] createHRIRs(int[][] positions, DataInputStream in, IHRIRReader reader) throws IOException {

        HRIR[][] hrirs = new HRIR[positions.length][];
        for (int i = 0; i < positions.length; i++) {
            HRIR[] ring = new HRIR[positions[i][1]];
            for (int j = 0; j < ring.length; j++) {
                ring[j] = reader.read(in, (360 / positions[i][1]) * j, positions[i][0]);
            }
            hrirs[i] = ring;
        }
        return hrirs;

    }

    private HRIR[][] createCIPIC_HRIRs(int[][] positions, DataInputStream in, IHRIRReader reader) throws IOException {

        HRIR[][] hrirs = new HRIR[positions.length][];
        for (int i = 0; i < positions.length; i++) {
            HRIR[] ring = new HRIR[positions[i][1]];
            for (int j = 0; j < ring.length; j++) {
                ring[j] = reader.read(in, positions[i][0], -45 + 5.625*j);
            }
            hrirs[i] = ring;
        }
        return hrirs;

    }

    private HRIR[][] baretto(DataInputStream s, IHRIRReader reader) throws IOException {
        return createHRIRs(listen_positions, s, reader);
    }

    private HRIR[][] listen(DataInputStream s, IHRIRReader reader) throws IOException {
        return createHRIRs(baretto_positions, s, reader);
    }

    public IHRTF listen(File f) throws IOException {
        return fromFile(f, listen_positions, new ListenHRIR());
    }

    public IHRTF listenBinary(File f) throws IOException {
        return fromFile(f, listen_positions, new ListenBinaryHRIR());
    }

    public IHRTF baretto(File f) throws IOException {
        return fromFile(f, baretto_positions, new BarettoHRIR());
    }

    private IHRTF fromFile(File f, int[][] positions, IHRIRReader reader) throws IOException {
        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));
        return new HRTF(createHRIRs(positions, in, reader));
    }

    public IHRTF fromFile(String s) throws UnrecognisedHRTFException, IOException {
        File f = new File(s);

        if (!f.exists()) {
            throw new FileNotFoundException(s);
        }

        if (s.contains(".cipic")) {
            DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));
            return new CipicHRTF(createCIPIC_HRIRs(cipic_positions, in, new CipicHRIR()));
        } else if (s.contains(".listen")) {
            return fromFile(f, listen_positions, (s.endsWith(".bin") ? new ListenBinaryHRIR() : new ListenHRIR()));
        } else if (s.contains(".baretto")) {
            return fromFile(f, baretto_positions, new BarettoHRIR());
        }

        throw new UnrecognisedHRTFException(s);
    }

}
