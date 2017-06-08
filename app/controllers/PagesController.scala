package controllers


import play.api.mvc._

/**
  * Created by yym on 6/5/17.
  */
class PagesController extends Controller {
    def goHome = Action {
        Ok(views.html.home())
    }

    def login=Action{
        Ok(views.html.login())
    }

    def contactus = Action {
        Ok(views.html.contactus())
    }

    def aboutus = Action {
        Ok(views.html.aboutus())
    }
}
