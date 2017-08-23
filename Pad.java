public class Pad {
	private int x, y;
	private PadModule l;
	
	private boolean readable, writable;
	
	public Pad(int x, int y, PadModule l) {
		this.x = x;
		this.y = y;
		this.l = l;
		readable = (l instanceof PadSource);
		writable = (l instanceof PadTarget);
	}
	
	public Color getColor() {
		if (!readable) {
			throw new UnsupportedOperationException("PadModule is not readable.");
		}
		return ((PadSource) l).getPadColor(x, y);
	}
	
	public void setColor(Color color) {
		if (!writable) {
			throw new UnsupportedOperationException("PadModule is not writable.");
		}
		((PadTarget) l).setPadColor(x, y, color);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
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
