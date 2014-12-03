package scalafx.graphics3d

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene._
import scalafx.scene.paint.{Color, PhongMaterial}
import scalafx.scene.shape.{Box, DrawMode}
import scalafx.scene.transform.Transform._
import scalafx.scene.transform.{Rotate, Translate}
import scalafx.scene.shape.Sphere
import scalafx.beans.property.DoubleProperty
import scalafx.scene.input.MouseEvent

/** Demo of a triangular frame of a 3D box, originally based on example in Ensemble 8. */
object Simple3DBoxApp extends JFXApp {

  stage = new PrimaryStage {
    scene = new Scene(300, 300, true,  SceneAntialiasing.Balanced) {
      
      // Background
      fill = Color.Black
      
      // 3D content
      val sphere1 = new Sphere(3) {
        material = new PhongMaterial(Color.Red)
        drawMode = DrawMode.Fill
        translateX = -5
        translateY = -5
        translateZ = -5
      }
      
      val sphere2 = new Sphere(3) {
        material = new PhongMaterial(Color.Blue)
        drawMode = DrawMode.Fill
        translateX = 5
        translateY = -5
        translateZ = -5
      }
      
      val sphere3 = new Sphere(3) {
        material = new PhongMaterial(Color.Green)
        drawMode = DrawMode.Fill
        translateX = -5
        translateY = 5
        translateZ = -5
      }
      
      val sphere4 = new Sphere(3) {
        material = new PhongMaterial(Color.Orange)
        drawMode = DrawMode.Fill
        translateX = 5
        translateY = 5
        translateZ = -5
      }
      
      val sphere5 = new Sphere(3) {
        material = new PhongMaterial(Color.Azure)
        drawMode = DrawMode.Fill
        translateX = -5
        translateY = -5
        translateZ = 5
      }
      
      val sphere6 = new Sphere(3) {
        material = new PhongMaterial(Color.Sienna)
        drawMode = DrawMode.Fill
        translateX = 5
        translateY = -5
        translateZ = 5
      }
      
      val sphere7 = new Sphere(3) {
        material = new PhongMaterial(Color.OrangeRed)
        drawMode = DrawMode.Fill
        translateX = -5
        translateY = 5
        translateZ = 5
      }
      
      val sphere8 = new Sphere(3) {
        material = new PhongMaterial(Color.Magenta)
        drawMode = DrawMode.Fill
        translateX = 5
        translateY = 5
        translateZ = 5
      }
      
      val spheres = new Group(sphere1, sphere2, sphere3, sphere4, sphere5, sphere6, sphere7, sphere8)
      content = spheres

      // Modify point of view
      camera = new PerspectiveCamera(true) {
        transforms +=(
          //new Rotate(-20, Rotate.YAxis),
          //new Rotate(-20, Rotate.XAxis),
          new Translate(0, 0, -50))
      }
      
      addMouseInteraction(this, spheres)
  
    }
  }
  
        /** Add mouse interaction to a scene, rotating given node. */
  private def addMouseInteraction(scene: Scene, node: Node) {
    val angleY = DoubleProperty(0)
    val yRotate = new Rotate {
      angle <== angleY
      //axis = Rotate.
    }
    var anchorX: Double = 0
    var anchorAngleY: Double = 0

    node.transforms = Seq(yRotate)

    scene.onMousePressed = (event: MouseEvent) => {
      anchorX = event.sceneX
      anchorAngleY = angleY()
    }
    scene.onMouseDragged = (event: MouseEvent) => {
      angleY() = anchorAngleY + anchorX - event.sceneX
    }
  }
}