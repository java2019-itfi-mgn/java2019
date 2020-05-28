package lesson13;

public class JMXInterface implements JMXInterfaceMBean {
	private final JMXServer 	parent;
	private volatile String		text = null;
	
	public JMXInterface(final JMXServer parent) {
		this.parent = parent;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(final String text) {
	    System.err.println("Change text to "+text);
		this.text = text;
	}

	@Override
	public int add(int value1, int value2) {
		return value1 + value2;
	}

	@Override
	public void stop() {
		parent.stop();
	}
}