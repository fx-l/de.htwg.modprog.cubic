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

object Gui extends JFXApp {

  val sphereSize = 50
  val sphereMargin = 200

  stage = new PrimaryStage {
    title = "Cubic"
    scene = new Scene(900, 900, true, SceneAntialiasing.Balanced) {
      root = new BorderPane {
        top = new VBox {
          content = List(
            createMenus())
        }
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
  private def createMenus() = new MenuBar {
    menus = List(
      new Menu("File") {
        items = List(
          new MenuItem("New Game") {
            onAction = {
              e: ActionEvent => println(e.eventType + " New... clicked!")
            }
          },
          new MenuItem("Quit") {
            accelerator = KeyCombination.keyCombination("Ctrl +Q")
            onAction = {
              e: ActionEvent => System.exit(0)
            }
          })
      },
      new Menu("Options") {
        items = List(
          new Menu("Difficulty Level") {
            items = List(
              new MenuItem("Easy"),
              new MenuItem("Hard"))
          })
      })
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
}