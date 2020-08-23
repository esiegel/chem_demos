import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;


public class particleEventListener implements GLEventListener {
	
	private GLU glu;  //helps with the perspective
	
	private PriorityQueue<Pevents> eventQueue;  //stores the ordering of the events
	
	private int numOfParticles; //needs to be square to help with the close packing
	private double sizeRatio; //ratio of space taken up, maximum of .785, or pi/4
	private ArrayList<Particle> particles;

   private boolean reshapeOccurred = true; // used in the display function to not update state after reshape
	
	private int borderDList;  //DisplayList for the border
	private int particleDList;  //DisplayList for the particle
	private int statGridDList;  //DisplayList for the statistics
	private int boltzGaussianDList;  //DisplayList for the gaussian
	private double temper; //temperature of system, is proportional to avg KE
	private double scalling;  //scaling factor of the distribution to make the peak good
	private static double statGridX = 125; //grid position X
	private static double statGridY = -50; //grid position Y
	private double lastX;  //x position of the position of the particle at the last time  step
	private double lastY;  //y position of the position of the particle at the last time step
	private int statCounter; //how many times we have performed statistics
	private int statUnit;  //unit of velocity groupings
	
	private int circleDetail; //number of iterations for the circle creation
	private double radianChange;  //change in radians per each change in circle detail
	private double gridRadiusX; //is the radius of a grid, if the particle is at maximum size (pi/4) then it touches the sides of this cell
	private double gridRadiusY; //Since the particles can fit in between the gaps, this y distance is not the same as the x.  Sqrt(3)/2
	private double particleRadius; //based on the ratio, and the num of particles and in effect the  gridratio
	private double particleMass; //mass of the particle
	private double particleVelocity; //velocity multiplier, random gaussian
	private boolean dispersed; //is velocity dispersed in the beginning or local
	private boolean isTraced;  //are we tracing the one particle
	private boolean showStats; //stats?
	private boolean instantOrCumul;  //what type of stats, instantaneous or cumulative
	private boolean showColors;  //show color gradient of velocities
	
	public static double xcoordinant = 100;  //x dimensions of the drawing plane assuming no alteration
	public static double ycoordinant;  //y dimensions of the drawing plane assuming no alteration
	
	public static double startTime;  //start time of simulation, runs till the end
	public double currentTime;  //current simulation time
	private double timeLastUpdate; //the simulation time of last update
	private double timeForNextRender; //next render time in seconds
	
	private double timeinterval;  //amount of time between each cycle
	
	private int statVelocities[];  //an array that holds groupings of velocities.
	private int statSections;  //number of sections of velocites
	
	public particleEventListener(int amountParticles, double ratio, double mass, double velocity, boolean dispersed, boolean trace,
			boolean stats, boolean instCumul, boolean colors) {
		numOfParticles = amountParticles;
		sizeRatio = ratio;
		particles = new ArrayList<Particle>();
		
		statUnit = 2;
		statVelocities = new int[(10/statUnit)*(int) velocity + 1];  //create the stat velocities at intervals of statUnit.
		statSections = statVelocities.length;
		for (int i = 0; i < statSections; i++) {
			//initialize velocities.
			statVelocities[i] = 0;
		}
		
		this.dispersed = dispersed; 
		isTraced = trace;
		showStats = stats;
		instantOrCumul = instCumul;
		statCounter = 0;
		showColors = colors;
		
		glu = new GLU();
		
		particleMass = mass;
		particleVelocity = velocity;
		
		circleDetail = 40;
		radianChange = 2*3.1415926535/circleDetail;
		
		timeinterval = .01;  //this is the timestep
		
		//this value needs to be changed if the gl perspective is changed
		gridRadiusX = 100/(Math.sqrt(numOfParticles));  //in the first row there should be 8 radius, the sidelength is 200
		gridRadiusY = Math.sqrt(3)/2 * gridRadiusX;
		particleRadius = Math.sqrt(ratio*(4*gridRadiusX*gridRadiusX)/Math.PI); //this give the radius
		
		//Need to rescale the ycooridinant such that the spheres all fit within the border.  Particles lie at a 120 degree angle.
		ycoordinant = gridRadiusY*(Math.sqrt(numOfParticles) - 1)+gridRadiusX;
		
		eventQueue = new PriorityQueue<Pevents>(numOfParticles);
		
	    if (dispersed) {
	    	initializeParticlesDispersed(numOfParticles, sizeRatio);  //create the particles and have a reference in the ArrayList of particles
	    } else {
	    	initializeParticlesLocal(numOfParticles, sizeRatio);  //create the particles and have a reference in the ArrayList of particles
	    }
	    
	    //set last positions
	    lastX = particles.get(numOfParticles/2).myPosition.x;
	    lastY = particles.get(numOfParticles/2).myPosition.y;
	    
	    calculateTemp();
	    double v = Math.sqrt(particleMass*temper);
	    //this scalling is defined by 90/MaxVel
	    scalling = 90/((1/(particleMass*temper))*v*Math.exp(-v*v/(2*particleMass*temper))); //scalling factor
	}
	
	public void init (GLAutoDrawable drawable) {
	    GL2 gl = (GL2) drawable.getGL();  
		
		gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearDepth(1.0f);                      // Depth Buffer Setup
        gl.glEnable(GL2.GL_DEPTH_TEST);							// Enables Depth Testing
        gl.glDepthFunc(GL2.GL_LEQUAL);
	    gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);  //white background
	    gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
	    
	    borderDList = gl.glGenLists(4);
	    
	    gl.glNewList(borderDList, GL2.GL_COMPILE);
	    drawBoundary(gl);
	    gl.glEndList();
	    
	    particleDList = borderDList + 1;
	    gl.glNewList(particleDList, GL2.GL_COMPILE);
	    drawCircle(circleDetail, gl);
	    gl.glEndList();
	    
	    statGridDList = borderDList + 2;
	    gl.glNewList(statGridDList, GL2.GL_COMPILE);
	    drawGrid(gl);
	    gl.glEndList();
	    
	    boltzGaussianDList = borderDList + 3;
	    gl.glNewList(boltzGaussianDList, GL2.GL_COMPILE);
	    drawGaussian(gl);
	    gl.glEndList();
	    
	    
	    System.out.println("Initialized Particles with this many particles " + particles.size());
	    timeForNextRender = 0 + timeinterval;
	    timeLastUpdate = 0;
	    
	    startTime = 0;  //start the simulation time
	    currentTime = startTime;
	   
	    findFirstCollisions();  //find the first collisions and adds them to the priority queue
	}
	
	public void display (GLAutoDrawable drawable) {
	   GL2 gl = (GL2) drawable.getGL();  
		
	    // clear colour and depth buffers
		if (!isTraced) {
			gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		}
	    gl.glLoadIdentity();
	    
	    if (showStats) {
	    	glu.gluLookAt(90 ,0, 230, 90,0,0, 0,1,0);   // position camera
	    } else {
	    	glu.gluLookAt(0 ,0, 230, 0,0,0, 0,1,0);   // position camera
	    }

       if (reshapeOccurred) {
          reshapeOccurred = false;
	    	 renderParticles(gl);
			 gl.glCallList(borderDList);
			 renderStats(gl);
          return;
       }
	    
	    if(eventQueue.isEmpty()) {
	    	//System.out.println("no more events");
	    	updateParticles(timeinterval);
	    	currentTime += timeinterval;
	    	timeForNextRender = currentTime + timeinterval;  //set next rendertime
	    	renderParticles(gl);
			//make border
			gl.glCallList(borderDList);
	    	findFirstCollisions();
	    	return;
	    } else {
	    	
	    

	    //If the eventQueue is not empty, then we have two options
	    //We check the next event, and process it by update the particles/collisions.
	    //We only render the scene after the currenttime passes the timefornextrender.
	    //This creates a smooth rendering, no jitters
	    while (eventQueue.peek().time < timeForNextRender) {
	    	Pevents temp = eventQueue.poll();
	    	if (!temp.shouldProcess) {
	    		//don't process this
	    		//make sure we don't continue this while loop on an empty list
	    		if (eventQueue.isEmpty()) {
	    			break;
	    		}
	    	} else {
	    		//process the event
	    		updateParticles(temp.time - currentTime);  //bring all particles to point of this collision
	    		
		    	//now update velocities of colliding particles
	    		temp.particleOne.updateCollision(temp.particleTwo);
	    		//System.out.println(eventQueue.size() + " " + temp.time);
	    		
	    		temp.particleOne.timesSinceCollision = 0;
	    		temp.particleTwo.timesSinceCollision = 0;
	    		
	    		//update simulation clock
	    		currentTime = temp.time;
	    		
	    		//REMOVE OTHER COLLISIONS INVOLVING THESE PARTICLES
	    		//now flag all collisions that these particles were included in to not process
	    		Iterator<Pevents> temp1 = temp.particleOne.myEvents.iterator();
	    		Iterator<Pevents> temp2 = temp.particleTwo.myEvents.iterator();
	    		
	    		//temp variable (events and particles)
	    		ArrayList<Particle> needUpdate = new ArrayList<Particle>();  //need to update these particles
	    		needUpdate.add(temp.particleOne);
	    		needUpdate.add(temp.particleTwo);
	    		Pevents e1 = null;
	    		Pevents e2 = null;
	    		
	    		//now set affected events to false, clear the particles event lists and find new collisions
	    		while(temp1.hasNext()) {
	    			e1 = temp1.next();
	    			//check that the other particle isn't particle two
	    			if ((e1.particleOne == temp.particleTwo) || (e1.particleTwo == temp.particleTwo)) {
	    				//do nothing, don't process this one
	    			} else if (e1.particleOne == temp.particleOne) {
	    				if (!needUpdate.contains(e1.particleTwo)) {
	    					needUpdate.add(e1.particleTwo);
	    				}
	    				//e1.particleTwo.myEvents.clear();
	    				//e1.particleTwo.myEvents.remove(temp);
	    			} else {
	    				if (!needUpdate.contains(e1.particleOne)) {
	    					needUpdate.add(e1.particleOne);
	    				}
	    				//e1.particleOne.myEvents.clear();
	    				//e1.particleOne.myEvents.remove(temp);
	    			}
	    			e1.shouldProcess = false;
	    		}

	    		while(temp2.hasNext()) {
	    			e2 = temp2.next();
	    			//check that the other particle isn't particle two
	    			if ((e2.particleOne == temp.particleOne) || (e2.particleTwo == temp.particleOne)) {
	    				//do nothing, don't process this one
	    			} else if (e2.particleTwo == temp.particleTwo) {
	    				if (!needUpdate.contains(e2.particleOne)) {
	    					needUpdate.add(e2.particleOne);
	    				}
	    				//e2.particleOne.myEvents.clear();
	    				e2.particleOne.myEvents.remove(temp);
	    			} else {
	    				if (!needUpdate.contains(e2.particleTwo)) {
	    					needUpdate.add(e2.particleTwo);
	    				}
	    				//e2.particleTwo.myEvents.clear();
	    				e2.particleTwo.myEvents.remove(temp);
	    			}
	    			e2.shouldProcess = false;
	    		}

	    		//clear particles events
	    		temp.particleOne.myEvents.clear();
	    		temp.particleTwo.myEvents.clear();

	    		//find new collisions for those particles that were changed
	    		Iterator<Particle> particleIterI = needUpdate.iterator();
	    		while (particleIterI.hasNext()) {
	    			getMyCollisions(particleIterI.next());
	    			
	    		}

	    		//make sure we don't continue this while loop on an empty list
	    		if (eventQueue.isEmpty()) {
	    			break;
	    		}
	    	}
	    }

	    
	    //Now update particles to timeforNextRender and Render.
	    updateParticles(timeForNextRender - currentTime);
	    currentTime = timeForNextRender;
	    timeForNextRender += timeinterval;
	    renderParticles(gl);
	   
		//make border
		gl.glCallList(borderDList);
	    
	    //draw stat stuff
		if (!isTraced && showStats) {
			renderStats(gl);
		}
		if (showStats) {
			gl.glCallList(statGridDList);
			gl.glCallList(boltzGaussianDList);
		}
	}
}
	

	public void reshape (GLAutoDrawable drawable, int x, int y, int width, int height) {
      reshapeOccurred = true;

	   GL2 gl = (GL2) drawable.getGL();  
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

	    //avoid division by 0
	    if (height == 0) {
	    	height = 1;
	    }

	    gl.glViewport(x, y, width, height);  // size of drawing area 

	    gl.glMatrixMode(GL2.GL_PROJECTION);
	    gl.glLoadIdentity();
	    glu.gluPerspective(45.0, (float)width/(float)height, 200, 231);  //perspective

	    gl.glMatrixMode(GL2.GL_MODELVIEW);
	    gl.glLoadIdentity();
	}
	
	public void displayChanged (GLAutoDrawable drawable, boolean arg1, boolean arg2) {
		//no need to implement
	}
	
	private void initializeParticlesDispersed(int numOfParticles, double ratio) {
		//the grid is a square -100 - 100,
		//to keep the close packing scheme working, we'll have to have the particles go out of bounds by half a radius.
		boolean shouldInterleave = false; // alternates between starting at the edge of interleaved between particles.
		
		for (double yPos = ycoordinant - gridRadiusX; yPos >= -ycoordinant; yPos -= 2*gridRadiusY) {
			Random randomizer = new Random();
			
			//loop for interleaved sections
			if (shouldInterleave) {
				for (double xPos = -xcoordinant + 2*gridRadiusX ; xPos < xcoordinant + gridRadiusX; xPos += 2*gridRadiusX) {
					Particle temp = new Particle(xPos, yPos, particleVelocity*randomizer.nextGaussian(), particleRadius, particleMass);
					temp.timesSinceCollision = -10000000;
					particles.add(temp);
				}
				shouldInterleave = false;
			} else {
				//loop for non interleaved sections
				for (double xPos = -xcoordinant + gridRadiusX; xPos < xcoordinant; xPos += 2*gridRadiusX) {
					Particle temp = new Particle(xPos, yPos, particleVelocity*randomizer.nextGaussian(), particleRadius, particleMass);
					temp.timesSinceCollision = -10000000;
					particles.add(temp);
				}
				shouldInterleave = true;
			}
		}
	}
	
	private void initializeParticlesLocal(int numOfParticles, double ratio) {
		//the grid is a square -100 - 100,
		//to keep the close packing scheme working, we'll have to have the particles go out of bounds by half a radius.
		boolean shouldInterleave = false; // alternates between starting at the edge of interleaved between particles.
		
		for (double yPos = ycoordinant - gridRadiusX; yPos >= -ycoordinant; yPos -= 2*gridRadiusY) {
			Random randomizer = new Random();
			
			//loop for interleaved sections
			if (shouldInterleave) {
				for (double xPos = -xcoordinant + 2*gridRadiusX ; xPos < xcoordinant + gridRadiusX; xPos += 2*gridRadiusX) {
					Particle temp = new Particle(xPos, yPos, 0, particleRadius, particleMass);
					temp.timesSinceCollision = -10000000;
					particles.add(temp);
				}
				shouldInterleave = false;
			} else {
				//loop for non interleaved sections
				for (double xPos = -xcoordinant + gridRadiusX; xPos < xcoordinant; xPos += 2*gridRadiusX) {
					Particle temp = new Particle(xPos, yPos, 0, particleRadius, particleMass);
					temp.timesSinceCollision = -10000000;
					particles.add(temp);
				}
				shouldInterleave = true;
			}
		}
		Random random = new Random();
		double tempv = 2*Math.PI*Math.random();
		particles.get(numOfParticles/2).myVelocityVector.x = particleVelocity*Math.cos(tempv);
		particles.get(numOfParticles/2).myVelocityVector.y = particleVelocity*Math.sin(tempv);
	}
	
	//finds the collisions of this particle
	private void getMyCollisions(Particle one) {
		Particle temp = null;
		Pevents toAdd = null;
		Pevents tempEvent = null;
		for (int i = 0; i < numOfParticles; i++) {
			temp = particles.get(i);
			if (temp == one) {
				// don't test this particle
			} else {
				tempEvent = isCollision(one, temp);
				if (tempEvent != null) {
					if (toAdd == null) {
						toAdd = tempEvent;
					} else if (tempEvent.time < toAdd.time) {
						toAdd = tempEvent;
					}
				}
			}
		}
		
		if (toAdd != null) {
			eventQueue.add(toAdd);
			toAdd.particleOne.myEvents.add(toAdd);
			toAdd.particleTwo.myEvents.add(toAdd);
		}
	}

	// these two particles just had a collision, see when there next times are.
	private void checkForCollsions(Particle one, Particle two) {
		Pevents onesEvent = null;
		Pevents twosEvent = null;
		
		Iterator<Particle> particleIter = particles.iterator();
		while (particleIter.hasNext()) {
			Particle temp = particleIter.next();
			Pevents temp1 = null;
			Pevents temp2 = null;
			
			if (temp == one) {
				//do nothing, we don't need to check two - one, we will do one - two later
			} else if (temp == two) {
				temp1 = isCollision(one, temp);
				if (temp1 == null) {
					//don't add
				} else if (onesEvent == null) {
					onesEvent = temp1;
				} else if (temp1.time < onesEvent.time) {
					onesEvent = temp1;
				}
			} else {
				temp1 = isCollision(one, temp);
				temp2 = isCollision(two, temp);
				
				if (temp1 != null) {
					if (onesEvent == null) {
						onesEvent = temp1;
					} else if (temp1.time < onesEvent.time) {
						onesEvent = temp1;
					}
				} 
				if (temp2 != null) {
					if (twosEvent == null) {
						twosEvent = temp2;
					} else if (temp2.time < twosEvent.time) {
						twosEvent = temp2;
					}
				} 
			}
		}
		
		if (onesEvent != null) {
			onesEvent.particleOne.myEvents.add(onesEvent);
			onesEvent.particleTwo.myEvents.add(onesEvent);
			eventQueue.add(onesEvent);
		}
		if (twosEvent != null) {
			twosEvent.particleOne.myEvents.add(twosEvent);
			twosEvent.particleTwo.myEvents.add(twosEvent);
			eventQueue.add(twosEvent);
		}
		
	}

	private void updateParticles(double timeChange) {
		Iterator<Particle> particleIter = particles.iterator();
		while(particleIter.hasNext()) {
			particleIter.next().updateMe(timeChange, xcoordinant, ycoordinant);
		}
	}
	
	private void findFirstCollisions() {
		//finds the first collisions and adds them to the EventsQueue
		for (int i = 0; i < numOfParticles; i++) {
			Pevents bestSoFar = null;  //this is the best event so far add to eventqueue if not null
			for (int j = (i+1); j<numOfParticles; j++) {
				Pevents temp = isCollision(particles.get(i), particles.get(j));
				if (temp == null) {
					//do nothing, its just null
				} else if (bestSoFar == null){
					bestSoFar = temp;
				} else if (temp.time < bestSoFar.time) {
					bestSoFar = temp;
				}
			}
			
			if (bestSoFar == null) {
				//don't add this
			} else {
				bestSoFar.particleOne.myEvents.add(bestSoFar);
				bestSoFar.particleTwo.myEvents.add(bestSoFar);
				eventQueue.add(bestSoFar);
			}
		}
	}
	
	private Pevents isCollision(Particle one, Particle two) {
		//first find deltaX, from particle one to "ghost" of particle two
		//x = x' - a_x*nint(x'/a_x)
		double changeX = (two.myPosition.x-one.myPosition.x) -
			2*xcoordinant*Math.rint((two.myPosition.x - one.myPosition.x)/(2*xcoordinant));
		
		//find deltaY
		double changeY = (two.myPosition.y-one.myPosition.y) -
			2*ycoordinant*Math.rint((two.myPosition.y - one.myPosition.y)/(2*ycoordinant));
		
		//relative velocity
		double velX = two.myVelocityVector.x - one.myVelocityVector.x;
		double velY = two.myVelocityVector.y - one.myVelocityVector.y;
		
		double distanceSquared = (one.myRadius+two.myRadius) * (one.myRadius+two.myRadius);
		
		double top = Math.sqrt((changeX*velX + changeY*velY)*(changeX*velX + changeY*velY) - 
				((velX*velX+velY*velY)*(changeX*changeX +changeY*changeY-distanceSquared)));
		
		double ans1 = (-(changeX*velX+changeY*velY) - top)/(velX*velX+velY*velY);
		
		if (ans1<0) {
			return null;
		}
		
		if (ans1> 0) {
			return new Pevents(ans1+ currentTime, one, two);
		} else {
			return null;
		}
	}

	private void renderParticles(GL _gl) {
		GL2 gl = (GL2) _gl;
		
		// displays all of the particles by translating the glmatrix, and pushing and popping.  It is a display list.
		for (int i = 0; i < particles.size(); i++) {
			Particle temp = particles.get(i);
			temp.timesSinceCollision += 1;
			
			statVelocities[(int) temp.myVelocityVector.length()/statUnit]++;
			
			if ((eventQueue.size() < 50) && (temp.timesSinceCollision > 5)) {
				getMyCollisions(temp);
			}

			
			if (!isTraced || (i== numOfParticles/2)) {
			 	
			 	
			 	
			 	if (!isTraced) {
					gl.glPushMatrix();
					gl.glTranslated(temp.myPosition.x, temp.myPosition.y, 0);
			 		
			 		//call particle, with specific color
					if (showColors) {
						if (dispersed) {
							gl.glColor3d(rConvert(temp.myVelocityVector.length(), 0, 2*particleVelocity),
									gConvert(temp.myVelocityVector.length(), 0, 2*particleVelocity), 
									bConvert(temp.myVelocityVector.length(), 0, 2*particleVelocity));
						} else {
							gl.glColor3d(rConvert(temp.myVelocityVector.length(), 0, particleVelocity/4),
									gConvert(temp.myVelocityVector.length(), 0, particleVelocity/4), 
									bConvert(temp.myVelocityVector.length(), 0, particleVelocity/4));
						}
					} else {
						gl.glColor3d(0,.8,0);
					}
			 		gl.glCallList(particleDList);
			 		
				 	gl.glColor3f(0.0f, 0.0f, 0.0f);
				 	gl.glBegin(GL2.GL_LINES);
					gl.glVertex2d(0, 0);
					gl.glVertex2d(temp.myVelocityVector.x/8, temp.myVelocityVector.y/8);
					gl.glEnd();	
					
			    	gl.glPopMatrix();
			 	} else {				 	
				 	//a check to make sure the lines don't jump through the walls
				 	if ( ((Math.abs(lastX - temp.myPosition.x)) > xcoordinant) || ((Math.abs(lastY - temp.myPosition.y)) > ycoordinant) ) {
				 		//if the point jump was too large, it must mean that we jumped across the
				 		//periodic boundaries.  We don't want a huge line across the screen so don't display this line.
				 		lastX = temp.myPosition.x;
				 		lastY = temp.myPosition.y;
				 		return;
				 	}
				 	
				 	gl.glBegin(GL2.GL_LINES);
				 	gl.glVertex2d(lastX, lastY);
					gl.glVertex2d(temp.myPosition.x, temp.myPosition.y);
					gl.glEnd();	
					lastX = temp.myPosition.x;
					lastY = temp.myPosition.y;
			 	}
			}
		}
			
	}
	
	private void drawBoundary(GL2 gl) {
		gl.glLineWidth(1.0f);
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		
		//draw outside black zone
		gl.glBegin(GL2.GL_QUADS);
			//bottom
			gl.glVertex2d(-xcoordinant - 2, -ycoordinant);
			gl.glVertex2d(-xcoordinant - 2, -ycoordinant - 100);
			gl.glVertex2d(xcoordinant + 2, -ycoordinant - 100);
			gl.glVertex2d(xcoordinant + 2, -ycoordinant);
			
			//top
			gl.glVertex2d(xcoordinant + 2, ycoordinant);
			gl.glVertex2d(xcoordinant + 2, ycoordinant + 100);
			gl.glVertex2d(-xcoordinant - 2, ycoordinant + 100);
			gl.glVertex2d(-xcoordinant - 2, ycoordinant);
			
			//left
			gl.glVertex2d(-xcoordinant - 2, -ycoordinant);
			gl.glVertex2d(-xcoordinant, -ycoordinant);
			gl.glVertex2d(-xcoordinant, ycoordinant);
			gl.glVertex2d(-xcoordinant - 2, ycoordinant);
			
			//right
			gl.glVertex2d(xcoordinant + 2, -ycoordinant);
			gl.glVertex2d(xcoordinant, -ycoordinant);
			gl.glVertex2d(xcoordinant, ycoordinant);
			gl.glVertex2d(xcoordinant + 2, ycoordinant);
		gl.glEnd();
	}

	private void drawCircle (int detail, GL2 gl) {
		//gl.glColor3d(Math.random(), Math.random(), Math.random());
		gl.glBegin(GL2.GL_POLYGON);
			for (int i = 0; i< detail; i++) {
				double x = Math.cos(i*radianChange);
				double y = Math.sin(i*radianChange);
				gl.glVertex2d(particleRadius*x, particleRadius*y);
			}
		gl.glEnd();
	}
	
	private void drawGrid(GL2 gl) {
		gl.glColor3d(0, 0, 0);
		gl.glLineWidth(2);
		gl.glBegin(GL2.GL_LINE_STRIP);
			gl.glVertex2d(statGridX, statGridY+1000);
			gl.glVertex2d(statGridX, statGridY);
			gl.glVertex2d(statGridX + 1000, statGridY);			
		gl.glEnd();
	}
	
	private void drawGaussian(GL2 gl) {
		gl.glColor3d(0, 0, 0);
		gl.glLineWidth(1);
		gl.glBegin(GL2.GL_LINE_STRIP);
			for (int v = 0; v < 10*particleVelocity; v++) {
				//equation on top would be for 3d boltzman
				//gl.glVertex2d(125+v, 100*(4*Math.PI*(Math.pow(1/(2*Math.PI*temp), 3/2))*v*v*Math.exp(-v*v/(2*temp))));
				gl.glVertex2d(statGridX+v, statGridY+scalling*((1/(particleMass*temper))*v*Math.exp(-v*v/(2*particleMass*temper))));
			}			
		gl.glEnd();
	}
	
	private void calculateTemp() {
		temper = 0;
		for (int i = 0; i < numOfParticles; i++) {
			temper += particles.get(i).myVelocityVector.length()*particles.get(i).myVelocityVector.length()*.5*
			particles.get(i).myMass;
		}
		
		temper /= numOfParticles;  //now we have the temp.
	}
	
	private void renderStats(GL2 gl) {
		gl.glColor3d(1, 0, 0);
		gl.glBegin(GL2.GL_QUADS);
		statCounter++;
		if (instantOrCumul) {
			for (int i = 0; i < statSections; i++) {
				gl.glVertex2d(statGridX + statUnit*i, statGridY);
				gl.glVertex2d(statGridX + statUnit*i, statGridY + scalling*statVelocities[i]/(numOfParticles*statCounter*statUnit));
				gl.glVertex2d(statGridX + statUnit*i + statUnit, statGridY + scalling*statVelocities[i]/(numOfParticles*statCounter*statUnit));
				gl.glVertex2d(statGridX + statUnit*i + statUnit, statGridY);	
				//statVelocities[i] = 0;
			}	
		} else {
			for (int i = 0; i < statSections; i++) {
				gl.glVertex2d(statGridX + statUnit*i, statGridY);
				gl.glVertex2d(statGridX + statUnit*i, statGridY + scalling*statVelocities[i]/(numOfParticles*statUnit));
				gl.glVertex2d(statGridX + statUnit*i + statUnit, statGridY + scalling*statVelocities[i]/(numOfParticles*statUnit));
				gl.glVertex2d(statGridX + statUnit*i + statUnit, statGridY);	
				statVelocities[i] = 0;
			}	
		}
		
		gl.glEnd();
	}
	
	private double rConvert(double velocity, double min, double max) {
		  velocity = 380 + (680-380)*(velocity-min)/(max-min);
		  if ( velocity < 440 ) return -(velocity-440)/(440-380);
		  if ( velocity < 510 ) return 0.0;
		  if ( velocity < 580 ) return (velocity-510)/(580-510);
		  return 1.0;
	}
	
	private double gConvert(double velocity, double min, double max) {
		  velocity = 380 + (680-380)*(velocity-min)/(max-min);
		  if ( velocity < 440 ) return 0.0;
		  if ( velocity < 490 ) return (velocity-440)/(490-440);
		  if ( velocity < 580 ) return 1.0;
		  if ( velocity < 645 ) return -(velocity-645)/(645-580);
		  return 0.0;
	}
	
	private double bConvert(double velocity, double min, double max) {
		  velocity = 380 + (680-380)*(velocity-min)/(max-min);
		  if ( velocity < 490 ) return 1.0;
		  if ( velocity < 510 ) return -(velocity-510)/(510-490);
		  return 0.0;
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}
	
}
