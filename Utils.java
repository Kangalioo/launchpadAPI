import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.awt.geom.Rectangle2D;

public class Utils {
	public static void printAsText(PadSource controller) { 
		for (int y = 0; y < controller.getHeight(); y++) {
			for (int x = 0; x < controller.getWidth(); x++) {
				System.out.print(controller.getPadColor(x, y).isInvisible() ? "░░" : "██");
			}
			System.out.println();
		}
	}
	
	static byte generateOldColorCode(Color color, boolean copy, boolean clear) {
		//~ int red = Math.round(color.getRed() / (255 / 3));
		//~ int green = Math.round(color.getGreen() / (255 / 3));
		int red = color.getRed() / 64;
		int green = color.getGreen() / 64;
		byte colorByte = (byte) ((green << 4) | red);
		if (copy) colorByte |= 1 << 2;
		if (clear) colorByte |= 1 << 3;
		return colorByte;
	}
	
	// Rotates around (0, 0)
	// rot: 0 = 0°, 1 = 90°, 2 = 180°, 3 = 270°
	static int[] rotate(int x, int y, int rot) {
		rot = Math.floorMod(rot, 4);
		
		if (rot % 2 == 1) {
			int temp = x;
			x = -y;
			y = temp;
		}
		if (rot >= 2) {
			x = -x;
			y = -y;
		}
		
		return new int[]{x, y};
	}
	
	// TODO: Revise this method
	// <return>: Array with length 2: index 0 = input device, index 1 = output device
	private static MidiDevice[] findDevice(String name) throws MidiUnavailableException {
		MidiDevice inputDevice = null;
		MidiDevice outputDevice = null;
		
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
		
		for (MidiDevice.Info info : infos) {
			if (info.getName().split(" ")[0].equals(name)) { // "info.getName().equals("Launchpad S")" for Windows
				try {
					if (inputDevice == null) {
						inputDevice = MidiSystem.getMidiDevice(info);
					} else if (outputDevice == null) {
						outputDevice = MidiSystem.getMidiDevice(info);
						break;
					}
				} catch (MidiUnavailableException e) {
					e.printStackTrace();
				}
			}
		}
		if (inputDevice == null || outputDevice == null) {
			throw new MidiUnavailableException("The device was not found.");
		}
		
		return new MidiDevice[]{inputDevice, outputDevice};
	}
	
	public static MidiDevice[] findLaunchpad(LowLevelLaunchpad device) throws MidiUnavailableException {
		if (device instanceof LaunchpadS) {
			return findDevice("S");
		} else if (device instanceof LaunchControl) {
			return findDevice("Control");
		}
		
		throw new IllegalArgumentException("Device not supported.");
	}
	
	/**
	 * @return an Iterable<Pad> which iterates through the smallest rectangle
	 *  which covers all pads in the PadSource.
	 */
	static Iterable<Pad> padSourceIterable(PadSource source) {
		return new Iterable<Pad>() {
			public Iterator<Pad> iterator() {
				return new Iterator<Pad>() {
					private int x = 0, y = 0;
					
					public boolean hasNext() {
						return y < source.getHeight();
					}
					
					public Pad next() {
						if (y >= source.getHeight()) {
							throw new NoSuchElementException();
						}
						Pad pad = new Pad(x + source.getXOffset(), y + source.getYOffset(), source);
						if (++x == source.getWidth()) {
							x = 0;
							y++;
						}
						return pad;
					}
				};
			}
		};
	}
	
	static boolean intersects(PadModule m1, PadModule m2) {
		return 
			(new Rectangle2D.Double(m1.getXOffset(), m1.getYOffset(), m1.getWidth(), m1.getHeight())).intersects(
			new Rectangle2D.Double(m2.getXOffset(), m2.getYOffset(), m2.getWidth(), m2.getHeight()));
	}
}
