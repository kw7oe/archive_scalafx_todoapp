package view

import model.Task
import main.Application
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.GridPane
import scalafx.scene.control.{TableView, TableColumn, Label, ListView, ListCell, Button}
import scalafx.scene.control.SelectionModel
import scalafxml.core.macros.sfxml
import scalafx.beans.property.{StringProperty, ObjectProperty} 
import scalafx.Includes._
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

@sfxml
class TaskController(
  private val taskTable: TableView[Task],
  private val taskNameColumn: TableColumn[Task, String],
  private val secondColumn: TableColumn[Task, String],
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
    val selectedIndex = taskTable.selectionModel().selectedIndex.value
    if (selectedIndex >= 0) {
      val selectedTask = Application.data.getTasks()(selectedIndex)
      val onClicked = Application.showEditTaskDialog(selectedTask)
    } else {
      val alert = new Alert(AlertType.Warning){
          initOwner(Application.stage)
          title       = "No Selection"
          headerText  = "No Task Selected"
          contentText = "Please select a task in the table."
        }.showAndWait()
    }
  }
  
  // Might need to be refactored
  def viewDoneTasks() = {
    viewTasks("Done")
    header.style = "-fx-background-color: #47afa0;"
    currentView = "Done"
    addButton.visible = false
    doneButton.visible = false
    editButton.visible = false
    viewDone.visible = false
    viewDefault.visible = true
    
  }
  
  def viewDefaultTasks() = {
    viewTasks()
    header.style = "-fx-background-color: #316079;"
    currentView = "Default"
    addButton.visible = true
    doneButton.visible = true
    editButton.visible = true
    viewDone.visible = true
    viewDefault.visible = false
  }
  
  def checkTaskDone() = {
    val selectedIndex = taskTable.selectionModel().selectedIndex.value
    if (selectedIndex >= 0) {
      Application.data.doneTaskAt(selectedIndex)
      updateUI()
    }
    
  }
  
  def removeTask() = {
    val selectedIndex = taskTable.selectionModel().selectedIndex.value
    if (selectedIndex >= 0) {
      Application.data.removeTaskAt(selectedIndex, currentView)
      updateUI()
    }
  }
  
  private def updateUI() {
    setTableView(Application.data.getTasksBasedOn(currentView)) 
  }
  private def viewTasks(status: String = "Default") = {
    val tempData = Application.data.getTasksBasedOn(status = status)
    setTableView(tempData) 
  }
  
  private def setTableView(data: Option[ObservableBuffer[Task]]) {
    data match {
      case Some(i) => {
        taskTable.setItems(i)
        taskNameColumn.cellValueFactory = {_.value.name}
        secondColumn.cellValueFactory = {_.value.status}
      }         
      case None => println("List is Empty")
    }
   
  }
}