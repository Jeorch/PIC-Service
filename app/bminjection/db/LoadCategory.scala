package bminjection.db

import bmutil.dao.from
import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json._
/**
  * Created by qianpeng on 2017/6/14.
  */
object LoadCategory {
	
	
	private val d2m: DBObject => Map[String, JsValue] = { item =>
		Map("level" -> toJson(item.getAs[Number]("level").map(x => x.intValue()).getOrElse(0)),
			"parent" -> toJson(item.getAs[String]("parent").map(x => x).getOrElse("")),
			"def" -> toJson(item.getAs[String]("def").map(x => x).getOrElse("")),
			"des" -> toJson(item.getAs[String]("des").map(x => x).getOrElse(""))
		)
	}
	
	lazy val category: Option[List[Map[String, JsValue]]] = Some((from db() in "category").select(x => d2m(x)).toList)
}
