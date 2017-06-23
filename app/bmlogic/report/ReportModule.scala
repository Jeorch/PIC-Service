package bmlogic.report

import bminjection.db.DBTrait
import bmlogic.conditions.ConditionSearchFunc
import bmlogic.report.ReportMessage._
import bmlogic.report.reportData.ReportData
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import bmutil.errorcode.ErrorCode
import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json._

import scala.collection.immutable.Map

/**
  * Created by qianpeng on 2017/6/20.
  */
object ReportModule extends ModuleTrait with ReportData with ConditionSearchFunc{
	
	def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
		case msg_InertParameterCommand(data) => insertparameter(data)
		case msg_ReportParameterSummary(data) => reportparametersummary(data)
		
		case msg_ReportGraph_One(data) => reportgraphone(data)(pr)
		case msg_ReportGraph_Tow(data) => reportgraphtow(data)(pr)
		case msg_ReportGraph_Thr(data) => reportgraphthr(data)(pr)
		case msg_ReportGraph_Four(data) => reportgraphfour(data)(pr)
		case msg_ReportGraph_Five(data) => reportgraphfive(data)(pr)
		case msg_ReportGraph_Six(data) => reportgraphsix(data)(pr)
		case msg_ReportGraph_Seven(data) => reportgraphseven(data)(pr)
		case msg_ReportGraph_Eight(data) => reportgrapheight(data)(pr)
		
		case _ => ???
	}
	
	def insertparameter(data: JsValue)
	                   (implicit cm : CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {
		try {
			val db=cm.modules.get.get("db").map(x=>x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
			val o: DBObject = data
			db.insertObject(o, "report", "reportid")
			(Some(Map("reportid" -> toJson(o.getAs[String]("reportid").getOrElse("")))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	def reportparametersummary(data: JsValue)
	                          (implicit cm : CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {
		try {
			val db=cm.modules.get.get("db").map(x=>x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
			val reportid = (data \ "reportid").asOpt[String].getOrElse("")
			val result = db.queryObject(DBObject("reportid" -> reportid), "report")(x => reportparameter(x))
			(Some(Map("parameter" -> toJson(result))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	
	def reportgraphone(data: JsValue)
	                  (pr: Option[Map[String, JsValue]])
	                  (implicit cm : CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {

		def resultdata(timecount: List[JsValue]): List[Option[Map[String, JsValue]]] = {
			val db=cm.modules.get.get("db").map(x=>x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
			val group = MongoDBObject("_id" -> MongoDBObject("ms" -> "reportgraphone"), "sales" -> MongoDBObject("$sum" -> "$sales"))
			timecount map { x =>
				val condition = (conditionParse(data, pr.get) :: oralNameConditionParse(data) :: dateConditionParse(x) :: Nil).filterNot(_ == None).map(_.get)
				db.aggregate($and(condition), "retrieval", group) { z =>
					Map("sales" -> toJson(aggregateSalesResult(z, "reportgraphone")),
						"start" -> toJson((x \ "condition" \ "date" \ "start").as[String]),
						"end" -> toJson((x \ "condition" \ "date" \ "end").as[String])
						)
				}
			}
		}

		try {
			var flag = 0D
			val lst = resultdata(dateCondition(timeList(1, data))).reverse.map { x =>
				if(flag == 0) {
					flag = x.get.get("sales").get.as[Double]
					 x.get ++ Map("trend" -> toJson("null"))
				}else {
					x.get ++ Map("trend" -> toJson((x.get.get("sales").get.as[Double] - flag)/flag))
				}
			}
			(Some(Map("reportgraphone" -> toJson(lst))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	def reportgraphtow (data: JsValue)
	                   (pr: Option[Map[String, JsValue]])
	                   (implicit cm : CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {
		try {
			val db=cm.modules.get.get("db").map(x=>x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
			val result = (conditionParse(data, pr.get) :: oralNameConditionParse(data) :: dateConditionParse(data) :: Nil).filterNot(_ == None).map(_.get)
			
			(Some(Map("reportid" -> toJson(0))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	def reportgraphthr (data: JsValue)
	                   (pr: Option[Map[String, JsValue]])
	                   (implicit cm : CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {
		try {
			val db=cm.modules.get.get("db").map(x=>x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
			val result = (conditionParse(data, pr.get) :: oralNameConditionParse(data) :: dateConditionParse(data) :: Nil).filterNot(_ == None).map(_.get)
			
			(Some(Map("reportid" -> toJson(0))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	def reportgraphfour (data: JsValue)
	                    (pr: Option[Map[String, JsValue]])
	                    (implicit cm : CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {
		try {
			val db=cm.modules.get.get("db").map(x=>x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
			val result = (conditionParse(data, pr.get) :: oralNameConditionParse(data) :: dateConditionParse(data) :: Nil).filterNot(_ == None).map(_.get)
			
			(Some(Map("reportid" -> toJson(0))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	def reportgraphfive (data: JsValue)
	                    (pr: Option[Map[String, JsValue]])
	                    (implicit cm : CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {
		
		def resultdata(timecount: List[JsValue]): List[Option[Map[String, JsValue]]] = {
			val db=cm.modules.get.get("db").map(x=>x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
			val group = MongoDBObject("_id" -> "$oral_name", "sales" -> MongoDBObject("$sum" -> "$sales"))
			timecount map{ x =>
				val condition = (conditionParse(data, pr.get) :: oralNameConditionParse(data) :: dateConditionParse(x) :: Nil).filterNot(_ == None).map(_.get)
				db.aggregate($and(condition), "retrieval", group) { z =>
					val r = aggregateResult(z)
					val sum = r.map(y => y._2).sum
					val scale = r.map ( y => Map("key" -> toJson(y._1), "value" -> toJson((y._2 / sum))))
					val sales = r.map ( y => Map("key" -> toJson(y._1), "value" -> toJson(y._2)))
					Map("sales" -> toJson(sales),
						"scale" -> toJson(scale),
						"start" -> toJson((x \ "condition" \ "date" \ "start").as[String]),
						"end" -> toJson((x \ "condition" \ "date" \ "end").as[String])
					)
				}
			}
		}
		
		def aggregateResult(x : MongoDBObject) : List[(String, Double)] = {
			val ok = x.getAs[Number]("ok").get.intValue
			if (ok == 0) throw new Exception("db aggregation error")
			else {
				val lst : MongoDBList = x.getAs[MongoDBList]("result").get
				lst.toList.asInstanceOf[List[BasicDBObject]].map( z => (z.getString("_id"), z.getDouble("sales") / 100))
			}
		}
		
		try {
			val result = resultdata(dateCondition(timeList(1, data)))
			(Some(Map("reportgraphfive" -> toJson(result)) ++ pr.get), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	def reportgraphsix (data: JsValue)
	                   (pr: Option[Map[String, JsValue]])
	                   (implicit cm : CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {
		
		def resultdata(timecount: List[JsValue]): List[Option[Map[String, JsValue]]] = {
			val db=cm.modules.get.get("db").map(x=>x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
			val group = MongoDBObject("_id" -> MongoDBObject("product_name" -> "$product_name","manufacture" -> "$manufacture","product_type" -> "$product_type"), "sales" -> MongoDBObject("$sum" -> "$sales"))
			timecount map{ x =>
				val condition = (conditionParse(data, pr.get) :: oralNameConditionParse(data) :: dateConditionParse(x) :: Nil).filterNot(_ == None).map(_.get)
				db.aggregate($and(condition), "retrieval", group) { z =>
					val r = aggregateResult(z)
					val key = r.map (y =>y._1)
					val value = r.map (y =>y._2)
					Map("key" -> toJson(key),
						"value" -> toJson(value),
						"start" -> toJson((x \ "condition" \ "date" \ "start").as[String]),
						"end" -> toJson((x \ "condition" \ "date" \ "end").as[String]))
				}
			}
		}
		
		def aggregateResult(x : MongoDBObject) : List[(String, Double)] = {
			val ok = x.getAs[Number]("ok").get.intValue
			if (ok == 0) throw new Exception("db aggregation error")
			else {
				val lst : MongoDBList = x.getAs[MongoDBList]("result").get
				lst.toList.asInstanceOf[List[BasicDBObject]].map { z =>
					val key = z.getAs[BasicDBObject]("_id")
					(key.get.getString("product_name") + key.get.getString("manufacture") + key.get.getString("product_type"), z.getDouble("sales") / 100)
				}
			}
		}
		
		try {
			val result = resultdata(dateCondition(timeList(1, data)))
			(Some(Map("reportgraphsix" -> toJson(result)) ++ pr.get), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	def reportgraphseven (data: JsValue)
	                     (pr: Option[Map[String, JsValue]])
	                     (implicit cm : CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {
		
		def resultdata(timecount: List[JsValue]): List[Option[Map[String, JsValue]]] = {
			val db=cm.modules.get.get("db").map(x=>x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
			val group = MongoDBObject("_id" -> MongoDBObject("ms" -> "oral_sum"), "sales" -> MongoDBObject("$sum" -> "$sales"))
			val lst = pr.get.get("reportgrapheight").get.as[List[Map[String, JsValue]]]
			timecount map{ x =>
				val topsalessum = lst.find(z => z.get("start").get.as[String] == (x \ "condition" \ "date" \ "start").as[String]).map(y => y.get("sales").get.as[List[Double]].sum).getOrElse(throw new Exception())
				val condition = (conditionParse(data, pr.get) :: oralNameConditionParse(data) :: dateConditionParse(x) :: Nil).filterNot(_ == None).map(_.get)
				db.aggregate($and(condition), "retrieval", group) { z =>
					val sum = aggregateSalesResult(z, "oral_sum")
					Map("sales" -> toJson(topsalessum / sum),
						"start" -> toJson((x \ "condition" \ "date" \ "start").as[String]),
						"end" -> toJson((x \ "condition" \ "date" \ "end").as[String]))
				}
			}
		}
		
		try {
			var flag = 0D
			val lst = resultdata(dateCondition(timeList(1, data))).reverse.map { x =>
				if(flag == 0) {
					flag = x.get.get("sales").get.as[Double]
					x.get ++ Map("trend" -> toJson("null"))
				}else {
					x.get ++ Map("trend" -> toJson((x.get.get("sales").get.as[Double] - flag)/flag))
				}
			}
			(Some(Map("reportgraphseven" -> toJson(lst)) ++ pr.get), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	def reportgrapheight (data: JsValue)
	                     (pr: Option[Map[String, JsValue]])
	                     (implicit cm : CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {
		
		def resultdata(timecount: List[JsValue]): List[Option[Map[String, JsValue]]] = {
			val db=cm.modules.get.get("db").map(x=>x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
			val group = MongoDBObject("_id" -> MongoDBObject("product_name" -> "$product_name"), "sales" -> MongoDBObject("$sum" -> "$sales"))
			timecount map{ x =>
				val condition = (conditionParse(data, pr.get) :: oralNameConditionParse(data) :: dateConditionParse(x) :: Nil).filterNot(_ == None).map(_.get)
				db.aggregate($and(condition), "retrieval", group) { z =>
					val r = aggregateResult(z).sortBy(y => y._2).reverse
					val sum = r.map(_._2).sum
					val key = r.take(10).map (y =>y._1)
					val value = r.take(10).map (y =>(y._2) / sum)
					val sales = r.take(10).map (y =>y._2)
					Map("key" -> toJson(key),
						"value" -> toJson(value),
						"sales" -> toJson(sales),
						"start" -> toJson((x \ "condition" \ "date" \ "start").as[String]),
						"end" -> toJson((x \ "condition" \ "date" \ "end").as[String]))
				}
			}
		}
		
		def aggregateResult(x : MongoDBObject) : List[(String, Double)] = {
			val ok = x.getAs[Number]("ok").get.intValue
			if (ok == 0) throw new Exception("db aggregation error")
			else {
				val lst : MongoDBList = x.getAs[MongoDBList]("result").get
				lst.toList.asInstanceOf[List[BasicDBObject]].map { z =>
					val key = z.getAs[BasicDBObject]("_id")
					(key.get.getString("product_name"), z.getDouble("sales") / 100)
				}
			}
		}
		
		try {
			val result = resultdata(dateCondition(timeList(1, data)))
			(Some(Map("reportgrapheight" -> toJson(result)) ++ pr.get), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	
	def reportparameter(obj: DBObject): Map[String, JsValue] = {
		val condition = obj.getAs[String]("condition").getOrElse("")
		Map("condition" -> parse(condition))
	}
	
	def aggregateSalesResult(x : MongoDBObject, id : String) : Double = {
		val ok = x.getAs[Number]("ok").get.intValue
		if (ok == 0) throw new Exception("db aggregation error")
		else {
			val lst : MongoDBList = x.getAs[MongoDBList]("result").get
			val tmp = lst.toList.asInstanceOf[List[BasicDBObject]]
			if(tmp.isEmpty) 0D
			else
				tmp.find(y => y.getAs[BasicDBObject]("_id").get.getString("ms") == id).map { z =>
					z.getDouble("sales") / 100
				}.getOrElse(throw new Exception("db aggregation error"))
		}
	}
	
	def dateCondition(lst: List[String]): List[JsValue] = {
		val tmp = lst match {
			case Nil => println("Nil"); None
			case (head :: tail) => {
				if(!tail.isEmpty) Some((head, tail.head)) else None
			}
			case _ => ???
		}
		
		if(lst.tail.isEmpty || tmp.isEmpty) Nil
		else
		//				($and("date" $lt sdf.parse(tmp.get._1).getTime, "date" $gte sdf.parse(tmp.get._2).getTime))
			toJson(Map("condition" -> toJson(Map("date" -> toJson(Map("start" -> toJson(tmp.get._2), "end" -> toJson(tmp.get._1))))))) :: dateCondition(lst.tail)
	}
}
