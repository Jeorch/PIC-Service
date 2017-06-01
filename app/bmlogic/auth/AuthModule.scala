package bmlogic.auth

import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import AuthMessage._
import bminjection.db.DBTrait
import bminjection.token.AuthTokenTrait
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
        case msg_AuthTokenParser(data) => authTokenPraser(data)
        case msg_CheckAuthTokenTest(data) => checkAuthTokenTest(data)(pr)
		case _ => ???
	}

    def authWithPassword(data : JsValue)(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
		try {
			val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

			val user_name = (data \ "user_name").asOpt[String].map (x => x).getOrElse(throw new Exception("input error"))
			val pwd = (data \ "pwd").asOpt[String].map (x => x).getOrElse(throw new Exception("input error"))

			val result = db.queryObject($and("user_name" -> user_name, "pwd" -> pwd), "users")
			if (result.isEmpty) throw new Exception("unkonw error")
			else (Some(result.get), None)
		} catch {
			case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
    }

    def queryUser(data : JsValue)(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val user_id = (data \ "conditions" \ "user_id").asOpt[String].getOrElse(throw new Exception("input error"))
            val result = db.queryObject(DBObject("user_id" -> user_id), "users")
            if (result.isEmpty) throw new Exception("unkonw error")
            else (Some(result.get), None)

        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def authTokenPraser(data : JsValue)(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val att = cm.modules.get.get("att").map (x => x.asInstanceOf[AuthTokenTrait]).getOrElse(throw new Exception("no encrypt impl"))

            val auth_token = (data \ "token").asOpt[String].map (x => x).getOrElse(throw new Exception("input error"))
            val auth = att.decrypt2JsValue(auth_token)
            (Some(Map("auth" -> auth)), None)

        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def checkAuthTokenTest(data : JsValue)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        println(pr)
        (Some(Map("test" -> toJson("test"))), None)
    }
}