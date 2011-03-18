package htape.gui;

import htape.geometry.Point;

import java.awt.Graphics;

public class ExtendedGraphics {
	
	private Graphics g;
	private int height;
	private int width;

	public void setGraphics(Graphics g, int width, int height){
		this.g = g;
		this.width = width;
		this.height = height;
	}
	
	public void line(Point a, Point b){
		g.drawLine((int) a.getX(), (int)(height - a.getY()), (int)b.getX(), (int)(height - b.getY()));
	}

	public void square(Point p, int i) {
		rect(p, i, i);
	}
	
	public void rect(Point p, int w, int h) {
		g.drawRect((int) (p.getX() -(w/2)), (int)(height - p.getY()-(h/2)), w, h);
		
	}

	public void circ(Point p, double r){
		arc(p, r, 0, 360);
	}
	
	public void circF(Point p, double r) {
		arcF(p, r, 0, 360);
	}
	
	public void arc(Point p, double r, int start, int end){
		g.drawArc((int)(p.getX()-r), (int)(height - p.getY()-r), (int)r*2, (int)r*2, start, end);
	}
	
	public void arcF(Point p, double r, int start, int end){
		g.fillArc((int)(p.getX()-r), (int)(height - p.getY()-r), (int)r*2, (int)r*2, start, end);
	}

}
