package bmlogic.auth.AuthScopes

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by alfredyang on 02/06/2017.
  */
trait AuthScope {
    def pushEdgeScope(data : JsValue) : MongoDBList = {
        val result = MongoDBList.newBuilder
        (data \ "scope" \ "edge").asOpt[List[String]].map (x => x).getOrElse(Nil).foreach(result += _)
        result.result
    }

    def pushProduceLevelScope(data : JsValue) : DBObject = {
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

    def queryEdgeScope(obj : MongoDBObject) : JsValue = {
        val scope = obj.getAs[MongoDBObject]("scope").map (x => x).getOrElse(throw new Exception("db prase error"))
        val lst = scope.getAs[List[String]]("edge").map (x => x).getOrElse(throw new Exception("db prase error"))
        toJson(lst)
    }

    def queryProductLevelScope(obj : MongoDBObject) : JsValue = {
        val scope = obj.getAs[MongoDBObject]("scope").map (x => x).getOrElse(throw new Exception("db prase error"))
        val product_level = scope.getAs[MongoDBObject]("product_level").map (x => x).getOrElse(throw new Exception("db prase error"))
        val product_level_one = product_level.getAs[String]("product_level_one").map (x => x).getOrElse(throw new Exception("db prase error"))
        val product_level_two = product_level.getAs[String]("product_level_two").map (x => x).getOrElse(throw new Exception("db prase error"))
        val product_level_three = product_level.getAs[String]("product_level_three").map (x => x).getOrElse(throw new Exception("db prase error"))
        toJson(
            Map("product_level" ->
                toJson(Map("product_level_one" -> product_level_one,
                            "product_level_two" -> product_level_two,
                            "product_level_three" -> product_level_three)))
        )
    }

    def queryManufactureNameScope(obj : MongoDBObject) : JsValue = {
        val scope = obj.getAs[MongoDBObject]("scope").map (x => x).getOrElse(throw new Exception("db prase error"))
        val lst = scope.getAs[List[String]]("manufacture_name").map (x => x).getOrElse(throw new Exception("db prase error"))
        toJson(lst)
    }

    def queryIsAdminScope(obj : MongoDBObject) : JsValue = {
        val scope = obj.getAs[MongoDBObject]("scope").map (x => x).getOrElse(throw new Exception("db prase error"))
        val is_admin = scope.getAs[Number]("is_admin").map (x => x).getOrElse(throw new Exception("db prase error"))
        toJson(is_admin.intValue)
    }
}
