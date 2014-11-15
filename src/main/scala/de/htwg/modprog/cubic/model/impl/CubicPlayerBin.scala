package de.htwg.modprog.cubic.model.impl

import de.htwg.modprog.cubic.model.Player
import de.htwg.modprog.cubic.model.PlayerBin

class CubicPlayerBin private(val players: List[Player]) extends PlayerBin {
  val current = players.head
  val waiting = players.tail
  def iterate = new CubicPlayerBin(waiting :+ current)
  def playerCount = players.length
}

object CubicPlayerBin {
  def apply(players: List[Player]) = {
    require(players.length > 0)
    val namedPlayers = players.map(defaultName(_))
    new CubicPlayerBin(disambiguate(namedPlayers))
  }
  def defaultName(p: Player) = (p.name, p.isCpu) match {
      case (name, _) if !name.trim.isEmpty => p
      case (_, isCpu) => if(isCpu) p.rename("CPU") else p.rename("Player")
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