import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import java.util.ArrayList;

// TODO: Fix control button system. It's a mess.
public class LaunchpadLayout extends AbstractLaunchpadController implements LaunchpadController, LaunchpadListener {
	private class Device {
		Launchpad launchpad;
		int xOffset, yOffset, rot;
		
		public Device(Launchpad launchpad, int xOffset, int yOffset, int rot) {
			this.launchpad = launchpad;
			this.xOffset = xOffset;
			this.yOffset = yOffset;
			this.rot = rot % 4;
			
			launchpad.setListener(new LaunchpadListener() {
				public void padPressed(int x, int y) {
					int[] coord = convert(x, y);
					LaunchpadLayout.this.padPressed(coord[0], coord[1]);
				}
				
				public void padReleased(int x, int y) {
					int[] coord = convert(x, y);
					LaunchpadLayout.this.padReleased(coord[0], coord[1]);
				}
			});
		}
		
		int[] convert(int x, int y) {
			int[] rotatedCoordinates = Utils.rotate(x, y, rot);
			rotatedCoordinates[0] += xOffset;
			rotatedCoordinates[1] += yOffset;
			return rotatedCoordinates;
		}
		
		int[] convertBack(int x, int y) {
			return Utils.rotate(x - xOffset, y - yOffset, -rot);
		}
	}
	
	
	private LaunchpadListener listener;
	
	private ArrayList<Device> devices = new ArrayList<>();
	
	private ArrayList<Pad> controlButtons = new ArrayList<>();
	
	private boolean ignoreIncorrectCoordinates = false;
	
	
	public void addDevice(Launchpad launchpad, int x, int y, int rot) {
		devices.add(new Device(launchpad, x, y, rot));
	}
	
	public void addControlButton(int x, int y) {
		check(x, y);
		controlButtons.add(new Pad(x, y, null));
	}
	
	// Adds an area of pads as control buttons
	public void addControlButtons(int xo, int yo, int width, int height, boolean reverse) {
		if (width < 0 || height < 0) {
			throw new IllegalArgumentException("Dimensions must not be negative.");
		}
		if (reverse) {
			for (int y = height - 1; y >= 0; y--) {
				for (int x = width - 1; x >= 0; x--) {
					addControlButton(x + xo, y + yo);
				}
			}
		} else {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					addControlButton(x + xo, y + yo);
				}
			}
		}
	}
	
	public void addControlButtons(int xo, int yo, int width, int height) {
		addControlButtons(xo, yo, width, height, false);
	}
	
	public void setListener(LaunchpadListener listener) {
		this.listener = listener;
	}
	
	public void padPressed(int x, int y) {
		if (listener != null) {
			int index = controlButtons.indexOf(new Pad(x, y, this));
			if (!treatButtonsAsPads() && index != -1) listener.buttonPressed(index);
			else if (isPadInBounds(x, y)) listener.padPressed(x, y);
		}
	}
	
	public void padReleased(int x, int y) {
		if (listener != null) {
			int index = controlButtons.indexOf(new Pad(x, y, this));
			if (!treatButtonsAsPads() && index != -1) listener.buttonReleased(index);
			else if (isPadInBounds(x, y)) listener.padReleased(x, y);
		}
	}
	
	public void setPadColor(int x, int y, Color color) {
		if (check(x, y)) return;
		setPadColorPrivate(x, y, color);
	}
	
	private void setPadColorPrivate(int x, int y, Color color) {
		Device device = findMatching(x, y);
		int[] c = device.convertBack(x, y);
		device.launchpad.setPadColor(c[0], c[1], color);
	}
	
	public Color getPadColor(int x, int y) {
		if (check(x, y)) return null;
		return getPadColorPrivate(x, y);
	}
	
	private Color getPadColorPrivate(int x, int y) {
		Device device = findMatching(x, y);
		int[] c = device.convertBack(x, y);
		return device.launchpad.getPadColor(c[0], c[1]);
	}
	
	public void setButtonColor(int index, Color color) {
		check(index);
		Pad button = controlButtons.get(index);
		setPadColorPrivate(button.getX(), button.getY(), color);
	}
	
	public Color getButtonColor(int index) {
		check(index);
		Pad button = controlButtons.get(index);
		return getPadColorPrivate(button.getX(), button.getY());
	}
	
	public boolean isButtonInBounds(int index) {
		return (index >= 0 && index < controlButtons.size());
	}
	
	public int isButton(int x, int y) {
		int counter = 0;
		for (Pad pad : controlButtons) {
			if (pad.getX() == x && pad.getY() == y) {
				return counter;
			}
			counter++;
		}
		return -1;
	}
	
	public boolean isPadInBounds(int x, int y) {
		// TODO: Currently, the matching launchpad is calculated twice (isPadInBounds and findDevice) on every event.
		return (findMatching(x, y) != null) && (treatButtonsAsPads() || isButton(x, y) == -1);
	}
	
	private Device findMatching(int x, int y) {
		for (Device device : devices) {
			int[] c = device.convertBack(x, y);
			if (device.launchpad.isPadInBounds(c[0], c[1])) {
				return device;
			}
		}
		
		return null;
	}
	
	public int sumOfSentMessages() {
		//return devices.stream()
				//.mapToInt(d -> ((AbstractLaunchpad) d.launchpad.getLowLevelLaunchpad()).getSentMessages())
				//.sum();
		
		int sum = 0;
		for (Device d : devices) {
			LowLevelLaunchpad lowLevel = d.launchpad.getLowLevelLaunchpad();
			if (lowLevel instanceof AbstractLaunchpad) {
				sum += ((AbstractLaunchpad) lowLevel).getSentMessages();
			} else {
				throw new RuntimeException("At least one device does not support the getSentMessages() method.");
			}
		}
		return sum;
	}
	
	// Redraws from cache
	public void redraw() {
		devices.forEach(d -> d.launchpad.redraw());
	}
	
	public boolean isFlashingModeActivated() {
		// TODO
		return false;
	}
	
	public void flash() {
		// TODO
	}
	
	public void enterFlashingMode() {
		// TODO
	}
	
	public void leaveFlashingMode() {
		// TODO
	}
	
	public void setDoubleBufferingMode(DoubleBufferingMode doubleBufferingMode) {
		devices.forEach(d -> d.launchpad.setDoubleBufferingMode(doubleBufferingMode));
	}
	
	public boolean isPreparing() {
		// TODO
		return false;
	}
	
	public void prepare() {
		devices.forEach(d -> d.launchpad.prepare());
	}
	
	public void present() {
		devices.forEach(d -> d.launchpad.present());
	}
	
	public void transmitChanges() {
		devices.forEach(d -> d.launchpad.transmitChanges());
	}
	
	public void clear() {
		clearPads();
		clearButtons();
	}
	
	public void clearPads() {
		// TODO
	}
	
	public void clearButtons() {
		for (int i = 0; i < controlButtons.size(); i++) {
			setButtonColor(i, Color.BLACK);
		}
	}
	
	public void reset() {
		devices.forEach(d -> d.launchpad.reset());
	}
	
	public void closeInput() {
		devices.forEach(d -> d.launchpad.closeInput());
	}
	
	public void closeOutput() {
		devices.forEach(d -> d.launchpad.closeOutput());
	}
	
	public void close() {
		closeInput();
		closeOutput();
	}
	
	public void ignoreIncorrectCoordinates(boolean state) {
		ignoreIncorrectCoordinates = state;
	}
	
	// Returns true if calling method has to be terminated
	private boolean check(int x, int y) {
		if (!isPadInBounds(x, y)) {
			if (ignoreIncorrectCoordinates) return true;
			throw new IndexOutOfBoundsException("Coordinates out of bounds: x = " + x + ", y = " + y + ".");
		}
		
		return false;
	}
	
	private void check(int index) {
		if (!isButtonInBounds(index)) {
			throw new IndexOutOfBoundsException("Button index out of bounds: index = " + index + ".");
		}
	}
	
	public LaunchpadListener getListener() {
		return listener;
	}
}
