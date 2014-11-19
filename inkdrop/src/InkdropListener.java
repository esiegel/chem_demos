/*InkdropListener:
 * 
 * Implements glEventListener and is attached to the GLCanvas created in the InkdropPanel.
 * 
 * It has four important methods
 * init() - creates the border, creates the latticeGrid, initializes the displayLists
 * 
 * display() - displays all of the particles and there corresponding transparency.
 * 
 * reshape() - changes the size of the canvas, based on the resize of the GLCanvas that it is attached to
 * 
 * displayChanged() - not implemented but used if new monitor input is changed
 * 
 * */

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;


public class InkdropListener implements GLEventListener {
	
	long[][] particlePositions;  //Array that holds the amount of particles
	int sideLength;  //length of each lattice side
	long numOfWalkers; // amount of walkers
	private long numOfSteps;  //amount of frames that will be rendered until the demo ends
	private boolean tracePath;  //trace the path of the walkers, good when few walkers
	private boolean[][] pathBooleans;  //when a position has been reached this holds its state
	private boolean showLattice;  //show the individual lattice separators;
	
	private long frameCounter;  //starts at zero and causes the program to stop when it reaches numOfSteps
	
	private int borderDList;  //DisplayList for the border
	private int latticesDList; //DisplayList for the lattices if they are to be drawn
	
	private GLU glu;  //helps with perspective
	
	long[] movedDown;  //prevents overcounting when things are moved down
	
	public InkdropListener (int latticeSide, long walkers, long steps, 
			boolean trace, boolean lattice) {
		
		sideLength = latticeSide;
		particlePositions = new long[latticeSide][latticeSide];
		numOfWalkers = walkers;
		particlePositions[latticeSide - 1][(latticeSide/2) - 1] = walkers;  //all walkers start at this position
		//particlePositions[(latticeSide - 1)/2][(latticeSide/2) - 1] = walkers;
		numOfSteps = steps;
		tracePath = trace;
		
		if (tracePath) {
			pathBooleans = new boolean[latticeSide][latticeSide];
		}
		
		showLattice = lattice;
		frameCounter = 0;
		glu = new GLU();
		
		movedDown = new long[sideLength];  //used in the walkABit method to prevent overcounting when going down and to eliminate the 
		//constant garbage collection that was slowing down the program with large arrays.	
	}

	public void init(GLAutoDrawable drawable) {
	    GL2 gl = (GL2) drawable.getGL();    /* don't make this gl a global! */

	    gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);  //white background
	    
	    gl.setSwapInterval(0);

	    // create a display list for drawing the border
	    //the 2 signifies that we have room for 2 display lists
	    borderDList = gl.glGenLists(2);
	    
	    
	    gl.glNewList(borderDList, GL2.GL_COMPILE);
	    drawBorder(gl);
	    gl.glEndList();
	    
	    //draw the lattices and keep it in the second DList location
	    if (showLattice) {
	    	latticesDList = borderDList + 1;  //in memory the latticeDList should be one more
	    	gl.glNewList(latticesDList, GL2.GL_COMPILE);
	    	drawLattices(gl);
	    	gl.glEndList();
	    }
	    
	    //enable blending for transparency
	    gl.glEnable(GL.GL_BLEND);
	    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
	    
	}
	
	public void display(GLAutoDrawable drawable) {		
		GL2 gl = (GL2) drawable.getGL();
		
	    // clear colour and depth buffers
	    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	    gl.glLoadIdentity();
		
	    glu.gluLookAt(0,0, 25, 0,0,0, 0,1,0);   // position camera
	    
	    gl.glCallList(borderDList);
	    
	    if (showLattice) {
	    	gl.glCallList(latticesDList);
	    }

	    //draws all of the ink squares at the appropriate intensity
	    drawInkSquares(gl);

	    //performs a random walk for each particle, this is the update
	    walkABit();


	    //add 1 to the framecounter
	    frameCounter++;
	    
	    if (frameCounter > numOfSteps) {
	    	InkdropPanel.animator.stop();
	    }
	}

	public void displayChanged(GLAutoDrawable drawable, boolean arg1, boolean arg2) {
		// Not implemented and un-needed
		
	}


	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

		GL2 gl = (GL2) drawable.getGL();

	    //avoid division by 0
	    if (height == 0) {
	    	height = 1;
	    }

	    gl.glViewport(x, y, width, height);  // size of drawing area 

	    gl.glMatrixMode(GL2.GL_PROJECTION);
	    gl.glLoadIdentity();
	    glu.gluPerspective(45.0, (float)width/(float)height, 1, 100);  //perspective

	    gl.glMatrixMode(GL2.GL_MODELVIEW);
	    gl.glLoadIdentity();
	}
	
	private void drawBorder(GL2 gl) {
		gl.glLineWidth(3.0f);
		gl.glColor3f(.4f, 1.0f, 0.0f);
		gl.glBegin(GL.GL_LINE_STRIP);
			gl.glVertex3f(-11.0f, 11.0f, 0.0f);
			gl.glVertex3f(-10.0f, 10.0f, 0.0f);
			gl.glVertex3f(-10.0f, -10.0f, 0.0f);
			gl.glVertex3f(10.0f, -10.0f, 0.0f);
			gl.glVertex3f(10.0f, 10.0f, 0.0f);
			gl.glVertex3f(11.0f, 11.0f, 0.0f);
		gl.glEnd();
	}
	
	private void drawLattices (GL2 gl) {
		float spacing = 20f/((float) sideLength);  //line separation on the lattiice grid
		
		gl.glLineWidth(1.0f);  //smaller width
		gl.glColor3f(.4f, 1.0f, 0.0f); //green lines
		
		//create vertical lines
		for (float horizPos = -10.0f + spacing; horizPos < 10f; horizPos += spacing) {
			gl.glBegin(GL.GL_LINES);
				gl.glVertex3f(horizPos, -10f, 0.0f);
				gl.glVertex3f(horizPos, 10f, 0.0f);
			gl.glEnd();
		}
		
		//create horizontal lines
		for (float vertPos = -10.0f + spacing; vertPos < 10.0f; vertPos += spacing) {
			gl.glBegin(GL.GL_LINES);
				gl.glVertex3f(-10.0f, vertPos, 0.0f);
				gl.glVertex3f(10.0f, vertPos, 0.0f);
			gl.glEnd();
		}
	}
	
	private void drawInkSquares (GL2 gl) {
		float spacing = (20.0f)/((float) sideLength);  //square separation on the lattiice grid
		long particles = 0;
		float squareIntensity = 0f;
		
		gl.glBegin(GL2.GL_QUADS);
		
		for (float lowerLeftRow = -10.0f, row = 0; row < sideLength; lowerLeftRow += spacing, row++) {
			for (float lowerLeftCol = -10.0f, col = 0; col < sideLength; lowerLeftCol += spacing, col++) {
				particles = particlePositions[(int) row][(int) col];
				
				if (particles == 0) {
					if (tracePath && pathBooleans[(int) row][(int) col]) {
						squareIntensity = 1.0f;
						gl.glColor4f(0.025f, 0.325f, 0.95f, squareIntensity);  //blue
						//gl.glColor4f(0.0f, 0.0f, 0.0f, squareIntensity);  //black
						gl.glVertex3f(lowerLeftCol, lowerLeftRow, 0.0f);
						gl.glVertex3f(lowerLeftCol, lowerLeftRow + spacing, 0.0f);
						gl.glVertex3f(lowerLeftCol + spacing, lowerLeftRow + spacing, 0.0f);
						gl.glVertex3f(lowerLeftCol + spacing, lowerLeftRow, 0.0f);
					}
				} else {
					//shading
					if (particles == 1) {
						squareIntensity = .4f;
					} else if (particles > 6) {
						squareIntensity = 1.0f;
					} else if (particles == 2) {
						squareIntensity = .5f;
					} else if (particles == 3) {
						squareIntensity = .6f;
					} else if (particles == 4) {
						squareIntensity = .7f;
					} else if (particles == 5) {
						squareIntensity = .8f;
					} else if (particles == 6){
						squareIntensity = .9f;
					}

					if (tracePath && pathBooleans[(int) row][(int) col]) {
						squareIntensity = 1.0f;
					}

					
						gl.glColor4f(0.025f, 0.325f, 0.95f, squareIntensity); //blue
						//gl.glColor4f(0.0f, 0.0f, 0.0f, squareIntensity); //black
						gl.glVertex3f(lowerLeftCol, lowerLeftRow, 0.0f);
						gl.glVertex3f(lowerLeftCol, lowerLeftRow + spacing, 0.0f);
						gl.glVertex3f(lowerLeftCol + spacing, lowerLeftRow + spacing, 0.0f);
						gl.glVertex3f(lowerLeftCol + spacing, lowerLeftRow, 0.0f);
				}
			}
		}
		
		gl.glEnd();
	}
	
//	private void walkABit () {
//		//this is here as to not overcount already moved particles
//		//****lower left corner is origin, as in opengl****
//		long[][] tempParticleMoves = new long[sideLength][sideLength];  
//		long particlesInPos = 0;  
//		
//		for(int row = 0; row < sideLength; row++) {
//			for(int col=0; col < sideLength; col++) {
//				//random [0,.25) left
//				//random [.25, .50) right
//				//random [.50, .75) down
//				//random [.75, 1] up
//				//A particle can not go past the borders, and will repeat until it moves.
//				
//				particlesInPos = particlePositions[row][col];		
//				
//				//If the trace is on it sets this position to true
//				if (tracePath && (particlesInPos != 0)) {
//					pathBooleans[row][col] = true;  //signal that we've been here
//				}
//				
//				while(particlesInPos > 0) {
//					double random = Math.random();
//					if ((random < .25) && (col !=0)) {
//						//move left
//						tempParticleMoves[row][col - 1] += 1;
//						
//					} else if ((random < .5) && (col != (sideLength - 1))) {
//						//move right
//						tempParticleMoves[row][col + 1] += 1;
//
//					} else if ((random < .75) && (row != 0)) {
//						//move down
//						tempParticleMoves[row - 1][col] += 1;
//
//					} else if ( row != (sideLength - 1) ) { //it must be .75 - 1.0
//						//move up
//						tempParticleMoves[row + 1][col] += 1;
//						
//					} else {
//						tempParticleMoves[row][col] += 1;
//					}
//					particlesInPos--;
//				}
//			}
//		}
//		//switch the arrays of positions, which corresponds to the newly moved particles;
//		particlePositions = tempParticleMoves;
//	}

	private void walkABit () {
		//this is here as to not overcount already moved particles
		//****lower left corner is origin, as in opengl****
		 
		long particlesInPos = 0;  //amount of particles to count
		long particlesMovedRight = 0;  //prevents overcounting
		
		
		for(int row = 0; row < sideLength; row++) {
			for(int col=0; col < sideLength; col++) {
				//random [0,.25) left
				//random [.25, .50) right
				//random [.50, .75) down
				//random [.75, 1] up
				//A particle can not go past the borders, and will repeat until it moves.
				
				particlesInPos = particlePositions[row][col] - movedDown[col] - particlesMovedRight;
				
				//reset particlesMovedRight and movedDown
				particlesMovedRight = 0;
				movedDown[col] = 0;
				
				//If the trace is on it sets this position to true
				if (tracePath && (particlesInPos != 0)) {
					pathBooleans[row][col] = true;  //signal that we've been here
				}
				
				while(particlesInPos > 0) {					
					
					double random = Math.random();
					if (random < .25) {
						if (col !=0) {
							//move left
							particlePositions[row][col - 1] += 1;
							particlePositions[row][col]--;
						}						
					} else if (random < .5) {
						if (col != (sideLength - 1)) {
							//move right
							particlePositions[row][col + 1] += 1;
							particlePositions[row][col]--;
							particlesMovedRight++;
						}
					} else if (random < .75) {
						if (row != 0) {
							//move down
							particlePositions[row - 1][col] += 1;
							particlePositions[row][col]--;
							movedDown[col]++;
						}
					} else if (row != (sideLength - 1)) { //it must be .75 - 1.0
						//move up
						particlePositions[row + 1][col] += 1;
						particlePositions[row][col]--;
					}
					particlesInPos--;
				}
			}
		}
		//reset the array without reinstantiating a new array, helps with gc and time.
		for (int i = 0; i < sideLength; i++) {
			movedDown[i] = 0;
		}
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}
	
}
