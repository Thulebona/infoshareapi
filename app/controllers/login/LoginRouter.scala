package controllers.login

import javax.inject.Inject

import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

/**
  * Created by hashcode on 2017/06/29.
  */
class LoginRouter @Inject() (loginController:LoginController) extends SimpleRouter {

  override def routes: Routes = {
    //Login
    case POST(p"/authenticate/") =>
      loginController.authenticate
    case POST(p"/change/") =>
      loginController.changePassword
    case GET(p"/accounts/$email") =>
      loginController.login(email)
  }
}
