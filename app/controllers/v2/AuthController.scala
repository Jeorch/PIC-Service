package controllers.v2

import play.api._
import play.api.mvc._

import play.api.libs.json.Json.toJson
import play.api.libs.json.JsValue

import bmmessages._
//import module.auth.AuthMessage.{ msg_AuthPhoneCode, msg_AuthThird, msg_AuthSignOut }
//import module.phonecode.PhoneCodeMessages.msg_CheckSMSCode
//import module.profile.v2.ProfileMessages.msg_UpdateProfile
//import module.emxmpp.EMMessages.msg_RegisterEMUser
import bmpattern.ResultMessage.msg_CommonResultMessage
import bmpattern.LogMessage.msg_log

import javax.inject._
import akka.actor.ActorSystem
import bmmodule.MongoDBSpark

import bmlogic.common.requestArgsQuery
import bmlogic.common.requestArgsQuery._

import bmlogic.auth.AuthMessage._

class AuthController @Inject () (as_inject : ActorSystem, msk : MongoDBSpark) extends Controller {
    implicit val as = as_inject
   
    def authCheck = Action (request => requestArgsQuery().requestArgsV2(request) { jv =>
            import bmpattern.ResultMessage.common_result
            import bmpattern.LogMessage.common_log
			MessageRoutes(msg_log(toJson(Map("method" -> toJson("auth check"))), jv)
                :: msg_AuthCheck(jv) :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> msk))))
        })
    
//	def authWithPhoneCode = Action (request => requestArgsQuery().requestArgsV2(request) { jv => 
//			import pattern.ResultMessage.common_result
//			import pattern.LogMessage.common_log
//			MessageRoutes(msg_log(toJson(Map("method" -> toJson("auth with phone code"))), jv)
//                :: msg_CheckSMSCode(jv) :: msg_AuthPhoneCode(jv) :: msg_UpdateProfile(jv)
//                :: msg_RegisterEMUser(jv) :: msg_CommonResultMessage() :: Nil, None)
//		})
//	def authWithThird = Action (request => requestArgsV2(request) { jv => 
//			import pattern.ResultMessage.common_result
//            import pattern.LogMessage.common_log
//			MessageRoutes(msg_log(toJson(Map("method" -> toJson("auth with sns"))), jv)
//                :: msg_AuthThird(jv) :: msg_UpdateProfile(jv) :: msg_RegisterEMUser(jv)
//                :: msg_CommonResultMessage() :: Nil, None)
//		})
//	def authSignOut = Action (request => requestArgsV2(request) { jv => 
//			import pattern.ResultMessage.common_result
//            import pattern.LogMessage.common_log
//			MessageRoutes(msg_log(toJson(Map("method" -> toJson("auth sign out"))), jv)
//                :: msg_AuthSignOut(jv) :: msg_CommonResultMessage() :: Nil, None)
//		})
}