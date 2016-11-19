package view

import scalafx.scene.control.{ListCell, Label, CheckBox, MenuItem}
import scalafx.scene.layout.AnchorPane
import model.Task
import scalafxml.core.macros.sfxml
import scalafx.event.ActionEvent
import main.Application


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
      
      taskName.text = task.name.value
      val result: Boolean = task.status.value match {
            case "Done" => true
            case _ => false
         }
      if (result) hideCheckBox
      taskCheckBox.selected = result
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
    def hideCheckBox {
      taskCheckBox.visible = false
    }

}