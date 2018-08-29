package uk.co.devexe

import uk.co.devexe.services.LdifService

object LdifController {

  def merge(ldifFileName: String, csvFileName: String): Unit = {
    val transformer = new LdifService
    val entries = transformer.getEntries(ldifFileName)
  }

}
