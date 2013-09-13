package org.ldv.melun.sio.swingpac.etudiants;

import org.ldv.melun.sio.swingpac.Bidule;

public class Toto extends Bidule {

  public Toto(String name) {
    super(name);
  }

  public Toto() {
    super("Superman");
  }

  
  @Override
  public void doMove() {
    super.doMove();
  }

  @Override
  protected void doAfterImpactByOther() {
    super.doAfterImpactByOther();
    if (isGoDown())
      goOnTop();

  }

  
  
}
