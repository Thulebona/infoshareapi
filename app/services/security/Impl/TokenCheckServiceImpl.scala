package services.security.Impl

import com.typesafe.config.ConfigFactory
import conf.util.Events
import domain.security.{LogInStatus, TokenFailException}
import play.api.libs.json.JsValue
import play.api.mvc.{AnyContent, MultipartFormData, Request}
import services.security.{ManageTokenService, TokenCheckService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.reflect.io.File

/**
  * Created by hashcode on 6/20/17.
  */
class TokenCheckServiceImpl extends TokenCheckService {

  override def getTokenForUpload(request: Request[MultipartFormData[File]]): Future[LogInStatus] = {

    getTokenValue(
      request.headers.get("Authorization").getOrElse(""),
      request.headers.get("User-Agent").getOrElse(""))
  }

  override def getTokenfromParam(request: Request[AnyContent]): Future[LogInStatus] = {
    getTokenValue(
      request.headers.get("Authorization").getOrElse(""),
      request.headers.get("User-Agent").getOrElse(""))
  }

  override def getToken(request: Request[JsValue]): Future[LogInStatus] = {
    getTokenValue(
      request.headers.get("Authorization").getOrElse(""),
      request.headers.get("User-Agent").getOrElse(""))
  }

  override def getUserToken[A](request: Request[A]): Future[LogInStatus] = {
    getTokenValue(request.headers.get("Authorization").getOrElse(""),
      request.headers.get("User-Agent").getOrElse(""))
  }

  override protected def getTokenValue(token: String, agent: String): Future[LogInStatus] = {
    val flag = ConfigFactory.load().getBoolean("token-security.enabled")
    if (flag) {
      ManageTokenService.apply.isTokenValid(token, agent) map (isValid => {
        if (isValid) LogInStatus(Events.TOKENVALID) else throw TokenFailException("Error")
      })
    } else Future.successful(LogInStatus(Events.TOKENVALID))
  }
}
