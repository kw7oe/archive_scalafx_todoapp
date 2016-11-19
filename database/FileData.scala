package database


import scalafx.collections.ObservableBuffer
import scala.io.Source
import java.io.{FileWriter, BufferedWriter, File}
import java.io.{FileNotFoundException, IOException, EOFException}
import java.io.{FileOutputStream, FileInputStream, ObjectOutputStream, ObjectInputStream}
import model.Task

class FileData {
	
  // To Do Tasks
	private var tasks = new ObservableBuffer[Task]()	
	private var fileName = "data.csv"
	
	// Done Tasks
	private var doneTasks = new ObservableBuffer[Task]()
	
	def readFile() {
	  try {
	    for(line <- Source.fromFile(fileName).getLines()) {
	      val array = line.split(",")
	      val name = array(0)
	      val notes = array(1)
	      val status = array(2)
	      val tempTask = Task(name, notes, status)
	      if (status == "Done") {
	        doneTasks.append(tempTask)
	      } else {
	        tasks.append(tempTask)
	      }
	    }
	  } catch {
	    case ex: FileNotFoundException => None
	    case ex: IOException => println(ex)
	  }
	}
	
	private def writeFile() {
	  try {
	    val file = new File(fileName)
	    val bw = new BufferedWriter(new FileWriter(file))
	    // Can be refactored
	    for(task <- tasks) {
	      val line = task.name.value + "," + task.notes.value + "," + task.status.value + "\n" 
	      bw.write(line)	      
	    }
	    for (task <- doneTasks) {
	      val line = task.name.value + "," + task.notes.value + "," + task.status.value + "\n" 
	      bw.write(line)
	    }
	    bw.close()
	  } catch {
	    case ex: FileNotFoundException => println("File unavailable")
	    case ex: IOException => println(ex)
	  }
	}
	
	def getTasks() : ObservableBuffer[Task] = {
	  return tasks
	}

	//  Using Option Pattern in scala
	def getTasksBasedOn(status: String) : Option[ObservableBuffer[Task]] = {
	  if (status == "Done") {
	    return Option(doneTasks)
	  }
		return Option(tasks)
	}

	def append(task: Task) {
		tasks += task
		writeFile()
	}

	def removeTaskAt(index: Int, status: String) {
	  if (status == "Done") {
	    doneTasks.remove(index)
	  } else {
		  tasks.remove(index)
	  }
		writeFile()
	}
	
	def doneTaskAt(index: Int) {
	  // Mark Task as Done
	  tasks(index).done
	  
	  // Remove Task for tasks, and append to doneTasks
	  doneTasks.append(tasks.remove(index))
	  writeFile()
	}
	
	def editTaskAt() {
	  writeFile()
	}
	

/* 
 * DEPRECATED METHODS
*/
	
//	def getDoneTasks : Option[ObservableBuffer[Task]] = {
//		return getTasksBasedOn(status = "Done")
//	}
//	
//	def getDefaultTasks : Option[ObservableBuffer[Task]] = {
//	  return getTasksBasedOn()
//	}
//	
//	def getTasksBasedOn(status: String) : Option[ObservableBuffer[Task]] = {
//		var array = new ObservableBuffer[Task]() 
//		for (task <- tasks) {
//			if (task.status.value == status) {
//				array += task
//			}
//		}
//		if (array.isEmpty) return None
//		return Some(array)
//	}



}