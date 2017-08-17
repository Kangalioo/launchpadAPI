import javax.sound.midi.MidiDevice;
import java.util.ArrayList;

public class LaunchpadPane extends AbstractLaunchpadController {
	private LaunchpadController launchpad;
	
	private int width, height, xOffset, yOffset;
	
	private Shape directDrawing;
	private ArrayList<ShapeView> shapes = new ArrayList<>();
	
	public LaunchpadPane(LaunchpadController launchpad) {
		this.launchpad = launchpad;
		launchpad.setListener(this);
		launchpad.allowShapeCutting(true);
		width = launchpad.getWidth();
		height = launchpad.getHeight();
		xOffset = launchpad.getXOffset();
		yOffset = launchpad.getYOffset();
		directDrawing = new Shape(width, height);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getXOffset() {
		return xOffset;
	}
	
	public int getYOffset() {
		return yOffset;
	}
	
	public void addShape(ShapeView view) {
		shapes.add(view);
	}
	
	public ShapeView[] getShapes() {
		return shapes.toArray(new ShapeView[shapes.size()]);
	}
	
	public void setButtonColor(int index, Color color) {
		launchpad.setButtonColor(index, color);
	}
	
	public Color getButtonColor(int index) {
		return launchpad.getButtonColor(index);
	}
	
	public void setPadColor(int x, int y, Color color) {
		directDrawing.setPadColor(x - xOffset, y - yOffset, color);
	}
	
	public Color getPadColor(int x, int y) {
		return directDrawing.getPadColor(x - xOffset, y - yOffset);
	}
	
	public void setDoubleBufferingMode(DoubleBufferingMode mode) {
		launchpad.setDoubleBufferingMode(mode);
	}
	
	public DoubleBufferingMode getDoubleBufferingMode() {
		return launchpad.getDoubleBufferingMode();
	}
	
	public void redraw() {
		prepare();
		clear();
		// Cannot use because Java is getting on nerves
		// with ConcurrentModificationException
		for (int i = 0; i < shapes.size(); i++) {
			launchpad.display(shapes.get(i));
		}
		launchpad.displayShape(directDrawing, xOffset, yOffset);
		present();
	}
	
	public void prepare() {
		launchpad.prepare();
	}
	
	public void present() {
		launchpad.present();
	}
	
	public void transmitChanges() {
		launchpad.transmitChanges();
	}
	
	public boolean isPreparing() {
		return launchpad.isPreparing();
	}
	
	public boolean isButtonInBounds(int index) {
		return launchpad.isButtonInBounds(index);
	}
	
	public boolean isPadInBounds(int x, int y) {
		return launchpad.isPadInBounds(x, y);
	}
	
	public void clearButtons() {
		launchpad.clearButtons();
	}
	
	public void clearPads() {
		launchpad.clearPads();
	}
	
	public void clear() {
		clearButtons();
		clearPads();
	}
	
	public void reset() {
		launchpad.reset();
		clear();
	}
	
	public void treatButtonsAsPads(boolean state) {
		super.treatButtonsAsPads(state);
		launchpad.treatButtonsAsPads(state);
	}
	
	public void openInput(MidiDevice device) {
		launchpad.openInput(device);
	}
	
	public void openOutput(MidiDevice device) {
		launchpad.openOutput(device);
	}
	
	public void closeInput() {
		launchpad.closeInput();
	}
	
	public void closeOutput() {
		launchpad.closeOutput();
	}
	
	public boolean isInputOpen() {
		return launchpad.isInputOpen();
	}
	
	public boolean isOutputOpen() {
		return launchpad.isOutputOpen();
	}
	
	public Exception checkError() {
		return launchpad.checkError();
	}
}
