package de.htwg.modprog.cubic.impl

import scala.util.control.TailCalls.TailRec
import scala.annotation.tailrec

class CubicBoard(cube: Vector[Int]) {
  val sideLength = math.cbrt(cube.length).toInt
  require(cube.length == math.pow(sideLength, 3).toInt)
  def coordToIndex(x: Int, y: Int, z: Int) = x + y * sideLength + z * math.pow(sideLength, 2).toInt
  def field(x: Int, y: Int, z: Int) = cube(coordToIndex(x, y, z))
  def fieldIsEmpty(x: Int, y: Int, z: Int) = field(x, y, z) == 0
  def insertCoin(x: Int, y: Int, z: Int, value: Int) = new CubicBoard(cube.updated(coordToIndex(x, y, z), value))
  override def toString() = "board: " + sideLength + ("x" + sideLength) * 2 + ", content: " + cube
  
  // val winningLines = Vector(1)
  //def hasWinner = browseLines(winningLines)
  
  def lineComplete(line: Vector[Int]) = line.forall(cube(_) == 1) || line.forall(cube(_) == 2)
  
    
}