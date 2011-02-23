package htape.util;

public class HistoryBuffer {

    private float[] values;
    private int pointer = 0;
    private int maxSoFar = 0;
    private int size;

    public HistoryBuffer(int size) {
        this.size = size;
        values = new float[size];
    }

    public void add(float data){
        values[pointer] = data;
        pointer++;
        pointer %= size;
        if (maxSoFar < (size)) maxSoFar++;
    }

    public float get(int position){
        int pos = (pointer - (position+1));
        if (pos >= 0){
            return values[pos];
        }else {
            return values[size+pos];
        }
    }

    public int length() {
        return maxSoFar;
    }

    public void resize(int size) {
        if (size > this.size) {
            float[] resized = new float[size];
            System.arraycopy(values, 0, resized, 0, maxSoFar);
            values = resized;
        }
        this.size = size;
    }
}
