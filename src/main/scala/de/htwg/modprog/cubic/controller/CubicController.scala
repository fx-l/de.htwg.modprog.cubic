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
  def currentPlayer = game.currentPlayer.name
  def players = game.players.map(p => (p.name))
  def hasWinner = game.winner != None
  def createQuickVersusGame = createCustomGame(Seq("Player 1","Player 2"), 4)
  def createCustomGame(players: Seq[String], size: Int) = {
    val cubicPlayers = players.map(CubicPlayer(_))
    game = game.create(cubicPlayers, size)
    statusText = "A new game was created."
    publish(GameCreated())
  }
  def occupyField(x: Int, y: Int, z: Int) = {
    val moveCount = game.moveCount
    game = game.occupyField(x, y, z)
    statusText = game.winner match {
      case Some(p) => p.name + " has won the game!"
      case None if moveCount == game.moveCount => "Field not available, try again."
      case _ => ""
    }
    publish(FieldChanged())
  }
  def restart = {
    game = game.restart
    statusText = "Game was restarted with current settings."
    publish(FieldChanged())
  }
  def field(x: Int, y: Int, z: Int) = {
    val f = game.field(x, y, z)
    f.occupiedBy match {
      case Some(p: Player) => (Some(p.name), f.isHighlighted)
      case _ => (None, f.isHighlighted)
    }
  }
} 