package de.htwg.modprog.cubic.view.tui

import de.htwg.modprog.cubic.controller.CubicController
import scala.swing.Reactor
import de.htwg.modprog.cubic.controller.CubicController
import de.htwg.modprog.cubic.controller.FieldChanged
import java.io.File
import scala.io.Source._
import de.htwg.modprog.cubic.controller.GameCreated
import scala.util.Try

class Tui(var controller: CubicController) extends Reactor {
  
  listenTo(controller)
  
  def n = controller.boardSize
  val nl = sys.props("line.separator") // new line
  def b = "-" * (n * 3 + 1) // border
  val s = 10 // spacer
  val defaultGameSize = 4
  val defaultNamePrefix = "Player"
  val intro = loadString("/tui_intro.txt")
  val menu = loadString("/tui_menu.txt")
  val symbols = Seq('X', 'O', 'T', 'Z', 'H')
  var symbolMapping = Map[String, Char]()
  
  reactions += {
    case e: GameCreated => onGameCreated
    case e: FieldChanged => onGameUpdated
  }
  
  def processInputLine(input: String) = {
    var continue = true
    input match {
      case "a" => controller.createQuickVersusGame
      case "b" => controller.createQuickCpuGame
      case "c" => controller.createCustomGame(askForPlayers(2, Nil), askForSize(2 to 10))
      case "r" => controller.restart
      case "q" => continue = false
      case _ => if(!controller.hasWinner) {
        input.toList.filter(c => c != ' ').map(c => c.toString.toInt) match {
          case (x: Int) :: (y: Int) :: (z: Int) :: Nil => controller.occupyField(x - 1, y - 1, z - 1)
          case _ => println("False Input")
        }
      }
    }
    continue
  }
  
  def onGameCreated = {
    val nameList = controller.players.map{ case(name, _) => name }.toList
    symbolMapping = assignSymbols(nameList, symbols, Map[String, Char]())
    onGameUpdated
  }
  
  def onGameUpdated = {
    drawFullUi
  }
  
  def drawHeader = println(intro)
  def drawMenu = println(menu)
  def drawStatus = println(" " + controller.statusText + nl)  
  def drawHud = println(" " + controller.currentPlayer._1  + ", it's your turn!")
  def drawCommands = println(" Enter xyz, where each is in the range from 1 to " + controller.boardSize)
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
      val symbol = player match {
        case Some(name) => symbolMapping(name)
        case None => "."
      }
      if(highlight) "[" + symbol + "]" else " " + symbol + " "
    }
    println(cube("", 0))
  }
  
  def drawFullUi = {
    drawHeader
    drawMenu
    drawGame
    drawStatus
    if(!controller.hasWinner) {
      drawHud
      drawCommands
    }
  }
  
  def askForPlayers(pc: Int, acc: List[(String, Boolean)]): List[(String, Boolean)] = pc match {
    case pc if pc > 0 => {
      println("Setup Player " + (acc.length + 1))
      val name  = ask("Enter name (leave empty for default)")
      val isCpu = ask("Is CPU? (y/n)") == "y"
      askForPlayers(pc - 1, (name, isCpu) :: acc)
    }
    case _ => acc reverse
  }
  
  def askForSize(r: Range): Int = {
    val size = ask("Enter size of board, (" + r + ")")
    Try(size.toInt).toOption match {
      case Some(i: Int) if r.contains(i) => i
      case _ => askForSize(r)
    }
  }
  
  def ask(q: String) = {
    println(q)
    readLine trim
  }
  
  def loadString(path: String) = fromURL(getClass.getResource(path)).mkString
  
  def assignSymbols(names: List[String], sym: Seq[Char], m: Map[String, Char]): Map[String, Char] = {
    names match {
      case name :: tail => { 
        val symbol = if(!sym.isEmpty) sym.take(1)(0) else '?' // ? = symbols depleted
        assignSymbols(tail, sym.drop(1), m + (name -> symbol))
      }
      case Nil => m
    }
    
  }
  
}