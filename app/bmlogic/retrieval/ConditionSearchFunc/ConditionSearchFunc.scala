package bmlogic.retrieval.ConditionSearchFunc

import play.api.libs.json.JsValue
import com.mongodb.casbah.Imports._

/**
  * Created by alfredyang on 08/06/2017.
  */
trait ConditionSearchFunc {
    def conditionParse(js : JsValue, pr : Map[String, JsValue]) : DBObject = {

        val data = (js \ "condition").asOpt[JsValue].map (x => x).getOrElse(throw new Exception("search condition parse error"))

        /**
          * 生产厂商名
          */
        val manufacture_name_condition =
            pr.get("search_manufacture_name_condition").
                map (x => x).getOrElse(throw new Exception("search condition parse error"))

        /**
          * 区域
          */
        val edge_condition =
            pr.get("search_edge_condition").
                map (x => x).getOrElse(throw new Exception("search condition parse error"))

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
        ("oral_name" :: "product_name" :: "manufacture_type"
            :: "product_type" :: "specifications" :: "package" :: Nil)
                .map (equalsConditions[String](data, _)).filterNot(_ == None).map (_.get)

        if (result.isEmpty) DBObject()
        else $and(result)
    }

    def equalsConditions[T](data : JsValue, name : String) : Option[DBObject] =
        (data \ name).asOpt[T].map (x => Some(name $eq x)).getOrElse(None)
}
