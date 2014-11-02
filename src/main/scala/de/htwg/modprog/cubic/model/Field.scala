package de.htwg.modprog.cubic.model

trait Field {
  def occupiedBy: Option[Player]
  def isHighlighted: Boolean
  def isOccupied = occupiedBy != None
}