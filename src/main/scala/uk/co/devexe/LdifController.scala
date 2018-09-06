package uk.co.devexe

import java.io.PrintWriter

import org.ldaptive.{LdapAttribute, LdapEntry, SearchResult}
import uk.co.devexe.services.{CsvService, LdifService}

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

object LdifController {

  def run(ldifFileName: String, csvFileName: String): Unit = {
    val controller = new LdifController(ldifFileName, csvFileName)
    val result = controller.merge
    controller.write(result)
  }

}

class LdifController(ldifFileName: String, csvFileName: String) {

  def merge(): SearchResult = {
    val ldifService = new LdifService
    val csvService = new CsvService
    val ldifEntries = ldifService.getEntries(ldifFileName)
    val csvEntries = csvService.getEntries(csvFileName)
    val groups = getGroups(csvEntries)
    val ldifResult = new SearchResult()
    val groupEntries = processGroups(groups)
    ldifResult.addEntries(groupEntries.asJava)
    val ldifIter = ldifEntries.iterator()
    while(ldifIter.hasNext) {
      val ldifEntry = ldifIter.next()
      if(ldifEntry.getDn.contains("employeeNumber")){
        val newEntry = processEmployee(cleanEntry(ldifEntry),csvEntries)
        ldifResult.addEntry(newEntry)
      }
    }
    ldifResult
  }

  def getGroups(csvEntries: Map[String,List[String]]): List[String] = {
    val groups = ListBuffer[String]()
    csvEntries.map(value =>
      value._2.foreach(group =>
        if(!groups.contains(group)){
          groups += group
        }
      )
    )
    groups.toList
  }

  def cleanEntry(entry: LdapEntry): LdapEntry = {
    entry.removeAttribute("result")
    entry.removeAttribute("search")
    entry
  }

  def processEmployee(entry: LdapEntry, csvEntries: Map[String,List[String]]): LdapEntry = {
    val uid = entry.getAttribute("uid").getStringValue.toLowerCase
    csvEntries.get(uid) match {
      case Some(groups) => {
        val dn = entry.getDn
        val dnBuilder = new StringBuilder(dn)
        groups.foreach(group =>  dnBuilder.append(",ou=" + group))
        entry.setDn(dnBuilder.mkString)
        entry
      }
      case None => entry
    }
  }

  def processGroups(groups: List[String]): List[LdapEntry] = {
    val entries = new ListBuffer[LdapEntry]()
    groups.foreach(group => {
      val entry = new LdapEntry()
      val ouAttrib = new LdapAttribute("ou", group)
      entry.addAttribute(ouAttrib)
      val objClassAttrib = new LdapAttribute("objectClass", "top","organizationalUnit")
      entry.addAttribute(objClassAttrib)
      // TODO create baplc entry and add below..
      entry.setDn("ou=" + group + ",dc=ba,dc=com")
      entries += entry
    })
    entries.toList

  }

  def write(result: SearchResult): Unit = {
    import org.ldaptive.io.LdifWriter
    import java.io.StringWriter
    val writer = new StringWriter
    val ldifWriter = new LdifWriter(writer)
    ldifWriter.write(result)
    val printWriter = new PrintWriter("result.ldif")
    printWriter.write(writer.toString)
    printWriter.close()
  }

}
