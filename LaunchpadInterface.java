public interface LaunchpadInterface {
	public void setListener(LaunchpadListener listener);
	
	public LaunchpadListener getListener();
	
	public boolean isPadInBounds(int x, int y);
	
	public boolean isButtonInBounds(int index);
	
	public void treatButtonsAsPads(boolean state);
	
	public boolean treatButtonsAsPads();
}
