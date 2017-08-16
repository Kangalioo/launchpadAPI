public interface LaunchControlListener extends LaunchpadListener {
	public default void knobChanged(int knob, int value) {}
}
