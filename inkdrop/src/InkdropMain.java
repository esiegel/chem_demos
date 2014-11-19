/* Inkdrop
 * Professor Geissler
 * Eric Siegel
 * 
 * An inkdrop is dropped in a glass of liquid at the top.
 * 
 * Originally, all particles of ink start in the same square quadrent and then random walk (diffuse) into
 * the rest of the container.
 * 
 * In this program, one can specify several things:
 * 1. Size of lattice sides (the size of this square container).
 * 2. Number of random walkers (number of particles).
 * 3. Number of steps that these walkers take before the experiment is completed.
 * 4. Do you want to trace out the path of each random walker? (beneficial when few walkers)?
 * 5. Do you want to view the actual lattice (will draw the line seperators between each lattice site)?
 * 6. Do you want to view this demonstration slowed down?
 *  
 * There are three classes for this program:
 * 1. InkdropMain: It implements JFrame and is where the main method begins as well as the program.  Most of the
 * code in this class is just for producing the JFrame, the textFields, buttons, and the events on these components.
 * 
 * 2.  InkdropPanel:  It implements JPanel and has a GLCanvas with a InkdropListener(glEventListener) attached to it.
 * This is where the FPSAnimator is produced that drives the animation forward.  It also hold references to the
 * InkdropListener and is able to add adition particles, pause the animator, etc.
 * 
 * 3.  InkdropListener - It implement glEventListener and it is where the substance of the animation happens.  This is 
 * where the border, the latticeGrid, and all of the particlesQuads get drawn.  There is an init(), reshape(), and a 
 * display() that get called.
 *  
 * */



public class InkdropMain extends javax.swing.JFrame {
    
    //This constructor just calls the method that creates the JFrame, which in turn produce the InkdropPanel
	//and the InkdropListener.
    public InkdropMain() {
        initComponents();
    }
    
    //Creates the borderLayout and its allignment, CREATED WITH NETBEANS, COMPLICATED
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        //Add the InkdropPanel to this layout
    	jPanel1 = new InkdropPanel(100, 1000, 100000, false, false, false);
    	
        jPanel2 = new javax.swing.JPanel();
        sideLengthText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        numOfParticlesText = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        framesText = new javax.swing.JTextField();
        traceCheck = new javax.swing.JCheckBox();
        showGridCheck = new javax.swing.JCheckBox();
        slowFramesCheck = new javax.swing.JCheckBox();
        pauseButton = new javax.swing.JButton();
        restartButton = new javax.swing.JButton();
        addOne = new javax.swing.JButton();
        addTen = new javax.swing.JButton();
        addHundred = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        sideLengthText.setText("100");
        sideLengthText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SideLengthListener(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Size of SideLength");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Number of Particles");

        numOfParticlesText.setText("1000");
        numOfParticlesText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setNumberParticles(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Number of Frames");

        framesText.setText("100000");
        framesText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setNumberFrames(evt);
            }
        });

        traceCheck.setText("Trace?");
        traceCheck.setMargin(new java.awt.Insets(0, 0, 0, 0));
        traceCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                traceToggle(evt);
            }
        });

        showGridCheck.setText("Show Grid?");
        showGridCheck.setMargin(new java.awt.Insets(0, 0, 0, 0));
        showGridCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gridToggle(evt);
            }
        });

        slowFramesCheck.setText("Slow Frames?");
        slowFramesCheck.setMargin(new java.awt.Insets(0, 0, 0, 0));
        slowFramesCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleFrames(evt);
            }
        });

        pauseButton.setText("Pause/Start");
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseAnimator(evt);
            }
        });

        restartButton.setText("Restart");
        restartButton.setToolTipText("Restart only function if the text fields are correct.");
        restartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restartAnimation(evt);
            }
        });

        addOne.setText("Add 1 Particle");
        addOne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addOneParticle(evt);
            }
        });

        addTen.setText("Add 10 Particles");
        addTen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTenParticles(evt);
            }
        });

        addHundred.setText("Add 100 Particles");
        addHundred.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addHundredParticles(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(29, 29, 29)
                        .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 146, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(66, 66, 66)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(traceCheck)
                            .add(showGridCheck)
                            .add(slowFramesCheck)))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(66, 66, 66)
                        .add(sideLengthText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 69, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(82, 82, 82)
                                .add(numOfParticlesText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(45, 45, 45)
                                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(addOne)
                                    .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 146, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(addTen)
                                    .add(addHundred))))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 71, Short.MAX_VALUE)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 146, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(40, 40, 40))
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(framesText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 78, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(72, 72, 72))))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                        .add(pauseButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(restartButton)
                        .add(47, 47, 47))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(11, 11, 11)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel1)
                            .add(jLabel2))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(sideLengthText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(numOfParticlesText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(jLabel3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(framesText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(36, 36, 36)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(traceCheck)
                    .add(addOne))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(showGridCheck)
                    .add(pauseButton)
                    .add(restartButton)
                    .add(addTen))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(slowFramesCheck)
                    .add(addHundred, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(19, 19, 19))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents



//  EVENTS    
//  Restarts the animation if the textFields have appropriate numbers.  It does this by creating a new
//	InkdropPanel and replacing the old one.
    private void restartAnimation(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restartAnimation
    	// TODO add your handling code here:
    	String sides = sideLengthText.getText();
    	String particles = numOfParticlesText.getText();
    	String frames = framesText.getText();

    	//The regExp to make sure all text fields have positive numbers
    	if (sides.matches("[0-9]+") && particles.matches("[0-9]+") && frames.matches("[0-9]+")) {
    		if (jPanel1.animator.isAnimating()) {
    			jPanel1.animator.stop();
    		}

    		getContentPane().remove(jPanel1);
    		jPanel1 = new InkdropPanel(Integer.parseInt(sides), Integer.parseInt(particles), Integer.parseInt(frames),
    				traceCheck.isSelected(), showGridCheck.isSelected(), slowFramesCheck.isSelected());
    		System.out.println("Animation Restarted");
    		getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    		pack();
    	}
    
    
}//GEN-LAST:event_restartAnimation

//  Pauses the animation
private void pauseAnimator(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseAnimator
    // TODO add your handling code here:
    jPanel1.animatorONOFF();
}//GEN-LAST:event_pauseAnimator


//Toggles from slow animation to normal animation.
private void toggleFrames(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleFrames
    // TODO add your handling code here:
    if (slowFramesCheck.isSelected()) {
        System.out.println("Frames Slow");
    } else {
        System.out.println("Frames Normal");
    }
}//GEN-LAST:event_toggleFrames

//Toggles betweenn showing the lattice grid and not.
private void gridToggle(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gridToggle
    // TODO add your handling code here:
    if (showGridCheck.isSelected()) {
        System.out.println("Grid ON");
    } else {
        System.out.println("Grid Off");
    }
}//GEN-LAST:event_gridToggle

//Toggles between tracing particles and not
private void traceToggle(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_traceToggle
    // TODO add your handling code here:
    if (traceCheck.isSelected()) {
        System.out.println("Trace ON");
    } else {
        System.out.println("Trace Off");
    }
}//GEN-LAST:event_traceToggle

//Adds 100 particles to the start position
private void addHundredParticles(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addHundredParticles
    // TODO add your handling code here:
    System.out.println("add one-hundred please");
    jPanel1.addMoreParticles(100);
}//GEN-LAST:event_addHundredParticles

//Adds 10 particles to the start position
private void addTenParticles(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTenParticles
    // TODO add your handling code here:
    System.out.println("add ten please");
    jPanel1.addMoreParticles(10);
}//GEN-LAST:event_addTenParticles

//Adds 1 particle to the start position.
private void addOneParticle(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addOneParticle
    // TODO add your handling code here:
    System.out.println("add one please");
    jPanel1.addMoreParticles(1);
}//GEN-LAST:event_addOneParticle

//Sets the amount of frames before the animation stops
private void setNumberFrames(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setNumberFrames
    // TODO add your handling code here:
        String tempText = framesText.getText();
    if (tempText.matches("[0-9]+")) {
        System.out.println(tempText);
    } else {
        framesText.setText("positive number please");
        
    }
}//GEN-LAST:event_setNumberFrames

//Sets the number of total particles in the container
private void setNumberParticles(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setNumberParticles
    // TODO add your handling code here:
        String tempText = numOfParticlesText.getText();
    if (tempText.matches("[0-9]+")) {
        System.out.println(tempText);
    } else {
        numOfParticlesText.setText("positive number please");
        
    }
}//GEN-LAST:event_setNumberParticles

//Sets the sidelenght and therefore the dimension of the container after the restart.
private void SideLengthListener(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SideLengthListener
    // TODO add your handling code here:
    String tempText = sideLengthText.getText();
    if (tempText.matches("[0-9]+")) {
        System.out.println(tempText);
    } else {
        sideLengthText.setText("positive number please");
        
    }
}//GEN-LAST:event_SideLengthListener
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InkdropMain().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton pauseButton;
    private javax.swing.JButton restartButton;
    private javax.swing.JButton addOne;
    private javax.swing.JButton addTen;
    private javax.swing.JButton addHundred;
    private javax.swing.JCheckBox traceCheck;
    private javax.swing.JCheckBox showGridCheck;
    private javax.swing.JCheckBox slowFramesCheck;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private InkdropPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField sideLengthText;
    private javax.swing.JTextField numOfParticlesText;
    private javax.swing.JTextField framesText;
    // End of variables declaration//GEN-END:variables
    
}
