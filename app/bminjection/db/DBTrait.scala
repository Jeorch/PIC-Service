package bminjection.db

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue

/**
  * Created by alfredyang on 01/06/2017.
  */
trait DBTrait {
    def insertObject(obj : DBObject, db_name : String, primary_key : String) : Unit
    def updateObject(obj : DBObject, db_name : String, primary_key : String) : Unit

    def queryObject(condition : DBObject, db_name : String)
                   (implicit t : DBObject => Map[String, JsValue]) : Option[Map[String, JsValue]]
    def queryMultipleObject(condition : DBObject, db_name : String, sort : String = "date", skip : Int = 0, take : Int = 20)
                           (implicit t : DBObject => Map[String, JsValue]) : List[Map[String, JsValue]]

    def deleteObject(obj : DBObject, db_name : String, primary_key : String) : Unit

    def restoreDatabase() = ???
    def dumpDatabase() = ???
}
