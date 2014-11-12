package de.htwg.modprog.cubic.model

trait Game {
  def size: Int
  def currentPlayer: Player
  def isFieldOccupied(x: Int, y: Int, z: Int): Boolean
  def occupyField(x: Int, y: Int, z: Int): Game
  def field(x: Int, y: Int, z: Int): Field
  def restart: Game
  def create(players: List[Player], size: Int): Game
}