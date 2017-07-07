import javax.inject.Inject

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
                (response.json \ "status").asOpt[String].get
            }
    }
}
