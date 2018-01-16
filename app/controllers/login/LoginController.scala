package controllers.login

import javax.inject.{Inject, Singleton}

import cats.data.OptionT
import domain.security.{Credential, TokenFailException, UserGeneratedToken}
import domain.users.Login
import play.api.libs.json.Json
import play.api.mvc._
import services.security.{LoginService, TokenCheckService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Singleton
class LoginController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def authenticate = Action.async(parse.json) { request =>
    val entity = Json.fromJson[Credential](request.body).get
    val agent = request.headers.toSimpleMap.getOrElse("User-Agent", "")
    val response: Future[UserGeneratedToken] = for {
      results: UserGeneratedToken <- LoginService.apply.createNewToken(entity, agent)
    } yield results
    response.map(ans => Ok(Json.toJson(ans))).recover {
      case _: TokenFailException => Unauthorized
      case _: Exception => InternalServerError
    }
  }

  def login(email: String) = Action.async {
    implicit request: Request[AnyContent] =>
      val response: Future[Seq[Login]] = for {
        results<- LoginService.apply.getLogins(email)
      } yield results
      response.map(ans => Ok(Json.toJson(ans))).recover {
        case _: TokenFailException => Unauthorized
        case _: Exception => InternalServerError
      }
  }

  def changePassword = Action.async(parse.json) { request =>
    val entity = Json.fromJson[Credential](request.body).get
    val token = request.headers.get("Authorization").getOrElse("")
    val response = for {
      _ <- OptionT.liftF(TokenCheckService.apply.getUserToken(request))
      results <- OptionT(LoginService.apply.changePassword(entity,token))
    } yield results

    response.value.map(ans => Ok(Json.toJson(ans))).recover {
      case _: TokenFailException => Unauthorized
      case _: Exception => InternalServerError
    }
  }

}
