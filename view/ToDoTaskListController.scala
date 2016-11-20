package view

import scalafx.scene.control.{ListCell, Label, CheckBox, MenuItem}
import scalafx.scene.layout.AnchorPane
import scalafx.beans.property.BooleanProperty
import model.Task
import main.Application
import scalafxml.core.macros.sfxml
import scalafx.event.ActionEvent
import scalafx.scene.input.{KeyEvent, KeyCode}

@sfxml
class ToDoTaskListController(
    private val taskName: Label,
    private val taskCheckBox: CheckBox,
    private val editMenuItem: MenuItem,
    private val viewMenuItem: MenuItem,
    private val deleteMenuItem: MenuItem
    ) {
    var index: Int = _
    private var _task: Task = null
    
    def task = _task
    
    def task_=(task: Task) {
      _task = task
      
      taskName.text <== task.name
      val result: Boolean = task.status.value match {
            case "Done" => true
            case _ => false
         }
      if (result) hideFunctionality
      taskCheckBox.selected = result
    }   
    
    def handleKeyBoard(action: KeyEvent) {
      println("Key Pressed in List Cell Controller")
      if(action.code == KeyCode.ENTER) {
        viewTask()
      } else if (action.code == KeyCode.DELETE) {
       deleteTask()
      }
    }
    def markTaskDone() {
      Application.data.doneTaskAt(index)
    }
    
    def deleteTask() {
      println(task)
      Application.data.removeTaskAt(index, task.status.value)
    }
    
    def editTask() {
      val onClicked = Application.showEditTaskDialog(task)
      if (onClicked) {
          Application.data.editTaskAt()  
       }
    }
    
    def viewTask() {      
    }
    
    def hideFunctionality {
      taskCheckBox.visible = false
      editMenuItem.visible = false
    }
    

}