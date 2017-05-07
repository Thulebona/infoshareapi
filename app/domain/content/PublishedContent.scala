package domain.content

import java.util.Date
import play.api.libs.json.Json


case class  PublishedContent(org: String,
                             id: String,
                             dateCreated: Date,
                             creator: String,
                             source: String,
                             category: String,
                             title: String,
                             content: String,
                             contentType: String,
                             status: String,
                             state: String)

object PublishedContent {
  implicit val publishedContentFmt = Json.format[PublishedContent]

}

