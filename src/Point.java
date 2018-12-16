public class Point {

	int x;
	int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Point))
			return false;
		Point p = (Point) obj;
		return p.x == x && p.y == y;
	}
}
