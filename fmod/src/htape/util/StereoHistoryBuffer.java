package htape.util;

public class StereoHistoryBuffer {

    HistoryBuffer left, right;

    public StereoHistoryBuffer() {

        left = new HistoryBuffer(0);
        right = new HistoryBuffer(0);

    }

    public void resize(int size) {

        left.resize(size);
        right.resize(size);

    }

    public HistoryBuffer getLeft() {
        return left;
    }

    public HistoryBuffer getRight() {
        return right;
    }

    public int length() {
        //they're both the same
        return left.length();
    }
}
