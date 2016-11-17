package main

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.stage.Stage
import scalafx.scene.control.{Dialog, DialogPane}
import scalafx.Includes._
import javafx.{scene => jfxs}
import scalafxml.core.{NoDependencyResolver, FXMLLoader}
import javafx.{scene => jfxs}
import scalafx.collections.ObservableBuffer
import model.Task
import database.FileData
import javafx.{scene => jfxs}
import scalafx.stage.Modality
import view.{AddTaskDialogController, EditTaskDialogController, ViewTaskDialogController}

object Application extends JFXApp {  
  val rootResource = getClass.getResourceAsStream("../view/RootLayout.fxml")  
  val data = new FileData()
  data.readFile()
  val loader = new FXMLLoader(null, NoDependencyResolver)
  loader.load(rootResource)
  
  val roots = loader.getRoot[jfxs.layout.BorderPane]
  
  stage = new PrimaryStage {
    title = "ToDoApp"
    scene = new Scene {
      root = roots
      stylesheets = List(this.getClass.getResource("../view/main.css").toExternalForm)
    }
    
  }
  
  def showMainScreen() = {
    val resource = getClass.getResourceAsStream("../view/MainScreen.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }
  
  def showAddTaskDialog(task: Task) : Boolean = {
    val resource = getClass.getResourceAsStream("../view/AddTaskDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val root2 = loader.getRoot[jfxs.layout.AnchorPane]
    val controller = loader.getController[AddTaskDialogController#Controller]
    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = root2
      }
    }
    controller.dialogStage = dialog
    controller.task = task
    dialog.showAndWait();
    return controller.okClicked;
  }
  
  def showEditTaskDialog(task: Task) : Boolean = {
    val resource = getClass.getResourceAsStream("../view/EditTaskDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val root2 = loader.getRoot[jfxs.layout.AnchorPane]
    val controller = loader.getController[EditTaskDialogController#Controller]
    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = root2
      }
    }
    controller.dialogStage = dialog
    controller.task = task
    dialog.showAndWait();
    return controller.okClicked;
  }
  
  def showViewTaskDialog(task: Task) : Boolean = {
    val resource = getClass.getResourceAsStream("../view/ViewTaskDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val root2 = loader.getRoot[jfxs.layout.AnchorPane]
    val controller = loader.getController[ViewTaskDialogController#Controller]
    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = root2
      }
    }
    controller.dialogStage = dialog
    controller.task = task
    dialog.showAndWait();
    return controller.okClicked;
  }

  showMainScreen()
}