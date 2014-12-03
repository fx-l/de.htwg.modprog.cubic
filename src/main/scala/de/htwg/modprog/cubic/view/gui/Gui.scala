package de.htwg.modprog.cubic.view.gui

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.event.{ Event, ActionEvent }
import scalafx.scene.control.MenuItem._
import scalafx.scene.control._
import scalafx.scene.input.KeyCombination
import scalafx.scene.layout.{ VBox, BorderPane }
import scalafx.scene._
import scalafx.beans.property.DoubleProperty
import scalafx.scene.input.MouseEvent
import scalafx.scene.transform.Rotate
import scalafx.scene.SceneAntialiasing
import scalafx.scene.paint.{ Color, PhongMaterial }
import scalafx.scene.shape.{ Sphere, Box }
import scalafx.geometry.Insets

object Gui extends JFXApp {

  val sphereSize = 50
  val sphereMargin = 200

  stage = new PrimaryStage {
    title = "Cubic"
    scene = new Scene(900, 900, true, SceneAntialiasing.Balanced) {
      root = new BorderPane {
        top = createMenu
        center = createGameWindow
        bottom = createStatusBar
      }
      val shapes = new Group
      shapes.children = createSpheres

      val light = new PointLight {
        color = Color.AntiqueWhite
        translateX = -265
        translateY = -260
        translateZ = -625
      }
      //      root = new Group {
      //        // Put light outside of `shapes` group so it does not rotate
      //        content = new Group(shapes, light)
      //        translateX = 450
      //        translateY = 250
      //        translateZ = 825
      //        rotationAxis = Rotate.YAxis
      //      }

      camera = new PerspectiveCamera(false)

      addMouseInteraction(this, shapes)
    }
  }
  
  
  /** Add mouse interaction to a scene, rotating given node. */
  private def addMouseInteraction(scene: Scene, group: Group) {
    val angleY = DoubleProperty(-50)
    val yRotate = new Rotate {
      angle <== angleY
      axis = Rotate.YAxis
    }
    var anchorX: Double = 0
    var anchorAngleY: Double = 0

    group.transforms = Seq(yRotate)

    scene.onMousePressed = (event: MouseEvent) => {
      anchorAngleY = angleY()
      anchorX = event.sceneX

      // Retrieve information about a pick
      val pickResult = event.pickResult

      // If picked on a Node, place green marker at the location of the pick
      pickResult.intersectedNode match {
        case Some(n) => {
          println("Picked node: '" + n.id() + "'")
          val p = pickResult.intersectedPoint
          group.content += createMarker(x = p.x + n.translateX(), y = p.y + n.translateY(), z = p.z + n.translateZ())
        }
        case None => println("Picked nothing.")
      }
    }

    scene.onMouseDragged = (event: MouseEvent) => {
      angleY() = anchorAngleY + anchorX - event.sceneX
    }

  }

  private def createMarker(x: Double, y: Double, z: Double): Sphere = new Sphere(35) {
    material = new PhongMaterial {
      diffuseColor = Color.Gold
      specularColor = Color.LightGreen
    }
    translateX = x
    translateY = y
    translateZ = z
  }

  def createSpheres = {
    for (x <- 0 until 4; y <- 0 until 4; z <- 0 until 4)
      yield (
      new Sphere(sphereSize) {
        material = new PhongMaterial {
          diffuseColor = Color.Red
          specularColor = Color.Pink
        }
        translateX = x * sphereMargin
        translateY = y * sphereMargin
        translateZ = z * sphereMargin
        id = x + "-" + y + "-" + z
      })
  }
  
  // creategame window
  def createGameWindow = {
    new VBox {
          content = new Label {
            text = "Game will be here"
          }
    }
  }
  
  // a method for creating the status bar
  def createStatusBar = {
    new VBox {
          padding = Insets(14)
          content = new Label {
            text = "Status Messages go here"
            id = "statusLabel"
          }
    }
  }
  
  // a method for building the menu
  def createMenu = new VBox {
          content = List(new MenuBar {
    menus = List(
      new Menu("Game") {
        items = List(new Menu("New Game") {
          items = List(new MenuItem("Player vs. Player") {
            accelerator = KeyCombination.keyCombination("a")
            onAction = {
              e: ActionEvent => println(e.eventType + " Player vs. Player... clicked!")
            }
          }, new MenuItem("Player vs. Computer") {
            accelerator = KeyCombination.keyCombination("b")
            onAction = {
              e: ActionEvent => println(e.eventType + " Player vs. Computer... clicked!")
            }
          }, new MenuItem("Custom Game") {
            accelerator = KeyCombination.keyCombination("c")
            onAction = {
              e: ActionEvent => println(e.eventType + " New... clicked!")
            }
          })
        },
        new MenuItem("Restart") {
          accelerator = KeyCombination.keyCombination("r")
          onAction = {
            e: ActionEvent => println(e.eventType + " Restart... clicked!")
          }
        },
        new MenuItem("Quit") {
          accelerator = KeyCombination.keyCombination("q")
          onAction = {
            e: ActionEvent => System.exit(0)
            }
          }
        )
    },
    new Menu("Info") {
        items = List(new MenuItem("How to play") {
            accelerator = KeyCombination.keyCombination("h")
            onAction = {
             e: ActionEvent => println(e.eventType + " How to play... clicked!")
            }
          }, new MenuItem("About") {
            accelerator = KeyCombination.keyCombination("i")
            onAction = {
              e: ActionEvent => println(e.eventType + " About... clicked!")
            }
          }
          )
      }  
    )
  })
        }
}