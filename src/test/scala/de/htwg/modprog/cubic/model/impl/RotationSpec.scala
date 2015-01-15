package de.htwg.modprog.cubic.model.impl

import org.specs2.mutable._

class RotationSpec extends Specification {
  
  "A new rotation" should {
    val players1 = Seq(CubicPlayer("John Doe"),CubicPlayer("Jane Doe"),CubicPlayer("John Doe"),CubicPlayer("Jim Doe"),CubicPlayer(""))
    val players2 = Seq(CubicPlayer("John Doe"),CubicPlayer("Jane Doe"),CubicPlayer("Jermain Doe"),CubicPlayer("Jim Doe"))
    val rot1 = CubicRotation(players1)
    val rot2 = CubicRotation(players2)
      "have the correct amount of players" in {
        rot1.playerCount must be_==(players1.size)
      }
      "have the correct order" in {
        rot2.current must be_==(players2(0))
        rot2.waiting must be_==(players2.drop(1))
      }
      "rename correctly" in {
        rot1.current.name must be_!=(rot1.waiting(1))
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