public class MovingAnimation extends Manipulator implements Animation {
	BasicManipulator manipulator;
	private int xSpeed = 0;
	private int ySpeed = 0;
	
	
	public MovingAnimation(PadSource source) {
		super(source);
		manipulator = new BasicManipulator(source, 0, 0);
	}
	
	public void setXOffset(int x) {
		manipulator.setXOffset(x);
	}
	
	public void setYOffset(int y) {
		manipulator.setYOffset(y);
	}
	
	public void setXSpeed(int xSpeed) {
		this.xSpeed = xSpeed;
	}
	
	public void setYSpeed(int ySpeed) {
		this.ySpeed = ySpeed;
	}
	
	public int getWidth() {
		return manipulator.getWidth();
	}
	
	public int getHeight() {
		return manipulator.getHeight();
	}
	
	public int getXOffset() {
		return manipulator.getXOffset();
	}
	
	public int getYOffset() {
		return manipulator.getYOffset();
	}
	
	public Color getPadColor(int x, int y) {
		return manipulator.getPadColor(x, y);
	}
	
	public void nextFrame() {
		manipulator.setXOffset(manipulator.getXOffset() + xSpeed);
		manipulator.setYOffset(manipulator.getYOffset() + ySpeed);
	}
	
	public boolean isLastFrame() {
		return false;
	}
}
