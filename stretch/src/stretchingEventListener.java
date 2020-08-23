import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;


public class stretchingEventListener implements GLEventListener {
	
	private GLU glu; //for perspective needs
	
	private ArrayList<Particle> particles;
	private int numOfParticles;
	
	private int circleDetail; //number of iterations for the circle creation
	private double radianChange;  //change in radians per each change in circle detail
	
	private int smallRadius = 2; //for the small particles
	private int largeRadius = 6; //for the end particles  should be fourtimes as large
	
	private int borderDList; //display list for the border
	private int largeParticleDList; //display list for the large particle
	private int smallParticleDList; //display list for the small particle
	
	public static double KbT = 1;  //K_b*T
	public static double k = .01;  //spring constant
	public static double l = 10;  //length scale
	public static double gamma = .01;
	public static double tau = (gamma*l*l)/(KbT);  //time scale
	
	public int xdistance = 125;
	public int ydistance = 0;
   public int zdistance = 400; // z distance

	public static double changeTime = .2; //delta t
	public double rate;  //velocity;
	private double simulationTime;
	
	//statistics
	ArrayList<dataPoint> workPoints;  //All of the points with (work,time),  This is also passed to the other frame for display

	//constructor
	public stretchingEventListener(int particles, double pullrate) {
		workPoints = new ArrayList<dataPoint>(); //initialize the statistics points
		
		glu = new GLU();
		circleDetail = 100;
		radianChange = 2*3.1415926535/circleDetail;
		xdistance = 125;
		ydistance = 0;
		zdistance = 400;
		
		rate = pullrate;
		
		numOfParticles = particles;
		simulationTime = 0;
		
		initParticles();
	}
	
	private void initParticles() {
		particles = new ArrayList<Particle>(numOfParticles);
		
		double tempX = 0;
		double tempY = 0;
		
		for (int i=0; i< (numOfParticles/2); i++) {
			Particle temp1 = new Particle(tempX, tempY, 0, smallRadius, 1);
			//Particle temp2 = new Particle(tempX, tempY, 0, smallRadius, 1);
			particles.add(i, temp1);
			//particles.add(numOfParticles-1-i,temp2);
			double random = Math.random();
			
			tempX += 10*Math.cos(2*Math.PI*random);
			tempY += 10*Math.sin(2*Math.PI*random);			
		}
		
		//add the very center one if the particles are odd
		if (2*(numOfParticles/2) != numOfParticles) {
			particles.add((numOfParticles/2), new Particle(tempX, tempY, 0, smallRadius, 1));
			
			for (int i = (numOfParticles/2 -1), j = (numOfParticles/2 +1); i >= 0; i--, j++) {
				Particle temp2 = new Particle(particles.get(i).myPosition.x, particles.get(i).myPosition.y,
						0, smallRadius, 1);
				particles.add(j, temp2);
			}
		} else {
			for (int i = (numOfParticles/2 -1), j = (numOfParticles/2); i >= 0; i--, j++) {
				Particle temp2 = new Particle(particles.get(i).myPosition.x, particles.get(i).myPosition.y,
						0, smallRadius, 1);
				particles.add(j, temp2);
			}
		}
		
		//change first and last radius
		particles.get(0).myRadius = largeRadius;
		particles.get(numOfParticles-1).myRadius = largeRadius;
				
	}

	public void init(GLAutoDrawable drawable) {
		// initializes the positions and sizes of the particles
		
	    GL2 gl = (GL2) drawable.getGL();  
	    
		gl.glShadeModel(GL2.GL_FLAT);
        gl.glClearDepth(1.0f);                      // Depth Buffer Setup
        gl.glEnable(GL2.GL_DEPTH_TEST);							// Enables Depth Testing
        gl.glDepthFunc(GL2.GL_LEQUAL);
	    gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);  //white background
	    
	    //Generate Display list for the border, small, large particles
	    borderDList = gl.glGenLists(3);
	    
	    gl.glNewList(borderDList, GL2.GL_COMPILE);
	    drawBoundary(gl);
	    gl.glEndList();
	    
	    smallParticleDList = borderDList + 1;
	    gl.glNewList(smallParticleDList, GL2.GL_COMPILE);
	    drawCircle(circleDetail, gl, smallRadius);
	    gl.glEndList();
		
	    largeParticleDList = borderDList + 2;
	    gl.glNewList(largeParticleDList, GL2.GL_COMPILE);
	    drawCircle(circleDetail, gl, largeRadius);
	    gl.glEndList();
	    
	    gl.glColor3f(0.6f, 0.0f, 0.0f);
	}
	
	public void display(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
		GL2 gl = (GL2) drawable.getGL();
		
	    // clear colour and depth buffers
	    gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
	    gl.glLoadIdentity();
	    
	    glu.gluLookAt(xdistance,ydistance, zdistance, xdistance,ydistance,0, 0,1,0);   // position camera
	    
	    //gl.glCallList(borderDList);
	    
	    simulationTime += changeTime;
	    
	    updateParticles();
	    drawParticles(gl);
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// reshape the renderable glcanvas
		
		GL2 gl = (GL2) drawable.getGL();

	    //avoid division by 0
	    if (height == 0) {
	    	height = 1;
	    }

	    gl.glViewport(x, y, width, height);  // size of drawing area 

       System.out.println("ZDistance: " + zdistance);

	    gl.glMatrixMode(GL2.GL_PROJECTION);
	    gl.glLoadIdentity();
	    glu.gluPerspective(45.0, (float)width/(float)height, 1, zdistance+1);  //perspective

	    gl.glMatrixMode(GL2.GL_MODELVIEW);
	    gl.glLoadIdentity();
		
	}
	
	public void displayChanged(GLAutoDrawable drawable, boolean arg1, boolean arg2) {
		// No need to implement
		
	}
	
	private void drawCircle (int detail, GL2 gl, double particleRadius) {
		//gl.glColor3d(Math.random(), Math.random(), Math.random());
		gl.glBegin(GL2.GL_POLYGON);
			for (int i = 0; i< detail; i++) {
				double x = Math.cos(i*radianChange);
				double y = Math.sin(i*radianChange);
				gl.glVertex2d(particleRadius*x, particleRadius*y);
			}
		gl.glEnd();
	}
	
	private void drawBoundary(GL2 gl) {
		gl.glLineWidth(3.0f);
		gl.glEnable(GL2.GL_LINE_STIPPLE);
		gl.glColor3f(0.6f, 0.0f, 0.0f);
		
		//draw border
		gl.glBegin(GL2.GL_LINE_LOOP);
			gl.glVertex2d(-100, -100);
			gl.glVertex2d(-100, 100);
			gl.glVertex2d(100, 100);
			gl.glVertex2d(100, -100);
		gl.glEnd();
	}
	
   private void updateParticles() {
      //particle 0 stays put
      //particle 1 moves by the set amount each time and then update all the other particles accoourdingly

      //get leading pulled particle
      Particle temp = particles.get(numOfParticles-1);

      //first move particle1 a bit
      temp.myPosition.x += (changeTime * rate);  //time change rate * velocity(1)

      //then for each of the others, excluding the ends, find forces.
      for (int i= (numOfParticles-2); i > 0; i-- ) {
         particles.get(i).findForce(particles.get(i-1), particles.get(i+1));
      }

      //after finding forces, move them based on the force.
      for (int i= (numOfParticles-2); i > 0; i-- ) {
         particles.get(i).moveParticle(changeTime);
      }

      //print the work
      System.out.println("WORK IS: " + temp.findWork(particles.get(numOfParticles - 2), changeTime, rate));
   }
	
	private void drawParticles (GL2 gl) {
		for (int i=0; i < numOfParticles; i++) {
			gl.glPushMatrix();
			gl.glTranslated(particles.get(i).myPosition.x, particles.get(i).myPosition.y, 0);
			//gl.glVertex2d(0,0);
			drawCircle(circleDetail, gl, particles.get(i).myRadius);
			gl.glPopMatrix();
		}
		
		//draw the lines
		gl.glBegin(GL2.GL_LINES);
			Iterator<Particle> tempIter = particles.iterator();
			Particle last = tempIter.next();
			Particle next = null;
			
			while(tempIter.hasNext()) {
				next = tempIter.next();
				gl.glVertex2d(last.myPosition.x, last.myPosition.y);
				gl.glVertex2d(next.myPosition.x, next.myPosition.y);
				last = next;
			}
		
		gl.glEnd();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

}
