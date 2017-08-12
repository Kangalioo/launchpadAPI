public class Pad {
	private int x, y;
	private ReadablePadStorage l;
	
	private boolean readOnly = false;
	
	public Pad(int x, int y, ReadablePadStorage l) {
		this.x = x;
		this.y = y;
		this.l = l;
	}
	
	public Color getColor() {
		return l.getPadColor(x, y);
	}
	
	public void setColor(Color color) {
		if (readOnly) {
			throw new UnsupportedOperationException("Pad is read only.");
		}
		l.setPadColor(x, y, color);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setReadOnly(boolean state) {
		this.readOnly = state;
	}
	
	// Only compares x and y
	public boolean equals(Object o) {
		if (o instanceof Pad) {
			Pad pad = (Pad) o;
			if (pad.getX() == x
					&& pad.getY() == y) {
				return true;
			}
		}
		
		return false;
	}
}
