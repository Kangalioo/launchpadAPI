import java.util.NoSuchElementException;

public class ClassicAnimation implements Animation {
	private boolean running = false;
	private boolean loopContinuously = false;
	private int cyclesLeft = 0;
	
	private Shape[] frames;
	private int currentFrame = 0;
	
	
	public ClassicAnimation(Shape... frames) {
		if (frames.length == 0) {
			throw new IllegalArgumentException("Animation must contain at least one frame.");
		}
		int width = frames[0].getWidth();
		int height = frames[0].getHeight();
		for (int i = 1; i < frames.length; i++) {
			if (frames[i].getWidth() != width || frames[i].getHeight() != height) {
				throw new IllegalArgumentException("Frames must all be of equal dimensions.");
			}
		}
		this.frames = frames;
	}
	
	private void checkRunning() {
		if (running) {
			throw new IllegalStateException("This cannot be done while the animation is running.");
		}
	}
	
	public void setCycles(int cycles) {
		if (cycles <= 0) {
			throw new IllegalArgumentException("Cycles argument must be at least 1.");
		}
		checkRunning();
		
		this.cyclesLeft = cycles - 1;
	}
	
	public void loopContinuously(boolean state) {
		checkRunning();
		
		loopContinuously = state;
	}
	
	public int getWidth() {
		return frames[0].getWidth();
	}
	
	public int getHeight() {
		return frames[0].getHeight();
	}
	
	public void nextFrame() {
		if (isLastFrame()) {
			throw new NoSuchElementException();
		}
		
		currentFrame++;
		if (currentFrame == frames.length) {
			cyclesLeft--;
			currentFrame = 0;
		}
	}
	
	public boolean isLastFrame() {
		return (!loopContinuously && cyclesLeft == 0 && currentFrame == frames.length - 1);
	}
	
	public void reset() {
		currentFrame = 0;
		cyclesLeft = 0;
		running = false;
	}
	
	public Color getPadColor(int x, int y) {
		return frames[currentFrame].getPadColor(x, y);
	}
}
