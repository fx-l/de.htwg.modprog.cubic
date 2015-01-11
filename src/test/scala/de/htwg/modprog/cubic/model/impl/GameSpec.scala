package de.htwg.modprog.cubic.model.impl

import org.specs2.mutable._

class GameSpec extends Specification {
  
  "A new Game without arguments" should {
    val game = CubicGame()
      " be a standard game with 2 default players and a size of 4" in {
        game.players.size must be_==(2)
        game.size must be_==(4)
      } 
  }
  
  "A new Game with arguments" should {
    val players = Seq(CubicPlayer("John Doe"),CubicPlayer("Jane Doe"))
      " throw an Exception if size is smaller than 2" in {
        CubicGame(players, 1) must throwA[Exception]
      } 
    " throw an Exception if players are less than 2" in {
        CubicGame(players.take(1), 4) must throwA[Exception]
      }
    " be properly initialized for valid arguments" in {
        val game = CubicGame(players, 4)
        game.players.size must be_==(2)
        game.currentPlayer must be_==(players(0))
        game.size must be_==(4)
      } 
  }
  
  "A correctly initialized game" should {
    val players = Seq(CubicPlayer("John Doe"),CubicPlayer("Jane Doe"))
    val game = CubicGame(players, 4)
      " make proper use of the player rotation class" in {
        // please note: the rotation class itself is tested in a separate test
        game.currentPlayer must be_==(players(0))
        val game2 = game.occupyField(0, 0, 0)
        game2.currentPlayer must be_==(players(1))
      } 
    " make proper use of the board class" in {
        // please note: the board class itself is tested in a separate test
        game.field(0, 0, 0).isOccupied must be_==(false) 
        val game2 = game.occupyField(0, 0, 0)
        game2.field(0, 0, 0).isOccupied must be_==(true)
        game2.moveCount must be_==(game.moveCount + 1)
        val game3 = game.occupyField(0, 0, 0) // again
        game3.moveCount must be_==(game2.moveCount)
        val game4 = game3.occupyField(0, 1, 0).occupyField(1, 0, 0)
        game4.winner must be_==(None)
        val game5 = game4.occupyField(0, 2, 0).occupyField(2, 0, 0)
        game5.winner must be_==(None)
        val game6 = game5.occupyField(0, 3, 0).occupyField(3, 0, 0)
        game6.winner must be_==(Some(CubicPlayer("John Doe")))
      } 
    " restart correctly" in {
        val game2 = game.occupyField(0, 0, 0)
        val game3 = game2.restart
        game3.field(0, 0, 0).isOccupied must be_==(false)
        game3.size must be_==(game.size)
        game3.players.size must be_==(game.players.size)
        game3.currentPlayer must be_==(game.currentPlayer)
      }
    " create a new game correctly" in {
        val game2 = game.occupyField(0, 0, 0)
        val game3 = game2.create(players :+ CubicPlayer("Another one"), 6)
        game3.field(0, 0, 0).isOccupied must be_==(false)
        game3.size must be_==(6)
        game3.players.size must be_==(players.size + 1)
        game3.currentPlayer must be_==(game.currentPlayer)
      }
    
  }

}