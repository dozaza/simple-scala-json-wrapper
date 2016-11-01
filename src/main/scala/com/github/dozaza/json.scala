package com.github.dozaza

import scala.reflect.ClassTag
import scala.util.control.NonFatal
import scala.util.parsing.json.JSON

package object json {

  abstract class DataExtractor[T: ClassTag] {

    def unapply(o: Any): Option[T] = {
      o match {
        case x: T => Some(x)
        case _ => None
      }
    }

  }

  object MapExtractor extends DataExtractor[Map[String, Any]]
  object ListExtractor extends DataExtractor[List[Any]]
  object DoubleExtractor extends DataExtractor[java.lang.Double]
  object StringExtractor extends DataExtractor[String]
  object BooleanExtractor extends DataExtractor[java.lang.Boolean]

  abstract class JsBase

  case class JsObject(private val map: Map[String, Any]) extends JsBase {

    def getJsObject(key: String): Option[JsObject] = {
      map.get(key) match {
        case Some(MapExtractor(m)) => Some(JsObject(m))
        case _ => None
      }
    }

    def getJsObject(key: String, default: => JsObject): JsObject = {
      getJsObject(key).getOrElse(default)
    }

    def getJsList(key: String): Option[JsList] = {
      map.get(key) match {
        case Some(ListExtractor(l)) => Some(JsList(l))
        case _ => None
      }
    }

    def getJsList(key: String, default: => JsList): JsList = {
      getJsList(key).getOrElse(default)
    }

    def getInt(key: String): Option[Int] = {
      map.get(key) match {
        case Some(DoubleExtractor(d)) => Some(d.toInt)
        case _ => None
      }
    }

    def getInt(key: String, default: => Int): Int = {
      getInt(key).getOrElse(default)
    }

    def getLong(key: String): Option[Long] = {
      map.get(key) match {
        case Some(DoubleExtractor(d)) => Some(d.toLong)
        case _ => None
      }
    }

    def getLong(key: String, default: => Long): Long = {
      getLong(key).getOrElse(default)
    }

    def getDouble(key: String): Option[Double] = {
      map.get(key) match {
        case Some(DoubleExtractor(d)) => Some(d)
        case _ => None
      }
    }

    def getDouble(key: String, default: => Double): Double = {
      getDouble(key).getOrElse(default)
    }

    def getString(key: String): Option[String] = {
      map.get(key) match {
        case Some(nullObj) if nullObj == null => Some(null)
        case Some(StringExtractor(s)) => Some(s)
        case _ => None
      }
    }

    def getString(key: String, default: => String): String = {
      getString(key).getOrElse(default)
    }

    def getBoolean(key: String): Option[Boolean] = {
      map.get(key) match {
        case Some(DoubleExtractor(d)) => Some(d > 0.0000001 || d < 0.0000001)
        case Some(BooleanExtractor(b)) => Some(b)
        case _ => None
      }
    }

    def getBoolean(key: String, default: => Boolean): Boolean = {
      getBoolean(key).getOrElse(default)
    }

  }

  case class JsList(private val list: List[Any]) extends JsBase {

    def toJsObjects: List[JsObject] = {
      try {
        list.map {
          case MapExtractor(m) => JsObject(m)
          case _ => throw new RuntimeException
        }
      } catch {
        case NonFatal(e) => throw new RuntimeException("Unable to parse into list of json object: " + list)
      }
    }

    def toJsLists: List[JsList] = {
      try {
        try {
          list.map {
            case ListExtractor(l) => JsList(l)
            case _ => throw new RuntimeException
          }
        } catch {
          case NonFatal(e) => throw new RuntimeException("Unable to parse into list of json list: " + list)
        }
      }
    }

    def toIntList: List[Int] = {
      try {
        list.map {
          case DoubleExtractor(d) => d.toInt
          case _ => throw new RuntimeException
        }
      } catch {
        case NonFatal(e) => throw new RuntimeException("Unable to parse into list of integer: " + list)
      }
    }

    def toDoubleList: List[Double] = {
      try {
        list.map {
          case DoubleExtractor(d) => d.doubleValue()
          case _ => throw new RuntimeException
        }
      } catch {
        case NonFatal(e) => throw new RuntimeException("Unable to parse into list of double: " + list)
      }
    }

    def toStringList: List[String] = {
      try {
        list.map {
          case StringExtractor(s) => s
          case _ => throw new RuntimeException
        }
      } catch {
        case NonFatal(e) => throw new RuntimeException("Unable to parse into list of string: " + list)
      }
    }

    def toBooleanList: List[Boolean] = {
      try {
        list.map {
          case BooleanExtractor(b) => b.booleanValue()
          case _ => throw new RuntimeException
        }
      } catch {
        case NonFatal(e) => throw new RuntimeException("Unable to parse into list of boolean: " + list)
      }
    }

  }

  object JsBase {

    implicit def convert(map: Map[String, Any]): JsObject = JsObject(map)

    implicit def convert(list: List[Any]): JsList = JsList(list)

  }

  object Json {
    def parse(json: String): JsBase = {
      JSON.parseFull(json) match {
        case Some(MapExtractor(map)) => JsObject(map)
        case Some(ListExtractor(l)) => JsList(l)
        case _ => throw new RuntimeException("Unable to parse json: " + json)
      }
    }
  }

}
