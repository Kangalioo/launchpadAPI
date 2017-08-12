import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;
import javax.sound.midi.Receiver;

// This doesn't (and can't) have support for transparent colors
public interface LowLevelLaunchpad extends LaunchpadInterface {
	public int getWidth();
	
	public int getHeight();
	
	public int getControlButtons();
	
	public boolean isInputOpen();
	
	public boolean isOutputOpen();
	
	public void setPadColor(int x, int y, Color color, boolean copy, boolean clear);
	
	public void setButtonColor(int index, Color color, boolean copy, boolean clear);
	
	public int isControlButton(int x, int y);
	
	public boolean isDoubleBufferingOn();
	
	public void setDoubleBuffering(boolean updateBuffer, boolean displayBuffer, boolean copy, boolean flash);
	
	public void reset();
	
	public void test(int mode);
	
	public void setDutyCycle(int numerator, int denominator);
	
	public void treatButtonsAsPads(boolean state);
	
	public void openInput(MidiDevice inputDevice) throws MidiUnavailableException;
	
	public void closeInput();
	
	public void openOutput(MidiDevice outputDevice) throws MidiUnavailableException;
	
	public void closeOutput();
	
	public default void open(MidiDevice[] devices) throws MidiUnavailableException {
		if (devices.length != 2) {
			throw new IllegalArgumentException("There must be only 2 devices in the array, not " + devices.length + ".");
		}
		openInput(devices[0]);
		openOutput(devices[1]);
	}
	
	public default void close() {
		closeInput();
		closeOutput();
	}
	
	public Transmitter getInput();
	
	public Receiver getOutput();
}
