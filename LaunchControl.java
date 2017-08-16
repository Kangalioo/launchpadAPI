import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

public class LaunchControl extends AbstractLaunchpad {
	public static final int WIDTH = 8, HEIGHT = 1, CONTROL_BUTTONS = 4;
	
	
	private LaunchControlListener launchControlListener = null;
	
	private int currentTemplate = 0;
	
	
	public int getWidth() {
		return WIDTH;
	}
	
	public int getHeight() {
		return HEIGHT;
	}
	
	public int getControlButtons() {
		return CONTROL_BUTTONS;
	}
	
	protected void processMessage(MidiMessage message) {
		if (getListener() == null) return;
		
		if (message instanceof ShortMessage) {
			ShortMessage msg = (ShortMessage) message;
			
			int index = msg.getData1();
			int status = msg.getStatus();
			boolean pressed = msg.getData2() > 0;
			if (status >= 176 && status < 192) {
				if (index < 100) {
					if (index > 40) {
						index -= 12;
					}
					index -= 21;
					
					if (launchControlListener != null) launchControlListener.knobChanged(index, msg.getData2());
				} else {
					if (pressed) {
						getListener().buttonPressed(index);
					} else {
						getListener().buttonReleased(index);
					}
				}
			} else if (status >= 128 && status < 160) {
				if (index >= 9 && index <= 12) {
					index -= 9;
				} else if (index >= 25 && index <= 28) {
					index -= 21;
				}
				if (pressed) {
					getListener().padPressed(index, 0);
				} else {
					getListener().padReleased(index, 0);
				}
			}
		} else if (message instanceof SysexMessage) {
			SysexMessage msg = (SysexMessage) message;
			
			if (msg.getData()[5] == 119) {
				currentTemplate = msg.getData()[6];
			}
		}
	}
	
	private void setLed(int index, Color color, boolean copy, boolean clear) {
		sendSysexMessage(240, 0, 32, 41, 2, 10, 120, currentTemplate, index, Utils.generateOldColorCode(color, copy, clear), 247);
	}
	
	public void setPadColor(int x, int y, Color color, boolean copy, boolean clear) {
		check(x, y);
		setLed(x, color, copy, clear);
	}
	
	public void setButtonColor(int index, Color color, boolean copy, boolean clear) {
		check(index);
		setLed(index + 8, color, copy, clear);
	}
	
	public boolean isPadInBounds(int x, int y) {
		return (x >= 0 && x < WIDTH && y == 0);
		// treatButtonsAsPads has no effect as the buttons are not suitable to be used as pads
	}
	
	public boolean isButtonInBounds(int index) {
		return (index >= 0 && index < CONTROL_BUTTONS);
	}
	
	public int isControlButton(int x, int y) {
		return -1; // See comment in isPadInBounds
	}
	
	@Override
	public void setListener(LaunchpadListener listener) {
		// When we have an advanced listener...
		if (listener instanceof LaunchControlListener) {
			// ...then we want to save it, when we have to access its functionality
			launchControlListener = (LaunchControlListener) listener;
		}
		
		super.setListener(listener);
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
