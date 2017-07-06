package bmlogic.common

import play.api.libs.json.JsValue
import play.api.libs.json.Json._
import bminjection.db.LoadCategory._

/**
  * Created by qianpeng on 2017/6/15.
  */
object LeftAtcLinkAge {
	lazy val c = category
	
	def linkage(value: JsValue): Int ={
		val data = (value \ "condition").asOpt[JsValue].map (x => x).getOrElse(throw new Exception("search condition parse error"))
		val category = (data \ "product_name").asOpt[String].getOrElse((data \ "oral_name").asOpt[String].getOrElse((data \ "category").asOpt[String].getOrElse("")))
		(AtcLinkAge(Some(category))(c) :: AtcOralLinkAge(Some(category))(c) :: Nil).find(x => x != 0).getOrElse(0)
	}
}

trait LinkAge {
	def atclinkage(lst: Option[String])(pr: Option[List[Map[String, JsValue]]]): Int
}

object AtcLinkAge extends LinkAge {
	
	def apply(obj: Option[String])(pr: Option[List[Map[String, JsValue]]]): Int = atclinkage(obj)(pr)
	override def atclinkage(obj: Option[String])(pr: Option[List[Map[String, JsValue]]]): Int = {
		getChild(obj.get)(pr).map(x => pr.get.filter(z => z.get("parent").get.as[String] == x.get("des").get.as[String]).size).sum
	}
	
	def getChild(obj: String)(pr: Option[List[Map[String, JsValue]]]): List[Map[String, JsValue]] = {
		def atcdef(o: String) = pr.get.find(x => x.get("des").get.as[String] == o)
		def atcfilter(o: String) = pr.get.filter{ y =>
			atcdef(o) match {
				case None => false
				case Some(t) =>
					val v = if(t.get("level").get.as[Int] == 2) t.get("parent").get.as[String] else t.get("def").get.as[String]
					y.get("parent").get.as[String] == v
			}
		}
		atcfilter(obj).map (x => x).map(z => if(z.get("level").get.as[Int] == 2) z :: Nil else atcfilter(z.get("des").get.as[String])).flatten
	}
}

object AtcOralLinkAge extends LinkAge {
	def apply(obj: Option[String])(pr: Option[List[Map[String, JsValue]]]): Int = atclinkage(obj)(pr)
	override def atclinkage(obj: Option[String])(pr: Option[List[Map[String, JsValue]]]): Int = {
		getSize(obj.get)(pr)
	}
	
	def getSize(obj: String)(pr: Option[List[Map[String, JsValue]]]): Int = {
		pr.get.filter(x => x.get("des").get.as[String] == obj) match {
			case Nil => pr.get.filter(x => x.get("def").get.as[String] == obj).size
			case lst => lst.size
		}
	}
}
