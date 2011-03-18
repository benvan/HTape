package htape.audio;

import htape.util.filtering.IFilter;

import org.jouvieje.fmodex.callbacks.FMOD_DSP_READCALLBACK;

public interface FilterCallback extends FMOD_DSP_READCALLBACK {
	
	void setFilter(IFilter filter);

}
