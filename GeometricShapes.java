/**
 * Utility class for generating geometric shapes.
 */
public class GeometricShapes {
	public static Shape rectangleHollow(int width, int height, Color color) {
		Shape shape = new Shape(width, height);
		for (int x = 0; x < width; x++) {
			shape.setPadColor(x, 0, color);
			shape.setPadColor(x, height - 1, color);
		}
		for (int y = 1; y + 1 < height; y++) {
			shape.setPadColor(0, y, color);
			shape.setPadColor(width - 1, y, color);
		}
		return shape;
	}
}
