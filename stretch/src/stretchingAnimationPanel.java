import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JPanel;

import com.jogamp.opengl.util.FPSAnimator;


public class stretchingAnimationPanel extends JPanel {
	
	stretchingEventListener listener;  //gleventlistener
	FPSAnimator animator; //animator to keep steady frame rate
	
	public int fps; //frame rate
	
	public int particles;
	public double rate;
	
	public GLCanvas renderableCanvas;
	
	
	public stretchingAnimationPanel (int numOfParticles, double pullrate) {
		//Create the glCapabilities to add to the particleEventListener (gleventlistener)
		//create a glCanvas to attach the listener, and add this to the JPanel.
		//fps = 30 + (int) (pullrate/6);
		fps = 80;

		particles = numOfParticles;
		rate = 10*pullrate/fps;
		
		GLCapabilities Rendercaps = new GLCapabilities(null);
		Rendercaps.setDoubleBuffered(true);

		renderableCanvas = new GLCanvas(Rendercaps);

		listener = new stretchingEventListener(numOfParticles, rate);

		animator = new FPSAnimator(renderableCanvas, fps, true);
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(512, 512));
		setOpaque(false);

		renderableCanvas.addGLEventListener(listener);

		this.add(renderableCanvas);
		
		setVisible(true);
		validate();   //Allows the frame to be layed out before the animation starts
		
		//Now create the statistics frame
	}

}