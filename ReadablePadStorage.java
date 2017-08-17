public interface ReadablePadStorage {
	public int getWidth();
	
	public int getHeight();
	
	public void setPadColor(int x, int y, Color color);
	
	public Color getPadColor(int x, int y);
}
