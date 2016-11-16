package view

import model.Task
import scalafx.scene.control.{Alert, TextField, TextArea}
import scalafx.scene.control.Alert.AlertType
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml
import scalafx.event.ActionEvent

@sfxml 
class AddTaskDialogController(
    private val taskNameField: TextField,
    private val notesArea: TextArea
    ) {
    var dialogStage: Stage = null 
    private var _task: Task = null
    var okClicked: Boolean = false
    
    def task = _task
    def task_=(x: Task) {
      _task = x
    }

    
    def handleAddTask(action: ActionEvent) {
      _task.name <== taskNameField.text
      stripOffNewLine()
      _task.notes <== notesArea.text
      okClicked = true
      dialogStage.close()
    }
    
    def handleCancel() {
      dialogStage.close()
    }
    
    private def stripOffNewLine() {
      val text = notesArea.text.value.replaceAll("\n", "")
      notesArea.text = text
    }
}