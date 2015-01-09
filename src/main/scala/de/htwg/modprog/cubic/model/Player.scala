package de.htwg.modprog.cubic.model

trait Player {
  def name: String
  def rename(name: String): Player
}