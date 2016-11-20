package database

import scalafx.collections.ObservableBuffer
import scala.io.Source
import java.io.{FileWriter, BufferedWriter, File}
import java.io.{FileNotFoundException, IOException, EOFException}
import java.io.{FileOutputStream, FileInputStream, ObjectOutputStream, ObjectInputStream}
import model.Task

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
  
  private def writeFile() {
    try {
      val file = new File(fileName)
      val bw = new BufferedWriter(new FileWriter(file))
      var line = csvFormattingFor(tasks)
      line += csvFormattingFor(doneTasks)
      bw.write(line)
      bw.close()
    } catch {
      case ex: FileNotFoundException => println("File unavailable")
      case ex: IOException => println(ex)
    }
  }
  
  // Might be removed
  def getTasks() : ObservableBuffer[Task] = {
    return tasks
  }

  //  Using Option Pattern in Scala
  def getTasksBasedOn(status: String) : Option[ObservableBuffer[Task]] = {
    if (isDone(status)) {
      return Option(doneTasks)
    }
    return Option(tasks)
  }

  def append(task: Task) {
    if (isDone(task.status.value)) {
      doneTasks += task
    } else {
      tasks += task
    }
    writeFile()
  }

  def removeTaskAt(index: Int, status: String) {
    if (isDone(status)) {
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
  
  // Check if Status is Done
  private def isDone(taskStatus: String) : Boolean = {
    return taskStatus == "Done"
  }
  
  // Format Tasks into CSV Format
  private def csvFormattingFor(array: ObservableBuffer[Task]) : String = {
    var line = "";
    for (task <- array) {
      line += (s"${task.name.value},${task.notes.value},${task.status.value}\n") 
    }
    return line
  }
}