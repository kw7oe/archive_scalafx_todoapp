package todoapp.view

import scalafx.scene.control.{ListCell, Label, MenuItem, Button}
import scalafx.scene.layout.AnchorPane
import scalafx.beans.property.BooleanProperty
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import todoapp.model.Task
import todoapp.Application
import scalafxml.core.macros.sfxml
import scalafx.event.ActionEvent
import scalafx.scene.input.{KeyEvent, KeyCode}

@sfxml
class ListCellController(
    private val taskName: Label,
    private val completeButton: Button,
    private val deleteButton: Button,
    private val editMenuItem: MenuItem,
    private val viewMenuItem: MenuItem,
    private val deleteMenuItem: MenuItem
    ) {
    var index: Int = _
    private var _task: Task = null
    deleteButton.visible = false
    
    def task = _task
    
    def task_=(task: Task) {
      _task = task
      
      taskName.text <== task.name
      val result: Boolean = task.status.value match {
            case "Completed" => true
            case _ => false
         }
      if (result) manageViewMode
    }   
    
    def markTaskCompleted() {
      Application.data.doneTaskAt(index)
    }
    
    def deleteTask() {
      Application.data.removeTaskAt(index, task.status.value)
    }
    
    def editTask() {
      val onClicked = Application.showEditTaskDialog(task)
      if (onClicked) {
          Application.data.writeFile()  
      }
    }
    
    def viewTask() {  
      Application.showViewTaskDialog(task)
    }
    
    private def manageViewMode {
      completeButton.visible = false
      editMenuItem.visible = false
      deleteButton.visible = true
    }
}