
import play.core.server.Server
import play.api.routing.sird._
import play.api.mvc._
import play.api.libs.json._
import play.api.test._

import scala.concurrent.Await
import scala.concurrent.duration._

import org.specs2.mutable.Specification

/**
  * Created by alfredyang on 07/07/2017.
  */
class PICTestSpec extends Specification {
    import scala.concurrent.ExecutionContext.Implicits.global

    override def is = s2"""
        This is a PIC specification to check the 'auth with password' string

            The 'PIC ' auth functions should
                login with user alfred and password 12345                     $e1
                                                                              """

    def e1 =
    WsTestClient.withClient { client =>
        val result = Await.result(
            new PICClient(client, "http://127.0.0.1:9000").authWithPasswordTest("alfred", "12345"), 5.seconds)
        result must_== "ok"
    }
}
