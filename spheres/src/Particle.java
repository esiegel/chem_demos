/*Particle
 *
 *Representation of a particle:
 *
 *Holds a position, a velocity vector, and methods to be called to draw this particle
 */

import java.util.ArrayList;

public class Particle {

	public Vector2d myVelocityVector;  //velocity vector
	public Vector2d myPosition; //position vector
	public Vector2d myForce; //acceleration vector
	public double myRadius;
	public double myMass;
	public int timesSinceCollision; //times since acted in a collision
	
	public ArrayList<Pevents> myEvents;  //collsion events where this particle is included

	
	//creates a particle with a randomly chosen velocity direction
	Particle (double x, double y, double velocity, double radius, double mass) {
		//Create a random Vector with that magnitude
		Double randomAngle = 2*Math.PI*Math.random();
		
		Double xVec = velocity*Math.cos(randomAngle);
		Double yVec = velocity*Math.sin(randomAngle);
		
		myVelocityVector = new Vector2d(xVec, yVec);
		myPosition = new Vector2d(x,y);
		
		myRadius = radius;
		myMass = mass;		
		myForce = new Vector2d(0,-50);
		
		myEvents = new ArrayList<Pevents>();
	}
	
	//updates the position based only on velocities
	//time in sec
	public void updateMe (double timeInterval, double xMod, double yMod) {		
		myPosition.x = myPosition.x + (myVelocityVector.x * timeInterval);
		myPosition.y = myPosition.y + (myVelocityVector.y * timeInterval);
		
		//including acceleration from force
		//myPosition.x += myVelocityVector.x*timeInterval - .5*(myForce.x/myMass)*timeInterval*timeInterval;
		//myPosition.y += myVelocityVector.y*timeInterval - .5*(myForce.y/myMass)*timeInterval*timeInterval;
		
		//myVelocityVector.x += myForce.x/myMass*timeInterval; 
		//myVelocityVector.y += myForce.y/myMass*timeInterval; 
		
		//mod operation not working correctly, so use if statements
		if (myPosition.x < -xMod) {
			myPosition.x += 2*xMod;
		} else if (myPosition.x > xMod) {
			myPosition.x -= 2*xMod;
		}
		
		if (myPosition.y < -yMod) {
			myPosition.y += 2*yMod;
		} else if (myPosition.y > yMod) {
			myPosition.y -= 2*yMod;
		}

	}
	
	
	//if there is a collision both partices velocities are fixed
	//we first updated to this time period, so only the velocities need to be repaired
	public void updateCollision (Particle particle) {
		Vector2d unitNormal = new Vector2d(this.myPosition.x - particle.myPosition.x, 
				this.myPosition.y - particle.myPosition.y );
		
		unitNormal.x -=
		2*particleEventListener.xcoordinant*Math.rint((this.myPosition.x - particle.myPosition.x)/(2*particleEventListener.xcoordinant));
		
		unitNormal.y -= 
		2*particleEventListener.ycoordinant*Math.rint((this.myPosition.y - particle.myPosition.y)/(2*particleEventListener.ycoordinant));
		
		//make the normal a unit vector
		unitNormal.normalize();
		
		//make the unittangent vector to the collision
		Vector2d unitTangent = new Vector2d(-unitNormal.y, unitNormal.x);
		
		//projections of this particle on the normal and then tangent
		double myNormalProjection = unitNormal.dot(myVelocityVector);
		double myTangentProjection = unitTangent.dot(myVelocityVector);		
		
		//projections of the other particle on the normal and then on the tangent
		double otherNormalProjection = unitNormal.dot(particle.myVelocityVector);
		double otherTangentProjection = unitTangent.dot(particle.myVelocityVector);
		
		//The tangential velocities stay the same, but we must now update the component velocites in the normal direction.
		double myNewNormal = (myNormalProjection*(myMass - particle.myMass) + 2*particle.myMass*otherNormalProjection)/(myMass + particle.myMass);
		double othernewNormal = (otherNormalProjection*(particle.myMass - myMass)+2*myMass*myNormalProjection)/(myMass+particle.myMass);
		
		//Final vectors
		myVelocityVector.x = (myNewNormal*unitNormal.x)+(myTangentProjection*unitTangent.x);
		myVelocityVector.y = (myNewNormal*unitNormal.y)+(myTangentProjection*unitTangent.y);
			
		particle.myVelocityVector.x = (othernewNormal*unitNormal.x)+(otherTangentProjection*unitTangent.x);
		particle.myVelocityVector.y = (othernewNormal*unitNormal.y)+(otherTangentProjection*unitTangent.y);
	}
	
}
