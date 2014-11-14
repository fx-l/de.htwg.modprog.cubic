package de.htwg.modprog.cubic.model.impl

import de.htwg.modprog.cubic.model.Player

case class CubicPlayer(val identifier: String) extends Player {
  override def toString = identifier
}