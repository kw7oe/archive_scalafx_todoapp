package todoapp.model

import scalafx.beans.property.{StringProperty, ObjectProperty}

class Task(name_ : String, notes_ : String, status_ : String) extends Serializable {
  var name = new StringProperty(name_)
  var notes = new StringProperty(notes_)
  var status = new StringProperty(status_)
  
	def complete {
		status.set("Completed")
  }

  def getNotes() : StringProperty = {
    formatNotesFor(mode = "view")
    return notes
  }

	override def toString() : String = {
		return s"${name.value},${notes.value},${status.value}\n"
	}
	
	// Remove the newline in the notes, as a CSV
	// file differentiate each data with a newline
	def formatNotesFor(mode: String) {
	  var text : String = ""
	  if (mode == "csv") {
	    text = notes.value.replaceAll("\n", "_newline_")
	  } else if (mode == "view") {
	    text = notes.value.replaceAll("_newline_", "\n") 
	  }	  
	  notes = StringProperty(text)
	}

}

object Task {
	def apply(name: String, notes: String, status: String) : Task = {
		return new Task(name, notes, status)
	}
}