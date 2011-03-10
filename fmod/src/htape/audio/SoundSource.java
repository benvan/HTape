package htape.audio;

import org.jouvieje.fmodex.Sound;

/**
 * Created by ben, on 3/10/11 at 1:48 PM
 */
public class SoundSource implements ISource{

    Sound sound;

    public SoundSource() {
        sound = new Sound();
    }

    public Sound getSound() {
        return sound;
    }
}
