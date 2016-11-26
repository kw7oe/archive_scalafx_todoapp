package todoapp.database

import scalafx.collections.ObservableBuffer
import scala.io.Source
import java.io.{FileWriter, BufferedWriter, File}
import java.io.{FileNotFoundException, IOException, EOFException}
import java.io.{FileOutputStream, FileInputStream, ObjectOutputStream, ObjectInputStream}
import todoapp.model.Task

class FileData {

  private var tasks = new ObservableBuffer[Task]()  
  private var doneTasks = new ObservableBuffer[Task]()
  private var fileName = "data.csv"
  
  def readFile() {
    try {
      for(line <- Source.fromFile(fileName).getLines()) {
        val array = line.split(",")
        val name = array(0)
        val notes = array(1)
        val status = array(2)
        val tempTask = Task(name, notes, status)
        this.append(tempTask)
      }
    } catch {
      case ex: FileNotFoundException => None
      case ex: IOException => println(ex)
    }
  }
  
  def writeFile() {
    try {
      val file = new File(fileName)
      val bw = new BufferedWriter(new FileWriter(file))
      var line = formattingFor(tasks)
      line += formattingFor(doneTasks)
      bw.write(line)
      bw.close()
    } catch {
      case ex: FileNotFoundException => println("File unavailable")
      case ex: IOException => println(ex)
    }
  }

  def getTasks() : ObservableBuffer[Task] = {
    return tasks
  }

  def getTasksBasedOn(status: String) : Option[ObservableBuffer[Task]] = {
    if (isComplete(status)) {
      return Option(doneTasks)
    }
    return Option(tasks)
  }

  def append(task: Task) {
    if (isComplete(task.status.value)) {
      doneTasks += task
    } else {
      tasks += task
    }
    writeFile()
  }

  def removeTaskAt(index: Int, status: String) {
    if (isComplete(status)) {
      doneTasks.remove(index)
    } else {
      tasks.remove(index)
    }
    writeFile()
  }
  
  def doneTaskAt(index: Int) {
    // Mark Task as Complete
    tasks(index).complete
    
    // Remove Task for tasks, and append to doneTasks
    doneTasks.append(tasks.remove(index))
    writeFile()
  }
  
  private def isComplete(taskStatus: String) : Boolean = {
    return taskStatus == "Completed"
  }
  
  // Organize Tasks into CSV Format
  private def formattingFor(array: ObservableBuffer[Task]) : String = {
    var line = "";
    for (task <- array) {
      task.formatNotesFor(mode = "csv")
      line += task
    }
    return line
  }
}