package htape;

import htape.audio.ISource;
import htape.audio.SoundSystem;
import htape.geometry.Camera;
import htape.geometry.Matrix;
import htape.geometry.PointSource;
import htape.geometry.Point;
import htape.util.filtering.DistanceFilter;
import htape.util.filtering.hrtf.EmptyHRTF;
import htape.util.filtering.hrtf.HRIR;
import htape.util.filtering.hrtf.IHRTF;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.Timer;

/**
 * Created by ben, on 3/10/11 at 1:17 PM
 */

public class World {

    SoundSystem soundSystem;
    IHRTF hrtf;
    Collection<PointSource> sources;
    Point origin;
    Matrix f;
    public Camera camera;

    public HRIR hrir = null;
	private DistanceFilter filt;

    public World() {
        soundSystem = new SoundSystem();
        sources = new ArrayList<PointSource>();
        camera = new Camera();
        origin = new Point(0,0,0);
        hrtf = new EmptyHRTF();
        filt = new DistanceFilter();
        hrir = hrtf.get(0, 0);
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

    private void update(PointSource source) {
        //get appropriate HRIR and update soundSystem
        Point projection;
        Point u = camera.getU(), v = camera.getV(), w = camera.getW(), p = camera.getPos();

        Matrix m = new Matrix(new double[][]{
                {u.getX(),  u.getY(),   u.getZ(),   -p.dot(u)},
                {v.getX(),  v.getY(),   v.getZ(),   -p.dot(v)},
                {w.getX(),  w.getY(),   w.getZ(),   -p.dot(w)},
                {0,0,0,1},
        });

        projection = m.mult(source.getPosition()).toPoint();


        //find az / elevation

        double el = -Math.atan2(projection.getY(), projection.getZ());
        

        Matrix elevationPlane = new Matrix(new double[][]{
                {1,0,0,0},
                {0,Math.cos(el),-Math.sin(el),0},
                {0,Math.sin(el),Math.cos(el),0},
                {0,0,0,1},
        });

        projection = elevationPlane.mult(projection).toPoint();
        double az = Math.atan2(projection.getZ(), projection.getX());

//        System.out.println(projection.toString() + " az: " + Math.toDegrees(az));
        

        hrir = hrtf.get(90 - Math.toDegrees(az), Math.toDegrees(el));
        filt.setDistance(p.distanceTo(source.getPosition()));
        filt.wrap(hrir);

        soundSystem.setFilter(filt);


    }

    public void update(){
    	
    	camera.alignToBinding();
    	
        for (PointSource pointSource : sources) {
            update(pointSource);
        }
    }

	//TODO marked for deletion
    public IHRTF getHRTF(){
        return hrtf;
    }

    //TODO marked for deletion
    public Camera getCamera(){
        return camera;
    }

    //TODO marked for deletion
    public Collection<PointSource> getSources() {
        return sources;
    }

    public void setHRTF(IHRTF HRTF) {
        this.hrtf = HRTF;
    }
}
