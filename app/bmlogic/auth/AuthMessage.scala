package bmlogic.auth

import play.api.libs.json.JsValue
import bmmessages.CommonMessage

abstract class msg_AuthCommand extends CommonMessage

object AuthMessage {
	case class msg_AuthWithPassword(data : JsValue) extends msg_AuthCommand
	case class msg_AuthTokenParser(data : JsValue) extends msg_AuthCommand
	case class msg_CheckAuthTokenTest(data : JsValue) extends msg_AuthCommand
}
