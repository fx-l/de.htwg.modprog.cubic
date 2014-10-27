package de.htwg.modprog.cubic.view.tui

import de.htwg.modprog.cubic.controller.CubicController
import scala.swing.Reactor
import de.htwg.modprog.cubic.controller.CubicController

class Tui(var controller: CubicController) extends Reactor {
  //  listenTo(controller)
  printTui
  def update = printTui
  def printTui = {
    println("Enter command: n-New, q-Quit, d-Drop disk at column (0-3), c-Change layer(0-2), s[1-3]-Set size")
  }
  def processInputLine(input: String) = {
    var continue = true
    input match {
      //      case "n" => controller.newGame
      case "q" => continue = false
      //      case "d" => controller.dropDisk
      //      case "c" => controller.changeLayer
    }
    continue
  }
}