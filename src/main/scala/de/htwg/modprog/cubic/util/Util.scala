package de.htwg.modprog.cubic.util

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

}