import javax.sound.midi.MidiDevice;
import javax.sound.midi.Transmitter;
import javax.sound.midi.Receiver;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.InvalidMidiDataException;

public abstract class AbstractLaunchpad implements LowLevelLaunchpad {
	private LaunchpadListener listener;
	private Transmitter input;
	private Receiver output;
	
	private boolean doubleBuffering = false;
	private boolean treatButtonsAsPads = false;
	
	private int sentMessages = 0;
	
	
	public void setListener(LaunchpadListener listener) {
		this.listener = listener;
	}
	
	protected abstract void processMessage(MidiMessage msg);
	
	protected void sendMessage(int d1, int d2, int d3) {
		try {
			output.send(new ShortMessage(d1, d2, d3), -1);
			sentMessages++;		
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
	protected void sendSysexMessage(int... data) {
		byte[] bytes = new byte[data.length];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) data[i];
		}
		try {
			output.send(new SysexMessage(bytes, bytes.length), -1);
			sentMessages++;
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
	public int getSentMessages() {
		return sentMessages;
	}
	
	public void reset() {
		sendMessage(0xB0, 0, 0);
	}
	
	public void test(int mode) { // 0 = low, 1 = medium, 2 = full (brightness)
		if (mode < 0 || mode > 2) {
			throw new IllegalArgumentException("Mode out of range: " + mode);
		}
		
		sendMessage(0xB0, 0, 125 + mode);
	}
	
	public void setDutyCycle(int numerator, int denominator) {
		if (numerator < 1 || numerator > 16) {
			throw new IllegalArgumentException("Numerator out of range: " + numerator);
		}
		if (denominator < 3 || denominator > 18) {
			throw new IllegalArgumentException("Denominator out of range: " + denominator);
		}
		
		if (numerator < 9) {
			sendMessage(0xB0, 0x1E, 0x10 * (numerator - 1) + denominator - 3);
		} else {
			sendMessage(0xB0, 0x1F, 0x10 * (numerator - 9) + denominator - 3);
		}
	}
	
	public boolean isDoubleBufferingOn() {
		return doubleBuffering;
	}
	
	public void setDoubleBuffering(boolean updateBuffer, boolean displayBuffer, boolean copy, boolean flash) {
		byte controlByte = 0b0100000;
		if (copy) controlByte |= 1 << 4;
		if (flash) controlByte |= 1 << 3;
		if (updateBuffer) controlByte |= 1 << 2;
		if (displayBuffer) controlByte |= 1;
		
		sendMessage(0xB0, 0, controlByte);
	}
	
	public void openInput(MidiDevice inputDevice) throws MidiUnavailableException {
		if (!inputDevice.isOpen()) inputDevice.open();
		
		input = inputDevice.getTransmitter();
		input.setReceiver(new Receiver() {
			public void send(MidiMessage msg, long timestamp) {
				processMessage(msg);
			}
			
			public void close() {};
		});
	}
	
	public void closeInput() {
		if (input != null) input.close();
		input = null;
	}
	
	public void openOutput(MidiDevice outputDevice) throws MidiUnavailableException {
		if (!outputDevice.isOpen()) outputDevice.open();
		
		output = outputDevice.getReceiver();
	}
	
	public void closeOutput() {
		if (output != null) output.close();
		output = null;
	}
	
	public boolean isInputOpen() {
		return input != null;
	}
	
	public boolean isOutputOpen() {
		return output != null;
	}
	
	public LaunchpadListener getListener() {
		return listener;
	}
	
	public Transmitter getInput() {
		return input;
	}
	
	public Receiver getOutput() {
		return output;
	}
	
	public boolean treatButtonsAsPads() {
		return treatButtonsAsPads;
	}
	
	public void treatButtonsAsPads(boolean state) {
		treatButtonsAsPads = state;
	}
}
