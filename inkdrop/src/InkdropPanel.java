/*InkdropPanel:
 * 
 * Extends JPanel and adds a GLCanvas that is linked to a InkdropListener(GLeventListener).
 * It has a reference to the InkdropListener listener, and can call methods within the listener
 * to add particles.
 * 
 * */


import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JPanel;

import com.jogamp.opengl.util.FPSAnimator;

public class InkdropPanel extends JPanel {

	//Frames per second, window size, and animation variables.
	private static int FPS = 80;
	private static final int PWIDTH = 512;
	private static final int PHEIGHT = 512; 
	private InkdropListener listener;
	static FPSAnimator animator;
	//static Animator animator;

	//variables that define this inkdrop demo
	private int latticeSideLength;
	private long numOfWalkers;
	private long numOfSteps;
	private boolean tracePath;
	private boolean showLattice;


	public InkdropPanel (int latticeSide, long walkers, long steps, 
			boolean trace, boolean lattice, boolean slow ) {


		latticeSideLength = latticeSide;
		numOfWalkers = walkers;
		numOfSteps = steps;
		tracePath = trace;
		showLattice = lattice;

		if (slow) {
			FPS = 5;
		} else {
			FPS = 80;
		}

		//This is where we add the GLCanvas to this Jpanel
		createRenderPanel(FPS);


		setVisible(true);
		validate();   //Allows the frame to be layed out before the animation starts
		
	}

	// Attaches the GLCanvas to this JPanel.
	//This GLCanvas is attached to the InkdropListener Listener.
	//Also, given the argument fps, frames per second, an animator is created and linked to the canvas.
	//This Canvas is where the animation will occur.
	private void createRenderPanel(int fps) {
		

		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
		setOpaque(false);

		GLCapabilities Rendercaps = new GLCapabilities(null);
		Rendercaps.setDoubleBuffered(true);

		GLCanvas renderableCanvas = new GLCanvas(Rendercaps);

		listener = new InkdropListener(latticeSideLength, numOfWalkers, numOfSteps, tracePath, showLattice);

		animator = new FPSAnimator(renderableCanvas, fps, true);
		//animator = new Animator(renderableCanvas);

		renderableCanvas.addGLEventListener(listener);

		this.add(renderableCanvas, BorderLayout.CENTER);
	}

	//turns the animator on or off, and is toggled from the JFrame in the main class
	public void animatorONOFF () {
		if (animator.isAnimating()) {
			animator.stop();
		} else {
			animator.start();
		}
	}
	
	//Adds more particles to the beginning position, and is called from the buttons in the main class
	public void addMoreParticles(int particles) {
		listener.particlePositions[latticeSideLength - 1][(latticeSideLength)/2 - 1] += particles;
		listener.numOfWalkers += particles;
	}

} 



