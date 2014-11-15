package de.htwg.modprog.cubic.controller

import scala.swing.Publisher
import scala.swing.event.Event
import de.htwg.modprog.cubic.model.Game
import de.htwg.modprog.cubic.model.Player
import de.htwg.modprog.cubic.model.impl.CubicPlayer
import de.htwg.modprog.cubic.model.impl.CubicGame

case class FieldChanged() extends Event

class CubicController(var game: Game) extends Publisher {
  var statusText = ""
  def boardSize = game.size
  def currentPlayer = game.currentPlayer.toString
  def moveCount = game.moveCount
  def createGame(playerIdentifiers: Seq[String], size: Int = 4) = {
    val players = playerIdentifiers.map(CubicPlayer(_)).toList
    game = game.create(players, size)
    publish(FieldChanged())
  }
  def occupyField(x: Int, y: Int, z: Int) = {
    game = game.occupyField(x, y, z)
    publish(FieldChanged())
  }
  def restart = {
    game = game.restart
    publish(FieldChanged())
  }
  def reset = ???
  def isFieldOccupied(x: Int, y: Int, z: Int) = game.isFieldOccupied(x, y, z)
  def field(x: Int, y: Int, z: Int) = {
    val f = game.field(x, y, z)
    f.occupiedBy match {
      case Some(p: Player) => (Some(p.name), f.isHighlighted)
      case _ => (None, f.isHighlighted)
    }
  }

} 