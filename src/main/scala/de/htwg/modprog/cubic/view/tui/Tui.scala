package de.htwg.modprog.cubic.view.tui

import de.htwg.modprog.cubic.controller.CubicController
import scala.swing.Reactor
import de.htwg.modprog.cubic.controller.CubicController
import de.htwg.modprog.cubic.controller.CubicController
import de.htwg.modprog.cubic.controller.FieldChanged

class Tui(var controller: CubicController) extends Reactor {
  listenTo(controller)
  printTui
  reactions += {
    case e: FieldChanged => printTui
  }
  def update = printTui
  def printTui = {
    drawGame
    println("Enter command: n: new game, q: quit")
  }
  
  def processInputLine(input: String) = {
    var continue = true
    input match {
      case "n" => controller.createGame(Seq("A", "B"), 4)
      case "q" => continue = false
      case _ => {
        input.toList.filter(c => c != ' ').map(c => c.toString.toInt) match {
          case x :: y :: z :: Nil => controller.occupyField(x, y, z)
          case _ => println("False Input")
        }
      }
    }
    continue
  }
  
  def drawGame = {
    val n = controller.boardSize
    val nl = sys.props("line.separator")
    val border = " -" * (n * 2 -1)
    
    def drawLine(accum: String, x: Int, y: Int, z: Int): String = {
      if(x < n) drawLine(accum + getField(x, y, z), x + 1, y, z) else accum + "/" + nl
    }
    
    def drawLayer(accum: String, y: Int, z: Int): String = {
      if(z >= 0) drawLayer(drawLine(accum, 0, y, z), y, z - 1) else accum + border + nl
    }
    
    def drawCube(accum: String, y: Int): String = {
      if(y < n) drawCube(drawLayer(accum, y, n - 1), y + 1) else border + nl + accum
    }
    
    def getField(x: Int, y: Int, z: Int) = {
      val (player, highlight) = controller.field(x, y, z)
      val symbol = if(player != None) "X" else "."
      if(highlight) "[" + symbol + "]" else " " + symbol + " "
    }
    
    println(drawCube("", 0))

  }
   

    
}