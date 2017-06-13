package bmlogic.conditions

import java.text.SimpleDateFormat

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue

/**
  * Created by alfredyang on 08/06/2017.
  */
trait ConditionSearchFunc {
    val sdf = new SimpleDateFormat("yyyyMM")


    def dateConditionParse(js : JsValue) : DBObject = {
        val data = (js \ "condition").asOpt[JsValue].map (x => x).getOrElse(throw new Exception("search condition parse error"))

        val date_input = (data \ "date").asOpt[JsValue].map (x => Some(x)).getOrElse(None)

        val date_condition =
        date_input match {
            case Some(date) => {
                /**
                  * start : yyyyMM 形式的时间表达式
                  * end : yyyyMM 形式的时间表达式
                  */
                val start = (date \ "start").asOpt[String].map (x => x).
                                getOrElse(throw new Exception("search condition parse error"))
                val end = (date \ "end").asOpt[String].map (x => x).
                                getOrElse(throw new Exception("search condition parse error"))

                val start_date = sdf.parse(start).getTime
                val end_date = sdf.parse(end).getTime

                Some($and("date" $lt end_date, "date" $gte start_date))
            }
            case None => None
        }

        if (date_condition.isEmpty) DBObject()
        else date_condition.get
    }

    def conditionParse(js : JsValue, pr : Map[String, JsValue]) : DBObject = {

        val data = (js \ "condition").asOpt[JsValue].map (x => x).getOrElse(throw new Exception("search condition parse error"))

        /**
          * 生产厂商名
          */
        val mnc = pr.get("search_manufacture_name_condition").map (x => x.asOpt[List[String]].get).getOrElse(Nil)
        val manufacture_name_condition : Option[DBObject] =
            if (mnc.isEmpty) None
            else Some($or(mnc map ("manufacture" $eq _)))

        /**
          * 区域
          */
        val ec = pr.get("search_edge_condition").map (x => x.asOpt[List[String]].get).getOrElse(Nil)
        val edge_condition : Option[DBObject] =
            if (ec.isEmpty) None
            else Some($or(ec map ("province" $eq _)))

        /**
          * 类型
          */
        val cat = pr.get("search_category_condition").map (x => x.asOpt[List[String]].get).getOrElse(Nil)
        val cat_condition : Option[DBObject] =
            if (cat.isEmpty) None
            else Some($or(cat map ("category" $eq _)))

        /**
          * 通用名 * 产品名 * 生产厂商类型 * 剂型 * 规格 * 包装
          */
        val result =
            (cat_condition :: edge_condition :: manufacture_name_condition
                :: ("oral_name" :: "product_name" :: "manufacture_type"
                :: "product_type" :: "specifications" :: "package" :: Nil)
                    .map (equalsConditions[String](data, _))).filterNot(_ == None).map (_.get)

        if (result.isEmpty) DBObject()
        else $and(result)
    }

    def equalsConditions[T <: String](data : JsValue, name : String) : Option[DBObject] =
        (data \ name).asOpt[String].map (x => Some(name $eq x)).getOrElse(None)
}
