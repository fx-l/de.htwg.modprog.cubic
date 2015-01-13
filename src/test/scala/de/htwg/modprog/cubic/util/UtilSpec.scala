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
    val board = CubicBoard(4).occupyField(0, 0, 0, CubicPlayer("bla"))
      "return a board representation" in {
        val txt = Util.board2Text(board)
        txt must be_!=("")
      }
      
    }
  
}