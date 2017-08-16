import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

public class Utils {
	public static void printAsText(LaunchpadController controller) { 
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
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
	
	// TODO_URGENT: Revise this method
	// <return>: Array with length 2: index 0 = input device, index 1 = output device
	private static MidiDevice[] findDevice(String name) {
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
		
		return new MidiDevice[]{inputDevice, outputDevice};
	}
	
	public static MidiDevice[] findLaunchpad(LowLevelLaunchpad device) {
		if (device instanceof LaunchpadS) {
			return findDevice("S");
		} else if (device instanceof LaunchControl) {
			return findDevice("Control");
		}
		
		throw new IllegalArgumentException("Device not supported.");
	}
}
