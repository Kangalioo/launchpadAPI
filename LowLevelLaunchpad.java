import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;
import javax.sound.midi.Receiver;

// This doesn't (and can't) have support for transparent colors
public interface LowLevelLaunchpad extends LaunchpadInterface {
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
	
	public Transmitter getInput();
	
	public Receiver getOutput();
}
