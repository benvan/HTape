package htape.util;

/**
 * Created by IntelliJ IDEA.
 * User: ben
 * Date: 1/23/11
 * Time: 7:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class HistoryBuffer {

    private float[] values;
    private int pointer = 0;
    private int maxSoFar = 0;
    private int size;
    private int extra;

    public HistoryBuffer(int size, int extra) {
        this.size = size;
        this.extra = extra;
        values = new float[size + extra];
    }

    public void add(float data){
        values[pointer] = data;
        pointer++;
        pointer %= size + extra;
        if (maxSoFar < (size)) maxSoFar++;
    }

    public float get(int position){
        int pos = (pointer - (position+1));
        if (pos >= 0){
            return values[pos];
        }else {

            if (size + extra + pos == -1) {
                System.out.println("position:" + position + " c-p:" + pos + " size:" + size + " pointer:" + pointer + " result: " + (size + (pos % size)));
            }
            return values[size + extra + pos];

        }
    }

    public int length() {
        return maxSoFar;
    }
}
