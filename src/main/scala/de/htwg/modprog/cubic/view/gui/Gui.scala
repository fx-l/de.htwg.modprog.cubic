package de.htwg.modprog.cubic.view.gui

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.event.{ Event, ActionEvent }
import scalafx.scene.control.MenuItem._
import scalafx.scene.control._
import scalafx.scene.input.KeyCombination
import scalafx.scene.layout._
import scalafx.scene._
import scalafx.scene.transform._
import scalafx.beans.property.DoubleProperty
import scalafx.scene.input.MouseEvent
import scalafx.scene.transform.Rotate
import scalafx.scene.SceneAntialiasing
import scalafx.scene.paint.{ Color, PhongMaterial }
import scalafx.scene.shape._
import scalafx.geometry.Insets
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.scene.text.Font
import scalafx.scene.paint.{ Stops, LinearGradient }
import scalafx.scene.text.Text
import scalafx.scene.effect.DropShadow
import scalafx.stage.Stage
import scalafx.geometry.Pos
import scalafx.scene.control.{ ContentDisplay, Label }
import scalafx.collections.ObservableBuffer
import javafx.scene.control.{ToggleButton => JfxToggleBtn}

object Gui extends JFXApp {

  val gameSize = 4
  val sphereSize = 3
  val spacer = 12
  val sideLength = gameSize * sphereSize + (gameSize - 1) * spacer
  val zoomOutFactor = 3
  val horizontalCorrectionFactor = 0.08F;

  val stringsBoardSize = ObservableBuffer(
    "2 x 2 x 2", "3 x 3 x 3", "4 x 4 x 4",
    "5 x 5 x 5", "6 x 6 x 6", "7 x 7 x 7",
    "8 x 8 x 8")

  val togglePl1 = new ToggleGroup {
    selectedToggle.onChange(
      (_, oldValue, newValue) => {
        println("Player 1: " + newValue.asInstanceOf[JfxToggleBtn].getText)
      })
  }
  
  val togglePl2 = new ToggleGroup {
    selectedToggle.onChange(
      (_, oldValue, newValue) => {
        println("Player 2: " + newValue.asInstanceOf[JfxToggleBtn].getText)
      })
  }

  stage = new PrimaryStage {
    title = "Cubic"
    scene = new Scene(900, 900, true, SceneAntialiasing.Balanced) {

      var tmpw = this.width
      var tmph = this.height

      fill = Color.Black

      root = new BorderPane {
        fill = Color.Black
        top = new VBox {
          content = Seq(createMenu, createStatusBar)
        }
        center = createView(tmpw, tmph)
      }
    }
    width onChange show
    height onChange show
  }

  // creategame window
  def createView(boundWidth: ReadOnlyDoubleProperty, boundHeight: ReadOnlyDoubleProperty): BorderPane = {
    new BorderPane {
      center = new SubScene(boundWidth.get(), boundHeight.get(), true, SceneAntialiasing.Balanced) {
        fill = Color.Black

        val spheres = new Group
        spheres.children = createSpheres(gameSize, sphereSize, spacer)
        content = spheres

        camera = new PerspectiveCamera(true) {
          farClip = sideLength + zoomOutFactor * sideLength
          transforms += (
            new Translate(0, sideLength * horizontalCorrectionFactor, -sideLength * zoomOutFactor))
        }
        this.width.bind(boundWidth.add(0))
        this.height.bind(boundHeight.add(0))
        addMouseInteraction(this, spheres)
      }
    }
  }

  // a method for creating the status bar
  def createStatusBar = {
    new VBox {
      padding = Insets(8)
      content = new Text {
        text = "Status messages go here"
        style = "-fx-font-size: 20pt"
        fill = new LinearGradient(
          endX = 0,
          stops = Stops(Color.Cyan, Color.DodgerBlue))
        effect = new DropShadow {
          color = Color.DodgerBlue
          radius = 25
          spread = 0.25
        }
      }
    }
  }

  def createSpheres(gameSize: Int, sphereSize: Int, spacer: Int) = {
    def coord(value: Int) = value * spacer - (gameSize - 1) * spacer / 2
    for (x <- 0 until gameSize; y <- 0 until gameSize; z <- 0 until gameSize)
      yield (
      new Sphere(sphereSize) {
        material = new PhongMaterial(Color.Red)
        drawMode = DrawMode.Fill
        translateX = coord(x)
        translateY = coord(y)
        translateZ = coord(z)
        id = x + "-" + y + "-" + z
      })
  }

  def addMouseInteraction(scene: SubScene, group: Group) {

    val angleX = DoubleProperty(0)
    val angleY = DoubleProperty(0)

    val xRotate = new Rotate {
      angle <== angleX
      axis = Rotate.XAxis
    }

    val yRotate = new Rotate {
      angle <== angleY
      axis = Rotate.YAxis
    }

    var anchorX: Double = 0
    var anchorY: Double = 0

    var anchorAngleX: Double = 0
    var anchorAngleY: Double = 0

    group.transforms = Seq(xRotate, yRotate)

    scene.onMousePressed = (event: MouseEvent) => {
      anchorAngleX = angleX()
      anchorAngleY = angleY()

      anchorX = event.sceneX
      anchorY = -event.sceneY

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
      angleX() = anchorAngleX + anchorY - (-event.sceneY)
      angleY() = anchorAngleY + anchorX - event.sceneX
    }
  }

  def createMarker(x: Double, y: Double, z: Double): Sphere = new Sphere(1) {
    material = new PhongMaterial(Color.Gold)
    translateX = x
    translateY = y
    translateZ = z
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

                e: ActionEvent => showCustomGameDialog()
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
            })
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
          })
        })
    })
  }

  private def showCustomGameDialog() {
    val dialogStage = new Stage {
      title = "Custom Game"
      width = 500
      height = 300
      resizable = false
      scene = new Scene {
        root = {
          new VBox {
            padding = Insets(18)
            spacing = 25
            alignment = Pos.Center
            content = Seq(
              new Label { text = "Please enter names" },
              new HBox {
                spacing = 10
                content = List(new Label { text = "Player 1: " }, new TextField { promptText = "Player 1" }, new ToggleButton {
                  minWidth = 100
                  text = "Human"
                  selected = true  
                  toggleGroup = togglePl1
                }, new ToggleButton {
                  minWidth = 100
                  text = "CPU"
                  toggleGroup = togglePl1
                })
              }, new HBox {
                spacing = 10
                content = List(new Label { text = "Player 2: " }, new TextField { promptText = "Player 2" }, new ToggleButton {
                  minWidth = 100
                  text = "Human"
                  toggleGroup = togglePl2
                }, new ToggleButton {
                  minWidth = 100
                  text = "CPU"
                  selected = true    
                  toggleGroup = togglePl2
                })
              }, new Label { text = "Edit or choose a board size"}, new HBox {
                spacing = 10
                content = List(new Label { text = "Board size:" }, new ComboBox[String] {
                  maxWidth = 200
                  editable = true
                  items = stringsBoardSize
                })
              }, new Button { text = "Start Game"
                onAction = {
                e: ActionEvent => System.exit(0)
              }})
          }
        }
      }
    }
    dialogStage.showAndWait()
  }

}