package bmlogic.auth

import java.util.Date

import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import AuthMessage._
import bminjection.db.DBTrait
import bminjection.token.AuthTokenTrait
import bmlogic.auth.AuthData.AuthData
import bmlogic.common.sercurity.Sercurity
import bmmessages.MessageDefines
import bmmessages.CommonModules
import bmpattern.ModuleTrait
import bmutil.errorcode.ErrorCode

import scala.collection.immutable.Map
import com.mongodb.casbah.Imports._

object AuthModule extends ModuleTrait with AuthData {

	def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_AuthPushUser(data) => authPushUser(data)
		case msg_AuthWithPassword(data) => authWithPassword(data)
        case msg_AuthTokenParser(data) => authTokenPraser(data)
        case msg_CheckAuthTokenTest(data) => checkAuthTokenTest(data)(pr)
		case _ => ???
	}

    def authPushUser(data : JsValue)(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
            val att = cm.modules.get.get("att").map (x => x.asInstanceOf[AuthTokenTrait]).getOrElse(throw new Exception("no encrypt impl"))

            val date = new Date().getTime
            val o : DBObject = data
            val user_name = (data \ "user_name").asOpt[String].map (x => x).getOrElse(throw new Exception("input error"))
            val pwd = (data \ "pwd").asOpt[String].map (x => x).getOrElse(throw new Exception("input error"))
            o += "user_id" -> Sercurity.md5Hash(user_name + pwd + Sercurity.getTimeSpanWithMillSeconds)
            o += "date" -> date.asInstanceOf[Number]

            db.insertObject(o, "users", "user_name")
            val result = toJson(o - "pwd" - "phoneNo" - "email" - "date" + ("expire_in" -> toJson(date + 60 * 60 * 1000 * 24))) // token 默认一天过期
            val auth_token = att.encrypt2Token(toJson(result))
            val reVal = toJson(o - "user_id" - "pwd" - "phoneNo" - "email" - "date" - "scope")

            (Some(Map(
                "auth_token" -> toJson(auth_token),
                "user" -> reVal
            )), None)

        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def authWithPassword(data : JsValue)(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
		try {
			val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

			val user_name = (data \ "user_name").asOpt[String].map (x => x).getOrElse(throw new Exception("input error"))
			val pwd = (data \ "pwd").asOpt[String].map (x => x).getOrElse(throw new Exception("input error"))

			val result = db.queryObject($and("user_name" -> user_name, "pwd" -> pwd), "users")
            val date = new Date().getTime

            if (result.isEmpty) throw new Exception("unkonw error")
			else {
                val att = cm.modules.get.get("att").map (x => x.asInstanceOf[AuthTokenTrait]).getOrElse(throw new Exception("no encrypt impl"))
                val reVal = result.get + ("expire_in" -> toJson(date + 60 * 60 * 1000 * 24))
                val auth_token = att.encrypt2Token(toJson(reVal))

                (Some(Map(
                    "auth_token" -> toJson(auth_token),
                    "user" -> toJson(result.get)
                )), None)
            }
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