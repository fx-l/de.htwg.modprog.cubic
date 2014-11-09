package de.htwg.modprog.cubic.model.impl

import de.htwg.modprog.cubic.model._

class CubicGame private(val board: Board, val currentPlayer: Player, val otherPlayers: List[Player]) {
  
  def proceed = {
    val others = otherPlayers :+ currentPlayer
    new CubicGame(board, others.head, others.tail)
  }
  
  override def toString = "current player: " + currentPlayer + ", waiting players: " + otherPlayers
  
  

}

object CubicGame {
  def apply(n: Int, players: List[Player]) = {
    require(n > 1)
    require(players.length > 1)
    new CubicGame(CubicBoard(n), players.head, players.tail)
  }
}