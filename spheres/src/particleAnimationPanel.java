import java.awt.BorderLayout;
import java.awt.Dimension;

import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.GLCapabilities;
import javax.swing.JPanel;

import com.jogamp.opengl.util.FPSAnimator;


public class particleAnimationPanel extends JPanel {
	
	particleEventListener listener;  //gleventlistener
	FPSAnimator animator = null;
	public GLCanvas renderableCanvas;
	
	//static int fps = 60; //frame rate
	
	public particleAnimationPanel (int numOfParticles, double ratio, double mass, double velocity, boolean dispersed, boolean trace,
			boolean stats, boolean instCumul, boolean colors) {
		//Create the glCapabilities to add to the particleEventListener (gleventlistener)
		//create a glCanvas to attach the listener, and add this to the JPanel.

		GLCapabilities Rendercaps = new GLCapabilities(null);
		Rendercaps.setDoubleBuffered(true);

		renderableCanvas = new GLCanvas(Rendercaps);

		listener = new particleEventListener(numOfParticles, ratio, mass, velocity, dispersed, trace, stats, instCumul, colors);
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(512, 512));
		setOpaque(false);

		renderableCanvas.addGLEventListener(listener);
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(512, 512));
		setOpaque(false);

		this.add(renderableCanvas);
		
		setVisible(true);
		validate();   //Allows the frame to be layed out before the animation starts
	}

   public void startAnimator() {
      if (animator == null) {
         animator = new FPSAnimator(renderableCanvas, 60, true);
      }

      animator.start();
   }

   public void stopAnimator() {
      if (animator != null && animator.isAnimating()) {
         animator.stop();
      }
   }

   public void toggleAnimator() {
      if (animator == null) {
         animator = new FPSAnimator(renderableCanvas, 60, true);
      }

      if (animator.isAnimating()) {
         animator.stop();
      } else {
         animator.start();
      }
   }

}
