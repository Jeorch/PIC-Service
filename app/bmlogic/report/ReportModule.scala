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

		def timeList(n: Int = 0): List[String] = {
			val start = (data \ "condition" \ "date" \ "start").get.as[String]
			val end = (data \ "condition" \ "date" \ "end").get.as[String]
			end :: start :: (n-n+1 to n).map( x =>sdf.format(getDateMatParse(sdf.parse(start), x * -12))).toList
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
			val lst = resultdata(dateCondition(timeList(1))).reverse.map { x =>
				if(flag == 0) {
					flag = x.get.get("sales").get.as[Double]
					 x.get ++ Map("trend" -> toJson(0))
				}else {
					x.get ++ Map("trend" -> toJson((x.get.get("sales").get.as[Double] - flag)/flag))
				}
				
			}
			(Some(Map("data" -> toJson(lst))), None)
		} catch {
			case ex: Exception =>
				println(ex)
				(None, Some(ErrorCode.errorToJson(ex.getMessage)))
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
		try {
			val db=cm.modules.get.get("db").map(x=>x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
			val result = (conditionParse(data, pr.get) :: oralNameConditionParse(data) :: dateConditionParse(data) :: Nil).filterNot(_ == None).map(_.get)
			
			(Some(Map("reportid" -> toJson(0))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	def reportgraphsix (data: JsValue)
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
	
	def reportgraphseven (data: JsValue)
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
	
	def reportgrapheight (data: JsValue)
	                     (pr: Option[Map[String, JsValue]])
	                     (implicit cm : CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {
		try {
			val db=cm.modules.get.get("db").map(x=>x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
			val result = (conditionParse(data, pr.get) :: oralNameConditionParse(data) :: dateConditionParse(data) :: Nil).filterNot(_ == None).map(_.get)
			
			(Some(Map("reportid" -> toJson(0),
					  "report" -> toJson(0))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	
	def reportparameter(obj: DBObject): Map[String, JsValue] = {
		val condition = obj.getAs[String]("condition").getOrElse("")
		Map("condition" -> parse(condition))
	}
	
	def aggregateSalesResult(x : MongoDBObject, id : String) : Long = {
		val ok = x.getAs[Number]("ok").get.intValue
		if (ok == 0) throw new Exception("db aggregation error")
		else {
			val lst : MongoDBList = x.getAs[MongoDBList]("result").get
			val tmp = lst.toList.asInstanceOf[List[BasicDBObject]]
			if(tmp.isEmpty) 0L
			else
				tmp.find(y => y.getAs[BasicDBObject]("_id").get.getString("ms") == id).map { z =>
					z.getLong("sales") / 100
				}.getOrElse(throw new Exception("db aggregation error"))
		}
	}
}
