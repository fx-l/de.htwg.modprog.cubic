package de.htwg.modprog.cubic.model

trait Board {
  def field(x: Int, y: Int, z: Int): Field
  def fieldIsOccupied(x: Int, y: Int, z: Int): Boolean
  def occupyField(x: Int, y: Int, z: Int, p: Player): Board
  def updateWinnerState: Board
  def hasWinner: Option[Player]
}