package de.htwg.modprog.cubic
import de.htwg.modprog.cubic.view.tui.Tui
import de.htwg.modprog.cubic.controller.CubicController
import de.htwg.modprog.cubic.model.impl.CubicBoard
import de.htwg.modprog.cubic.model.impl.CubicGame

object Cubic {
  val controller = new CubicController(CubicGame())
  val tui = new Tui(controller)
  controller.createQuickVersusGame("Player 1", "Player 2")
  def main(args: Array[String]) {
    while (tui.processInputLine(readLine())) {}
  }
}