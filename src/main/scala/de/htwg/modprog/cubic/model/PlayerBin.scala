package de.htwg.modprog.cubic.model

trait PlayerBin {
  def players: List[Player]
  def current: Player
  def waiting: List[Player]
  def playerCount: Int
  def iterate: PlayerBin
}