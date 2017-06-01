package controllers

import bmmessages._
import play.api.libs.json.Json.toJson
import play.api.mvc._
import javax.inject._

import akka.actor.ActorSystem
import bminjection.db.DBTrait
import bminjection.token.AuthTokenTrait
import bmlogic.auth.AuthMessage.msg_AuthWithPassword
import bmlogic.common.requestArgsQuery
import bmpattern.LogMessage.msg_log
import bmpattern.ResultMessage.msg_CommonResultMessage

class AuthController @Inject () (as_inject : ActorSystem, dbt : DBTrait, att : AuthTokenTrait) extends Controller {
    implicit val as = as_inject

    def authWithPassword = Action (request => requestArgsQuery().requestArgsV2(request) { jv =>
            import bmpattern.LogMessage.common_log
            import bmpattern.ResultMessage.common_result
			MessageRoutes(msg_log(toJson(Map("method" -> toJson("auth check"))), jv)
                :: msg_AuthWithPassword(jv) :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt))))
        })

    def authTokenCheckTest = Action (request => requestArgsQuery().requestArgsV2(request) { jv =>
            import bmpattern.LogMessage.common_log
            import bmpattern.ResultMessage.common_result
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("auth check"))), jv)
                :: msg_AuthWithPassword(jv) :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
        })
}