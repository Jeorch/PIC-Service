package bmlogic.auth

import play.api.libs.json.JsValue
import AuthMessage._
import bminjection.db.DBTrait
import bmlogic.auth.AuthData.AuthData
import bmmessages.MessageDefines
import bmmessages.CommonModules
import bmpattern.ModuleTrait
import bmutil.errorcode.ErrorCode

import scala.collection.immutable.Map
import com.mongodb.casbah.Imports._

object AuthModule extends ModuleTrait with AuthData {

	def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
		case msg_AuthWithPassword(data) => authWithPassword(data)
		case _ => ???
	}

    def authWithPassword(data : JsValue)(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
		try {
			val db = cm.modules.map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

			val user_name = (data \ "user_name").asOpt[String].map (x => x).getOrElse(throw new Exception("input error"))
			val pwd = (data \ "pwd").asOpt[String].map (x => x).getOrElse(throw new Exception("input error"))

			val result = db.queryObject($and("user_name" -> user_name, "pwd" -> pwd), "users")
			if (result.isEmpty) throw new Exception("unkonw error")
			else (Some(result.get), None)
		} catch {
			case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
    }
}