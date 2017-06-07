package bmlogic.auth.AuthScopes

import com.mongodb.casbah.Imports.{MongoDBList, MongoDBObject}
import play.api.libs.json.JsValue

/**
  * Created by alfredyang on 02/06/2017.
  */
trait AuthScope extends Enumeration {
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
