/** Copyright © 2013-2016 DataYes, All Rights Reserved. */

package com.github.dozaza

import com.github.dozaza.json._

import org.scalatest._

class JsonTest extends WordSpec with ShouldMatchers {

  private val jsonObj =
    """{
      |  "id": 1,
      |  "name": "dozaza",
      |  "languages": ["chinese", "english", "french"],
      |  "detail": {
      |    "sex": "male",
      |    "age": 26
      |  },
      |  "type": null,
      |  "isSingle": false
      |}
    """.stripMargin

  private val jsonObj2 =
    """{
      |  "name": "dozaza",
      |  "languages": ["chinese", "english", "french"],
      |  "detail": {
      |    "sex": "male",
      |    "age": 26
      |  }
      |}
    """.stripMargin

  private val testJson2 =
    """[{
      |  "id": 1,
      |  "name": "dozaza",
      |  "languages": ["chinese", "english", "french"],
      |  "detail": {
      |    "sex": "male",
      |    "age": 26
      |  },
      |  "type": null
      |}]
    """.stripMargin

  "parse json object" should {
    val jsObj = Json.parse(jsonObj).asInstanceOf[JsObject]

    "parse int" in {
      jsObj.getInt("id") match {
        case Some(id) => id should equal (1)
        case _ => throw new RuntimeException
      }
    }

    "parse string" in {
      jsObj.getString("name") match {
        case Some(str) => str should equal ("dozaza")
        case _ => throw new RuntimeException
      }
    }

    "parse string list" in {
      jsObj.getJsList("languages") match {
        case Some(jsList) =>
          val list = jsList.toStringList
          list.size should equal (3)
          list.head should equal ("chinese")
        case _ => throw new RuntimeException
      }
    }

    "parse null string" in {
      val str = jsObj.getString("type", throw new RuntimeException)
      str should equal (null)
    }

    "parse boolean" in {
      jsObj.getBoolean("isSingle") match {
        case Some(flag) => flag should equal (false)
        case _ => throw new RuntimeException
      }
    }
  }
}
