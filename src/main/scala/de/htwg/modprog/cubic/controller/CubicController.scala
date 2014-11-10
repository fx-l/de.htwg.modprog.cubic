package de.htwg.modprog.cubic.controller

import scala.swing.Publisher
import de.htwg.modprog.cubic.model.Game

class CubicController(var game: Game) extends Publisher {
  var statusText = ""
  def sideLength = game.sideLength
  def isOccupied(x: Int, y: Int, z: Int) = game.isOccupied(x, y, z)
  def field(x: Int, y: Int, z: Int): (Option[String], Boolean) = (None, false)

} 