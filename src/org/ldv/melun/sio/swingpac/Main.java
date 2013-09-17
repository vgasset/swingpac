package org.ldv.melun.sio.swingpac;

import javax.swing.JFrame;
/**
*
* Lanceur du l'application
* @author lycée Léonard de Vinci - Melun - SIO-SLAM
*
*/
public class Main {
    
  /**
* <a href="http://docs.oracle.com/javase/tutorial/uiswing/index.html">api doc</a>
* Create the GUI and show it. For thread safety,
* this method should be invoked from the
* event-dispatching thread.
*/
  private static void createAndShowGUI() {
    JFrame f = new FenetreMain();
    f.setVisible(true);
  }

  public static void main(String[] args) {
      //Schedule a job for the event-dispatching thread:
      //creating and showing this application's GUI.
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
          public void run() {
              createAndShowGUI();
          }
      });
  }
  
}
