import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Arrays;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Shape implements Iterable<Pad>, PadStorage {
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
	
	/**
	 * Can be used for cloning, but it is also very useful for creating a
	 * static shape of a manipulator.
	 */
	public Shape(PadSource shape) {
		width = shape.getWidth();
		height = shape.getHeight();
		raster = new Color[width][height];
		int i = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				raster[x][y] = shape.getPadColor(x, y);
			}
		}
		
	}
	
	public static Shape readImage(File file) throws IOException {
		BufferedImage image = ImageIO.read(file);
		
		Shape shape = new Shape(image.getWidth(), image.getHeight());
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				java.awt.Color javaColor = new java.awt.Color(image.getRGB(x, y), true);
				Color color = new Color(javaColor.getRed(), javaColor.getGreen(), javaColor.getBlue(), 255 - javaColor.getAlpha());
				shape.setPadColor(x, y, color);
			}
		}
		
		return shape;
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
