package org.ldv.melun.sio.swingpac.etudiants;

import java.awt.Color;

import org.ldv.melun.sio.swingpac.Bidule;

public class Simple extends Bidule {

  
  public Simple() {
    super("Simple");
    setBackground(Color.GREEN);    
  }

  @Override
  public void doMove() {  
    super.doMove();    
  }

  @Override
  protected void doAfterImpactByOther() {
    super.doAfterImpactByOther();
  }

  
}
