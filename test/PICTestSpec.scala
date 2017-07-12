
import play.core.server.Server
import play.api.routing.sird._
import play.api.mvc._
import play.api.libs.json._
import play.api.test._

import scala.concurrent.Await
import scala.concurrent.duration._
import org.specs2.mutable.Specification
import play.api.libs.json.Json.toJson

/**
  * Created by alfredyang on 07/07/2017.
  */
class PICTestSpec extends Specification {
    import scala.concurrent.ExecutionContext.Implicits.global

    var token = ""

    val user_name = "alfred"
    val pwd = "12345"
    val start_time = "201501"
    val end_time = "201601"
    val category = "他汀类"
    val oral_name = "阿托伐他汀"
    val product_name = "阿乐"
    val edge = List("北京")
    val manufacture_name = List("北京嘉林药业股份有限公司")

    val condition3_21 = Map("date" -> toJson(Map("start" -> toJson(start_time),"end" -> toJson(end_time))),"category" -> toJson(category),"oral_name" -> toJson(oral_name))
    val condition3_23 = Map("date" -> toJson(Map("start" -> toJson(start_time),"end" -> toJson(end_time))),"category" -> toJson(category),"product_name" -> toJson(product_name))
    val condition3_25 = Map("date" -> toJson(Map("start" -> toJson(start_time),"end" -> toJson(end_time))),"category" -> toJson(category),"product_name" -> toJson(product_name),"edge" -> toJson(edge))
    val condition3_27 = Map("date" -> toJson(Map("start" -> toJson(start_time),"end" -> toJson(end_time))),"category" -> toJson(category),"product_name" -> toJson(product_name),"edge" -> toJson(edge),"manufacture_name" -> toJson(manufacture_name))

    val contains = "他汀类"

    val skip = 1
    val time_out = 120

    override def is = s2"""
        This is a PIC specification to check the 'conditionSearch' string

            The 'PIC ' conditionSearch functions should
                auth with password user_name:${user_name},pwd:${pwd}                        $authToken
                search with condition=>category: ${category}, oral_name: ${oral_name}, date=>start: ${start_time}, end: ${end_time}                                 $testCase3_21
                search with condition=>category: ${category}, product_name: ${product_name}, date=>start: ${start_time}, end: ${end_time}                           $testCase3_23
                search with condition=>category: ${category}, product_name: ${product_name}, edge: ${edge.head}, date=>start: ${start_time}, end: ${end_time}       $testCase3_25
                search with condition=>category: ${category}, product_name: ${product_name}, edge: ${edge.head}, manufacture_name: ${manufacture_name.head}, date=>start: ${start_time}, end: ${end_time}       $testCase3_27
                                                                              """

    def authToken =
    WsTestClient.withClient { client =>
        val result = Await.result(
            new PICClient(client, "http://127.0.0.1:9000").authWithPasswordTest(user_name, pwd), time_out.seconds)
        token = result
        result must_!= ""
    }

    def testCase3_21 =
    WsTestClient.withClient { client =>
        val result = Await.result(
            new PICClient(client, "http://127.0.0.1:9000").conditionSearchTest(token, condition3_21, skip, contains), time_out.seconds)
        result must_== 0
    }
    def testCase3_23 =
    WsTestClient.withClient { client =>
        val result = Await.result(
            new PICClient(client, "http://127.0.0.1:9000").conditionSearchTest(token, condition3_23, skip, contains), time_out.seconds)
        result must_== 0
    }
    def testCase3_25 =
    WsTestClient.withClient { client =>
        val result = Await.result(
            new PICClient(client, "http://127.0.0.1:9000").conditionSearchTest(token, condition3_25, skip, contains), time_out.seconds)
        result must_== 0
    }
    def testCase3_27 =
    WsTestClient.withClient { client =>
        val result = Await.result(
            new PICClient(client, "http://127.0.0.1:9000").conditionSearchTest(token, condition3_27, skip, contains), time_out.seconds)
        result must_== 0
    }
}
