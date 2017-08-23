public interface PadModule {
	public default int getXOffset() {
		return 0;
	}
	
	public default int getYOffset() {
		return 0;
	}
		
	public int getWidth();
	
	public int getHeight();
}
