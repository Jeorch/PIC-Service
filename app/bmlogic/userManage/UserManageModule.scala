package bmlogic.userManage


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
        case msg_userManage_query(data) => query_user_func(data)
        case msg_deleteUserManage(data) => delete_user_func(data)

        case _ => ???
    }

    def query_user_func(data: JsValue)(implicit cm : CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {

            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

//            val createDate=db.queryMultipleObject(DBObject(),"users").map(x=>x.get("createDate").get)

            val userList=db.queryMultipleObject(DBObject(),"users").map(x=>x)
            if (userList.isEmpty) throw new Exception("unknown error")
            else {
                val record=userList.length
                val pages=record/10+1
                (Some(Map(
                    "serialNum" -> toJson(0),
                    "pageIndex" -> toJson(1),
                    "pageSize" -> toJson(10),
                    "results" -> toJson(userList),
                    "totalPage" -> toJson(pages),
                    "totalRecord" -> toJson(record)
                )), None)
            }

        } catch {
            case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }

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

}
