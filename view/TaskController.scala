package view

import model.Task
import main.Application
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.GridPane
import scalafx.scene.control.{Label, ListView, ListCell, Button}
import scalafx.scene.control.SelectionModel
import scalafx.scene.input.{KeyEvent, KeyCode}
import scalafxml.core.macros.sfxml
import scalafx.beans.property.{StringProperty, ObjectProperty} 
import scalafx.Includes._
import javafx.{scene => jfxs}
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafxml.core.{NoDependencyResolver, FXMLLoader}

@sfxml
class TaskController(
  private val taskList: ListView[Task],
  private val viewDone: Button,
  private val viewDefault: Button,
  private val addButton: Button,
  private val removeButton: Button,
  private val doneButton: Button,
  private val editButton: Button,
  private val viewButton: Button,
  private val header: GridPane) {
  
  var currentView = "Default"
  updateUI()
  viewDefault.visible = false
  
  // On KeyPressed, Respond Accordingly
  taskList.onKeyPressed = (action: KeyEvent) => {
    // Press Delete to remove task (in Mac OS fn + delete is required)
    if (action.code == KeyCode.DELETE) {
      removeTask()
    } 
    // Press Ctrl + E to edit task
    if (action.code == KeyCode.E && action.controlDown) {
      editTask()
    }
  }
  setUpCell()

  // Add task after clicking button
  def addTask() = {
    val tempTask = Task("", "", "Default")
    val okClicked : Boolean = Application.showAddTaskDialog(tempTask);
    if (okClicked) {
       Application.data.append(tempTask)
       updateUI()     
    }
  }
  
  // Edit task after clicking button
  def editTask() = {
    val selectedIndex = taskList.selectionModel().selectedIndex.value
    if (selectedIndex >= 0) {
      val selectedTask = Application.data.getTasks()(selectedIndex)
      val onClicked = Application.showEditTaskDialog(selectedTask)
       if (onClicked) {
          Application.data.editTaskAt()
          updateUI()     
       }
    } else {
      val alert = new Alert(AlertType.Warning){
          initOwner(Application.stage)
          title       = "No Selection"
          headerText  = "No Task Selected"
          contentText = "Please select a task in the table."
        }.showAndWait()
    }
  }
  
  def viewDoneTasks() = {
    viewTasks("Done")    
  }
  
  def viewDefaultTasks() = {
    viewTasks()
  }
  
  def checkTaskDone() = {
    val selectedIndex = taskList.selectionModel().selectedIndex.value
    if (selectedIndex >= 0) {
      Application.data.doneTaskAt(selectedIndex)
      updateUI()
    }    
  }
  
  def removeTask() = {
    val selectedIndex = taskList.selectionModel().selectedIndex.value
    if (selectedIndex >= 0) {
      Application.data.removeTaskAt(selectedIndex, currentView)
      updateUI()
    }
  }
  
  def updateUI() {
    setListView(Application.data.getTasksBasedOn(currentView)) 
  }
  
  private def colorFor(status: String = "Default") : String = {
    if (status == "Done") return "#47afa0"
    return "#316079"
  }
  
  private def viewTasks(status: String = "Default") = {
    val tempData = Application.data.getTasksBasedOn(status = status)
    setListView(tempData) 
    header.style = s"-fx-background-color: ${colorFor(status)};"
    currentView = status
    toggleButtonVisibility()
  }
  
  private def toggleButtonVisibility() {    
    addButton.visible = !addButton.visible.value
    doneButton.visible = !doneButton.visible.value
    editButton.visible = !editButton.visible.value
    viewDone.visible = !viewDone.visible.value
    viewDefault.visible = !viewDefault.visible.value
  }
  
  private def setListView(data: Option[ObservableBuffer[Task]]) {
    data match {
      case Some(i) => {
        taskList.setItems(i)
      }         
      case None => println("List is Empty")
    }
   
  }
  
  private def setUpCell() {
    taskList.cellFactory = { _ =>
      new ListCell[Task]() {          
        // Update value in ListCell when Task Change
        item.onChange { (task, oldValue, newValue) => {
          if (newValue == null) {
            graphic = null
          } else {
            val loader = new FXMLLoader(null, NoDependencyResolver)
            val resource = getClass.getResourceAsStream("../view/ToDoTaskListCell.fxml")
            loader.load(resource)    
            val root = loader.getRoot[jfxs.layout.AnchorPane]
            val controller = loader.getController[ToDoTaskListController#Controller]
            controller.index = this.index.value
            controller.task = task.value         
            graphic = root 
          }
        }}  
      }
    } 
  }
}