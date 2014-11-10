package de.htwg.modprog.cubic.model

trait Game {
  def sideLength: Int
  def currentPlayer: String
  def isOccupied(x: Int, y: Int, z: Int): Boolean
  def occupy(x: Int, y: Int, z: Int): Game
  def field(x: Int, y: Int, z: Int): (Option[String], Boolean)
  //def restart: Game
}