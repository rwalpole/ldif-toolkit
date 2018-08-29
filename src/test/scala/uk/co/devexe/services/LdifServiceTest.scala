package uk.co.devexe.services

import org.junit.Assert._
import org.junit._

class LdifServiceTest {

    @Test
    def testRead(): Unit = {
        val transformer = new LdifService
        val result = transformer.getEntries("src/test/resources/BA_Working.ldif")
        assertEquals(5, result.size())
    }

}


