package de.htwg.modprog.cubic.controller

import org.specs2.mutable._
import de.htwg.modprog.cubic.model.impl._

class ControllerSpec extends Specification {
  
  sequential
  
  "A controller with a custom game of 3" should {
    val controller = new CubicController(CubicGame())
    controller.createCustomGame(Seq("A","B"),3)
    
    "have the correct status message" in {
        controller.statusText must be_==("A new game was created.")
    }
    " show the correct size" in {
        controller.boardSize must be_==(3)
    }
    " show the correct current player" in {
        controller.currentPlayer must be_==("A")
    }
    " show the correct players" in {
        controller.players must be_==(Seq("A","B"))
    }
    " not have a winner" in {
        controller.hasWinner must be_==(false)
    }
    " allow a legal move" in {
    	controller.field(0,0,0) must be_==((None,false))
        controller.occupyField(0, 0, 0)
        controller.field(0,0,0) must be_==((Some("A"),false))
        controller.statusText must be_==("")
    }
    " avoid an illegal move" in {
        controller.occupyField(0, 0, 0)
        controller.statusText must be_==("Field not available, try again.")
    }
    " show the winner" in {
        controller.occupyField(0, 0, 1) // p2
        controller.occupyField(1, 0, 0) // p1
        controller.occupyField(0, 0, 2) // p2
        controller.occupyField(2, 0, 0) // p1 -> win
        controller.statusText must be_==("A has won the game!")
    }
    
    " restart correctly" in {
    	controller.restart
        controller.field(0,0,0) must be_==((None,false))
    }
    

    
  }
  
  "A controller with a new verus game" should {
    val controller = new CubicController(CubicGame())
    controller.createQuickVersusGame
    
    "have the correct status message" in {
        controller.statusText must be_==("A new game was created.")
      }
    
  }

}