package lesson15;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// https://www.baeldung.com/java-8-streams
		System.err.println("Count="+
			createStream()	// Source 
			.filter((item)->item.length() < 5)	// Intermediate 1 
			.distinct()		// Intermediate 2
			.count());		// Destination
		System.err.println("Sum="+IntStream.builder().add(0).add(1).add(2).build().sum());
	}

	static Stream<String> createStream() {
//		return Stream.of("123","abc","123","abc","12345","abcde","12345","abcde");
		return Arrays.asList("123","abc","123","abc","12345","abcde","12345","abcde").stream();
	}
}
