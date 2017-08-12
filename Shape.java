import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Arrays;

public class Shape implements Iterable<Pad>, ReadablePadStorage {
	private int width, height;
	private Color[][] raster;
	
	public Shape(int width, int height) {
		this.width = width;
		this.height = height;
		raster = new Color[width][height];
		fill(Color.TRANSPARENT);
	}
	
	public Shape(Color[][] padArray) {
		width = padArray.length;
		height = padArray[0].length;
		raster = padArray;
	}
	
	// Clone
	public Shape(Shape shape) {
		width = shape.getWidth();
		height = shape.getHeight();
		raster = new Color[width][height];
		int i = 0;
		for (Color[] row : shape.getRawRaster()) {
			raster[i] = row.clone();
			i++;
		}
		
	}
	
	public Color[][] getRawRaster() {
		return raster;
	}
	
	public void fill(Color color) {
		for (Color[] row : raster) Arrays.fill(row, color);
	}
	
	public void setPadColor(int x, int y, Color color) {
		raster[x][y] = color;
	}
	
	public Color getPadColor(int x, int y) {
		return raster[x][y];
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Iterator<Pad> iterator() {
		return new Iterator<Pad>() {
			private int x = 0, y = 0;
			
			public boolean hasNext() {
				return y < height;
			}
			
			public Pad next() {
				if (y >= height) {
					throw new NoSuchElementException();
				}
				Pad pad = new Pad(x, y, Shape.this);
				if (++x == width) {
					x = 0;
					y++;
				}
				return pad;
			}
		};
	}
}
