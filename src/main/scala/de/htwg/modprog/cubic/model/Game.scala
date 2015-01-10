package de.htwg.modprog.cubic.model

trait Game {
  def size: Int
  def players: Seq[Player]
  def currentPlayer: Player
  def waitingPlayers: Seq[Player]
  def hasWinner: Option[Player]
  def moveCount: Int
  def occupyField(x: Int, y: Int, z: Int): Game
  def field(x: Int, y: Int, z: Int): Field
  def restart: Game
  def create(players: Seq[Player], size: Int): Game
}