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
  
  // Initialize Everything That is necessary for the main screen
  initializeView()
  setUpKeyEvent()  
  setUpCell()
  setListView(data = Application.data.getTasksBasedOn(currentView))

  // Add Task When Button is Clicked
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
  
  // Add Task When User press Enter after task name is provided in the Text Field
  def enterToAddTask(action: KeyEvent) {
    if (action.code == KeyCode.ENTER) {
      addTask()
    }
  }
  
  // Allow User to add notes during Add Task
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
      // Note that "getTaskBasedOn(currentView)" return a Option[ObservableBuffer[Task]]
      //   - When the user are able to select the ListCell,
      //     it also means that the ObservableBuffer[Task] is  
      //     definitely not "null". So, we can call "get" directly
      //     without worrying the NullPointerException.
      val selectedTask = Application.data.getTasksBasedOn(currentView).get(selectedIndex)
      val onClicked = Application.showEditTaskDialog(selectedTask)
       if (onClicked) {
          Application.data.editTask()
          setListView(data = Application.data.getTasksBasedOn(currentView))     
       } 
    } else {
      noSelectionAlert.showAndWait()
    }
  }
  
  def viewTask() {
    val selectedIndex = taskList.selectionModel().selectedIndex.value
    if (selectedIndex >= 0) {
      val selectedTask = Application.data.getTasksBasedOn(currentView).get(selectedIndex)
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
  
  // Hide certain UI components during the initialization
  private def initializeView() {
    viewIncompleteButton.visible = false
    header2.visible = false
  }
  
  // Toggle Different View depending on User Selection
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
  
  // Update the items in the ListView
  private def setListView(data: Option[ObservableBuffer[Task]]) {
    data match {
      case Some(i) => {
        taskList.setItems(i)
      }         
      case None => println("List is Empty")
    }   
  }
  
  // Setup proper response for certain Key Event
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
  
  // Customize the ListCell in the ListView
  private def setUpCell() {
    taskList.cellFactory = { _ =>
      new ListCell[Task]() {  
        styleClass = List("custom_cell")
        item.onChange { (task, oldValue, newValue) => {
          if (newValue == null) {
            graphic = null
          } else {
            val loader = new FXMLLoader(null, NoDependencyResolver)
            val resource = getClass.getResourceAsStream("ToDoTaskListCell.fxml")
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