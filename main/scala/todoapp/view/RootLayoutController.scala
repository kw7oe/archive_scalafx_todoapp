package todoapp.view

import todoapp.Application
import scalafxml.core.macros.sfxml
import scalafx.scene.control.{MenuItem, Button, ButtonType}
import scalafx.Includes._
import javafx.{scene => jfxs}
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafxml.core.{NoDependencyResolver, FXMLLoader}
import scalafx.application.Platform

@sfxml
class RootLayoutController(
    private val closeMenuItem: MenuItem,
    private val aboutMenuItem: MenuItem) {
  
  def closeApp() {
    val alert = new Alert(AlertType.Confirmation) {
      initOwner(Application.stage)
      title = "Confirm Exit"
      headerText = "Are you sure you want to exit?"
    }
    
    val result = alert.showAndWait()

    result match {
      case Some(ButtonType.OK) => Platform.exit()
      case _                   => 
    }    
  }
  
  def showAboutDialog() {
    Application.showAboutDialog()
  }
  
  def viewShortcutDialog() {
    Application.showShortcutDialog()
  }

}