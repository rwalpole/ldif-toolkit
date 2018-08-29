package uk.co.devexe.services

import org.junit.Assert._
import org.junit._

class CsvServiceTest {

  @Test
  def testGetEntries(): Unit = {
    val service = new CsvServices
    val result = service.getEntries("src/test/resources/RSusers.csv")
    assertEquals(2, result.size)
  }

}
