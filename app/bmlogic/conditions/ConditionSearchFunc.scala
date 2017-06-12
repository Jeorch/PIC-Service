package bmlogic.conditions

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue

/**
  * Created by alfredyang on 08/06/2017.
  */
trait ConditionSearchFunc {
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
          * 日期
          */
        val timespan_condition = (data \ "date").asOpt[Long].map (x => x).getOrElse(-1.toLong)

        /**
          * TODO: 数据库设计修改，先不动
          * data_type: 0 是月， 1 是年
          *     我只做月，1 留给你们做
          */
        val date_type_condition = (data \ "data_type").asOpt[Int].map (x => x).getOrElse(0)

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
