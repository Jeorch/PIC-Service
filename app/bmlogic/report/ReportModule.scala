package bmlogic.report

import java.util.Calendar

import bminjection.db.DBTrait
import bmlogic.conditions.ConditionSearchFunc
import bmlogic.report.ReportMessage._
import bmlogic.report.ReportModule.oralNameConditionParse
import bmlogic.report.reportData.ReportData
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import bmutil.errorcode.ErrorCode
import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json._

import scala.collection.JavaConverters._
import scala.collection.immutable.Map
import scala.collection.mutable.ArrayBuffer

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
		case msg_ReportChart_one(data)=>reportChartOne(data)(pr)
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
		//	val oral_name= (data \ "condition"\"oral_name").asOpt[String].map (x => x).getOrElse(throw new Exception("input error"))
			val condition= (conditionParse(data, pr.get) :: oralNameConditionParse(data) :: dateConditionParse(data) :: Nil).filterNot(_ == None).map(_.get)
			//val con=dateConditionParse
		//	println(oral_name)
			val group =MongoDBObject("_id"->"$manufacture_type", "sales" -> MongoDBObject("$sum" -> "$sales"))
//			var interNum=null
			var outerNum:Double=1
			println("ok")
			//val result=db.aggregate(MongoDBObject("oral_name"->oral_name),"retrieval",group)
			val res=db.aggregate($and(condition),"retrieval",group)
			{x=>
				println(x)
				val interNum=getByID(x,"内资")
				println(interNum)
				outerNum=getByID(x,"合资")
				println(outerNum)
				val per=outerNum/(interNum+outerNum)*100

				Map("percent" -> toJson(per),
				"start" -> toJson((data\ "condition" \ "date" \ "start").as[String]),
				"end" -> toJson((data \ "condition" \ "date" \ "end").as[String])
				)
			}
			println(res)
			(res,None)
			//(Some(Map("reportid" -> toJson(0))), None)
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
			if (result.isEmpty) throw new Exception("unkonw error")
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
	//表一
	def reportChartOne(data: JsValue)
					  (pr: Option[Map[String, JsValue]])
					  (implicit cm : CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {
		
		val db = cm.modules.get.get("db").map(x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
		val group = MongoDBObject("_id"->"$manufacture", "sales" -> MongoDBObject("$sum" -> "$sales"))
			//			val sort=MongoDBObject(s"$sort"->MongoDBObject("salesQuantity"-> -1))
			//			val limit=MongoDBObject(s"$limit"->10)
		val condition= (conditionParse(data, pr.get)::categoryConditionParse(data,pr.get)  :: dateConditionParse(data) :: Nil).filterNot(_ == None).map(_.get)
		var marketShare:List[Map[String,JsValue]]=null
		//前十数据
		val topTen:ArrayBuffer[(String,Double)]=new ArrayBuffer[(String, Double)]()
		val allRes=db.aggregate($and(condition),"retrieval",group){x=>
			val tmp=getObj(x).map{y=>
				val manufacture=y.getString("_id")
				val saleQuantity=y.getDouble("sales")
				val kv=manufacture->saleQuantity
				kv
			}
			val sum:Double=tmp.map(x=>x._2.toDouble).reduce((a,b)=>a+b)
			val res=tmp.sortBy(x=>x._2.toDouble).reverse.take(10)
			val top=res.map{ x=>
				topTen.append(x)
				Map(x._1->x._2)
			}
			marketShare=res.map(x=>Map(x._1->toJson(x._2.toDouble/sum)))
			Map("top10"->toJson(marketShare),
			"rawData"->toJson(top)
			)
		}

		val per_year_start=(data \ "condition"\"date" \ "start").get.as[String]
		val per_year_last=(data\"condition" \"date"\ "end").get.as[String]
		val per_start=updateMonth(per_year_start,-12)
		val per_end=updateMonth(per_year_last,-12)
		println(per_start)
		println(per_end)
		val time=toJson(Map("condition" -> toJson(Map("date" -> toJson(Map("start" -> toJson(per_start), "end" -> toJson(per_end)))))))
		val perCondition= (conditionParse(data, pr.get) :: categoryConditionParse(data,pr.get) :: dateConditionParse(time) :: Nil).filterNot(_ == None).map(_.get)
		val preData:ArrayBuffer[(String,Double)]=new ArrayBuffer[(String, Double)]()
		val preRes=db.aggregate($and(perCondition),"retrieval",group){x=>
			val tmp=getObj(x).map{y=>
				val manufacture=y.getString("_id")
				val saleQuantity=y.getDouble("sales")
				val kv=Map(manufacture->saleQuantity)
				preData.append((manufacture,saleQuantity))
				kv
			}
			Map("pre_sale"->toJson(tmp))
		}.get

		val percentGrowth=topTen.map{ x=>
			val now=x._2
			val past=preData.toList.filter(y=>y._1==x._1).head._2

			
			val growth=(now-past)/past*100
			Map(x._1->growth)
		}
		(Some(Map(
			"marketShare"->toJson(marketShare),
			"percentGrowth" -> toJson(percentGrowth))),None)
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
	
	
	def getByID(x : MongoDBObject, id : String) : Double = {
		val ok = x.getAs[Number]("ok").get.intValue
		if (ok == 0) throw new Exception("db aggregation error")
		else {
			val lst: MongoDBList = x.getAs[MongoDBList]("result").get
			val tmp = lst.toList.asInstanceOf[List[BasicDBObject]]
			if (tmp.isEmpty) throw new Exception("db aggregation find None")
			else {
				val res = tmp.find(x => x.getString("_id") == id)
				res.get.getDouble("sales")
			}
		}
	}
	
	def dateCondition(lst: List[String]): List[JsValue] = {
		val tmp = lst match {
			case Nil => println("Nil"); None
			case (head :: tail) => if(!tail.isEmpty) Some((head, tail.head)) else None
			case _ => ???
		}
		
		if(lst.tail.isEmpty || tmp.isEmpty) Nil
		else
		//				($and("date" $lt sdf.parse(tmp.get._1).getTime, "date" $gte sdf.parse(tmp.get._2).getTime))
			toJson(Map("condition" -> toJson(Map("date" -> toJson(Map("start" -> toJson(tmp.get._2), "end" -> toJson(tmp.get._1))))))) :: dateCondition(lst.tail)
	}
	
	def getObj(x : MongoDBObject) : List[BasicDBObject] ={
		val ok = x.getAs[Number]("ok").get.intValue
		if (ok == 0) throw new Exception("db aggregation error")
		else {
			val lst : MongoDBList = x.getAs[MongoDBList]("result").get
			val tmp = lst.toList.asInstanceOf[List[BasicDBObject]]
			if(tmp.isEmpty) throw new Exception("db aggregation find None")
			else {
				tmp
			}
			
		}
		
	}
	
	
	def updateMonth(date:String,month:Int):String  ={
		val c = Calendar.getInstance()
		c.setTime(sdf.parse(date))
		c.add(Calendar.MONTH,month)
		val per_time=c.getTime
		sdf.format(per_time)
	}
}
