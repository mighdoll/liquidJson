/*
   Copyright 2012  Lee Mighdoll

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package liquidj.json
import cc.spray.json.{JsValue, JsonParser, JsObject, JsArray, JsonReader}
import cc.spray.json.DefaultJsonProtocol._
import annotation.implicitNotFound


/** TODO
 *  capture field name in Json instances to include path based error reporting
 *  consider support for null property values
 */

 /** Factory for making $Json instances 
   * 
   * @define Json [[liquidj.json.Json]] */
object Json {
  /** Create a $Json object from a `JsValue` */
  private [json] def apply(jsValue:JsValue):Json = SomeJson(jsValue)

  /** Return a $Json object created by parsing a json string */
  def apply(jsonString:String):Json = SomeJson(JsonParser(jsonString))
}

/** Provides dynamic access to the properties and elements within a parsed 
  * chunk of json encoded data. 
  *
  * To parse the json text and return a [[liquidj.json.Json]] instance, just call
  * [[[liquidj.json.JsonStringWrap.json]]] on any `String`.
  * {{{
  * import liquidj.json.Implicits._ 
  * val json = """
  *   { "number": 9,
  *     "nested": 
  *        { "child": true }
  *     "array": [5,4,3]
  *   }""".json
  * }}}
  *
  * To retrieve a json property, simply use field access syntax with the property name.
  * The json property value is implicitly converted to the requested type.
  * {{{
  * val n:Int = json.number
  * }}}   
  * 
  * `for` comprehensions may also be used to retrieve json properties and array elements. 
  * This yields a `Some` if 'number' is a property of the json object, and if the property
  * value is convertable to an Int:
  * {{{
  * for {n:Int <- json.number} yield {n} 
  * }}}
  * 
  *  @define valueConvert The json property value is implicitly type converted to the type requested by the caller. 
  *  @define Json [[liquidj.json.Json]]
  *  @define alsoTypeConvert Useful for converting to collection classes and custom classes 
  *  with their own JsonReader implementations.  
  *  For simple scala types (`Int` and other numeric types, `String`, `Char`. And `Option`
  *  and Either variants of the simple types), the $Json object itself can be implicitly 
  *  type converted to the target type.
  *  @define PathException [[liquidj.json.PathException]]
  *  @define ConversionException [[liquidj.json.ConversionException]]
  */
sealed abstract class Json extends Dynamic {
  /** Retrieve json array element */
  def apply(index:Int):Json

  /** Retrieve json property via dynamic name mapping.
    *    `json.propertyName`
    * Array elements can be retrieved as well via the optional 
    * index parameter.
    *    `json.propertyName(index)`
    * If [[liquid.json.Implicit]] is imported
    * Json results can be implicitly converted into basic scala types
    */
  def applyDynamic(property:String)(index:Int = -1):Json 

  /** return true if this $Json does not contain a valid json value */
  def isEmpty:Boolean 

  /** return true if this $Json contains a valid json value */
  def isDefined = !isEmpty

  /** if nonempty, run a function on the unconverted json value */
  def mapJsValue[T](fn: JsValue=>T):Option[T] 

  /** if nonempty, run a function on the json value and return the result.  $valueConvert */
  def map[X,T](fn: T=>X)(implicit convert:Json=>Option[T]):Option[X] 

  /** if nonempty, run a function on the json value.  $valueConvert */
  def foreach[T](fn: T=>Unit)(implicit convert: Json=>Option[T]):Unit 

  /** if nonempty, use the json value in a function that returns an option.  $valueConvert  */
  def flatMap[X,T](fn: T=>Option[X])(implicit convert:Json=>Option[T]):Option[X] 

  /** if nonemepty and the given predicate holds, return the json value.  $valueConvert */
  def filter[T](fn: T=>Boolean)(implicit convert:Json=>Option[T]):Option[T] 

  /** if nonemepty, convert the contained json property to the requested type and return it
    * wrapped in a `Some`.  Otherwise return None.   $alsoTypeConvert */
  def toOption[T :JsonReader]:Option[T]

  /** Convert the contained json property to the requested type 
    * throws an $ConversionException if the property can't be converted to the requested
    * type. Throws a $PathException if the if the path or property name that produced this
    * $Json instance is invalid */
  def toType[T :JsonReader]:T 


  /** Return a `Right` containing the contained json property converted to the requested type.
    * Return a `Left` containing a $ConversionException if the property can't be 
    * converted to the requested type.
    * Return a `Left` containing a $PathException if the if the path or property name 
    * isn't valid */
  def toEither[T :JsonReader]:Either[Exception, T] 
}

/** An empty $Json object.  Normally created by referring to a json property, json 
  * array element or json object that doesn't exist in the json text.  
  */
case object NoneJson extends Json with Dynamic {
  def applyDynamic(property:String)(index:Int):Json = NoneJson
  def isEmpty = true
  def apply(index:Int):Json = NoneJson
  def mapJsValue[T](fn: JsValue=>T):Option[T] = None
  def foreach[T](fn: T=>Unit)(implicit convert: Json=>Option[T]) {}
  def flatMap[X,T](fn: T=>Option[X])(implicit convert:Json=>Option[T]):Option[X] = None
  def filter[T](fn: T=>Boolean)(implicit convert:Json=>Option[T]):Option[T] = None
  def map[X,T](fn: T=>X)(implicit convert:Json=>Option[T]):Option[X] = None
  def toOption[T :JsonReader]:Option[T] = None
  def toType[T :JsonReader]:T = throw PathException()
  def toEither[T :JsonReader]:Either[Exception, T] = Left(PathException())
}

/** $Json object containing a json property, json object, json array or json array alement.  
  * @see $Json 
  */
case class SomeJson(jsValue:JsValue) extends Json with Dynamic {
  def toOption[T :JsonReader]:Option[T] = jsValue.convertTo(safeReader[T]).right.toOption
  def toType[T :JsonReader]:T = jsValue.convertTo[T]
  def toEither[T :JsonReader]:Either[Exception, T] = jsValue.convertTo(safeReader[T])

  def isEmpty = false

  def map[X,T](fn: T=>X)(implicit convert:Json=>Option[T]):Option[X] = {
    convert(this) map {converted =>
      fn(converted)
    }
  }

  def mapJsValue[T](fn: JsValue=>T):Option[T] =  {
    Some(fn(jsValue))
  }

  def foreach[T](fn: T=>Unit)(implicit convert: Json=>Option[T]) {
    convert(this) foreach {converted =>
      fn(converted)
    }
  }

  def flatMap[X,T](fn: T=>Option[X])(implicit convert:Json=>Option[T]):Option[X] = {
    convert(this) flatMap {converted =>
      fn(converted)
    }
  }

  def filter[T](fn: T=>Boolean)(implicit convert:Json=>Option[T]):Option[T] = {
    convert(this) filter fn
  }

  def applyDynamic(property:String)(index:Int):Json = {
    val propertyJson = 
      jsValue match {
        case obj:JsObject =>
          obj.fields.get(property).map {SomeJson(_)} getOrElse NoneJson
        case _ => NoneJson
      }
    index match {
          // SCALA is there a way to use Nothing for this?
      case -1 => propertyJson      // default is no array access (-1 is set in class Json)
      case n => propertyJson(n)    // use array accessor  (apply)
    }
  }

  def apply(index:Int):Json = {
    jsValue match {
      case obj:JsArray =>
        obj.elements.slice(index,index+1).headOption.map {SomeJson(_)} getOrElse NoneJson
      case _ => NoneJson
    }
  }
}

