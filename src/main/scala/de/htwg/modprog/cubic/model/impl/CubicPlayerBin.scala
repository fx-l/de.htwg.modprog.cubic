package de.htwg.modprog.cubic.model.impl

import de.htwg.modprog.cubic.model.Player
import de.htwg.modprog.cubic.model.PlayerBin

class CubicPlayerBin private(
    val players: List[Player],
    val current: Player,
    val waiting: List[Player]) extends PlayerBin {
  override def rotate = new CubicPlayerBin(players, waiting.head, waiting.drop(1) :+ current)
  override def reset = new CubicPlayerBin(players, players.head, players.tail)
  override def playerCount = players.length
}

object CubicPlayerBin {
  def apply(players: Seq[Player]) = {
    require(players.length > 0)
    val namedPlayers = disambiguate(players.map(defaultName(_)).toList)
    new CubicPlayerBin(namedPlayers, namedPlayers.head, namedPlayers.tail)
  }
  def defaultName(p: Player) = p.name match {
      case name if !name.trim.isEmpty => p
      case _ => p.rename("Player")
  } 
  def disambiguate(ps: List[Player]) = {
    def uniqueness(old: List[Player], unique: List[Player]): List[Player] = old match {
      case name :: tail => uniqueness(tail, rename(name, 1, unique) :: unique)
      case Nil => unique.reverse
    }
    def rename(p: Player, step: Int, unique: List[Player]): Player = {
      val renamed = if(step < 2) p else p.rename(p.name + "(" + step + ")")
      if(unique.forall(_.name != renamed.name)) renamed else rename(p, step + 1, unique)
    }
    uniqueness(ps, Nil)
  }
}