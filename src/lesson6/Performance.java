package lesson6;

import java.util.Timer;
import java.util.TimerTask;

public class Performance {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Timer	t = new Timer();
		
		TimerTask	tt = new TimerTask() {
			@Override
			public void run() {
				System.err.println("Tick!");
			}
		};
		
		t.schedule(tt,2000);
		tt.cancel();
		System.err.println("the end!!");
		Runtime.getRuntime().halt(0);
	}

}
