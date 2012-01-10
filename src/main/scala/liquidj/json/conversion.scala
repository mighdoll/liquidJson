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
import cc.spray.json.{JsValue, JsonFormat}
import cc.spray.json.DefaultJsonProtocol._

/** Failure to find a property or one of its parents while converting a property to
  * a native type.  
  * If this is thrown, the json text is syntactically valid, but the conversion 
  * to a user specified type has failed.
  */
case class PathException(msg:String = "") extends RuntimeException(msg)

/** Failure while converting a [[liquidj.json.Json]] to a user specified type.
  * If this is thrown, the json text is syntactically valid, but the conversion 
  * to a user specified type has failed.
  * For example, if a caller attempts to convert a json property value containing 
  * more than one character to a `Char`, a [[liquidj.json.ConversionException]] would 
  * be thrown.  
  */
case class ConversionException(msg:String = "") extends RuntimeException(msg)


/** Crate functions for implicit type conversions based on implicit JsonFormat context 
  * bounds.  (The context bounds conversions work correctly w/o conversion, but 
  * require the library user to make a method call `convertTo` to bring them 
  * into scope.  By adapting them for implicit type conversion, the caller can drop
  * the convertTo call.  
  */
private[json] object Converter {

  /** Convert Json to a caller specified type.  
    * @return Right() for a successful conversion, or Left() for an error 
    */
  def toEither[T :JsonFormat :Manifest](json:Json):Either[Exception, T] = {
    json.mapJsValue { _.convertTo(safeReader[T]) } getOrElse {
      Left(PathException("property not found"))
    }
  }

  /** Convert Json to a caller specified type.  
    * @return Some() for a successful conversion, or [[scala.None]] for an error 
    */
  def toOption[T :JsonFormat :Manifest](json:Json):Option[T] = {
    val format = implicitly[JsonFormat[T]]
    val m = manifest[T]
    toOption(toEither(json)(format, m))
  }

  /** Convert Json to a caller specified type.  
    * @return the converted value.
    * Throws [[liquidj.json.ConversionException]] if the conversion fails 
    */
  def toType[T :JsonFormat :Manifest](json:Json):T = {
    val format = implicitly[JsonFormat[T]]
    val m = manifest[T]
    requireRight(toEither(json)(format,m))
  }

  private def toOption[A,B](either:Either[A,B]):Option[B] = either.right.toOption

  private def requireRight[T](result:Either[Exception, T]):T = {
    result.fold({exception => throw exception}, {conversion => conversion})
  }
}

