package lesson15;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
		
		Stream.generate(()->Math.random()).limit(10).forEach((t)->System.err.println("Random="+t));
		Stream.iterate(1,(n)->2*n).limit(10).forEach((t)->System.err.println("Iterator="+t));
		StreamSupport.stream(Arrays.asList("123","abc","123","abc","12345","abcde","12345","abcde").spliterator(),false).forEach((t)->System.err.println("Built="+t)); 
	}

	static Stream<String> createStream() {
//		return Stream.of("123","abc","123","abc","12345","abcde","12345","abcde");
		return Arrays.asList("123","abc","123","abc","12345","abcde","12345","abcde").stream();
	}
}
