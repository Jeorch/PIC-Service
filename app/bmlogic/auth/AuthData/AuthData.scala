package bmlogic.auth.AuthData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by alfredyang on 01/06/2017.
  */
trait AuthData {
    implicit val m2d : JsValue => DBObject = { js =>
        val build = MongoDBObject.newBuilder
        val user_name = (js \ "user_name").asOpt[String].map (x => x).getOrElse(throw new Exception("input error"))
        val pwd = (js \ "pwd").asOpt[String].map (x => x).getOrElse(throw new Exception("input error"))
        build += "user_name" -> user_name
        build += "pwd" -> pwd

        build += "screen_name" -> (js \ "screen_name").asOpt[String].map (x => x).getOrElse("")
        build += "screen_photo" -> (js \ "screen_photo").asOpt[String].map (x => x).getOrElse("")
        build += "phoneNo" -> (js \ "phoneNo").asOpt[String].map (x => x).getOrElse("")
        build += "email" -> (js \ "email").asOpt[String].map (x => x).getOrElse("")

        // 权限这个我再想想
        build += "scrope" -> MongoDBList.newBuilder.result

        build.result
    }

    // for query
    implicit val d2m : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "screen_name" -> toJson(obj.getAs[String]("screen_name").map (x => x).getOrElse(throw new Exception("db prase error"))),
            "screen_photo" -> toJson(obj.getAs[String]("screen_photo").map (x => x).getOrElse(throw new Exception("db prase error")))
        )
    }
}
