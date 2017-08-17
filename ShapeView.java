import java.util.Iterator;
import java.util.NoSuchElementException;

public class ShapeView implements Iterable<Pad>, ReadablePadStorage {
	private Shape shape;
	private int x = 0, y = 0;
	
	private Color colorFilter = Color.WHITE;
	
	public ShapeView(Shape shape) {
		this.shape = shape;
	}
	
	public ShapeView(Shape shape, int x, int y) {
		this.shape = shape;
		this.x = x;
		this.y = y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getX() {
		return x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getY() {
		return y;
	}
	
	public Color getPadColor(int x, int y) {
		return shape.getPadColor(x - this.x, y - this.y).multiply(colorFilter);
	}
	
	public void setPadColor(int x, int y, Color color) {
		throw new UnsupportedOperationException("ShapeView is an immutable wrapper.");
	}
	
	public int getWidth() {
		return shape.getWidth();
	}
	
	public int getHeight() {
		return shape.getHeight();
	}
	
	public void setColorFilter(Color colorFilter) {
		this.colorFilter = colorFilter.applyAlphaInverted();
	}
	
	public Color getColorFilter() {
		return colorFilter;
	}
	
	public Iterator<Pad> iterator() {
		return new Iterator<Pad>() {
			private int x = 0, y = 0;
			
			public boolean hasNext() {
				return y < getHeight();
			}
			
			public Pad next() {
				if (y >= getHeight()) {
					throw new NoSuchElementException();
				}
				Pad pad = new Pad(x + ShapeView.this.x, y + ShapeView.this.y, ShapeView.this);
				pad.setReadOnly(true);
				if (++x == getWidth()) {
					x = 0;
					y++;
				}
				return pad;
			}
		};
	}
}
