package de.htwg.modprog.cubic.model.impl

import org.specs2.mutable._

class RotationSpec extends Specification {
  
  "A new rotation" should {
    val players = Seq(CubicPlayer("John Doe"),CubicPlayer("John Doe"),CubicPlayer("Jane Doe"),CubicPlayer("Jim Doe"))
    val rot = CubicRotation(players)
      "have the correct amount of players" in {
        rot.playerCount must be_==(players.size)
      }
      "have the correct order" in {
        rot.current must be_==(players(0))
        rot.waiting must be_==(players.drop(1))
      }
      "rename correctly" in {
        rot.current.name must be_!=(rot.waiting(0))
      }
    }
  
  "A changed rotation" should {
    val players = Seq(CubicPlayer("John Doe"),CubicPlayer("Jane Doe"),CubicPlayer("Jim Doe"))
    val rot = CubicRotation(players).rotate.rotate
      "have the correct amount of players" in {
        rot.playerCount must be_==(players.size)
      }
      "have the correct order" in {
        rot.current must be_==(players(2))
        rot.waiting must be_==(players.dropRight(1))
      }
      "be in the correct order after a reset" in {
        val rotReset = rot.reset
        rotReset.current must be_==(players(0))
        rotReset.waiting must be_==(players.drop(1))
      }
    }
  
  

}