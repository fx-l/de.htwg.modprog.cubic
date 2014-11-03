package de.htwg.modprog.cubic.model

trait Field {
  def occupiedBy: Option[Player]
  def occupy(player: Player): Field
  def isOccupied = occupiedBy != None
  def isHighlighted: Boolean
  def highlight: Field
}