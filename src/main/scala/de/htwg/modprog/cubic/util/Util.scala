package de.htwg.modprog.cubic.util

import de.htwg.modprog.cubic.model._

object Util {
  
  def assignSymbols[T](names: List[String], sym: Seq[T], d: T, m: Map[String, T]): Map[String, T] = {
    names match {
      case name :: tail => { 
        val symbol = if(!sym.isEmpty) sym.take(1)(0) else d // d (default) = symbols depleted
        assignSymbols(tail, sym.drop(1), d, m + (name -> symbol))
      }
      case Nil => m
    }
  }
  
  def board2Text(b: Board) = {
    val nl = sys.props("line.separator")
    val n = b.n
    def line(acc: String, x: Int, y: Int, z: Int): String = {
      if(x < n) line(acc + getField(x, y, z), x + 1, y, z) else acc + nl
    }
    def layer(acc: String, y: Int, z: Int): String = {
      if(z >= 0) layer(line(acc + " ", 0, y, z), y, z - 1) else acc + nl
    }
    def cube(acc: String, y: Int): String = {
      if(y < n) cube(layer(acc, y, n - 1), y + 1) else acc
    }
    def getField(x: Int, y: Int, z: Int) = {
      val field: Field = b.field(x, y, z)
      val symbol = field.occupiedBy match {
        case Some(p) => p.name.charAt(0).toUpper
        case None => "."
      }
      if(field.isHighlighted) "[" + symbol + "]" else " " + symbol + " "
      
    }
    nl * 2 + cube("", 0)
  }

}