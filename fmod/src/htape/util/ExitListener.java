package htape.util;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
* Created by IntelliJ IDEA.
* User: ben
* Date: 1/27/11
* Time: 5:08 PM
* To change this template use File | Settings | File Templates.
*/
public class ExitListener extends WindowAdapter {
  public void windowClosing(WindowEvent event) {
    System.exit(0);
  }
}
