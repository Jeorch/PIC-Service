package bmmessages

import akka.actor.ActorRef
import akka.actor.Actor
import play.api.libs.json.JsValue
import play.api.libs.json.JsLookup

trait MessageDefines
abstract class CommonMessage extends MessageDefines

//class ParallelMessage extends MessageDefines {
//	type data_type = List[MessageDefines]
//}

case class excute(msr : MessageRoutes)
case class result(rst : JsValue)

case class error(err : JsValue)
case class timeout()

case class MessageRoutes(lst : List[MessageDefines], rst : Option[Map[String, JsValue]])(implicit val cm : CommonModules)

case class CommonModules(modules : Option[Map[String, AnyRef]])