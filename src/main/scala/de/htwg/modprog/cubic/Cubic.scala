package de.htwg.modprog.cubic
import de.htwg.modprog.cubic.view.tui.Tui
import de.htwg.modprog.cubic.view.gui._
import de.htwg.modprog.cubic.controller.CubicController
import de.htwg.modprog.cubic.model.impl.CubicBoard
import de.htwg.modprog.cubic.model.impl.CubicGame

object Cubic {
  
  val controller = new CubicController(CubicGame())
  val tui = new Tui(controller)
  controller.createQuickVersusGame
  
  def main(args: Array[String]) {
    startFxGui(controller)
    while (tui.processInputLine(readLine())) {}
    System.exit(0); // explicit exit needed to close ScalaFX Thread
  }
  
  def startFxGui(controller: CubicController) = {
    new Thread(new Runnable {
      def run() {
        Gui.registerController(controller)
        Gui.main(Array())
      }
    }).start()
  }
  
}