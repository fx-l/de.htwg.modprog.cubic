package de.htwg.modprog.cubic.model.impl

import org.specs2.mutable._

class PlayerSpec extends Specification {
  
  "A new Player" should {
    val name = "John Doe"
    val player = CubicPlayer(name)
      "have its assigned name" in {
        player.name must be_==(name)
      }
      "generate a string from its name" in {
        player.toString.contains(name) must be_==(true)
      }
    }
  
  "A Player" should {
    val name1 = "John Doe"
    val name2 = "Jane Doe"
    val player1 = CubicPlayer(name1)
      "correctly rename" in {
        val player2 = player1.rename(name2)
        player1.name must be_==(name1)
        player2.name must be_==(name2)
      }
    }

}