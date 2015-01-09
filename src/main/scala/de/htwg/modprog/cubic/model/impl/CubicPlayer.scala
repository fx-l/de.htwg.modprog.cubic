package de.htwg.modprog.cubic.model.impl

import de.htwg.modprog.cubic.model.Player

case class CubicPlayer(val name: String) extends Player {
  override def toString = name
  override def rename(newName: String) = copy(name = newName)
}