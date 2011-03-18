package htape.util;

import htape.util.filtering.hrtf.IHRTF;

import java.util.ArrayList;
import java.util.LinkedList;

public class ResourcePool {


    Pool<IHRTF> hrtfs;
    Pool<String> audioFiles;

    public ResourcePool() {
        hrtfs = new Pool<IHRTF>();
        audioFiles = new Pool<String>();
    }

    public Pool<IHRTF> hrtfs(){
        return hrtfs;
    }

    public Pool<String> audioFiles() {
        return audioFiles;
    }


}
