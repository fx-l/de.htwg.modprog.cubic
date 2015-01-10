package de.htwg.modprog.cubic.model

trait Rotation {
  def players: List[Player]
  def current: Player
  def waiting: List[Player]
  def playerCount: Int
  def rotate: Rotation
  def reset: Rotation
}