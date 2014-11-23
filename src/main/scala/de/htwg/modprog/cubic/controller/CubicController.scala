package de.htwg.modprog.cubic.controller

import scala.swing.Publisher
import scala.swing.event.Event
import de.htwg.modprog.cubic.model.Game
import de.htwg.modprog.cubic.model.Player
import de.htwg.modprog.cubic.model.impl.CubicPlayer
import de.htwg.modprog.cubic.model.impl.CubicGame

case class GameCreated() extends Event
case class FieldChanged() extends Event

class CubicController(var game: Game) extends Publisher {
  var statusText = ""
  def boardSize = game.size
  def currentPlayer = (game.currentPlayer.name, game.currentPlayer.isCpu)
  def players = game.players.map(p => (p.name, p.isCpu))
  def moveCount = game.moveCount
  def hasWinner = game.hasWinner != None
  def createQuickVersusGame = createCustomGame(Seq(("Player 1", false),("Player 2", false)), 4)
  def createQuickCpuGame = createCustomGame(Seq(("Player", false),("", true)), 4)
  def createCustomGame(players: Seq[(String, Boolean)], size: Int) = {
    val cubicPlayers = players.map {
      case(name, isCpu) => CubicPlayer(name, isCpu)
    }
    game = game.create(cubicPlayers, size)
    statusText = "A new game was created"
    publish(GameCreated())
  }
  def occupyField(x: Int, y: Int, z: Int) = {
    val moveCount = game.moveCount
    game = game.occupyField(x, y, z)
    statusText = game.hasWinner match {
      case Some(p) => p + " has won the game!"
      case None if moveCount == game.moveCount => "Field not available, try again"
      case _ => ""
    }
    publish(FieldChanged())
  }
  def restart = {
    game = game.restart
    statusText = "Game was restarted with current settings"
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