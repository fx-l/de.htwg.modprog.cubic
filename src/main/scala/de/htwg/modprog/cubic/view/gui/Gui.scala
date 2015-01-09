package de.htwg.modprog.cubic.view.gui

import scala.swing.Reactor

import de.htwg.modprog.cubic.controller.CubicController
import de.htwg.modprog.cubic.controller.FieldChanged
import de.htwg.modprog.cubic.controller.GameCreated
import de.htwg.modprog.cubic.util.Util
import javafx.scene.control.{ ToggleButton => JfxToggleBtn }
import scalafx.Includes.eventClosureWrapperWithParam
import scalafx.Includes.jfxActionEvent2sfx
import scalafx.Includes.jfxMouseEvent2sfx
import scalafx.Includes.observableList2ObservableBuffer
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.Platform
import scalafx.beans.property.DoubleProperty
import scalafx.beans.property.DoubleProperty.sfxDoubleProperty2jfx
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.beans.property.ReadOnlyDoubleProperty.sfxReadOnlyDoubleProperty2jfx
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.geometry.Pos
import scalafx.scene.Group
import scalafx.scene.PerspectiveCamera
import scalafx.scene.Scene
import scalafx.scene.SceneAntialiasing
import scalafx.scene.SubScene
import scalafx.scene.control._
import scalafx.scene.effect.DropShadow
import scalafx.scene.input.KeyCombination
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout._
import scalafx.scene.paint._
import scalafx.scene.shape.DrawMode
import scalafx.scene.shape.Sphere
import scalafx.scene.text.Text
import scalafx.scene.transform.Rotate
import scalafx.scene.transform.Translate
import scalafx.scene.transform.Translate.sfxTranslate2jfx
import scalafx.stage.Stage
import scalafx.util.StringConverter

object Gui extends JFXApp with Reactor {

  val occupiedSphereSize = 3
  val highlightedSphereSize = 4
  val emptySphereSize = 0.8
  val spacer = 12
  val zoomOutFactor = 3
  val horizontalCorrectionFactor = 0.08F;
  val sphereIdPrefix = "sphere"
  val highlightMat = new PhongMaterial {
    diffuseColor = Color.Yellow
    specularColor = Color.White
    specularPower = 20.0
  }
  val defaultMat = new PhongMaterial(Color.LightSteelBlue)
  val symbols = Seq(new PhongMaterial(Color.LawnGreen), new PhongMaterial(Color.DeepPink))
  var symbolMapping = Map[String, Material]()
  val configMaxGameSize = 20
  def coordsToId(x: Int, y: Int, z: Int) = sphereIdPrefix + "-" + x + "-" + y + "-" + z
  def gameSize = controller.boardSize
  def sideLength = gameSize * occupiedSphereSize + (gameSize - 1) * spacer

  val comboBoxBoardSize = new ComboBox[String] {
    maxWidth = 200
    editable = true
    items = ObservableBuffer(
      "2 x 2 x 2", "3 x 3 x 3", "4 x 4 x 4",
      "5 x 5 x 5", "6 x 6 x 6", "7 x 7 x 7",
      "8 x 8 x 8")
    value = "4 x 4 x 4"
  }

  val textFieldPl1 = new TextField { promptText = "Player 1" }
  val textFieldPl2 = new TextField { promptText = "Player 2" }

  private var controller: CubicController = _

  def registerController(controller: CubicController) {
    this.controller = controller;
    listenTo(controller)
    reactions += {
      case e: GameCreated => onGameCreated
      case e: FieldChanged => onGameUpdated
    }
  }

  def onGameCreated = {
    val nameList = controller.players.toList
    symbolMapping = Util.assignSymbols(nameList, symbols, new PhongMaterial(Color.Red), Map[String, Material]())
    Platform.runLater {
      val subScene = getSubScene
      val basicContent = createBasic3dContent
      subScene.content = basicContent
      subScene.camera = createCamera
      addMouseInteraction(subScene, basicContent)
    }
  }

  def onGameUpdated = {
    Platform.runLater {
      update3dContent
    }
  }

  def update3dContent = {
    for (x <- 0 until gameSize; y <- 0 until gameSize; z <- 0 until gameSize) {
      val sphere = getSphereById(coordsToId(x, y, z))
      val (player, highlight) = controller.field(x, y, z)
      sphere.material = player match {
        case Some(name: String) => if (highlight) highlightMat else symbolMapping(name)
        case None => defaultMat
      }
      sphere.radius = player match {
        case Some(_) => if (highlight) highlightedSphereSize else occupiedSphereSize
        case None => emptySphereSize
      }
    }
  }

  def getSphereById(id: String) = {
    val subScene = getSubScene
    val content = subScene.getChildren(0)
    val javaSphere = content.lookup("#" + id).asInstanceOf[javafx.scene.shape.Sphere]
    new Sphere(javaSphere)
  }

  def getSubScene = {
    val javaSubScene = stage.scene().lookup("#sub").asInstanceOf[javafx.scene.SubScene]
    new SubScene(javaSubScene)
  }

  def setStatusText(statusText: String) {

  }

  stage = new PrimaryStage {
    title = "Cubic"
    scene = new Scene(900, 900, true, SceneAntialiasing.Balanced) {
      fill = Color.Black
      var tmpw = this.width
      var tmph = this.height
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
        id = "sub"
        fill = Color.Black
        this.width.bind(boundWidth.add(0))
        this.height.bind(boundHeight.add(0))
      }
    }
  }

  def createBasic3dContent = {
    val spheres = new Group
    spheres.children = createSpheres(controller.boardSize, emptySphereSize, spacer)
    spheres
  }

  def createCamera = new PerspectiveCamera(true) {
    farClip = sideLength + zoomOutFactor * sideLength
    transforms += (
      new Translate(0, sideLength * horizontalCorrectionFactor, -sideLength * zoomOutFactor))
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

  def createSpheres(gameSize: Int, sphereSize: Double, spacer: Int) = {
    def coord(value: Int) = value * spacer - (gameSize - 1) * spacer / 2
    for (x <- 0 until gameSize; y <- 0 until gameSize; z <- 0 until gameSize)
      yield (
      new Sphere(sphereSize) {
        material = defaultMat
        drawMode = DrawMode.Fill
        translateX = coord(x)
        translateY = coord(y)
        translateZ = coord(z)
        id = coordsToId(x, y, z)
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
        case Some(n) if n.id().startsWith(sphereIdPrefix) => {
          //println("Picked sphere: '" + n.id() + "'")
          val coords = n.id().split("[^0-9]").filter(_.length > 0).map(_.toInt)
          controller.occupyField(coords(0), coords(1), coords(2))
        }
        case _ => //println("Picked nothing or not a sphere at least.")
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
            items = List(new MenuItem("Quickstart") {
              accelerator = KeyCombination.keyCombination("s")
              onAction = {
                e: ActionEvent => controller.createQuickVersusGame
              }
            }, new MenuItem("Custom Setup") {
              accelerator = KeyCombination.keyCombination("c")
              onAction = {
                e: ActionEvent => showCustomGameDialog()
              }
            })
          },
            new MenuItem("Restart (curr. setup)") {
              accelerator = KeyCombination.keyCombination("r")
              onAction = {
                e: ActionEvent => controller.restart
              }
            },
            new MenuItem("Quit") {
              accelerator = KeyCombination.keyCombination("q")
              onAction = {
                e: ActionEvent => System.exit(0)
              }
            })
        })
    })
  }

  private def showCustomGameDialog() {
    val dialogStage = new Stage {
      outer =>
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
                content = List(new Label { text = "Player 1: " }, textFieldPl1)
              }, new HBox {
                spacing = 10
                content = List(new Label { text = "Player 2: " }, textFieldPl2)
              }, new Label { text = "Choose a board size" }, new HBox {
                spacing = 10
                content = List(new Label { text = "Board size:" }, comboBoxBoardSize)
              }, new Button {
                text = "Start Game"
                onAction = {
                  e: ActionEvent =>
                    val player1 = textFieldPl1.getText()
                    val player2 = textFieldPl2.getText()
                    val boardSize = comboBoxBoardSize.getValue().charAt(0).asDigit
                    controller.createCustomGame(Seq(player1, player2), boardSize)
                    outer.close
                }
              })
          }
        }
      }
    }
    dialogStage.showAndWait()
  }

}