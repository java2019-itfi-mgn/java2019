package lesson13;

public interface JMXInterfaceMBean {
	String getText();
	void setText(String text);
	int add(int value1, int value2);
	void stop();
}
