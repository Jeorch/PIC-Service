package bmlogic.aggregateCalc

import java.util.Date

import bminjection.db.DBTrait
import bmlogic.aggregateCalc.AggregateCalcMessage._
import bmlogic.conditions.ConditionSearchFunc
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import bmutil.errorcode.ErrorCode
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import com.mongodb.casbah.Imports._

/**
  * Created by jeorch on 17-6-12.
  */
object AggregateModule extends ModuleTrait with ConditionSearchFunc {

    def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_CalcPercentage(data) => calcPercentage(data)(pr)
        case msg_CalcTrend(data) => calcTrend(data)(pr)
//        case msg_CalcMarketSize(data) => calcMarketSize(data)(pr)
        case msg_CalcMarketSize(data) => calcMarketSize2(data)(pr)


        case _ => ???
    }

    def calcPercentage(data : JsValue)
                      (pr : Option[Map[String, JsValue]])
                      (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val oral_name_condition = oralNameConditionParse(data)
            val product_name_condition = productNameConditionParse(data)

            val parent_name_conditon =
            if (oral_name_condition.isEmpty && product_name_condition.isEmpty) throw new Exception("calc percentage without oral name or product name")
            else parentNameConditionParse(data)

            val basic_conditions = (conditionParse(data, pr.get) :: dateConditionParse(data) :: Nil)

            val group = MongoDBObject("_id" -> MongoDBObject("ms" -> "market size"), "sales" -> MongoDBObject("$sum" -> "$sales"))

            val ori_con = (oral_name_condition :: product_name_condition :: basic_conditions).
                                filterNot(_ == None).map (_.get)

            val par_con = (parent_name_conditon :: basic_conditions).filterNot(_ == None).map (_.get)

            val ori_result = db.aggregate($and(ori_con), "retrieval", group){ x =>
                Map("sales" -> toJson(aggregateSalesResult(x, "market size")))
            }

            val par_result = db.aggregate($and(par_con), "retrieval", group){ x =>
                Map("sales" -> toJson(aggregateSalesResult(x, "market size")))
            }

            val percentage =
            if (ori_result.isEmpty || par_result.isEmpty) throw new Exception("")
            else (ori_result.get.get("sales").get.asOpt[Long].get.floatValue()) /
                    (par_result.get.get("sales").get.asOpt[Long].get.floatValue())

            (Some(Map(
                "percentage" -> toJson(percentage)
            )), None)

        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def calcTrend(data : JsValue)
                 (pr : Option[Map[String, JsValue]])
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

    /**
      * 这个速度太慢，利用MongoDB的 Advance Map Reduce 操作 Aggregate 查询
      * 详情见 calcMarketSize2
      */
    def calcMarketSize(data : JsValue)
                      (pr : Option[Map[String, JsValue]])
                      (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val condition = (conditionParse(data, pr.get) :: dateConditionParse(data) ::
                                oralNameConditionParse(data) :: productNameConditionParse(data) :: Nil).
                                    filterNot(_ == None).map(_.get)
            val result = db.querySum($and(condition), "retrieval"){(s, a) =>
                val os = s.get("sales").map (x => x.asOpt[Long].get).getOrElse(0.toLong)
                val as = a.get("sales").map (x => x.asOpt[Long].get).getOrElse(0.toLong)
                Map("sales" -> toJson(os + as))
            } { o =>
                Map(
                    "sales" -> toJson(o.getAs[Number]("sales")
                                        .map (x => x.longValue)
                                        .getOrElse(throw new Exception("product without sales value")))
                )
            }

            if (result.isEmpty) throw new Exception("calc market size func error")
            else (result, None)

        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    /**
      * 市场规模：客户输入的市场的最新月份，累计一年的销售额
      * 累计一年的条件没有添加，这个算是一个练习，@qianpeng
      */
    def calcMarketSize2(data : JsValue)
                       (pr : Option[Map[String, JsValue]])
                       (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

         try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val condition = (conditionParse(data, pr.get) :: dateConditionParse(data) ::
                                oralNameConditionParse(data) :: productNameConditionParse(data) :: Nil).
                                    filterNot(_ == None).map(_.get)
            val group = MongoDBObject("_id" -> MongoDBObject("ms" -> "market size"), "sales" -> MongoDBObject("$sum" -> "$sales"))

            val result = db.aggregate($and(condition), "retrieval", group){ x =>
                Map("sales" -> toJson(aggregateSalesResult(x, "market size")))
            }

            if (result.isEmpty) throw new Exception("calc market size func error")
            else (Some(Map("calc" -> toJson(result))), None)

        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def aggregateSalesResult(x : MongoDBObject, id : String) : Long = {
        val ok = x.getAs[Number]("ok").get.intValue
        if (ok == 0) throw new Exception("db aggregation error")
        else {
            val lst : BasicDBList = x.getAs[BasicDBList]("result").get
            val tmp = lst.toList.asInstanceOf[List[BasicDBObject]]
            tmp.find(y => y.getAs[BasicDBObject]("_id").get.getString("ms") == id).map { z =>
                z.getLong("sales") / 100
            }.getOrElse(throw new Exception("db aggregation error"))
        }
    }
}
