package htape.geometry;

import htape.audio.ISource;

/**
 * Created by ben, on 3/10/11 at 1:45 PM
 */
public class PointSource {

    private ISource soundSource;
    private Point position;

    public PointSource(ISource source) {
        this.soundSource = source;
        position = new Point();
    }

    public ISource getSoundSource() {
        return soundSource;
    }

    public void setPosition(Point point) {
        position.setX(point.getX());
        position.setY(point.getY());
        position.setZ(point.getZ());
    }
}
