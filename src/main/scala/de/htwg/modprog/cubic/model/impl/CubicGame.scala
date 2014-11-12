package de.htwg.modprog.cubic.model.impl

import de.htwg.modprog.cubic.model._

class CubicGame private(val board: Board, val players: List[Player]) extends Game {
  
  val currentPlayer = players.head
  override def size: Int = board.n
  override def isFieldOccupied(x: Int, y: Int, z: Int) = ???
  override def occupyField(x: Int, y: Int, z: Int) = {
    val move = board.occupyField(x, y, z, currentPlayer).updateWinnerState
    val step = players.drop(1) :+ currentPlayer
    new CubicGame(move, step)
  }
  override def field(x: Int, y: Int, z: Int) = board.field(x, y, z)
  
  override def restart = create(players, size)
  override def create(players: List[Player], size: Int) = CubicGame(players, size)
  
  override def toString = "current player: " + currentPlayer + ", all players: " + players
  
  

}

object CubicGame {
  def apply(players: List[Player], n: Int = 4) = {
    require(n > 1)
    require(players.length > 0)
    new CubicGame(CubicBoard(n), players)
  }
  def apply(): Game = {
    apply(List(CubicPlayer("Player 1")), 4)
  }
}