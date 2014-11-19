import java.util.Comparator;

//This is basically a timestamp, and helps order the events to be processed in the queue


public class Pevents implements Comparable {
	
	public double time;  //in sec, converted from nano
	public boolean shouldProcess;  //if this collision becomes unimportant, don't process this collision
	
	public Particle particleOne;
	public Particle particleTwo;
	
	public Pevents(double timeUntilCollision, Particle one, Particle two) {
		// the first part finds the current time when this collision is calculated, then I add the timeUntil this collision.
		//This creates gives this event a simulation time.
		time = timeUntilCollision; 
		shouldProcess = true;
		
		particleOne = one;
		particleTwo = two;
		
	}

	//with the priority queue will help with the positioning.
	public int compareTo(Object o) {
		if (time > ((Pevents) o).time) {
			return 1;
		} else if (time < ((Pevents) o).time) {
			return -1;
		} else {
			return 0;
		}
	}
}
