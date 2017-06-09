package bmlogic.retrieval

import java.util.Date

import bminjection.db.DBTrait
import bminjection.token.AuthTokenTrait
import bmlogic.common.sercurity.Sercurity
import bmlogic.retrieval.ConditionSearchFunc.ConditionSearchFunc
import bmlogic.retrieval.RetrievalData.RetrievalData
import bmlogic.retrieval.RetrievalMessage._
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import bmutil.errorcode.ErrorCode
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

import scala.collection.immutable.Map
import com.mongodb.casbah.Imports._

/**
  * Created by alfredyang on 01/06/2017.
  */
object RetrievalModule extends ModuleTrait with RetrievalData with ConditionSearchFunc {
    def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_ConditionSearchCommand(data) => conditionSearch(data)(pr)

        case msg_PushProduct(data) => pushProduct(data)
        case msg_UpdateProduct(data) => updateProduct(data)
        case msg_DeleteProduct(data) => deleteProduct(data)

        case msg_CalcPercentage(data) => calcPercentage(data)
        case msg_CalcTrend(data) => calcTrend(data)

        case _ => ???
    }

    def conditionSearch(data : JsValue)
                       (pr : Option[Map[String, JsValue]])
                       (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {


        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val condition = conditionParse(data, pr.get)
            val result = Map("search_result" -> toJson(db.queryMultipleObject(condition, "retrieval")))

            (Some(result), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def pushProduct(data : JsValue)
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o : DBObject = data
            val product_unit = (data \ "product_unit").asOpt[String].map (x => x).getOrElse(throw new Exception("input error"))
            val date = (data \ "date").asOpt[String].map (x => x).getOrElse(throw new Exception("input error"))
            o += "sales_id" -> Sercurity.md5Hash(product_unit + date)

            db.insertObject(o, "retrieval", "sales_id")

            (Some(Map(
                "retrival" -> toJson(o - "sales_id")
            )), None)

        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def updateProduct(data : JsValue)
                     (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o : DBObject = data
            db.updateObject(o, "retrieval", "sales_id")

            (Some(Map(
                "retrival" -> toJson(o - "sales_id")
            )), None)

        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def deleteProduct(data : JsValue)
                     (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o : DBObject = data
            db.deleteObject(o, "retrieval", "sales_id")

            (Some(Map(
                "retrival" -> toJson(o - "sales_id")
            )), None)

        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    //by clock
    def calcPercentage(data : JsValue)
                     (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
//            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
//            val att = cm.modules.get.get("att").map (x => x.asInstanceOf[AuthTokenTrait]).getOrElse(throw new Exception("no encrypt impl"))

            val queryName = (data \ "oral_name").asOpt[String].map (x => x).getOrElse((data \ "category").asOpt[String].map (x => x).getOrElse(throw new Exception("input error")))
            val province = (data \ "province").asOpt[String].map (x => x).getOrElse("all")
            val date = (data \ "date").asOpt[String].map (x => x).getOrElse(new Date().getTime)

            (Some(Map(
                "queryname" -> toJson(queryName),
                "province" -> toJson(province),
                "date" -> toJson(date.toString),
                "percentage" -> toJson("13.72 %")
            )), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    //by clock
    def calcTrend(data : JsValue)
                      (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
//            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
//            val att = cm.modules.get.get("att").map (x => x.asInstanceOf[AuthTokenTrait]).getOrElse(throw new Exception("no encrypt impl"))

            val queryName = (data \ "oral_name").asOpt[String].map (x => x).getOrElse((data \ "category").asOpt[String].map (x => x).getOrElse(throw new Exception("input error")))
            val province = (data \ "province").asOpt[String].map (x => x).getOrElse("all")
            val date = (data \ "date").asOpt[String].map (x => x).getOrElse(new Date().getTime)

//            val thisYearSales = ???
//            val listYearSales = ???

            (Some(Map(
                "queryname" -> toJson(queryName),
                "province" -> toJson(province),
                "date" -> toJson(date.toString),
                "trend" -> toJson("1.26 %")
            )), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}
