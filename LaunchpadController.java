import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;

public interface LaunchpadController extends LaunchpadInterface, PadStorage {
	/**
	 * This method is used to tell the controller if an exception should be
	 * thrown when a drawn shape does not fit onto the grid.
	 * 
	 * @param state true if no exceptions should be thrown.
	 */
	public void allowShapeCutting(boolean state);
	
	/**
	 * @return true if no exceptions should be thrown when a drawn shape does not fit onto the grid.
	 */
	public boolean allowsShapeCutting();
	
	/**
	 * @return the x offset of this controller.
	 */
	public int getXOffset();
	
	/**
	 * @return the y offset of this controller.
	 */
	public int getYOffset();
	
	/**
	 * Displays the given <code>PadSource</code> at the given coordinates. This is the same as
	 * calling <code>display(new BasicManipulator(shape, x, y))</code>.
	 * 
	 * @param shape the displayed <code>PadSource</code>.
	 * @param x the x coordinate.
	 * @param y the y coordinate.
	 */
	public void display(PadSource source, int x, int y);
	
	/**
	 * Displays the given <code>PadSource</code>.
	 * 
	 * @param shapeView the displayed <code>PadSource</code>.
	 */
	public void display(PadSource source);
	
	/**
	 * Sets the color of a control button.
	 * 
	 * @param index The index of the button whose color gets changed.
	 * @param color The color which the button will turn into.
	 */
	public void setButtonColor(int index, Color color);
	 
	 /**
	  * Gets the color of a control button.
	  * 
	  * @param index The index of the button whose color gets returned.
	  * @return The color of the control button.
	  */
	public Color getButtonColor(int index);
	
	/*
	 * Redraws all the pads and buttons, using the saved color states. This can
	 * be used to synchronize the Launchpad with the
	 * <code>LaunchpadController</code>.
	 */
	public void redraw();
	
	/**
	 * Sets how the <code>LaunchpadController</code> behaves when a double
	 * buffering functionality is used.
	 * 
	 * @param doubleBufferingMode The new double buffering mode.
	 */
	public void setDoubleBufferingMode(DoubleBufferingMode doubleBufferingMode);
	
	/**
	 * Returns an object which indicates how the <code>LaunchpadController</code>
	 * behaves when a double buffering functionality is used.
	 * 
	 * @return doubleBufferingMode the double buffering mode.
	 */
	public DoubleBufferingMode getDoubleBufferingMode();
	
	/**
	 * @return true if the <code>LaunchpadController</code> is currently
	 * in the preparing mode.
	 */
	public boolean isPreparing();
	
	/**
	 * Goes into the preparing mode. This supports multiple layers, which means
	 * that when this method is called twice, <code>present()</code> also has
	 * to be called twice to fully leave that mode. This is to ensure that
	 * utility methods which use this functionality do not create conflicts when
	 * used within code that also uses the preparing mode.
	 */
	public void prepare();
	
	/**
	 * Leaves the preparing mode. This supports multiple layers, which means
	 * that when <code>prepare()</code> is called twice, this method also has
	 * to be called twice to fully leave that mode. This is to ensure that
	 * utility methods which use this functionality do not create conflicts when
	 * used within code that also uses the preparing mode.
	 */
	public void present();
	
	/**
	 * Transmits the changes made while in preparing mode to the Launchpad.
	 */
	public void transmitChanges();
	
	/**
	 * Clears all pads on the grid.
	 */
	public void clearPads();
	
	/**
	 * Clears all buttons on the grid.
	 */
	public void clearButtons();
	
	/**
	 * Clears all pads and buttons on the grid.
	 */
	public void clear();
	
	/**
	 * Resets the Launchpad completely, including the duty cycle (if supported)
	 * and the current double buffering state.
	 */
	public void reset();
	
	/**
	 * Opens a midi input.
	 */
	public void openInput(MidiDevice device);
	
	/**
	 * Opens a midi output.
	 */
	public void openOutput(MidiDevice device);
	
	/**
	 * Closes the midi input.
	 */
	public void closeInput();
	
	/**
	 * Closes the midi output.
	 */
	public void closeOutput();
}
