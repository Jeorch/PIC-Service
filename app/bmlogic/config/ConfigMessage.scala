package bmlogic.config

/**
  * Created by alfredyang on 08/06/2017.
  */
import bmmessages.CommonMessage
import play.api.libs.json.JsValue

abstract class msg_ConfigCommand extends CommonMessage

object ConfigMessage {
    case class msg_QueryProvinceCommand(data : JsValue) extends msg_ConfigCommand
}
