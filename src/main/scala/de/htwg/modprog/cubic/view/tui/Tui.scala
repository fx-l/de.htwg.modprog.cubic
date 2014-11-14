package de.htwg.modprog.cubic.view.tui

import de.htwg.modprog.cubic.controller.CubicController
import scala.swing.Reactor
import de.htwg.modprog.cubic.controller.CubicController
import de.htwg.modprog.cubic.controller.CubicController
import de.htwg.modprog.cubic.controller.FieldChanged
import java.io.File
import scala.io.Source._

class Tui(var controller: CubicController) extends Reactor {
  
  def n = controller.boardSize
  val nl = sys.props("line.separator") // new line
  val b = "-" * (n * 3 + 1) // border
  val s = 10 // spacer
    
  listenTo(controller)
  //println(fromFile("scala/main/resources/tui_intro_template.txt").mkString)
  printTui
  reactions += {
    case e: FieldChanged => printTui
  }
  def update = printTui
  def printTui = {
    drawHud
    drawGame
    drawCommands
    drawCommands2
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
  
  def drawHud = println("turn: " + controller.currentPlayer + ", move: " + controller.moveCount)
  def drawCommands = println("Enter command: n: new game, q: quit")
  def drawCommands2 = println("Enter xyz, where each is in the range 0 to " + (controller.boardSize - 1))
  
  def drawGame = {
    def line(acc: String, x: Int, y: Int, z: Int): String = {
      if(x < n) line(acc + getField(x, y, z), x + 1, y, z) else acc + "/" + nl
    }
    def layer(acc: String, y: Int, z: Int): String = {
      if(z >= 0) layer(line(acc + " " * (z + s) + "/", 0, y, z), y, z - 1) else acc + " " * s + b + nl
    }
    def cube(acc: String, y: Int): String = {
      if(y < n) cube(layer(acc + " " * (s + n) + b + nl, y, n - 1), y + 1) else acc
    }
    def getField(x: Int, y: Int, z: Int) = {
      val (player, highlight) = controller.field(x, y, z)
      val symbol = if(player != None) "X" else "."
      if(highlight) "[" + symbol + "]" else " " + symbol + " "
    }
    println(cube("", 0))
  }
  
}