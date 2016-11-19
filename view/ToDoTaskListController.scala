package view

import scalafx.scene.control.{ListCell, Label, CheckBox}
import scalafx.scene.layout.AnchorPane
import model.Task
import scalafxml.core.macros.sfxml
import scalafx.event.ActionEvent
import main.Application


@sfxml
class ToDoTaskListController(
    private val taskName: Label,
    private val taskStatus: CheckBox,
    private val anchorPane: AnchorPane
    ) {
    var index: Int = _
    def setText(value: String) {
       taskName.text = value     
    }
    def getTaskName : String = {
      return taskName.text.value
    }
    
    def setStatus(value: String) {
       val result: Boolean= value match {
            case "Done" => true
            case _ => false
         }
        taskStatus.selected = result
    }    
    
    def markTaskDone() {
      Application.data.doneTaskAt(index)
    }
    
    def hideCheckBox {
      taskStatus.visible = false
    }

}