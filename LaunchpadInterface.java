import javax.sound.midi.MidiDevice;

public interface LaunchpadInterface {
	public void setListener(LaunchpadListener listener);
	
	public LaunchpadListener getListener();
	
	/**
	 * @return the width of the rectangle which is just big enough to fit all of the pads into
	 */
	public int getWidth();
	
	/**
	 * @return the height of the rectangle which is just big enough to fit all of the pads into
	 */
	public int getHeight();
	
	public boolean isPadInBounds(int x, int y);
	
	public boolean isButtonInBounds(int index);
	
	public void treatButtonsAsPads(boolean state);
	
	public boolean treatButtonsAsPads();
	
	public boolean isInputOpen();
	
	public boolean isOutputOpen();
	
	public void openInput(MidiDevice device);
	
	public void openOutput(MidiDevice device);
	
	public default void open(MidiDevice[] devices) {
		if (devices.length != 2) {
			throw new IllegalArgumentException("Devices array must be of length 2.");
		}
		openInput(devices[0]);
		openOutput(devices[1]);
	}
	
	public void closeInput();
	
	public void closeOutput();
	
	public Exception checkError();
	
	public default void close() {
		closeInput();
		closeOutput();
	}
}
