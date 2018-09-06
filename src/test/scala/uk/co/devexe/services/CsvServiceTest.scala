package uk.co.devexe.services

import org.junit.jupiter.api.{Assertions, Test}

class CsvServiceTest {

  @Test
  def testGetEntries(): Unit = {
    val service = new CsvService
    val result = service.getEntries("src/test/resources/RSusers.csv")
    Assertions.assertEquals(2, result.size)
  }

}
