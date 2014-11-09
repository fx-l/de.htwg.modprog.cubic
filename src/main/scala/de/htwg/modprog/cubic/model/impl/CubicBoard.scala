package de.htwg.modprog.cubic.model.impl

import scala.annotation.tailrec
import de.htwg.modprog.cubic.model._
import de.htwg.modprog.cubic.model.impl.CubicBoard._

class CubicBoard private(
    val cube: IndexedSeq[Field],
    val winningLines: List[Seq[Int]],
    val hasWinner: Option[Player])
extends Board {
  val n = math.cbrt(cube.length).toInt
  def coordToIndex = transformCoords(n) _
  override def field(x: Int, y: Int, z: Int) = cube(coordToIndex(x, y, z))
  override def fieldIsOccupied(x: Int, y: Int, z: Int) = !field(x, y, z).isOccupied
  override def toString() = "board: " + n + ("x" + n) * 2 + ", winner: " + hasWinner + ", content: " + cube
  // occupy a field
  override def occupyField(x: Int, y: Int, z: Int, p: Player) = {
    val i = coordToIndex(x, y, z)
    if(cube(i).isOccupied) this else new CubicBoard(cube.updated(i, cube(i).occupy(p)), winningLines, hasWinner)
  }
  // in case of available winner, involved line gets highlighted and player is set
  override def updateWinnerState = isSolved(winningLines) match {
    case Some(at: Seq[Int]) => new CubicBoard(highlightFields(at, cube), winningLines, cube(at(0)).occupiedBy)
    case _ => this
  }
  // check if the game is solved by recursively evaluating winning lines
  private def isSolved(lines: List[Seq[Int]]): Option[Seq[Int]] = lines match {
    case line :: tail => if(lineComplete(line)) Some(line) else isSolved(tail)
    case Nil => None
  }  
  // check if a specific winning line is occupied by the same player
  private def lineComplete(line: Seq[Int]) = {
    val proto = cube(line.head)
    if(proto.isOccupied) line.forall(cube(_).occupiedBy == proto.occupiedBy) else false
  }
  // recursively highlight a set of fields in the cube
  private def highlightFields(f: Seq[Int], cube: IndexedSeq[Field]): IndexedSeq[Field] = {
    if(!f.isEmpty) highlightFields(f.tail, cube.updated(f.head, cube(f.head).highlight)) else cube
  }
}

object CubicBoard {
  def transformCoords(n: Int)(x: Int, y: Int, z: Int) = x + y * n + z * math.pow(n, 2).toInt
  def apply(sideLength: Int) = {
    require(sideLength > 1)
    val cube = Vector.fill(math.pow(sideLength, 3).toInt)(CubicField())
    val winningCoords = determineWinningCoords(sideLength)
    new CubicBoard(cube, winningCoords.toList, None)
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
    val winningLinesGenericDescription = Seq(
        ((0,0,0), List((0,1,0),(0,0,1),(1,0,0))), 		// lineset along x axis
        ((0,0,0), List((1,0,0),(0,0,1),(0,1,0))), 		// ... y axis
        ((0,0,0), List((1,0,0),(0,1,0),(0,0,1))), 		// ... z axis
        ((0,0,0), List((1,0,0),(0,1,1))),				// diagonalset #1 along x axis
        ((0,0,max), List((1,0,0),(0,1,-1))),				// ... #2
        ((0,0,0), List((0,1,0),(1,0,1))),				// diagonalset #1 along y axis
        ((0,0,max), List((0,1,0),(1,0,-1))),			// ... #2
        ((0,0,0), List((0,0,1),(1,1,0))),				// diagonalset #1 along z axis
        ((max,0,0), List((0,0,1),(-1,1,0))),			// ... #2
        ((0,0,0), List((1,1,1))),						// space diagonal #1
        ((0,0,max), List((1,1,-1))),					// ... #2
        ((max,0,max), List((-1,1,-1))),					// ... #3
        ((max,0,0), List((-1,1,1)))						// ... #4
    )
    val result = (for((base, trans) <- winningLinesGenericDescription) yield span(n, Seq(base), trans)).flatten
    (for((x, y, z) <- result) yield(transformCoords(n)(x, y, z))).grouped(n)
  }
}