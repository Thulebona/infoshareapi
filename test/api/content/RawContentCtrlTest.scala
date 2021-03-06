package api.content

import java.time.{LocalDateTime => Date}

import domain.content.RawContent
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers._

class RawContentCtrlTest extends FunSuite with BeforeAndAfter with GuiceOneAppPerTest {

  var entity, updateEntity : RawContent = _
  var baseUrl = "/content/raw/"

  before {
    entity =  RawContent(org="DUT", id ="1", dateCreated= Date.now(), creator="test@me.com", source="3", category ="3",
      title = "birth control", content = "we not animals", contentType = "Text/Image",
      status = "raw",  state ="active")

  }

  test("Create raw content"){
    val request = route(app, FakeRequest(POST, baseUrl + "create")
      .withJsonBody(Json.toJson(entity))
      .withHeaders(AUTHORIZATION -> "Token")
    ).get

    assert(status(request) equals OK)
    assert(contentAsString(request) equals Json.toJson( entity).toString())
  }

  test("update raw content"){
    updateEntity =  entity.copy(contentType="images")
    val request = route(app, FakeRequest(POST, baseUrl+"create")
      .withJsonBody(Json.toJson(updateEntity))
      .withHeaders(AUTHORIZATION -> "Token")
    ).get
    assert(status(request) equals OK)
    assert(contentAsString(request) != Json.toJson( entity).toString())
    assert(contentAsString(request) equals Json.toJson(updateEntity).toString())
  }

  test("get raw content by id"){
    val request = route(app, FakeRequest(GET, baseUrl+ entity.org+ "/" +entity.id)
      .withHeaders(AUTHORIZATION -> "Token")
    ).get
    assert(status(request) equals OK)
    assert(contentAsString(request) equals Json.toJson(updateEntity).toString())
  }

  test("get paginated raw content"){
    val request = route(app, FakeRequest(GET, baseUrl+"range/"+entity.org +"/"+2)
      .withHeaders(AUTHORIZATION -> "Token")
    ).get
    assert(status(request) equals OK)
    println(contentAsString(request))
  }

  test("get all raw content"){
    val request = route(app, FakeRequest(GET, baseUrl+"all/"+entity.org)
      .withHeaders(AUTHORIZATION -> "Token")
    ).get
    assert(status(request) equals OK)
    println(contentAsString(request))
  }

}
