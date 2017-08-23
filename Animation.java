public interface Animation extends PadSource {
	/**
	 * Load next frame. If the current frame is the last, throw
	 * NoSuchElementException.
	 */
	public void nextFrame();
	
	/**
	 * @return true when the current frame is the last one.
	 */
	public boolean isLastFrame();
}
