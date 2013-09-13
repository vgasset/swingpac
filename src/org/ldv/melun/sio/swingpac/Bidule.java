package org.ldv.melun.sio.swingpac;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Modèle générique d'objet se déplacant dans la fenêtre (la scene)
 * 
 * @date 2013-09-09
 * @author kpu (lycée Léonard de Vinci - Melun - SIO-SLAM)
 * 
 *         TODO: mémoriser le nombre d'impacts d'objet bidules réalisés (pas
 *         subis)
 *         
 *         TODO : reféfinir toString afin de remonter des informations pertinentes 
 * 
 *         TODO (plus difficile) : charger des images dans un tableau et
 *         appliquer la bonne image (dans paintComponent) en fonction de la
 *         direction de déplacement (à la pacman)
 * 
 */
public class Bidule extends JPanel {

  /**
   * Objet reponsable des déclenchement d'appels (voir MoveAction)
   */
  private Timer timer;

  /**
   * valeur de déplacement en X, Y
   */
  private int incY, incX;

  /**
   * nom de l'instance (TODO : pourrait être pris par défaut via getClass().getName()...)
   */
  
  	String Bidule = getName();
  	
  	public String getName() {
  		return name;
  	
  	}
  	
  	
  
  private String name;

  /**
   * utilisé pour déterminer une valeur 'aléatoire' du DELAI ayant un impact sur
   * le déplacement
   */
  static private Random alea;

  /**
   * initialisation de la propriété de classe.
   */
  static {
    alea = new Random();
  }

  final int DELAYMAX = 10;

  final int DELAYMIN = 5;

  final int DELAY;

  /**
   * dimension minimale considérant un bidule en vie
   */
  final int NB_MINMAL_PIXELS_VIE = 3;

  /**
   * Déclaration du Template/Hook
   * 
   * @author kpu
   * 
   */
  public class MoveAction implements ActionListener {

    @Override
    // traitement générique de la logique de déplacement
    public void actionPerformed(ActionEvent e) {
      doMove();
      setLocation(getX() + incX, getY() + incY);
      stayOnStage();
      manageCollisions();
      testWiner();
    }
  }

  /**
   * Constructeur : initialisateur d'instance
   * 
   * @param name
   *          nickname de l'objet
   */
  public Bidule(String name) {
    super();
    this.name = name;
    this.setSize(100, 100);
    this.setBackground(Color.BLUE);
    this.incX = 1;
    this.incY = 1;

    // demande d'une vitesse de déplacement (d'appel du timer)
    // comprise entre DELAYMAX et DELAYMIN
    // plus le delai est cours, plus c'est rapide
    DELAY = alea.nextInt(DELAYMAX - DELAYMIN) + DELAYMIN;

    this.timer = new Timer(DELAY, new MoveAction());
    this.start();
  }

  /**
   * Rester dans l'espace de la scene
   */
  private void stayOnStage() {
    Rectangle rect = getParent().getBounds();
    // System.out.println(rect);
    int newX = getX();
    int newY = getY();
    // remise dans le cadre
    if (getX() + getWidth() > rect.width)
      newX = rect.width - getWidth();
    if (getX() < 0)
      newX = 0;
    if (getY() + getHeight() > rect.height)
      newY = rect.height - getHeight();
    if (getY() < 0)
      newY = 0;

    if (newX != getX() || newY != getY())
      this.setLocation(newX, newY);
  }

  public void start() {
    this.timer.start();
  }

  public void stop() {
    this.timer.stop();
  }

  public int getIncY() {
    return incY;
  }

  public void setIncY(int incY) {
    if (Math.abs(incY) <= 1)
      this.incY = incY;
    else
      this.incY = 0; // punition !
  }

  public int getIncX() {
    return incX;
  }

  public void setIncX(int incX) {
    if (Math.abs(incX) <= 1)
      this.incX = incX;
    else
      this.incX = 0; // punition !
  }

  /**
   * appelé après un déplacement. Vérifie si impacts, et préviens les objets
   * touchés.
   */
  private void manageCollisions() {
    // ai-je touché d'autres bidules ?
    List<Bidule> bidules = getCollisions();
    for (Bidule bidule : bidules) {
      if (bidule.isGoDown()
          && bidule.getY() + bidule.getHeight() >= this.getY())
        bidule.tuEstouchePar(this);
      else if (bidule.isGoUp()
          && bidule.getY() <= this.getY() + this.getHeight())
        bidule.tuEstouchePar(this);
      else if (bidule.isGoRight()
          && bidule.getX() + bidule.getWidth() >= this.getX())
        bidule.tuEstouchePar(this);
      else if (bidule.isGoLeft()
          && bidule.getX() <= this.getWidth() + this.getX())
        bidule.tuEstouchePar(this);
    }
  }

  /**
   * Vérifie si l'instance courante gagne la partie. Afffiche un message si
   * c'est le cas, puis disparait.
   */
  private void testWiner() {
    // le vainqueur est celui qui reste seul
    if (aloneInTheWorld()) {
      timer.stop();
      JOptionPane.showMessageDialog(getParent(), "GAGNÉ : " + name);
      getParent().remove(this);
    }
  }

  /**
   * Détermine si l'objet courant est seul dans la scene
   * 
   * @return true si aucun autre objet de type Bidule ne partage la scene avec
   *         l'objet courant
   */
  private boolean aloneInTheWorld() {
    for (Component obj : getParent().getComponents())
      if (obj instanceof Bidule && obj != this)
        return false;
    return true;
  }

  /**
   * Appelé par un autre objet lorsqu'il me touche
   * 
   * @param biduleImpacteur
   *          l'objet qui vient de rentrer en collision avec moi
   */
  public void tuEstouchePar(Bidule biduleImpacteur) {
    // je retrécis
    this.setBounds(getX() + incX, getY() + incY, getWidth() - 1,
        getHeight() - 1);

    // TODO (plus difficile) : augmenter la taille de biduleImpacteur (dans la
    // limite du quart (un pourcentage) de la taille initiale)
    // si celui-ci a touché au moins x autres bidules

    // en dessous d'une dimension minimale, l'objet
    // courant disparait de ce monde...
    if (getWidth() < NB_MINMAL_PIXELS_VIE || getHeight() < NB_MINMAL_PIXELS_VIE) {
      // sucide...
      this.stop();
      System.out.println("Je meurs :-(   " + this.name);
      getParent().remove(this);
    }

    doAfterImpactByOther();
  }

  /**
   * établir une stratégie après impact ; un autre bidule vient de (me) toucher
   * (toucher l'objet courant)
   */
  protected void doAfterImpactByOther() {
    // à redéfinir par les classes enfants
  }

  /**
   * obtenir le sens de déplacement
   * 
   * @return true if down
   */
  public boolean isGoDown() {
    return incY > 0;
  }

  /**
   * obtenir le sens de déplacement
   * 
   * @return true if up
   */
  public boolean isGoUp() {
    return incY < 0;
  }

  /**
   * obtenir le sens de déplacement
   * 
   * @return true if right
   */
  public boolean isGoRight() {
    return incX > 0;
  }

  /**
   * obtenir le sens de déplacement
   * 
   * @return true if left
   */
  public boolean isGoLeft() {
    return incX < 0;
  }

  /**
   * Obtenir les objets de la scence en collision avec l'objet courant
   * 
   * @return une liste de références aux objets en collision ou une collection
   *         vide
   */
  private List<Bidule> getCollisions() {
    return getBidulesProches(0);
  }

  /**
   * Obtenir les objets de la scence à une certaine distance de l'objet courant
   * 
   * @param distance
   *          en pixel
   * @return une liste de références aux objets proches ou une collection vide
   */
  protected List<Bidule> getBidulesProches(int distance) {
    Rectangle mySpace = getBounds();
    // on agrandit le périmetre (en fonction du paramètre distance)
    mySpace.setBounds(mySpace.x - distance, mySpace.y - distance, mySpace.width
        + distance, mySpace.height + distance);

    List<Bidule> bidulesEnCollision = new ArrayList<Bidule>();

    for (Component obj : getParent().getComponents()) {
      if (obj instanceof Bidule && obj != this)
        if (obj.getBounds().intersects(mySpace))
          // on est sur ici que obj référence un Bidule
          // force le type (cast)
          bidulesEnCollision.add((Bidule) obj);
    }
    return bidulesEnCollision;
  }

  /**
   * oriente le déplacement vers le bas
   */
  public void goOnDown() {
    if (incY < 0)
      incY *= -1;
  }

  /**
   * oriente le déplacement vers le haut
   */
  public void goOnTop() {
    if (incY > 0)
      incY *= -1;
  }

  /**
   * oriente le déplacement vers la droite
   */
  public void goOnRight() {
    if (incX < 0)
      incX *= -1;
  }

  /**
   * oriente le déplacement vers la gauche
   */
  public void goOnLeft() {
    if (incX > 0)
      incX *= -1;
  }

  /**
   * appelé par la tache du timer pour déplacer l'objet courant. Suite à cet
   * appel, l'objet est positionné par setLocation(getX() + incX, getY() + incY);
   * puis automatiquement recadré dans la scene si nécessaire.
   */
  public void doMove() {
    // obtenir les coordonnées de la scene
    Rectangle rect = getParent().getBounds();

    // changement de direction si une frontière est atteinte
    if (getX() + getWidth() + incX > rect.width)
      goOnLeft();
    if (getX() + incX < 0)
      goOnRight();
    if (getY() + getHeight() + incY > rect.height)
      goOnTop();
    if (getY() + incY < 0)
      goOnDown();
  }
}
