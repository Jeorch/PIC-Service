package bmlogic.config

import bminjection.db.DBTrait
import bmlogic.config.ConfigData.ConfigData
import bmlogic.config.ConfigMessage.{msg_QueryInfoCommand}
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
        case msg_QueryInfoCommand(data) => infoQuery(data)

        case _ => ???
    }

    def infoQuery(data : JsValue)(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
        val result = db.queryMultipleObject(DBObject("index" -> "PIC"), "config")
        (Some(Map(
            "info" -> toJson(result)

        )), None)
    }
   
}
