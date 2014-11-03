package de.htwg.modprog.cubic.model.impl

import de.htwg.modprog.cubic.model._

case class CubicField(val occupiedBy: Option[Player] = None, val isHighlighted: Boolean = false) extends Field {
  def occupy(p: Player) = if(!isOccupied) copy(occupiedBy = Some(p)) else this
  def highlight = if(!isHighlighted) copy(isHighlighted = true) else this
}