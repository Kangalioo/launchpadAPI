public abstract class Manipulator implements PadSource {
	PadSource padSource;
	
	
	public Manipulator(PadSource padSource) {
		setSource(padSource);
	}
	
	public void setSource(PadSource padSource) {
		if (padSource == null) throw new NullPointerException();
		this.padSource = padSource;
	}
	
	public PadSource getSource() {
		return padSource;
	}
}
