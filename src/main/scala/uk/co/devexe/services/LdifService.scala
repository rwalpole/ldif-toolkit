package uk.co.devexe.services

import java.io.FileReader
import java.util

import org.ldaptive.LdapEntry
import org.ldaptive.io.LdifReader

class LdifService {

  def getEntries(fileName: String): util.Collection[LdapEntry] = {
    val reader = new FileReader(fileName)
    val ldifReader = new LdifReader(reader)
    val result = ldifReader.read()
    result.getEntries
  }


}
