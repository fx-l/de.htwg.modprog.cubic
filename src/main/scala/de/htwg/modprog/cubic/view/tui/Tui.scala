package de.htwg.modprog.cubic.view.tui

import scala.io.Source.fromURL
import scala.swing.Reactor
import scala.util.Try

import de.htwg.modprog.cubic.controller.CubicController
import de.htwg.modprog.cubic.controller.FieldChanged
import de.htwg.modprog.cubic.controller.GameCreated

class Tui(var controller: CubicController) extends Reactor {
  
  listenTo(controller)
  
  def n = controller.boardSize
  val nl = sys.props("line.separator") // new line
  def b = "-" * (n * 3 + 1) // border
  val s = 10 // spacer
  val defaultGameSize = 4
  val defaultNamePrefix = "Player"
  val symbols = Seq('X', 'O', 'T', 'H')
  var symbolMapping = Map[String, Char]()
  
  reactions += {
    case e: GameCreated => onGameCreated
    case e: FieldChanged => onGameUpdated
  }
  
  def processInputLine(input: String) = {
    var continue = true
    input match {
      case "q" => controller.createQuickVersusGame
      case "c" => controller.createCustomGame(askForPlayers(), askForSize(2 to 10))
      case "r" => controller.restart
      case "e" => continue = false
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
    val nameList = controller.players.toList
    symbolMapping = assignSymbols(nameList, symbols, Map[String, Char]())
    onGameUpdated
  }
  
  def onGameUpdated = {
    drawFullUi
  }
  
  def drawHeader = println(introString)
  def drawMenu = println(menuString)
  def drawStatus = println(" " + controller.statusText + nl)  
  def drawHud = println(" " + controller.currentPlayer  + ", it's your turn!")
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
  
  def askForPlayers(acc: List[String] = List[String]()): List[String] = {
    println("Setup Player " + (acc.length + 1))
    val name  = ask("Enter name (leave empty for default)")
    val continue = ask("Add another player? (y/n)") == "y"
    if(continue) askForPlayers(name :: acc) else (name :: acc) reverse
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
  
  def assignSymbols(names: List[String], sym: Seq[Char], m: Map[String, Char]): Map[String, Char] = {
    names match {
      case name :: tail => { 
        val symbol = if(!sym.isEmpty) sym.take(1)(0) else '?' // ? = symbols depleted
        assignSymbols(tail, sym.drop(1), m + (name -> symbol))
      }
      case Nil => m
    }
    
  }
  
  val menuString = """
 Menu      - create quick game (2 Players, 4x4x4)  [q]
           - create customized game                [c] 
           - restart with current settings         [r]
           - exit                                  [e]
  """
  
  val introString = """
  =========================================
   _____      _     _        ___    ___  
  / ____|    | |   (_)      |__ \  / _ \ 
 | |    _   _| |__  _  ___     ) || | | |
 | |   | | | | '_ \| |/ __|   / / | | | |
 | |___| |_| | |_) | | (__   / /_ | |_| |
  \_____\__,_|_.__/|_|\___| |____(_)___/  

 =========================================
 """
  
}