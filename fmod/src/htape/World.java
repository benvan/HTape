package htape;

import htape.audio.ISource;
import htape.audio.SoundSystem;
import htape.geometry.PointSource;
import htape.geometry.SourceTag;
import htape.geometry.Point;
import org.jouvieje.fmodex.DSP;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by ben, on 3/10/11 at 1:17 PM
 */
public class World {

    SoundSystem soundSystem;
    Collection<PointSource> sources;
    Point perspective;
    Point origin;

    public World() {
        soundSystem = new SoundSystem();
        sources = new ArrayList<PointSource>();
        perspective = new Point();
        origin = new Point(0,0,0);
    }

    public void render(Graphics g){

    }

    public PointSource createSource(){

        ISource source = soundSystem.createSource();
        PointSource point = new PointSource(source);

        sources.add(point);

        return point;
    }

    public void play(PointSource source){
        soundSystem.play(source.getSoundSource());
    }

    public void bind(PointSource source, String file) {
        soundSystem.bind(source.getSoundSource(), file);
    }

    public void move(PointSource source, Point point) {
        source.setPosition(point);
    }
}
