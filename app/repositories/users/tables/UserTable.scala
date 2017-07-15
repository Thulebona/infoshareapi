package repositories.users.tables

import java.time.LocalDateTime

import com.outworkers.phantom.dsl._
import com.outworkers.phantom.jdk8._
import com.outworkers.phantom.streams._
import domain.users.User

import scala.concurrent.Future



abstract class UserTable extends Table[UserTable, User] {

  object siteId extends StringColumn with PrimaryKey

  object email extends StringColumn with PartitionKey

  object firstName extends OptionalStringColumn

  object lastName extends OptionalStringColumn

  object middleName extends OptionalStringColumn

  object screenName extends StringColumn

  object password extends StringColumn

  object state extends StringColumn

  object date extends Col[LocalDateTime]

}


abstract class UserTableImpl extends UserTable with RootConnector {

  override lazy val tableName = "users"

  def save(user: User): Future[ResultSet] = {
    insert
      .value(_.siteId, user.siteId)
      .value(_.email, user.email)
      .value(_.state, user.state)
      .value(_.screenName, user.screenName)
      .value(_.firstName, user.firstName)
      .value(_.middleName, user.middleName)
      .value(_.lastName, user.lastName)
      .value(_.password, user.password)
      .value(_.date, user.date)
      .future()
  }

  def getUser(email: String, siteId:String): Future[Option[User]] = {
    select.where(_.email eqs email).and(_.siteId eqs siteId).one()
  }

  def getUserAccounts(email: String):Future[Seq[User]] = {
    select.where(_.email eqs email).fetchEnumerator() run Iteratee.collect()
  }

  def getUsers: Future[Seq[User]] = {
    select.fetchEnumerator() run Iteratee.collect()
  }

  def deleteUser(email: String, siteId:String): Future[ResultSet] = {
    delete.where(_.email eqs email).and(_.siteId eqs siteId).future()
  }
}

