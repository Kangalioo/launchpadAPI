import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import java.util.Arrays;

public class Launchpad extends AbstractLaunchpadController implements LaunchpadListener, LaunchpadController {
	private LaunchpadListener listener;
	private LowLevelLaunchpad launchpad;
	
	private int width, height, controlButtons;
	
	private Color[][] padCache;
	private Color[] buttonCache;
	
	private DoubleBufferingMode doubleBufferingMode = DoubleBufferingMode.NONE;
	private boolean currentWorkingBuffer = false; // false = buffer 0, true = buffer 1.
	private int preparationLevel = 0;
	private Color[][] preparationPadCache;
	private Color[] preparationButtonCache;
	
	private boolean flashingModeActivated = false;
	
	
	public Launchpad(LowLevelLaunchpad launchpad, DoubleBufferingMode doubleBufferingMode) {
		this.launchpad = launchpad;
		launchpad.setListener(this);
		
		this.doubleBufferingMode = doubleBufferingMode;
		
		width = launchpad.getWidth();
		height = launchpad.getHeight();
		controlButtons = launchpad.getControlButtons();
		createCaches();
	}
	
	public Launchpad(LowLevelLaunchpad launchpad) {
		this(launchpad, DoubleBufferingMode.NONE);
	}
	
	private void createCaches() {
		padCache = new Color[width][height];
		buttonCache = new Color[controlButtons];
		resetCaches(padCache, buttonCache);
		
		preparationPadCache = new Color[width][height];
		preparationButtonCache = new Color[controlButtons];
		resetCaches(preparationPadCache, preparationButtonCache);
	}
	
	public void setListener(LaunchpadListener listener) {
		this.listener = listener;
	}
	
	public void padPressed(int x, int y) {
		listener.padPressed(x, y);
	}
	
	public void padReleased(int x, int y) {
		listener.padReleased(x, y);
	}
	
	public void buttonPressed(int index) {
		if (getListener() != null) listener.buttonPressed(index);
	}
	
	public void buttonReleased(int index) {
		if (getListener() != null) listener.buttonReleased(index);
	}
	
	public void setPadColor(int x, int y, Color color) {
		if (!isPadInBounds(x, y)) {
			throw new IndexOutOfBoundsException("Incorrect coordinates.");
		}
		
		int button = isControlButton(x, y);
		if (button != -1) {
			setButtonColor(button, color);
			return;
		}
		
		Color[][] cache;
		if (isPreparing()) {
			cache = preparationPadCache;
		} else {
			cache = padCache;
		}
		//color = Color.layer(cache[x][y], color);
		color = color.applyAlpha();
		if (!cache[x][y].equalsVisually(color)) {
			if (isPreparing() && doubleBufferingMode == DoubleBufferingMode.ALWAYS) {
				launchpad.setPadColor(x, y, color, false, false);
			} else if (!isPreparing()) {
				launchpad.setPadColor(x, y, color, true, true);
			}
			cache[x][y] = color;
		}
	}
	
	public Color getPadColor(int x, int y) {
		if (!isPadInBounds(x, y)) {
			throw new IndexOutOfBoundsException("Incorrect coordinates.");
		}
		
		int button = isControlButton(x, y);
		if (button != -1) return getButtonColor(button);
		
		return (isPreparing() ? preparationPadCache : padCache)[x][y];
	}
	
	public void setButtonColor(int index, Color color) {
		if (!isButtonInBounds(index)) {
			throw new IndexOutOfBoundsException("Incorrect index.");
		}
		
		Color[] cache;
		if (isPreparing()) {
			cache = preparationButtonCache;
		} else {
			cache = buttonCache;
		}
		
		color = color.applyAlpha();
		if (!cache[index].equalsVisually(color)) {
			if (isPreparing() && doubleBufferingMode == DoubleBufferingMode.ALWAYS) {
				launchpad.setButtonColor(index, color, false, false);
			} else if (!isPreparing()) {
				launchpad.setButtonColor(index, color, true, true);
			}
			cache[index] = color;
		}
	}
	
	public Color getButtonColor(int index) {
		if (!isButtonInBounds(index)) {
			throw new IndexOutOfBoundsException("Incorrect index.");
		}
		
		return (isPreparing() ? preparationButtonCache : buttonCache)[index];
	}
	
	// Redraws everything from cache
	public void redraw() {
		// Redraw pads
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				launchpad.setPadColor(x, y, padCache[x][y], true, true);
			}
		}
		
		// Redraw buttons
		for (int i = 0; i < controlButtons; i++) {
			launchpad.setButtonColor(i, buttonCache[i], true, true);
		}
	}
	
	public void drawPreparationCaches() {
		// Redraw pads
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				setPadColor(x, y, preparationPadCache[x][y]);
			}
		}
		
		// Redraw buttons
		for (int i = 0; i < controlButtons; i++) {
			setButtonColor(i, preparationButtonCache[i]);
		}
	}
	
	public boolean isFlashingModeActivated() {
		return flashingModeActivated;
	}
	
	public void flash() {
		if (isPreparing()) {
			throw new IllegalStateException("Flashing cannot be activated when double buffering is active.");
		}
		
		// TODO
	}
	
	public void enterFlashingMode() {
		if (flashingModeActivated == true) {
			throw new IllegalStateException("Flashing mode is already activated.");
		} else if (isPreparing()) {
			throw new IllegalStateException("Flashing mode cannot be activated when double buffering is active.");
		} else {
			flashingModeActivated = true;
		}
		
		// TODO
	}
	
	public void leaveFlashingMode() {
		if (flashingModeActivated == false) {
			throw new IllegalStateException("Flashing mode is already deactivated.");
		} else {
			flashingModeActivated = false;
		}
		
		// TODO
	}
	
	public void setDoubleBufferingMode(DoubleBufferingMode doubleBufferingMode) {
		if (isPreparing()) {
			throw new IllegalStateException("Cannot change double buffering mode while preparing.");
		} else {
			this.doubleBufferingMode = doubleBufferingMode;
		}
	}
	
	public boolean isPreparing() {
		return preparationLevel > 0;
	}
	
	public void prepare() {
		if (isFlashingModeActivated()) {
			throw new IllegalStateException("Double buffering cannot be activated when flashing mode or flashing is active.");
		}
		
		preparationLevel++;
		if (preparationLevel == 1) {
			if (doubleBufferingMode == DoubleBufferingMode.ALWAYS) {
				currentWorkingBuffer = !currentWorkingBuffer;
				launchpad.setDoubleBuffering(currentWorkingBuffer, !currentWorkingBuffer, false, false);
			}
			copyStateIntoPreparationBuffers();
		}
	}
	
	private void copyStateIntoPreparationBuffers() {
		preparationPadCache = new Color[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				preparationPadCache[x][y] = padCache[x][y];
				
			}
		}
		preparationButtonCache = new Color[controlButtons];
		for (int i = 0; i < controlButtons; i++) {
			preparationButtonCache[i] = buttonCache[i];
		}
	}
	
	public void present() {
		if (isPreparing()) {
			preparationLevel--;
			if (!isPreparing()) {
				// Apply changes
				if (doubleBufferingMode == DoubleBufferingMode.WHEN_REQUESTED) {
					transmitChanges(true);
				} else {
					if (doubleBufferingMode == DoubleBufferingMode.NONE) {
						drawPreparationCaches();
					}
					padCache = preparationPadCache;
					buttonCache = preparationButtonCache;
					if (doubleBufferingMode == DoubleBufferingMode.ALWAYS) {
						launchpad.setDoubleBuffering(currentWorkingBuffer, currentWorkingBuffer, true, false);
					}
				}
			}
		}
	}
	
	public void transmitChanges() {
		transmitChanges(false);
	}
	
	private void transmitChanges(boolean showChanges) {
		launchpad.setDoubleBuffering(!currentWorkingBuffer, currentWorkingBuffer, false, false); // Switch working buffer and send changes
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (!padCache[x][y].equals(preparationPadCache[x][y])) {
					launchpad.setPadColor(x, y, preparationPadCache[x][y], false, false);
				}
			}
		}
		for (int i = 0; i < controlButtons; i++) {
			if (!buttonCache[i].equals(preparationButtonCache[i])) {
				launchpad.setButtonColor(i, preparationButtonCache[i], false, false);
			}
		}
		padCache = preparationPadCache;
		buttonCache = preparationButtonCache;
		
		currentWorkingBuffer = !currentWorkingBuffer;
		launchpad.setDoubleBuffering(currentWorkingBuffer, currentWorkingBuffer, true, false);
	}
	
	public void reset() {
		resetCaches(padCache, buttonCache);
		
		launchpad.reset();
	}
	
	public void clearPads() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				setPadColor(x, y, Color.BLACK);
			}
		}
	}
	
	public void clearButtons() {
		for (int i = 0; i < controlButtons; i++) {
			setButtonColor(i, Color.BLACK);
		}
	}
	
	public void clear() {
		clearPads();
		clearButtons();
	}
	
	private void resetCaches(Color[][] pads, Color[] buttons) {
		for (Color[] row : pads) {
			for (int i = 0; i < pads[0].length; i++) {
				row[i] = new Color();
			}
		}
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new Color();
		}
	}
	
	public void treatButtonsAsPads(boolean state) {
		launchpad.treatButtonsAsPads(state);
	}
	
	public boolean treatButtonsAsPads() {
		return launchpad.treatButtonsAsPads();
	}
	
	public boolean isPadInBounds(int x, int y) {
		return launchpad.isPadInBounds(x, y);
	}
	
	public boolean isButtonInBounds(int index) {
		return launchpad.isButtonInBounds(index);
	}
	
	public int isControlButton(int x, int y) {
		return launchpad.isControlButton(x, y);
	}
	
	public void openInput(MidiDevice inputDevice) throws MidiUnavailableException {
		launchpad.openInput(inputDevice);
	}
	
	public void closeInput() {
		if (isInputOpen()) launchpad.closeInput();
	}
	
	public void openOutput(MidiDevice outputDevice) throws MidiUnavailableException {
		launchpad.openOutput(outputDevice);
		reset();
	}
	
	public boolean isInputOpen() {
		return launchpad.isInputOpen();
	}
	
	public boolean isOutputOpen() {
		return launchpad.isOutputOpen();
	}
	
	public void closeOutput() {
		if (isOutputOpen()) {
			launchpad.reset();
			launchpad.closeOutput();
		}
	}
	
	public void close() {
		closeInput();
		closeOutput();
	}
	
	public LaunchpadListener getListener() {
		return listener;
	}
	
	public LowLevelLaunchpad getLowLevelLaunchpad() {
		return launchpad;
	}
}
