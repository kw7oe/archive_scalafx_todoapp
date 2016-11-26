package todoapp.view

import todoapp.model.Task
import todoapp.Application
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.GridPane
import scalafx.scene.control.{Label, ListView, ListCell, Button, TextField, SelectionModel}
import scalafx.scene.input.{KeyEvent, KeyCode, MouseEvent, MouseButton}
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
  private val addButton: Button,
  private val addTaskField: TextField,
  private val viewCompletedButton: Button,
  private val viewIncompleteButton: Button,
  private val advanceAddLabel: Label,
  private val header: GridPane,
  private val header2: GridPane,
  private val taskCompletedLabel: Label) {
  
  private var currentView = "Default"
  private val noSelectionAlert = new Alert(AlertType.Warning) {
    initOwner(Application.stage)
    title       = "No Selection"
    headerText  = "No Task Selected"
    contentText = "Please select a task in the table."
  }
  initializeView()
  setUpKeyEvent()  
  setUpCell()
  setListView(data = Application.data.getTasksBasedOn(currentView))


  def addTask() {
    val taskName = addTaskField.text.value
    if (taskName.length == 0) {
      Application.showNoTaskNameAlert()
    } else {
      val tempTask = Task(taskName,"", "Default")
      Application.data.append(tempTask)
      setListView(data = Application.data.getTasksBasedOn(currentView))         
      addTaskField.text = ""
    }  
  }
  
  def enterToAddTask(action: KeyEvent) {
    if (action.code == KeyCode.ENTER) {
      addTask()
    }
  }
  
  def advanceAddTask(action: MouseEvent) {
    if (action.button == MouseButton.PRIMARY) {
      val tempTask = Task("", "", "Default")
      val okClicked : Boolean = Application.showAddTaskDialog(tempTask);
      if (okClicked) {
        Application.data.append(tempTask)
        setListView(data = Application.data.getTasksBasedOn(currentView))     
      } 
    }
  }

  def editTask() {
    val selectedIndex = taskList.selectionModel().selectedIndex.value
    if (selectedIndex >= 0) {
      val selectedTask = Application.data.getTasks()(selectedIndex)
      val onClicked = Application.showEditTaskDialog(selectedTask)
       if (onClicked) {
          Application.data.writeFile()
          setListView(data = Application.data.getTasksBasedOn(currentView))     
       } 
    } else {
      noSelectionAlert.showAndWait()
    }
  }
  
  def viewTask() {
    val selectedIndex = taskList.selectionModel().selectedIndex.value
    if (selectedIndex >= 0) {
      val selectedTask = Application.data.getTasks()(selectedIndex)
      val onClicked = Application.showViewTaskDialog(selectedTask)
    } else {
      noSelectionAlert.showAndWait()
    }
  }
  
  def viewCompletedTasks() {
    viewAllTasks(status = "Completed")    
  }
  
  def viewIncompleteTasks() {
    viewAllTasks()
  }
  
  def markTaskCompleted() {
    val selectedIndex = taskList.selectionModel().selectedIndex.value
    if (selectedIndex >= 0) {
      Application.data.doneTaskAt(selectedIndex)
      setListView(data = Application.data.getTasksBasedOn(currentView))
    }    
  }
  
  def removeTask() {
    val selectedIndex = taskList.selectionModel().selectedIndex.value
    if (selectedIndex >= 0) {
      Application.data.removeTaskAt(selectedIndex, currentView)
      setListView(data = Application.data.getTasksBasedOn(currentView))
    }
  }  
  
  private def initializeView() {
    viewIncompleteButton.visible = false
    header2.visible = false
  }
  
  private def toggleViewMode() {
    viewIncompleteButton.visible = !viewIncompleteButton.visible.value
    viewCompletedButton.visible = !viewCompletedButton.visible.value
    header2.visible = !header2.visible.value
    header.visible = !header.visible.value
  }
  
  private def viewAllTasks(status: String = "Default") {
    val tempData = Application.data.getTasksBasedOn(status)
    setListView(tempData) 
    currentView = status
    toggleViewMode()
  }
  
  private def switchViewMode() {
    if (currentView == "Completed") {
      viewIncompleteTasks()
    } else {
      viewCompletedTasks()
    }
  }
  
  private def setListView(data: Option[ObservableBuffer[Task]]) {
    data match {
      case Some(i) => {
        taskList.setItems(i)
      }         
      case None => println("List is Empty")
    }   
  }

  private def setUpKeyEvent() {
    taskList.onKeyPressed = (action: KeyEvent) => {
      action.code match {
        case KeyCode.BACK_SPACE => removeTask()
        case KeyCode.V => viewTask()
        case KeyCode.E => if (currentView == "Default") editTask()
        case KeyCode.D => if (currentView == "Default") markTaskCompleted()
        case KeyCode.Z => switchViewMode()
        case _ => 
      }
    }
  }
  
  private def setUpCell() {
    taskList.cellFactory = { _ =>
      new ListCell[Task]() {  
        styleClass = List("custom_cell")
        item.onChange { (task, oldValue, newValue) => {
          if (newValue == null) {
            graphic = null
          } else {
            val loader = new FXMLLoader(null, NoDependencyResolver)
            val resource = getClass.getResourceAsStream("../view/ToDoTaskListCell.fxml")
            loader.load(resource)    
            val root = loader.getRoot[jfxs.layout.AnchorPane]
            val controller = loader.getController[ListCellController#Controller]
            controller.index = this.index.value
            controller.task = task.value         
            graphic = root 
          }
        }}  
      }
    } 
  }
}