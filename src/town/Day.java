package town;

import java.util.Timer;
import java.util.TimerTask;

public class Day extends Phase {
	int time = 4000;
	Phase next = new Accusation();
	
	@Override
	public void run() {
		Timer timer = new Timer("Phase Timer");
		timer.schedule(next, time);
		
	}
		
}
