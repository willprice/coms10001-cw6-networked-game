package ai;

public class Pair<X, Y> {
	public X x;
	public Y y;
	
	public Pair(X x, Y y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + x.toString() + ", " +  y.toString() + ")";
	}
}
