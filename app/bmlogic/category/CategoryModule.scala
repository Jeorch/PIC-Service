package bmlogic.category

import bminjection.db.DBTrait
import bmlogic.category.categoryData.CategoryData
import bmlogic.category.CategoryMessage._
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import bmutil.errorcode.ErrorCode
import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import bminjection.db.LoadCategory._

import scala.collection.immutable.Map

/**
  * Created by yym on 6/15/17.
  */
object CategoryModule extends ModuleTrait with CategoryData {
    
    lazy val c = category
    
    def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_FirstChildCategoryCommand(data)=>FirstChildCategory(data)
        case msg_SecondChildCategoryCommand(data)=>SecondChildCategory(data)
        //case  msg_ThridChildCategoryCommand(data)=>ThridChildCategory(data)

        case msg_Category(data) => Category(data)
        case _ => ???
    }
    //查出治疗1中包含的治疗2
    def FirstChildCategory(data : JsValue)(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) ={
        try {
            val db = cm.modules.get.get("db").map(x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
            val des=(data \ "fir_des").asOpt[String].get
            val parentList=db.queryMultipleObject(DBObject("des"->des),"category").map(x=> x.get("def"))
            var parent=""
            if(!parentList.isEmpty){
                parent=parentList.head.get.asOpt[String].get
            }
            val childList=db.queryMultipleObject(DBObject("parent"->parent),"category").map(x=>x.get("des").get)
           
            (Some(Map(
                "sec_des" -> toJson(childList)

            )), None)
            
        }catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
    //查出治疗2中包含的治疗3
    def SecondChildCategory(data:JsValue)(implicit cm:CommonModules):(Option[Map[String,JsValue]],Option[JsValue])={
       try{
           val db=cm.modules.get.get("db").map(x=>x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
           val desList=(data\"sec_des").asOpt[List[String]].get.map(x=>Map("des"->toJson(x)))
           val parentList=db.queryMultipleObject($or(desList.map(x=>DBObject("des"->x.get("des").get.toString()))),"category").map(x=>x.get("def"))
           val childList=db.queryMultipleObject($or(parentList.map(x=>DBObject("def"->x.get.toString()))),"category").map(x=>x.get("des").get)
           (Some(Map(
               "thr_des" -> toJson(childList)
           )), None)
           
           
       }catch {
           case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
       }
      
    }
    //查出治疗3的产品数量
//    def ThridChildCategory(data:JsValue)(implicit cm:CommonModules):(Option[Map[String,JsValue]],Option[JsValue])={
//        try{
//            val db=cm.modules.get.get("db").map(x=>x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
//            val desList=(data\"thr_des").asOpt[List[String]].get.map(x=>Map("des"->toJson(x)))
//            val categoryList=db.queryMultipleObject($or(desList.map(x=>DBObject("category"->x.get("des").get.toString()))),"retrieval")
//
//            val sum=categoryList.size
//            (Some(Map(
//                "thr_des" -> toJson(sum)
//            )), None)
//
//
//        }catch {
//            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
//        }
        
 //   }
 
 
    def Category(data: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val result = c match {
            case None => None
            case Some(ca) =>
                val atc_one = ca.filter(x => x.get("level").get.as[Int] == 0).map(x => x.get("des").get.as[String]).distinct
                val atc_tow = ca.filter(x => x.get("level").get.as[Int] == 1).map(x => x.get("des").get.as[String]).distinct
                val atc_three = ca.filter(x => x.get("level").get.as[Int] == 2).map(x => x.get("des").get.as[String]).distinct
                val oral = ca.filter(x => x.get("level").get.as[Int] == 3).map(x => x.get("des").get.as[String]).distinct
                val product = ca.filter(x => x.get("level").get.as[Int] == 3).map(x => x.get("def").get.as[String]).distinct
                Some(Map("atc_one" -> toJson(atc_one),
                    "atc_tow" -> toJson(atc_tow),
                    "atc_three" -> toJson(atc_three),
                    "oral" -> toJson(oral),
                    "product" -> toJson(product)))
            case _ => ???
        }
        (result, None)
    }
}
