public abstract class AbstractLaunchpadController implements LaunchpadController, LaunchpadListener {
	private boolean allowsShapeCutting = false;
	private boolean treatButtonsAsPads = false;
	
	public void allowShapeCutting(boolean state) {
		allowsShapeCutting = state;
	}
	
	public boolean allowsShapeCutting() {
		return allowsShapeCutting;
	}
	
	public void treatButtonsAsPads(boolean state) {
		treatButtonsAsPads = state;
	}
	
	public boolean treatButtonsAsPads() {
		return treatButtonsAsPads;
	}
	
	public void layerPadColor(int x, int y, Color color) {
		setPadColor(x, y, Color.layer(getPadColor(x, y), color));
	}
	
	public void layerButtonColor(int index, Color color) {
		setButtonColor(index, Color.layer(getButtonColor(index), color));
	}
	
	public void displayShape(Shape shape, int xOffset, int yOffset) {
		ShapeView shapeView = new ShapeView(shape, xOffset, yOffset);
		
		display(shapeView);
	}
	
	public void display(ShapeView shape) {
		for (Pad pad : shape) {
			if (isPadInBounds(pad.getX(), pad.getY())) {
				layerPadColor(pad.getX(), pad.getY(), pad.getColor());
			} else if (!allowsShapeCutting) {
				throw new RuntimeException("Shape doesn't fit onto LaunchpadController");
			}
		}
	}
}
