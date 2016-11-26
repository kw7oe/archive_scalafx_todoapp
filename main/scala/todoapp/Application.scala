package todoapp

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.stage.Stage
import scalafx.scene.control.{Dialog, DialogPane, Alert, ListCell}
import scalafx.scene.control.Alert.AlertType
import scalafx.Includes._
import scalafxml.core.{NoDependencyResolver, FXMLLoader}
import javafx.{scene => jfxs}
import scalafx.collections.ObservableBuffer
import todoapp.model.Task
import todoapp.database.FileData
import scalafx.stage.Modality
import todoapp.view._

object Application extends JFXApp {  
  val rootResource = getClass.getResourceAsStream("view/RootLayout.fxml")  
  val data = new FileData()
  data.readFile()
  val loader = new FXMLLoader(null, NoDependencyResolver)
  loader.load(rootResource)
  
  val roots = loader.getRoot[jfxs.layout.BorderPane]
  
  stage = new PrimaryStage {
    title = "To Do List"
    scene = new Scene {
      root = roots
    }
  }
  
  def showMainScreen() = {
    val resource = getClass.getResourceAsStream("view/MainScreen.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }  
  
  def showAddTaskDialog(task: Task) : Boolean = {
    val resource = getClass.getResourceAsStream("view/AddTaskDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val root2 = loader.getRoot[jfxs.layout.AnchorPane]
    val controller = loader.getController[AddTaskDialogController#Controller]
    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      title = "Add Task"
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
    val resource = getClass.getResourceAsStream("view/EditTaskDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val root2 = loader.getRoot[jfxs.layout.AnchorPane]
    val controller = loader.getController[EditTaskDialogController#Controller]
    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      title = "Edit Task"
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
    val resource = getClass.getResourceAsStream("view/ViewTaskDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val root2 = loader.getRoot[jfxs.layout.AnchorPane]
    val controller = loader.getController[ViewTaskDialogController#Controller]
    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      title = "Task"
      scene = new Scene {
        root = root2
      }
    }
    controller.dialogStage = dialog
    controller.task = task
    dialog.showAndWait();
    return controller.okClicked;
  }
  
  def showAboutDialog() {
    val resource = getClass.getResourceAsStream("view/AboutDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val root2 = loader.getRoot[jfxs.layout.AnchorPane]
    val dialog = new Stage() {      
      resizable = false
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)      
      title = "About"
      scene = new Scene {
        root = root2
      }
    }
    dialog.showAndWait();
  }
  
  def showShortcutDialog() {
    val resource = getClass.getResourceAsStream("view/ShortcutDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val root2 = loader.getRoot[jfxs.layout.AnchorPane]
    val dialog = new Stage() {      
      resizable = false
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)      
      title = "Shortcut"
      scene = new Scene {
        root = root2
      }
    }
    dialog.showAndWait();
  }
  
  def showNoTaskNameAlert() {
    val noTaskNameAlert = new Alert(AlertType.Warning) {
      initOwner(stage)
      title       = "Invalid Input"
      headerText  = "Task Name is Required"
      contentText = "Please ensure the task name is not blank."
    }.showAndWait
  }
  
  showMainScreen()
}