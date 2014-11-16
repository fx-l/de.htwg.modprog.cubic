package de.htwg.modprog.cubic.controller

import scala.swing.Publisher
import scala.swing.event.Event
import de.htwg.modprog.cubic.model.Game
import de.htwg.modprog.cubic.model.Player
import de.htwg.modprog.cubic.model.impl.CubicPlayer
import de.htwg.modprog.cubic.model.impl.CubicGame

case class GameCreated() extends Event
case class FieldChanged() extends Event
case class HasWinner() extends Event

class CubicController(var game: Game) extends Publisher {
  var statusText = ""
  def boardSize = game.size
  def currentPlayer = (game.currentPlayer.name, game.currentPlayer.isCpu)
  def players = game.players.map(p => (p.name, p.isCpu))
  def moveCount = game.moveCount
  
  def createQuickVersusGame(p1Name: String, p2Name: String) = {
    createCustomGame(Seq((p1Name, false),(p2Name, false)), 4)
  }
  
  def createQuickCpuGame(pName: String) = {
    createCustomGame(Seq((pName, false),("", true)), 4)
  }
  
  def createCustomGame(players: Seq[(String, Boolean)], size: Int) = {
    val cubicPlayers = players.map {
      case(name, isCpu) => CubicPlayer(name, isCpu)
    }
    game = game.create(cubicPlayers, size)
    publish(GameCreated())
  }
  
  def occupyField(x: Int, y: Int, z: Int) = {
    game = game.occupyField(x, y, z)
    publish(FieldChanged())
  }
  def restart = {
    game = game.restart
    publish(FieldChanged())
  }
  def isFieldOccupied(x: Int, y: Int, z: Int) = game.isFieldOccupied(x, y, z)
  def field(x: Int, y: Int, z: Int) = {
    val f = game.field(x, y, z)
    f.occupiedBy match {
      case Some(p: Player) => (Some(p.name), f.isHighlighted)
      case _ => (None, f.isHighlighted)
    }
  }

} 