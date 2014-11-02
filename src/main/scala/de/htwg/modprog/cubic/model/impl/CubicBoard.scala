package de.htwg.modprog.cubic.model.impl

import scala.annotation.tailrec
import de.htwg.modprog.cubic.model.Board
import de.htwg.modprog.cubic.model.impl.CubicBoard._

class CubicBoard private(val cube: Vector[Int], val winningLines: Vector[Int]) extends Board {
  val n = math.cbrt(cube.length).toInt
  def coordToIndex = transformCoords(n) _
  override def field(x: Int, y: Int, z: Int) = cube(coordToIndex(x, y, z))
  override def fieldIsEmpty(x: Int, y: Int, z: Int) = field(x, y, z) == 0
  override def toString() = "board: " + n + ("x" + n) * 2 + ", content: " + cube
  override def insertCoin(x: Int, y: Int, z: Int, value: Int) = {
    new CubicBoard(cube.updated(coordToIndex(x, y, z), value), winningLines)
  }
  override def hasWinner = {
    ???
  }
  def lineComplete(line: Vector[Int]) = line.forall(cube(_) == 1) || line.forall(cube(_) == 2)
}

object CubicBoard {
  def transformCoords(n: Int)(x: Int, y: Int, z: Int) = x + y * n + z * math.pow(n, 2).toInt
  def apply(sideLength: Int) = {
    require(sideLength > 1)
    val cube = Vector.fill(math.pow(sideLength, 3).toInt)(0)
    val winningCoords = determineWinningCoords(sideLength)
    new CubicBoard(cube, winningCoords)
  }
  def spanLines(n: Int, base: Seq[(Int, Int, Int)], to: (Int, Int, Int)) = {
    for ((x, y, z) <- base; i <- (0 until n)) yield (x + to._1  * i, y + to._2 * i, z + to._3 * i)
  }
  @tailrec
  def span(n: Int, baseLines: Seq[(Int, Int, Int)], dirs: List[(Int, Int, Int)]): Seq[(Int, Int, Int)] = {
    dirs match {
      case Nil => baseLines
      case d :: tail => span(n, spanLines(n, baseLines, d), tail)
    }	  
  }
  def determineWinningCoords(n: Int) = {
    val max = n - 1
    val winningLinesGenericDescription = Vector(
        ((0,0,0), List((0,1,0),(0,0,1),(1,0,0))), 		// lineset along x axis
        ((0,0,0), List((1,0,0),(0,0,1),(0,1,0))), 		// ... y axis
        ((0,0,0), List((1,0,0),(0,1,0),(0,0,1))), 		// ... z axis
        ((0,0,0), List((1,0,0),(0,1,1))),				// diagonalset #1 along x axis
        ((0,0,max), List((1,0,0),(0,1,1))),				// ... #2
        ((0,0,0), List((0,1,0),(1,0,1))),				// diagonalset #1 along y axis
        ((0,0,max), List((0,1,0),(1,0,-1))),			// ... #2
        ((0,0,0), List((0,0,1),(1,1,0))),				// diagonalset #1 along z axis
        ((max,0,0), List((0,0,1),(-1,1,0))),			// ... #2
        ((0,0,0), List((1,1,1))),						// space diagonal #1
        ((0,0,max), List((1,1,-1))),					// ... #2
        ((max,0,max), List((-1,1,-1))),					// ... #3
        ((max,0,0), List((-1,1,1)))						// ... #4
    )
    val result = (for((base, trans) <- winningLinesGenericDescription) yield span(n, Vector(base), trans)).flatten
    for((x, y, z) <- result) yield(transformCoords(n)(x, y, z))
  }
}