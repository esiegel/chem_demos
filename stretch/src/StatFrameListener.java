import java.util.ArrayList;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;


public class StatFrameListener implements GLEventListener {
	
	ArrayList<dataPoint> workPoints;  //All of the points with (work,time)
	
	public StatFrameListener(ArrayList<dataPoint> points) {
		//constructor for the program
		workPoints = points;
	}

	public void display(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		// TODO Auto-generated method stub
		
	}

	public void init(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

}
