public class Color {
	public static final Color
			TRANSPARENT = new Color(0, 0, 0, 255),
			BLACK = new Color(0, 0, 0),
			RED = new Color(255, 0, 0),
			GREEN = new Color(0, 255, 0),
			BLUE = new Color(0, 0, 255),
			CYAN = new Color(0, 255, 255),
			MAGENTA = new Color(255, 0, 255),
			YELLOW = new Color(255, 255, 0),
			WHITE = new Color(255, 255, 255),
			
			MEDIUM_YELLOW = new Color(2, 2),
			WEAK_YELLOW = new Color(1, 1),
			MEDIUM_RED = new Color(2, 0),
			WEAK_RED = new Color(1, 0),
			MEDIUM_GREEN = new Color(0, 2),
			WEAK_GREEN = new Color(0, 1);
	
	private java.awt.Color color;
	
	
	public Color(java.awt.Color color) {
		this.color = color;
	}
	
	public Color(int red, int green, int blue) {
		this(red, green, blue, 0);
	}
	
	// All parameters ranging from 0 - 256
	public Color(int red, int green, int blue, int alpha) {
		this(new java.awt.Color(red, green, blue, alpha));
	}
	
	public Color(int red, int green) {
		if (red < 0 || red > 3) {
			throw new IllegalArgumentException("Red value out of range: " + red);
		}
		if (green < 0 || green > 3) {
			throw new IllegalArgumentException("Green value out of range: " + green);
		}
		
		color = new java.awt.Color(red * (255 / 3), green * (255 / 3), 0, 0);
	}
	
	// Sets to transparent
	public Color() {
		this(0, 0, 0, 255);
	}
	
	public java.awt.Color getColor() {
		return color;
	}
	
	public int getRed() {
		return color.getRed();
	}
	
	public int getGreen() {
		return color.getGreen();
	}
	
	public int getBlue() {
		return color.getBlue();
	}
	
	public int getAlpha() {
		return color.getAlpha();
	}
	
	public boolean isBlack() {
		return (getRed() + getGreen() + getBlue()) == 0;
	}
	
	// Full alpha -> black
	public Color applyAlpha() {
		return new Color((int) (getRed() * (1 - getAlpha() / 255.0)),
				(int) (getGreen() * (1 - getAlpha() / 255.0)),
				(int) (getBlue() * (1 - getAlpha() / 255.0)),
				0);
	}
	
	// Full alpha -> white
	public Color applyAlphaInverted() {
		return new Color((int) ((getRed() - 255) * (1 - getAlpha() / 255.0)) + 255,
				(int) ((getGreen() - 255) * (1 - getAlpha() / 255.0)) + 255,
				(int) ((getBlue() - 255) * (1 - getAlpha() / 255.0)) + 255,
				0);
	}
	
	// This ignores alpha
	public Color multiply(Color c) {
		return new Color((int) (getRed() * (c.getRed() / 255.0)),
				(int) (getGreen() * (c.getGreen() / 255.0)),
				(int) (getBlue() * (c.getBlue() / 255.0)),
				getAlpha());
	}
	
	// Layers colors on top of each other, first element at bottom (subtractive adding)
	// TODO: Make this work correctly
	public static Color layer(Color... colors) {
		double alpha = 1;
		Color color = new Color();
		
		// First method
		for (int i = colors.length - 1; i >= 0; i--) {
			Color c = colors[i];
			if (c.getAlpha() < 128) {
				color = c;
				break;
			}
		}
		
		// Second method
		//for (Color c : colors) {
			//alpha *= (c.getAlpha() / 256.0);
			//int redChange = c.getRed() - color.getRed();
			//int greenChange = c.getGreen() - color.getGreen();
			//int blueChange = c.getBlue() - color.getBlue();
			//redChange *= c.getAlpha() / 256.0;
			//greenChange *= c.getAlpha() / 256.0;
			//blueChange *= c.getAlpha() / 256.0;
			//color = new Color(color.getRed() + redChange, color.getGreen() + greenChange, color.getBlue() + blueChange);
		//}
		//color = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) Math.round(alpha * 256));
		
		return color;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Color) {
			Color c = (Color) o;
			return (c.getGreen() == this.getGreen()
					&& c.getRed() == this.getRed()
					&& c.getBlue() == this.getBlue()
					&& c.getAlpha() == this.getAlpha());
		} else {
			return false;
		}
	}
	
	public boolean equalsVisually(Object o) {
		if (o instanceof Color) {
			return applyAlpha().equals(((Color) o).applyAlpha());
		} else {
			return false;
		}
	}
	
	public String toString() {
		return color.toString();
	}
	
	@Deprecated
	public static Color makeColorForS(int red, int green) {
		return new Color(red, green);
	}
	
	public static Color makeRandom() {
		return new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
	}
}
