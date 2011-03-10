package htape.audio;

import htape.util.filtering.IFilter;

/**
 * Created by ben, on 3/10/11 at 7:52 PM
 */
public class EmptyFilter implements IFilter {


    public int getTaps() {
        return 0;
    }

    public float getLeft(int i) {
        return 1;
    }

    public float getRight(int i) {
        return 1;
    }
}
