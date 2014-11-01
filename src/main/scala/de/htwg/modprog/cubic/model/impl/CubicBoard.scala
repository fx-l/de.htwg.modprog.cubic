package de.htwg.modprog.cubic.model.impl

import scala.annotation.tailrec

class CubicBoard(cube: Vector[Int]) {
  val sideLength = math.cbrt(cube.length).toInt
  require(cube.length == math.pow(sideLength, 3).toInt)
  val winningLines = determineWinningLines
  def coordToIndex(x: Int, y: Int, z: Int) = x + y * sideLength + z * math.pow(sideLength, 2).toInt
  def field(x: Int, y: Int, z: Int) = cube(coordToIndex(x, y, z))
  def fieldIsEmpty(x: Int, y: Int, z: Int) = field(x, y, z) == 0
  def insertCoin(x: Int, y: Int, z: Int, value: Int) = new CubicBoard(cube.updated(coordToIndex(x, y, z), value))
  override def toString() = "board: " + sideLength + ("x" + sideLength) * 2 + ", content: " + cube
  
  
  //def hasWinner = browseLines(winningLines)
  
  def lineComplete(line: Vector[Int]) = line.forall(cube(_) == 1) || line.forall(cube(_) == 2)
  
  def determineWinningLines = {
    val max = sideLength - 1
    val winningLinesDescription = Vector(
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
    val result = for((base, trans) <- winningLinesDescription) yield span(Vector(base), trans)
    result.flatten
  }
     
  def spanLines(base: Seq[(Int, Int, Int)], direction: (Int, Int, Int)) = {
    val (vX, vY, vZ) = direction
    for {
      (x, y, z) <- base 
      i <- (0 until sideLength)
    } yield (x + vX * i, y + vY * i, z + vZ * i)
  }
  
  //@tailrec
  def span(baseLines: Seq[(Int, Int, Int)], dirs: List[(Int, Int, Int)]): Seq[(Int, Int, Int)] = {
    dirs match {
      case Nil => baseLines
      case d :: tail => span(spanLines(baseLines, d), tail)
    }	  
  }
   
}