package com.robot

import javafx.application.Platform
import javafx.beans.property.{SimpleBooleanProperty, SimpleObjectProperty}
import javafx.beans.{InvalidationListener, Observable}
import javafx.event.EventHandler
import javafx.scene.input.{KeyCode, KeyEvent, MouseEvent}
import javafx.scene.paint.{Color}
import javafx.scene._
import org.fxyz3d.controls.factory.ControlFactory
import org.fxyz3d.geometry.Point3D
import org.fxyz3d.samples.shapes.ShapeBaseSample
import org.fxyz3d.scene.BillboardNode.BillboardMode
import org.fxyz3d.scene.{BillboardNode, CameraView}
import org.fxyz3d.shapes.composites.PolyLine3D

import java.util._

object MainAppDemo1{
  def main(args: Array[String]) {
    javafx.application.Application.launch(classOf[SwForm], args: _*)
  }
}

class SwForm() extends ShapeBaseSample[Board] () {
  val useCameraView = new SimpleBooleanProperty(this, "CameraView Enabled", true)
  val active = new SimpleBooleanProperty(this, "Billboarding Active") //Flag for toggling behavior

  val mode = new SimpleObjectProperty[BillboardNode.BillboardMode](this, "BillBoard Mode", BillboardMode.SPHERICAL) {
    override protected def invalidated(): Unit = {
      if (model != null) {
        model.setBillboardMode(getValue)
      }
    }
  }
  protected var cameraView: CameraView = null
  private def appendSubScene(): Unit = {
    camera.setTranslateX(100)
    camera.setTranslateY(150)
    camera.setTranslateZ(-7000)
  }
   private def initFirstPersonControls(scene: SubScene): Unit = { //make sure Subscene handles KeyEvents
    scene.setOnMouseEntered(new EventHandler[MouseEvent]() {
      override def handle (event:MouseEvent) {
        scene.requestFocus()
      }
    })
    scene.setOnKeyPressed (new EventHandler[KeyEvent]() {
      override def handle(event:KeyEvent){
        var change: Double = 10.0
        if (event.isShiftDown)  change = 50.0
        val keycode: KeyCode = event.getCode()
        if (keycode eq KeyCode.W)   camera.setTranslateZ(camera.getTranslateZ + change)
        if (keycode eq KeyCode.S)  camera.setTranslateZ(camera.getTranslateZ - change)
        if (keycode eq KeyCode.A)  camera.setTranslateX(camera.getTranslateX - change)
        if (keycode eq KeyCode.D)  camera.setTranslateX(camera.getTranslateX + change)
      }
    })
  }
  implicit def funToRunnable(fun: () => Unit) = new Runnable() { def run() = fun() }
  implicit def funToObservable(fun:  Observable => Unit) = new InvalidationListener() { def invalidated(l: Observable) = fun(l) }
  override protected def createMesh(): Unit = {
    model = new Board(camera)
    model.setBillboardMode(mode.get)
    Platform.runLater(() => {
      def foo() = {
        appendSubScene()
        subScene.requestFocus()
      }
      foo()
    })
  }
  override protected def addMeshAndListeners(): Unit = {
    model.activeProperty.bind(active)
  }
  override protected def buildControlPanel: Node = {
    ControlFactory.buildSingleListControlPanel
  }
}
import javafx.scene.image.ImageView

class Board( other: Node) extends BillboardNode[ImageView]() {
  setDepthTest(javafx.scene.DepthTest.DISABLE)
  import scala.collection.JavaConverters._
  val rand = new Random()
  val m = 10
  val (xSize,ySize) = (30,30)
  val (ox1,oy1) = (5,5)
  val (ox2,oy2) = (25,25)
  var t = 0.0
  import scala.math.pow
  def z(x:Int,y:Int):Float = {
       (Math.cos(-t+Math.sqrt(pow(ox1 - x,2)  + pow(oy1 - y,2)) / 5 * 2 * 3.14) * 5 * (Math.max(0,1-Math.sqrt(pow(ox1 - x,2) + pow(oy1 - y,2))/30))
      + Math.cos(-t+Math.sqrt(pow(ox2 - x,2)  + pow(oy2 - y,2)) / 5 * 2 * 3.14) * 4 * (Math.max(0,1-Math.sqrt(pow(ox2 - x,2) + pow(oy2 - y,2))/30))).toFloat
  }
  val pnts = (for (x <- 0 to xSize; y <- 0 to ySize) yield ((x,y)->new Point3D(x*m,y*m,z(x,y)))).toMap
  var poly = (for (x <- 0 until xSize; y <- 0 until ySize) yield ((x,y)->
    new PolyLine3D(Seq(pnts((x,y)),pnts((x+1,y)),pnts((x+1,y+1)),pnts((x,y+1)),pnts((x,y))).toList.asJava,3.0f,Color.BLACK))).toMap

  val timer = new Timer()
  val task = new TimerTask()
  {
    def run()
    {
      t = t+0.3
      for (x <- 0 to xSize; y <- 0 to ySize)
        pnts(x,y).z = z(x,y)
      poly = (for (x <- 0 until xSize; y <- 0 until ySize)
        yield ((x,y)->new PolyLine3D(Seq(pnts((x,y)),pnts((x+1,y)),pnts((x+1,y+1)),pnts((x,y+1)),pnts((x,y))).toList.asJava,3.0f,Color.BLACK))).toMap
      Platform.runLater(() => {
        getChildren.clear()
        getChildren.addAll(poly.map(_._2).toList.asJava);
      })
    }
  }
  timer.schedule(task,0,200)
  getChildren.addAll(poly.map(_._2).toList.asJava);
  override def getBillboardNode(): ImageView = null//view
  override def getTarget(): Node = other
}


