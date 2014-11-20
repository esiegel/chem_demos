/*
 * EnergyDiffusionMain.java
 *
 * Created on June 13, 2007, 2:04 PM
 */
public class EnergyDiffusionMain extends javax.swing.JFrame {
    
    /** Creates new form energyDiffusionMain */
    public EnergyDiffusionMain() {
        initComponents(1024, .2, 1, 500, false, false, true, false, false);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents(int particles, double ratio, double mass, double velocity, boolean dispersed, boolean trace,
    		boolean stats, boolean instCumul, boolean colors) {

        //jPanel1 = new particleAnimationPanel(64, .2, 1, 50, false);
    	jPanel1 = new particleAnimationPanel(particles, ratio, mass, velocity, dispersed, trace, stats, instCumul, colors);
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(512, 512));

        jTextField1.setText(Integer.toString(particles));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Particles(Square #)");

        jTextField2.setText(Double.toString(ratio));
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel2.setText("Density(0-Pi/4)");

        jTextField3.setText(Double.toString(velocity));
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jLabel3.setText("Velocity");

        jCheckBox1.setText("Dispersed(off) local(1)");
        jCheckBox1.setSelected(dispersed);
        jCheckBox1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jCheckBox1.setInheritsPopupMenu(true);
        jCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jCheckBox1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jButton1.setText("Pause/Start");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Restart");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jCheckBox2.setText("trace");
        jCheckBox2.setSelected(trace);
        jCheckBox2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jCheckBox2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jCheckBox2.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jCheckBox3.setText("Show Stats");
        jCheckBox3.setSelected(stats);
        jCheckBox3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jCheckBox3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jCheckBox3.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jCheckBox4.setText("Instantaneous/Cumulative (off/on)");
        jCheckBox4.setSelected(instCumul);
        jCheckBox4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jCheckBox4.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox4.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jCheckBox4.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        jCheckBox6.setText("Colors");
        jCheckBox6.setSelected(colors);
        jCheckBox6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jCheckBox6.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox6.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jCheckBox6.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jCheckBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox6ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 1150, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(layout.createSequentialGroup()
                        .add(24, 24, 24)
                        .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(24, 24, 24)
                        .add(jTextField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jTextField3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBox1)
                .add(26, 26, 26)
                .add(jCheckBox2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jCheckBox3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jCheckBox4)
                .add(18, 18, 18)
                .add(jCheckBox6)
                .add(36, 36, 36)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jButton1)
                    .add(jButton2))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                .add(35, 35, 35)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel1)
                            .add(jLabel2)
                            .add(jLabel3))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jTextField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jTextField3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jCheckBox1)
                        .add(jCheckBox2)
                        .add(jCheckBox3)
                        .add(jCheckBox4))
                    .add(jCheckBox6)
                    .add(layout.createSequentialGroup()
                        .add(jButton1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton2)))
                .addContainerGap())
        );

        pack();
    }
    
private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    // TODO add your handling code here:
	//make sure numbers
	if ( (!jTextField1.getText().matches("[0-9]*[.]?[0-9]+"))||
			(!jTextField2.getText().matches("[0-9]*[.]?[0-9]+")) ||
			(!jTextField3.getText().matches("[0-9]*[.]?[0-9]+")) ) {
		jTextField1.setText("");
		jTextField2.setText("");
		jTextField3.setText("");
		return;
	}
	
	int particles = Integer.parseInt(jTextField1.getText());
	double density = Double.parseDouble(jTextField2.getText());
	double vel = Double.parseDouble(jTextField3.getText());
	boolean dispersed = jCheckBox1.isSelected();
	boolean trace = jCheckBox2.isSelected();
	boolean stats = jCheckBox3.isSelected();
	boolean instCumul = jCheckBox4.isSelected();
	boolean colors = jCheckBox6.isSelected();
	
	//make sure appropriate values
	if (Math.rint(Math.sqrt(particles)) != Math.sqrt(particles)) {
		jTextField1.setText("Needs to be a square number");
		return;
	}
	if ((density <= 0) || (density >= Math.PI/4)) {
		jTextField2.setText("Needs to be (0-Pi/4) noninclusive");
		return;
	}
	
	System.out.println(particles + " " + density + " " + vel);
	
   jPanel1.stopAnimator();
	
	getContentPane().remove(jPanel1);
	this.remove(jTextField1);
	this.remove(jTextField2);
	this.remove(jTextField3);
	this.remove(jLabel1);
	this.remove(jLabel2);
	this.remove(jLabel3);
	this.remove(jButton1);
	this.remove(jButton2);
	this.remove(jCheckBox1);
	this.remove(jCheckBox2);
	this.remove(jCheckBox3);
	this.remove(jCheckBox4);
	this.remove(jCheckBox6);
	
	jPanel1 = new particleAnimationPanel(particles, density, 1, vel, dispersed, trace, stats, instCumul, colors);
	getContentPane().add(jPanel1);
	initComponents(particles, density, 1, vel, dispersed, trace, stats, instCumul, colors);
	
	validate();
	pack();
	
	
    System.out.println("restart pressed");
}//GEN-LAST:event_jButton2ActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
   jPanel1.toggleAnimator();
}//GEN-LAST:event_jButton1ActionPerformed

private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
    // TODO add your handling code here
    System.out.println("dispersed local pressed");
}//GEN-LAST:event_jCheckBox1ActionPerformed

private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {
    // TODO add your handling code here:
    System.out.println("trace clicked");
}

private void jCheckBox6ActionPerformed(java.awt.event.ActionEvent evt) {
    // TODO add your handling code here:
	System.out.println("colors");
}

private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {
    // TODO add your handling code here:
    System.out.println("instantaneous histogram");
}

private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {
    // TODO add your handling code here:
    System.out.println("show stats");
}

private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
    // TODO add your handling code here:
    System.out.println("speed pressed");
}//GEN-LAST:event_jTextField3ActionPerformed

private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
    // TODO add your handling code here:
    System.out.println("clicked density");
}//GEN-LAST:event_jTextField2ActionPerformed

private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
    // TODO add your handling code here:
    System.out.println("clicked particles" + Integer.parseInt(jTextField1.getText()));
}//GEN-LAST:event_jTextField1ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EnergyDiffusionMain().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private particleAnimationPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox6;
    // End of variables declaration//GEN-END:variables
    
}
