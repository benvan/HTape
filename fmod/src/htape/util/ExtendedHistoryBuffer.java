package htape.util;

public class ExtendedHistoryBuffer {

    private float[] values;
    private int pointer = 0;
    private int maxSoFar = 0;
    private int size;
    private int extra;

    public ExtendedHistoryBuffer(int size, int extra) {
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
            return values[size + extra + pos];
        }
    }

    public int length() {
        return maxSoFar;
    }
}
