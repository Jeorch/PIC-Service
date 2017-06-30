package bmlogic.userManage


import java.text.SimpleDateFormat
import java.util.Date

import bmutil.errorcode.ErrorCode
import bminjection.db.DBTrait
import bmlogic.auth.AuthData.AuthData
import bmlogic.userManage.UserManageMessage._
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

import scala.collection.immutable.Map

/**
  * Created by jeorch on 17-6-27.
  */
object UserManageModule extends ModuleTrait with AuthData {
    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])(implicit cm : CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_deleteUserManage(data) => delete_user_func(data)
        case msg_saveUserManage(data) => save_user_func(data)

        case _ => ???
    }

    def delete_user_func(data: JsValue)(implicit cm : CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o : DBObject = data
            db.deleteObject(o,"users","user_name")

            (Some(Map(
                "delete_result" -> toJson("ok")
            )), None)

        } catch {
            case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }

    }
    def save_user_func(data : JsValue)(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
            val sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val o : DBObject = data
            o += "user_id" -> (data \ "user_id").get.asOpt[String].get
            val date = (data \ "date").get.asOpt[String].get
            o += "date" -> sdf.parse(date).getTime.asInstanceOf[Number]
            o += "updateDate" -> new Date().getTime.asInstanceOf[Number]

            db.updateObject(o,"users","user_id")

            (Some(Map(
                "update_result" -> toJson("ok")
            )), None)

        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

}
