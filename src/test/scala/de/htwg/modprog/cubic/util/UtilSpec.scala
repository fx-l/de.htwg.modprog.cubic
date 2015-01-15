package de.htwg.modprog.cubic.util

import org.specs2.mutable._
import de.htwg.modprog.cubic.model.impl._


class UtilSpec extends Specification {
  
  "Util's assign symbols should " should {
    val playerNames = List("A","B", "C", "D")
    val symbols = Seq("1","2","3")
    var symbolMapping = Map[String, String]()
      "assign symbols correctly" in {
        symbolMapping = Util.assignSymbols(playerNames, symbols, "X", symbolMapping)
        symbolMapping("A") must be_==("1")
        symbolMapping("B") must be_==("2")
        symbolMapping("C") must be_==("3")
        symbolMapping("D") must be_==("X")
      }
      
    }
  
  "Util's board2Text " should {
    val player = CubicPlayer("A")
    val board = CubicBoard(2)
      "return a board representation" in {
        val c = Util.board2Text(board).count(_ == '.')
        println(Util.board2Text(board))
        c must be_==(8)
      }
      "mark winning line" in {
        val board2 = board.occupyField(0, 0, 0, player).occupyField(1, 0, 0, player).updateWinnerState
        Util.board2Text(board2).contains("[") must be_==(true)
      }
      
    }
  
}