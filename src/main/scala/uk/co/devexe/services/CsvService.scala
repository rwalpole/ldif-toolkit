package uk.co.devexe.services

import scala.annotation.tailrec

case class User(id:String,group:String)

class CsvServices {

  def getEntries(fileName: String): Map[String,List[String]] = {
    val bufferedSource = io.Source.fromFile(fileName)
    createMap(bufferedSource.getLines.toList, Map[String,List[String]]())
  }

  @tailrec
  private def createMap(lines:List[String],map: Map[String,List[String]]): Map[String,List[String]] = {
    if(lines.isEmpty){
       return map
    }
    val line = lines.head
    val value = line.split(',')
    val id = value(0)
    val group = value(1)
    map.get(id) match {
      case Some(groups) => {
        createMap(lines.tail, map + (id -> (groups ++ List(group))))
      }
      case None => {
        createMap(lines.tail, map + (id -> List(group)))
      }
    }
  }

}
