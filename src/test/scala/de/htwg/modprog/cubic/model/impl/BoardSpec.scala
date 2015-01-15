package de.htwg.modprog.cubic.model.impl

import org.specs2.mutable._

class BoardSpec extends Specification {
  
  sequential
  
  "A new Board with size of 2" should {
    val size = 2
    val board = CubicBoard(size)
      " have the correct size" in {
        board.n must be_==(size)
      }
     " correctly calculate the index from coordinates" in {
        board.coordToIndex(0,0,0) must be_==(0)
        board.coordToIndex(1,0,0) must be_==(1)
        board.coordToIndex(0,1,0) must be_==(2)
        board.coordToIndex(1,1,0) must be_==(3)
        board.coordToIndex(0,0,1) must be_==(4)
        board.coordToIndex(1,0,1) must be_==(5)
        board.coordToIndex(0,1,1) must be_==(6)
        board.coordToIndex(1,1,1) must be_==(7)
      }
     " correctly check for valid indices" in {
        board.validCoords(0,0,0) must be_==(true)
        board.validCoords(1,1,1) must be_==(true)
        board.validCoords(2,1,1) must be_==(false)
        board.validCoords(1,2,1) must be_==(false)
        board.validCoords(1,1,2) must be_==(false)
      }
     " be empty" in {
        board.fieldIsOccupied(0,0,0) must be_==(false)
        board.fieldIsOccupied(1,0,0) must be_==(false)
        board.fieldIsOccupied(0,1,0) must be_==(false)
        board.fieldIsOccupied(1,1,0) must be_==(false)
        board.fieldIsOccupied(0,0,1) must be_==(false)
        board.fieldIsOccupied(1,0,1) must be_==(false)
        board.fieldIsOccupied(0,1,1) must be_==(false)
        board.fieldIsOccupied(1,1,1) must be_==(false)
      }
     " not have a winner" in {
        val board2 = board.updateWinnerState
        board2.winner must be_==(None)
      }
     " return a new board when occupying a field" in {
       val player = new CubicPlayer("John Doe")
        val board2 = board.occupyField(0, 0, 0, player)
        board2.field(0, 0, 0).occupiedBy must be_==(Some(player))
      }
     " be solvable in x directions" in {
       val player = new CubicPlayer("John Doe")
       val board2 = board.occupyField(0, 0, 0, player).occupyField(1, 0, 0, player).updateWinnerState
       val board3 = board.occupyField(0, 0, 1, player).occupyField(1, 0, 1, player).updateWinnerState
       val board4 = board.occupyField(0, 1, 0, player).occupyField(1, 1, 0, player).updateWinnerState
       val board5 = board.occupyField(0, 1, 1, player).occupyField(1, 1, 1, player).updateWinnerState
       board2.winner must be_==(Some(player))
       board3.winner must be_==(Some(player))
       board4.winner must be_==(Some(player))
       board5.winner must be_==(Some(player))
      }
     " be solvable in y directions" in {
       val player = new CubicPlayer("John Doe")
       val board2 = board.occupyField(0, 0, 0, player).occupyField(0, 1, 0, player).updateWinnerState
       val board3 = board.occupyField(1, 0, 0, player).occupyField(1, 1, 0, player).updateWinnerState
       val board4 = board.occupyField(0, 0, 1, player).occupyField(0, 1, 1, player).updateWinnerState
       val board5 = board.occupyField(1, 0, 0, player).occupyField(1, 1, 1, player).updateWinnerState
       board2.winner must be_==(Some(player))
       board3.winner must be_==(Some(player))
       board4.winner must be_==(Some(player))
       board5.winner must be_==(Some(player))
      }
     " be solvable in z directions" in {
       val player = new CubicPlayer("John Doe")
       val board2 = board.occupyField(0, 0, 0, player).occupyField(0, 0, 1, player).updateWinnerState
       val board3 = board.occupyField(1, 0, 0, player).occupyField(1, 0, 1, player).updateWinnerState
       val board4 = board.occupyField(0, 1, 0, player).occupyField(0, 1, 1, player).updateWinnerState
       val board5 = board.occupyField(1, 1, 0, player).occupyField(1, 1, 1, player).updateWinnerState
       board2.winner must be_==(Some(player))
       board3.winner must be_==(Some(player))
       board4.winner must be_==(Some(player))
       board5.winner must be_==(Some(player))
      }
     " be solvable for all layer diagonals" in {
       val player = new CubicPlayer("John Doe")
       val board2 = board.occupyField(0, 0, 0, player).occupyField(1, 0, 1, player).updateWinnerState
       val board3 = board.occupyField(0, 1, 0, player).occupyField(1, 1, 1, player).updateWinnerState
       val board4 = board.occupyField(1, 0, 0, player).occupyField(0, 0, 1, player).updateWinnerState
       val board5 = board.occupyField(1, 1, 0, player).occupyField(0, 1, 1, player).updateWinnerState
       val board6 = board.occupyField(0, 0, 0, player).occupyField(1, 1, 0, player).updateWinnerState
       val board7 = board.occupyField(0, 0, 1, player).occupyField(1, 1, 1, player).updateWinnerState
       val board8 = board.occupyField(0, 1, 0, player).occupyField(1, 0, 0, player).updateWinnerState
       val board9 = board.occupyField(0, 1, 1, player).occupyField(1, 0, 1, player).updateWinnerState
       val board10 = board.occupyField(0, 1, 0, player).occupyField(0, 0, 1, player).updateWinnerState
       val board11 = board.occupyField(1, 1, 0, player).occupyField(1, 0, 1, player).updateWinnerState
       val board12 = board.occupyField(0, 0, 0, player).occupyField(0, 1, 1, player).updateWinnerState
       val board13 = board.occupyField(1, 0, 0, player).occupyField(1, 1, 1, player).updateWinnerState
       board2.winner must be_==(Some(player))
       board3.winner must be_==(Some(player))
       board4.winner must be_==(Some(player))
       board5.winner must be_==(Some(player))
       board6.winner must be_==(Some(player))
       board7.winner must be_==(Some(player))
       board8.winner must be_==(Some(player))
       board9.winner must be_==(Some(player))
       board10.winner must be_==(Some(player))
       board11.winner must be_==(Some(player))
       board12.winner must be_==(Some(player))
       board13.winner must be_==(Some(player))
      }
      " be solvable for all room diagonals" in {
       val player = new CubicPlayer("John Doe")
       val board2 = board.occupyField(0, 0, 0, player).occupyField(1, 1, 1, player).updateWinnerState
       val board3 = board.occupyField(1, 0, 0, player).occupyField(0, 1, 1, player).updateWinnerState
       val board4 = board.occupyField(0, 0, 1, player).occupyField(1, 1, 0, player).updateWinnerState
       val board5 = board.occupyField(1, 0, 1, player).occupyField(0, 1, 0, player).updateWinnerState
       board2.winner must be_==(Some(player))
       board3.winner must be_==(Some(player))
       board4.winner must be_==(Some(player))
       board5.winner must be_==(Some(player))
      }
      " return a string for worksheet development" in {
       val str = board.toString.count(_ == '.')
       str must be_==(8)
      }
      " work with the for expression" in {
       val fields = for(field <- board; if field.isOccupied) yield field
       fields.size must be_==(0)
      }
  }

}