package repositories.content.tables


import com.outworkers.phantom.dsl._
import com.outworkers.phantom.streams._
import scala.concurrent.Future

import domain.content.Category

class CategoryTable extends CassandraTable[CategoryTable, Category] {

  object id extends StringColumn(this) with PartitionKey

  object name extends StringColumn(this)

  object description extends StringColumn(this)

}

abstract class CategoryTableImpl extends CategoryTable with RootConnector {
  override lazy val tableName = "category"

  def save(category: Category): Future[ResultSet] = {
    insert
      .value(_.id,category.id )
      .value(_.name, category.name)
      .value(_.description, category.description)
      .future()
  }

  def getCategoryById(id: String): Future[Option[Category]] = {
    select.where(_.id eqs id).one()
  }

  def getAllCategories: Future[Seq[Category]] = {
    select.fetchEnumerator() run Iteratee.collect()
  }
}