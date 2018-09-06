package uk.co.devexe.services

import org.junit.jupiter.api.{Assertions, Test}

class LdifServiceTest {

    @Test
    def testRead(): Unit = {
        val transformer = new LdifService
        val result = transformer.getEntries("src/test/resources/BA_Working.ldif")
        Assertions.assertEquals(5, result.size())
    }

}


