package controllers

import play.api.mvc._

/**
  * Created by yym on 6/5/17.
  */
class Application extends Controller {
    def index = Action {
        Ok(views.html.index1())
    }
    def login=Action{
        Ok(views.html.login())
    }
    def loginToIndex=Action{
        Redirect(routes.Application.index())
    }
}
