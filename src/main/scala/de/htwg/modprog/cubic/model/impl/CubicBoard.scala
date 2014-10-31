package de.htwg.modprog.cubic.model.impl

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
  
  def lines = {
    val alongLayer = (for {
      x <- (0 until sideLength)
      y <- (0 until sideLength)
      z <- (0 until sideLength) 
      }	yield (coordToIndex(x, y, z), coordToIndex(y, z, x), coordToIndex(z, x, y))).unzip3
      alongLayer._1 ++ alongLayer._2 ++ alongLayer._3
  }
  
  def spaceDiagonals = {
    val max = sideLength - 1
    spanDiagonals(Vector(((0,0,0), (1,1,1)),
    					((0,0,max), (1,1,-1)),
    					((max,0,max), (-1,1,-1)),
    					((max,0,0), (-1,1,1))))
  }
  
  def spanDiagonals(transformations: Seq[((Int,Int,Int),(Int,Int,Int))]) = {
    transformations.map {
      case (a,b) => (a._1 + b._1 * sideLength, a._2 + b._2 * sideLength, a._3 + b._3 * sideLength)
    }
  }
  

  

    
}