import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jogamp.opengl.util.Animator;


public class StatPanel extends JPanel {
	
	ArrayList<dataPoint> workPoints;  //All of the points with (work,time)
	private StatFrameListener listener;  //the gleventslistener for the statistics frame graphics
	
	public StatPanel (ArrayList<dataPoint> points, Animator animator) {
		workPoints = points;  //pass in the points from the stretchingAnimationPanel
		this.setLayout(new BorderLayout());
		setPreferredSize(new Dimension(512, 512));
		
		//Create the glCapabilities to add to the particleEventListener (gleventlistener)
		//create a glCanvas to attach the listener, and add this to the JPanel.
		GLCapabilities Rendercaps = new GLCapabilities(null);
		Rendercaps.setDoubleBuffered(true);

		GLCanvas renderableCanvas = new GLCanvas(Rendercaps);

		listener = new StatFrameListener(points);
		
		renderableCanvas.addGLEventListener(listener);

		this.add(renderableCanvas, BorderLayout.CENTER);
		
		setVisible(true);
		validate();   //Allows the frame to be layed out before the animation starts
	}
}