package model
import scalafx.beans.property.{StringProperty, ObjectProperty}
//sealed trait TaskStatus
//case object DONE extends TaskStatus
//case object ACTIVE extends TaskStatus

@SerialVersionUID(100L)
class Task(name_ : String, var notes_ : String, status_ : String) extends Serializable {
  var name = new StringProperty(name_)
  var notes = new StringProperty(notes_)
  var status = new StringProperty(status_)
  
	def done {
		status.set("Done")
	}

	override def toString() : String = {
		return name.get()
	}

}

object Task {
	def apply(name: String, notes: String, status: String) : Task = {
		return new Task(name, notes, status)
	}
}