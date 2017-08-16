import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;

public interface LaunchpadController extends LaunchpadInterface, ReadablePadStorage {
	/**
	 * This method is used to tell the controller if an exception should be
	 * thrown when a drawn shape does not fit onto the grid.
	 * 
	 * @param state True if no exceptions should be thrown.
	 */
	public void allowShapeCutting(boolean state);
	
	/**
	 * @return Returns true if no exceptions should be thrown when a drawn shape does not fit onto the grid.
	 */
	public boolean allowsShapeCutting();
	
	/**
	 * Displays the given <code>Shape</code> at the given coordinates. This is the same as
	 * calling <code>display(new ShapeView(shape, x, y))</code>.
	 * 
	 * @param shape The displayed <code>Shape</code>.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public default void displayShape(Shape shape, int x, int y) {
		display(new ShapeView(shape, x, y));
	}
	
	/**
	 * Displays the given <code>ShapeView</code>.
	 * 
	 * @param shapeView The displayed <code>ShapeView</code>.
	 */
	public void display(ShapeView shapeView);
	
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
	
	//~ public boolean isFlashingModeActivated();
	
	//~ public void flash();
	
	//~ public void enterFlashingMode();
	
	//~ public void leaveFlashingMode();
	
	/**
	 * Sets how the <code>LaunchpadController</code> behaves when a double
	 * buffering functionality is used.
	 * 
	 * @param doubleBufferingMode The new double buffering mode.
	 */
	public void setDoubleBufferingMode(DoubleBufferingMode doubleBufferingMode);
	
	/**
	 * @return Returns true if the <code>LaunchpadController</code> is currently
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
	 * Closes the midi input.
	 */
	public void closeInput();
	
	/**
	 * Closes the midi output.
	 */
	public void closeOutput();
}
