package de.htwg.modprog.cubic.model.impl

import org.specs2.mutable._

class FieldSpec extends Specification {
  
  "A new empty Field" should {
    val field = CubicField()
      " not be occupied" in {
        field.isOccupied must be_==(false)
        field.occupiedBy must be_==(None)
      } 
     " not be highlighted" in {
        field.isHighlighted must be_==(false)
      } 
  }
  
  "A new occupied Field" should {
    val player = CubicPlayer("John Doe")
    val field = CubicField(Some(player))
      " be occupied" in {
        field.isOccupied must be_==(true)
        field.occupiedBy must be_==(Some(CubicPlayer("John Doe")))
      } 
     " not be highlighted" in {
        field.isHighlighted must be_==(false)
      }
     " able to be highlighted" in {
        val hField = field.highlight
        hField.isHighlighted must be_==(true)
      } 
  }
  
  "A 'winning' Field" should {
    val player = CubicPlayer("John Doe")
    val field = CubicField(Some(player), true)
      " be occupied" in {
        field.isOccupied must be_==(true)
        field.occupiedBy must be_==(Some(CubicPlayer("John Doe")))
      } 
     " be highlighted" in {
        field.isHighlighted must be_==(true)
      } 
  }

}