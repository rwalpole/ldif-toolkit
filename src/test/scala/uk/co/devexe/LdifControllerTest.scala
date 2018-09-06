package uk.co.devexe

import org.junit.jupiter.api.Test

class LdifControllerTest {

  @Test
  def test(): Unit = {
    LdifController.run("src/test/resources/BA_Working.ldif","src/test/resources/RSusers.csv")
  }

}
