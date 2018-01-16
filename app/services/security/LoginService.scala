package services.security

import com.datastax.driver.core.ResultSet
import domain.security.{Credential, UserGeneratedToken}
import domain.users.{Login, User}
import services.security.Impl.LoginServiceImpl

import scala.concurrent.Future

/**
  * Created by hashcode on 6/24/17.
  */
trait LoginService {
  def changePassword(entity: Credential, token: String): Future[Option[Boolean]]

  def getLogins(email: String): Future[Seq[Login]]

  def getUser(email: String, siteId: String): Future[Option[User]]

  def createNewToken(credential: Credential, agent: String): Future[UserGeneratedToken]

  def revokeToken(token: String): Future[ResultSet]

  def getTokenRolesFromToken(token: String): Future[String]

  def getEmailFromToken(token: String): Future[String]

  def getOrganisationCodeFromToken(token: String): Future[String]

  def isTokenValid(token: String, password: String): Future[Boolean]
}

object LoginService {
  def apply: LoginService = new LoginServiceImpl()
}
