package bmlogic.config

import bminjection.db.DBTrait
import bmlogic.config.ConfigData.ConfigData
import bmlogic.config.ConfigMessage.msg_QueryProvinceCommand
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

import scala.collection.immutable.Map
import com.mongodb.casbah.Imports._

/**
  * Created by alfredyang on 08/06/2017.
  */
object ConfigModule extends ModuleTrait with ConfigData{
    def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_QueryProvinceCommand(data) => provinceQuery(data)

        case _ => ???
    }

    def provinceQuery(data : JsValue)(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
        
        val result = db.queryMultipleObject(DBObject("index" -> "PIC"), "config")
//        val province = result map (x => toJson(x.get("province")))
//        val lev_one= result map (x=>x.get("category").filter(x=>(x \ "level").asOpt[Number].get.intValue() !=0).map(x=>x \ "des"))
//        val lev_two= result map (x=>x.get("category").filter(x=>(x \ "level").asOpt[Number].get.intValue() !=1).map(x=>x \ "des"))
//        val lev_thr= result map (x=>x.get("category").filter(x=>(x \ "level").asOpt[Number].get.intValue() !=2).map(x=>x \ "des"))
//        val province=result(1).get("province")
     //   val lev_one=result(2).get("category").toList.filter(x=>(x \ "level").asOpt[Number].get.intValue() !=0).map(x=>x \ "des")
//        val lev_two=result(2).get("category").toList.filter(x=>((x \ "level") !=1)).map(x=>x \ "des")
//        val lev_thr=result(2).get("category").toList.filter(x=>((x \ "level") !=2)).map(x=>x \ "des")
//        println(lev_one)
       
        (Some(Map(
            "info" -> toJson(result)
            
 //           "province"->toJson(province)
//            "lev_one"->toJson(lev_one),
//            "lev_one"->toJson(lev_two),
//            "lev_one"->toJson(lev_thr)
        )), None)
    }
}
