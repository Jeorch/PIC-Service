package bmlogic.config

import bmlogic.config.ConfigMessage.msg_QueryProvinceCommand
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import play.api.libs.json.JsValue

import scala.collection.immutable.Map

/**
  * Created by alfredyang on 08/06/2017.
  */
object ConfigModule extends ModuleTrait {
    def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_QueryProvinceCommand(data) => proviceQuery(data)

        case _ => ???
    }

    def proviceQuery(data : JsValue)(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        // TODO : 数据库中有这些数据了，谁弄出来
        null
    }
}
