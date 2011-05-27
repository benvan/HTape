package htape.util.filtering;

import htape.audio.EmptyFilter;

public class DistanceFilter implements IFilter{

	private IFilter child;
	private double distance = 0;
	
	public void wrap(IFilter filter){
		child = filter;
	}
	
	public DistanceFilter(){
		child = new EmptyFilter();
	}
	
	@Override
	public float getLeft(int i) {
		// TODO Auto-generated method stub
		double sq = distance*distance;
		return child.getLeft(i)*(float)((1+distance)/(1+sq));
	}

	@Override
	public float getRight(int i) {
		// TODO Auto-generated method stub
		double sq = distance*distance;
		return child.getRight(i)*(float)((1+distance)/(1+sq));
	}

	@Override
	public int getTaps() {
		// TODO Auto-generated method stub
		return child.getTaps();
	}

	public void setDistance(double d) {
		distance = d;
	}
	
	

}
