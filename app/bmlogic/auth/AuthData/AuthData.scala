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

        val scope_builder = MongoDBObject.newBuilder
        scope_builder += "edge" -> pushEdgeScope(js)
        scope_builder += "product_level" -> pushProduceLevelScope(js)
        scope_builder += "manufacture_name" -> pushManufactureNameScope(js)

        build += "scope" -> scope_builder.result

        build.result
    }

    // for query
    implicit val d2m : DBObject => Map[String, JsValue] = { obj =>
        // 需要添加Scrope，的解析
        Map(
            "user_id" -> toJson(obj.getAs[String]("user_id").map (x => x).getOrElse(throw new Exception("db prase error"))),
            "user_name" -> toJson(obj.getAs[String]("user_name").map (x => x).getOrElse(throw new Exception("db prase error"))),
            "phoneNo" -> toJson(obj.getAs[String]("phoneNo").map (x => x).getOrElse(throw new Exception("db prase error"))),
            "email" -> toJson(obj.getAs[String]("email").map (x => x).getOrElse(throw new Exception("db prase error"))),
            "scope" -> toJson(obj.getAs[List[String]]("scope").map (x => x).getOrElse(throw new Exception("db prase error"))),
            "screen_name" -> toJson(obj.getAs[String]("screen_name").map (x => x).getOrElse(throw new Exception("db prase error"))),
            "screen_photo" -> toJson(obj.getAs[String]("screen_photo").map (x => x).getOrElse(throw new Exception("db prase error"))),
            "date" -> toJson(obj.getAs[Number]("date").map (x => x.longValue).getOrElse(throw new Exception("db prase error")))
        )
    }

    def pushEdgeScope(data : JsValue) : MongoDBList = {
        val result = MongoDBList.newBuilder
        (data \ "scope" \ "edge").asOpt[List[String]].map (x => x).getOrElse(Nil).foreach(result += _)
        result.result
    }

    def pushProduceLevelScope(data : JsValue) : MongoDBObject = {
        val result = MongoDBObject.newBuilder
        val tmp = "product_level_one" :: "product_level_two" :: "product_level_three" :: Nil
        tmp foreach(result += _ -> (data \ "scope" \ "product_level_one").asOpt[String].map (x => x).getOrElse(""))
        result.result
    }

    def pushManufactureNameScope(data : JsValue) : MongoDBList = {
        val result = MongoDBList.newBuilder
        (data \ "scope" \ "manufacture_name").asOpt[List[String]].map (x => x).getOrElse(Nil).foreach(result += _)
        result.result
    }
}
