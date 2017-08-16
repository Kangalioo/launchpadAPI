public interface LaunchpadListener {
	public default void padPressed(int x, int y) {}
	public default void padReleased(int x, int y) {}
	
	public default void buttonPressed(int index) {}
	public default void buttonReleased(int index) {}
}
