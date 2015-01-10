package de.htwg.modprog.cubic.model.impl

import de.htwg.modprog.cubic.model._

class CubicGame private(val board: Board, val rot: Rotation) extends Game {
  override val currentPlayer = rot.current
  override val waitingPlayers = rot.waiting
  override val players = rot.players
  override val hasWinner = board.hasWinner
  override def size: Int = board.n
  override def moveCount = board.moves
  override def field(x: Int, y: Int, z: Int) = board.field(x, y, z)
  override def restart = new CubicGame(CubicBoard(size), rot.reset)
  override def create(players: Seq[Player], size: Int) = CubicGame(players, size)
  override def toString = "move: " + moveCount + ", current player: " + currentPlayer + ", others: " + waitingPlayers
  override def occupyField(x: Int, y: Int, z: Int) = {
    val nextBoard = board.occupyField(x, y, z, currentPlayer).updateWinnerState
    if(nextBoard.moves > board.moves) new CubicGame(nextBoard, rot.rotate) else this
  }
}

object CubicGame {
  def apply(players: Seq[Player], n: Int) = {
    require(n > 1)
    require(players.length > 0)
    new CubicGame(CubicBoard(n), CubicRotation(players))
  }
  def apply(): Game = {
    apply(List(CubicPlayer("Player 1"), CubicPlayer("Player 2")), 4)
  }
}