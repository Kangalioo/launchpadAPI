public class BasicManipulator extends Manipulator {
	private int x = 0, y = 0;
	
	private Color colorFilter = Color.WHITE;
	
	
	public BasicManipulator(PadSource source) {
		super(source);
	}
	
	public BasicManipulator(PadSource source, int x, int y) {
		this(source);
		setOffset(x, y);
	}
	
	public void setOffset(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setXOffset(int x) {
		this.x = x;
	}
	
	public int getXOffset() {
		return x;
	}
	
	public void setYOffset(int y) {
		this.y = y;
	}
	
	public int getYOffset() {
		return y;
	}
	
	public Color getPadColor(int x, int y) {
		return getSource().getPadColor(x - this.x, y - this.y).multiply(colorFilter);
	}
	
	public int getWidth() {
		return getSource().getWidth();
	}
	
	public int getHeight() {
		return getSource().getHeight();
	}
	
	public void setColorFilter(Color colorFilter) {
		this.colorFilter = colorFilter.applyAlphaInverted();
	}
	
	public Color getColorFilter() {
		return colorFilter;
	}
}
