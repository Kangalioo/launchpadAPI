import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;

public interface LaunchpadController extends LaunchpadInterface, ReadablePadStorage {
	public void allowShapeCutting(boolean state);
	
	public boolean allowsShapeCutting();
	
	public void displayShape(Shape shape, int x, int y);
	
	public void setButtonColor(int index, Color color);
	
	public Color getButtonColor(int index);
	
	// Redraws from cache
	public void redraw();
	
	public boolean isFlashingModeActivated();
	
	public void flash();
	
	public void enterFlashingMode();
	
	public void leaveFlashingMode();
	
	public void setDoubleBufferingMode(DoubleBufferingMode doubleBufferingMode);
	
	public boolean isPreparing();
	
	public void prepare();
	
	public void present();
	
	public void transmitChanges();
	
	public void clearPads();
	
	public void clearButtons();
	
	public void clear();
	
	public void reset();
	
	public void closeInput();
	
	public void closeOutput();
}
