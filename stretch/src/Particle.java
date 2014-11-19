/*Particle
 *
 *Representation of a particle:
 *
 *Holds a position, a velocity vector, and methods to be called to draw this particle
 */

import java.util.ArrayList;
import java.util.Random;

//import javax.vecmath.Vector2d;

public class Particle {

	public Vector2d myVelocityVector;  //velocity vector
	public Vector2d myPosition; //position vector
	public Vector2d myForce; //acceleration vector
	public double myRadius;
	public double myMass;
	
	public Vector2d myLastPosition; // useful for calculating forces
	

	
	//creates a particle with a randomly chosen velocity direction
	Particle (double x, double y, double velocity, double radius, double mass) {
		//Create a random Vector with that magnitude
		Double randomAngle = 2*Math.PI*Math.random();
		Double xVec = velocity*Math.cos(randomAngle);
		Double yVec = velocity*Math.cos(randomAngle);
		
		myVelocityVector = new Vector2d(0, 0);
		myPosition = new Vector2d(x,y);
		myLastPosition = (Vector2d) myPosition.clone();
		myForce = new Vector2d(0,0);
		
		myRadius = radius;
		myMass = mass;		
	}
	
	//used to find the work from stretching the polymer
	//is calculated by Force*distance, force in x direction on the lead pulled particle times distance
	public double findWork (Particle left, double time, double pullrate) {
		Vector2d lForce = ((Vector2d) left.myPosition.clone());
		lForce.sub(myPosition);
		lForce.scale((lForce.length())*stretchingEventListener.k);
		
		return lForce.x*(time*pullrate);
	}
	

	public void findForce (Particle left, Particle right) {
		Vector2d rForce = ((Vector2d) right.myPosition.clone());
		rForce.sub(myPosition);
		rForce.scale((rForce.length())*stretchingEventListener.k);
		//rForce.scale(rForce.length()*.01);
		
		Vector2d lForce = ((Vector2d) left.myPosition.clone());
		lForce.sub(myPosition);
		lForce.scale((lForce.length())*stretchingEventListener.k);
		//lForce.scale(rForce.length()*.01);
		
		rForce.add(lForce);  //add forces
		
		//rforce now equals myForce, divide out tau to make it work
		myForce = rForce;
		myForce.x /= stretchingEventListener.tau;
		myForce.y /= stretchingEventListener.tau;

	}
	
	public void moveParticle (double time) {
		//assuming constant velocity
//		myPosition.x += myVelocityVector.x*time+(.5*myForce.x/myMass*time*time);
//		myPosition.y += myVelocityVector.y*time+(.5*myForce.y/myMass*time*time);

		myPosition.x += myForce.x*time;
		myPosition.y += myForce.y*time;
		
		//add the random force
		Random rand = new Random();
		double xrand = Math.sqrt(2*(stretchingEventListener.changeTime/stretchingEventListener.tau))*rand.nextGaussian();
		double yrand = Math.sqrt(2*(stretchingEventListener.changeTime/stretchingEventListener.tau))*rand.nextGaussian();
		
		myPosition.x += xrand;
		myPosition.y += yrand;
	}
	
	
}
