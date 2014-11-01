package de.htwg.modprog.cubic.model

trait Board {
  def field(x: Int, y: Int, z: Int): Int
  def fieldIsEmpty(x: Int, y: Int, z: Int): Boolean
  def hasWinner: Boolean
  def insertCoin(x: Int, y: Int, z: Int, value: Int): Board
}