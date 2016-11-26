package todoapp.view

import todoapp.model.Task
import scalafx.scene.control.{Alert, Label}
import scalafx.scene.control.Alert.AlertType
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml
import scalafx.event.ActionEvent
import scalafx.scene.input.{KeyEvent, KeyCode}

@sfxml
class ViewTaskDialogController(
    private val taskText: Label,
    private val notesText: Label
    ) {
    var dialogStage: Stage = null 
    private var _task: Task = null
    var okClicked: Boolean = false
    
    def task = _task
    def task_=(x: Task) {
      _task = x
      
      if (_task.name.value != "") {
        taskText.text = _task.name.value
      }
      if (_task.notes.value != "") {
        notesText.text = _task.getNotes.value
      }
       
    }
    
    def handleKeyBoard(action: KeyEvent) {
      if (action.code == KeyCode.ESCAPE) {
        handleClose()
      }
    }
    
    def handleClose() {
      dialogStage.close()
    }

}