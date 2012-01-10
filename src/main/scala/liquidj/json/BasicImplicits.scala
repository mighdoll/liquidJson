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
import cc.spray.json.DefaultJsonProtocol._

/** Implicit conversions for property access of basic types  */
private[json] trait BasicImplicits {
  /** SCALA is there a better way to expose these implicits? */
  import Converter._

  implicit def implicitStringOption = toOption[String] _
  implicit def implicitStringEither = toEither[String] _
  implicit def implicitStringType = toType[String] _

  implicit def implicitCharOption = toOption[Char] _
  implicit def implicitCharEither = toEither[Char] _
  implicit def implicitCharType = toType[Char] _

  implicit def implicitBigDecimalOption = toOption[BigDecimal] _
  implicit def implicitBigDecimalEither = toEither[BigDecimal] _
  implicit def implicitBigDecimalType = toType[BigDecimal] _

  implicit def implicitBigIntOption = toOption[BigInt] _
  implicit def implicitBigIntEither = toEither[BigInt] _
  implicit def implicitBigIntType = toType[BigInt] _

  implicit def implicitLongOption = toOption[Long] _
  implicit def implicitLongEither = toEither[Long] _
  implicit def implicitLongType = toType[Long] _

  implicit def implicitIntOption = toOption[Int] _
  implicit def implicitIntEither = toEither[Int] _
  implicit def implicitIntType = toType[Int] _

  implicit def implicitShortOption = toOption[Short] _
  implicit def implicitShortEither = toEither[Short]  _
  implicit def implicitShortType = toType[Short]  _

  implicit def implicitByteOption = toOption[Byte] _
  implicit def implicitByteEither = toEither[Byte] _
  implicit def implicitByteType = toType[Byte] _

  implicit def implicitDoubleOption = toOption[Double] _
  implicit def implicitDoubleEither = toEither[Double] _
  implicit def implicitDoubleType = toType[Double] _

  implicit def implicitFloatOption = toOption[Float] _
  implicit def implicitFloatEither = toEither[Float] _
  implicit def implicitFloatType = toType[Float] _

  implicit def implicitBooleanOption = toOption[Boolean] _
  implicit def implicitBooleanEither = toEither[Boolean] _
  implicit def implicitBooleanType = toType[Boolean] _
}

