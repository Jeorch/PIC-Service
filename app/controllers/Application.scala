package controllers

import play.api.mvc._

/**
  * Created by yym on 6/5/17.
  */
class Application extends Controller {
    def index = Action {
        Ok(views.html.index())
    }
    def login=Action{
        Ok(views.html.login())
    }
    def loginToIndex=Action{
        Redirect("/test")
    }
    def contactus = Action {
        Ok(views.html.contactus())
    }
    def aboutus = Action {
        Ok(views.html.aboutus())
    }
}
