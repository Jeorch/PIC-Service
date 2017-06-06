package models

import play.api.data._
import play.api.data.Forms._

/**
  * Created by yym on 6/5/17.
  */
case class User(name:String,password:String)
object User {
    //用于存储用户
    var list: List[User] = Nil
    
    //定义表单及其校验要求，nonEmptyText表示该项内容不得为空
    val form = Form(tuple(
        "name" -> nonEmptyText,
        "password" -> nonEmptyText
    ))
}
