import javax.sound.midi.MidiMessage;

public class LaunchpadS extends AbstractLaunchpad {
	public static final int WIDTH = 8, HEIGHT = 8, CONTROL_BUTTONS = 16;
	
	
	public int getWidth() {
		return WIDTH;
	}
	
	public int getHeight() {
		return HEIGHT;
	}
	
	public int getControlButtons() {
		return CONTROL_BUTTONS;
	}
	
	public void processMessage(MidiMessage msg) {
		if (getListener() == null) return; // There is no point.
		
		byte[] bytes = msg.getMessage();
		byte status = bytes[0];
		byte button = bytes[1];
		byte velocity = bytes[2];
		
		int index = -1, x = button % 16, y = button / 16;
		if (status == -80) { // Round button at the top
			index = button - 104;
		} else if (x == 8) { // Round button on the right side
			index = y + 8;
		}
		
		if (index == -1 || treatButtonsAsPads()) { // If pad or can be treated as one
			if (index != -1 && treatButtonsAsPads()) { // If button
				if (!isButtonInBounds(index)) return; // Continue only if valid
				int[] point = buttonToPad(index); // Convert
				x = point[0];
				y = point[1];
			}
			
			if (isPadInBounds(x, y)) { // If valid
				if (velocity == 0) { // If released
					getListener().padReleased(x, y);
				} else { // If pressed
					getListener().padPressed(x, y);
				}
			}
		} else { // If button
			if (isButtonInBounds(index)) { // If valid
				if (velocity == 0) { // If released
					getListener().buttonReleased(index);
				} else { // If pressed
					getListener().buttonPressed(index);
				}
			}
		}
	}
	
	public void setPadColor(int x, int y, Color color, boolean copy, boolean clear) {
		check(x, y);
		
		if (treatButtonsAsPads() && (x == 8 || y == -1)) {
			setButtonColor(padToButton(x, y), color, copy, clear);
		} else {
			sendMessage(0x90, y * 16 + x, Utils.generateOldColorCode(color, copy, clear));
		}
	}
	
	public void setButtonColor(int index, Color color, boolean copy, boolean clear) {
		check(index);
		
		byte colorCode = Utils.generateOldColorCode(color, copy, clear);
		if (index < 8) {
			sendMessage(0xB0, 104 + index, colorCode);
		} else {
			sendMessage(0x90, 8 + (index - 8) * 16, colorCode);
		}
	}
	
	private int[] buttonToPad(int index) {
		check(index);
		
		int[] res = new int[2];
		if (index < 8) {
			res[0] = index;
			res[1] = -1;
		} else {
			res[0] = 8;
			res[1] = index - 8;
		}
		return res;
	}
	
	private int padToButton(int x, int y) {
		check(x, y);
		return (x == 8) ? y + 8 : x;
	}
	
	public boolean isPadInBounds(int x, int y) {
		return ((x >= 0 && x < WIDTH // If x in regular bounds
				&& y >= 0 && y < WIDTH) // And y in regular bounds
				|| (treatButtonsAsPads() // Or, if buttons should be treated as pads,
				&& ((y == -1 && x >= 0 && x < WIDTH) // If it is one of the first 8 control buttons
				|| (x == WIDTH && y >= 0 && y < WIDTH)) // Or one of the last 8
				));
	}
	
	public boolean isButtonInBounds(int index) {
		return (index >= 0 && index < CONTROL_BUTTONS); // If it is one of the 16 control buttons
	}
	
	public int isControlButton(int x, int y) {
		check(x, y);
		if (x == 8) {
			return y + 8;
		} else if (y == -1) {
			return x;
		} else {
			return -1;
		}
	}
	
	private void check(int x, int y) {
		if (!isPadInBounds(x, y)) {
			throw new IndexOutOfBoundsException("Coordinates out of bounds: x = " + x + ", y = " + y + ".");
		}
	}
	
	private void check(int index) {
		if (!isButtonInBounds(index)) {
			throw new IndexOutOfBoundsException("Button index out of bounds: index = " + index + ".");
		}
	}
}
