package com.github.dozaza

import com.github.dozaza.json._

case class ReportDefParam(
  id: Int,
  parentId: Int,
  label: String,
  labelShort: String,
  remark: String,
  viewType: String,
  viewOption: String,
  valueDefault: String,
  valueUnit: String,
  nullable: Boolean,
  isLeaf: Boolean,
  permission: Int
)

object ReportDefParam {

  def apply(jsObj: JsObject): ReportDefParam = {
    ReportDefParam(
      jsObj.getInt("id", -1),
      jsObj.getInt("parentId", -1),
      jsObj.getString("label").orNull,
      jsObj.getString("labelShort").orNull,
      jsObj.getString("remark").orNull,
      jsObj.getString("viewType").orNull,
      jsObj.getString("viewOption").orNull,
      jsObj.getString("valueDefault").orNull,
      jsObj.getString("valueUnit").orNull,
      jsObj.getBoolean("nullable", default = false),
      jsObj.getBoolean("isLeaf", default = false),
      jsObj.getInt("permission", -1)
    )
  }

  def unapply(a: Any): Option[ReportDefParam] = {
    a match {
      case JsObject(map) => Some(ReportDefParam(map))
      case _ => None
    }
  }

}

case class ReportValue(
  paramId: Int,
  unit: String,
  valueLong: String,
  defaultOverride: String,
  defaultValue: String,
  tpe: String,
  value: String
)

object ReportValue {

  def apply(jsObj: JsObject): ReportValue = {
    ReportValue(
      jsObj.getInt("paramId", -1),
      jsObj.getString("unit").orNull,
      jsObj.getString("valueLong").orNull,
      jsObj.getString("defaultOverride").orNull,
      jsObj.getString("defaultValue").orNull,
      jsObj.getString("type").orNull,
      jsObj.getString("value").orNull
    )
  }

  def unapply(a: Any): Option[ReportValue] = {
    a match {
      case JsObject(map) => Some(ReportValue(map))
      case _ => None
    }
  }
}

object main {

  private val testJson =
    """{
      |  "id": 1,
      |  "name": "dozaza",
      |  "languages": ["chinese", "english", "french"],
      |  "detail": {
      |    "sex": "male",
      |    "age": 26
      |  },
      |  "type": null
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

  def main(args: Array[String]): Unit = {
    val jsObj = Json.parse(testJson).asInstanceOf[JsObject]

    val id = jsObj.getInt("id", throw new RuntimeException("no id"))
    println(id)

    val languages = jsObj.getJsList("languages", throw new RuntimeException("no languages")).toStringList
    println(languages)

    val detail = jsObj.getJsObject("detail", Map.empty[String, Any])
    println(detail)

    val tpe = jsObj.getString("type", null)
    println(tpe)

    val jsList = Json.parse(testJson2)
    println(jsList)
//    val list = jsList.toJsLists
//    println(list)
  }

}

