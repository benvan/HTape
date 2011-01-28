package htape.util;

import htape.util.io.hrir.BarettoHRIR;
import htape.util.io.hrir.IHRIRReader;
import htape.util.io.hrir.ListenBinaryHRIR;
import htape.util.io.hrir.ListenHRIR;

import java.io.*;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: ben
 * Date: 1/24/11
 * Time: 6:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class HRTFFactory {


    private final static int[][] baretto_positions = new int[][]{{-36,12},{-18,12},{0,12},{18,12},{36,12},{54,12}};
    private final static int[][] listen_positions = new int[][]{{-45,24},{-30,24},{-15,24},{0,24},{15,24},{30,24},{45,24},{60,12},{75,6},{90,1}};

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

    private HRIR[][] baretto(DataInputStream s, IHRIRReader reader) throws IOException {
        return createHRIRs(listen_positions, s, reader);
    }

    private HRIR[][] listen(DataInputStream s, IHRIRReader reader) throws IOException {
        return createHRIRs(baretto_positions, s, reader);
    }

    public HRTF listen(File f) throws IOException {
        return fromFile(f, listen_positions, new ListenHRIR());
    }

    public HRTF listenBinary(File f) throws IOException {
        return fromFile(f, listen_positions, new ListenBinaryHRIR());
    }

    public HRTF baretto(File f) throws IOException {
        return fromFile(f, baretto_positions, new BarettoHRIR());
    }



    public HRTF fallbackWTF(File f) throws FileNotFoundException {
        Scanner s = new Scanner(f);
        int[][] positions = new int[][]{
                {-45,24},
                {-30,24},
                {-15,24},
                {0,24},
                {15,24},
                {30,24},
                {45,24},
                {60,12},
                {75,6},
                {90,1}
        };
        HRIR[][] hrirs = new HRIR[10][];

        for (int i = 0; i < positions.length; i++) {
            HRIR[] ring = new HRIR[positions[i][1]];
            for (int j = 0; j < ring.length; j++) {
                float[] coeffs = new float[1024];
                for (int k = 0; k < 1024; k++) {
                    coeffs[k] = s.nextFloat();
                }
                ring[j] = new HRIR((360 / positions[i][1]) * j, positions[i][0], coeffs);
            }
            hrirs[i] = ring;
        }

        return new HRTF(hrirs);
    }



    public HRTF fromFile(File f, int[][] positions, IHRIRReader reader) throws IOException {
        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));
        return new HRTF(createHRIRs(positions, in, reader));
    }

}
