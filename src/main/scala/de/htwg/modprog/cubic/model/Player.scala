package de.htwg.modprog.cubic.model

trait Player {
  def name: String
  def isCpu: Boolean
  def rename(name: String): Player
}