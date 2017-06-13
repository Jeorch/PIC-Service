package bmlogic.adjustdata

import bmlogic.adjustdata.AdjustDataMessage.msg_AdjustNameDataCommand
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import bmutil.dao.{_data_connection, from}
import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by jeorch on 17-6-13.
  */
object AdjustDataModule extends ModuleTrait {
    def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_AdjustNameDataCommand(data) => adjustNameData(data)

        case _ => ???
    }

    def adjustNameData(data : JsValue) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        /**
          * 1. category 提出单一数据
          */
        val config = (from db() in "config" where ("index" -> "PIC")).selectTop(1)("index")(x => x).toList.head
        val category = config.getAs[BasicDBList]("category").get.toList.asInstanceOf[List[BasicDBObject]]

        for (cat <- category) {
            _data_connection.getCollection("category") += cat
        }

        /**
          * 2. 将通用名与商品名整合到category 中
          */
        val ct = (from db() in "retrieval").selectCursor

        while (ct.hasNext) {
            val iter = ct.next()

            val oral_name = iter.getAs[String]("oral_name").get
            val product_name = iter.getAs[String]("product_name").get

            (from db() in "category" where("des" -> oral_name, "def" -> product_name)).count match {
                case n if n == 0 => {
                    val builder = MongoDBObject.newBuilder
                    builder += "level" -> 3
                    builder += "parent" -> iter.getAs[String]("category").get
                    builder += "def" -> iter.getAs[String]("product_name").get
                    builder += "des" -> iter.getAs[String]("oral_name").get

                    _data_connection.getCollection("category") += builder.result()
                }
                case _ => Unit
            }
        }

        (Some(Map("ok" -> toJson("ok"))), None)
    }
}
