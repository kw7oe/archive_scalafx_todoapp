package todoapp.view

import todoapp.model.Task
import todoapp.Application
import scalafx.scene.control.{TextField, TextArea}
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml
import scalafx.event.ActionEvent
import scalafx.scene.input.{KeyEvent, KeyCode}

@sfxml
class EditTaskDialogController(
    private val taskNameField: TextField,
    private val notesArea: TextArea
    ) {
    var dialogStage: Stage = null 
    private var _task: Task = null
    var okClicked: Boolean = false
    def task = _task
    def task_=(x: Task) {
      _task = x
      
      taskNameField.text = _task.name.value
      notesArea.text = _task.getNotes.value
    }

    def handleEditTask() {
      if (taskNameField.text.value.length == 0) {
        Application.showNoTaskNameAlert()
      } else {
        _task.name <== taskNameField.text
        _task.notes <== notesArea.text
        okClicked = true
        dialogStage.close()
      }
    }
    
    def handleCancel() {
      dialogStage.close()
    }
    
    def handleKeyBoard(action: KeyEvent) {
      if(action.code == KeyCode.ENTER) {
        handleEditTask()
      } else if (action.code == KeyCode.ESCAPE) {
        handleCancel()
      }
    }
}