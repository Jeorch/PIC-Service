import javax.inject.Inject

import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by alfredyang on 07/07/2017.
  */
class PICClient(ws: WSClient, baseUrl: String)(implicit ec: ExecutionContext) {
    @Inject def this(ws: WSClient, ec: ExecutionContext) = this(ws, "http://127.0.0.1:9000")(ec)

    def authWithPasswordTest(name : String, pwd : String) : Future[String] = {
        ws.url(baseUrl + "/auth/password")
            .withHeaders("Accept" -> "application/json", "Content-Type" -> "application/json")
            .post(toJson(Map("user_name" -> name, "pwd" -> pwd)))
            .map { response =>
                println(response.json)
                (response.json \ "result").get.asOpt[Map[String,JsValue]].get.get("auth_token").get.asOpt[String].get
            }
    }

    def conditionSearchTest(token : String, condition : Map[String, JsValue], skip : Int, contains : String) : Future[Int] = {
        ws.url(baseUrl + "/data/search")
            .withHeaders("Accept" -> "application/json","Content-Type" -> "application/json")
                .post(toJson(Map("token" -> toJson(token),"condition" -> toJson(condition),"skip" -> toJson(skip))))
                .map{   response =>
                    println(response.json)
                    var result = 0
                    val pages = (response.json \ "page").get.asOpt[List[Map[String,JsValue]]].get.head.get("TOTLE_PAGE").get.asOpt[Int].get
                    if (pages > 0){
                        if ((response.json \ "search_result").asOpt[List[Map[String,JsValue]]].get.filterNot(x => x.get("html").get.asOpt[String].get.contains(contains)).length>0){
                            result = 1
                        }
                        if (result==0){
                            for (i <- 1 to pages){
                                ws.url(baseUrl + "/data/search")
                                    .withHeaders("Accept" -> "application/json","Content-Type" -> "application/json")
                                    .post(toJson(Map("token" -> toJson(token),"condition" -> toJson(condition),"skip" -> toJson(i))))
                                    .map{   response_ =>
                                        if ((response_.json \ "search_result").asOpt[List[Map[String,JsValue]]].get.filterNot(x => x.get("html").get.asOpt[String].get.contains(contains)).length>0){
                                            result = 1
                                        }
                                    }
                            }
                        }
                        result
                    }else{
                        println("--> No Results! <--")
                        result = 1
                        result
                    }
                }
    }
}
