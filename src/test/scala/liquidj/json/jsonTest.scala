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
import org.scalatest.FunSuite
import Implicits._

class JsonSuite extends FunSuite {
  val arrayJson = """ { "array": [3,1,4,5] } """.json
  val numberJson = """ { "number": 9 } """.json
  val stringJson = """ { "name": "lee" } """.json

  test("simple numeric property") {
    val number:Int = numberJson.number
    assert(number === 9)
  }

  test("optional numeric property") {
    val number:Option[Int] = numberJson.number
    assert(number === Some(9))
  }

  test("even more nested field") {
    val json = """ { "parent": { "childNumber":2 } } """.json
    val number:Int = json.parent.childNumber
    assert(number === 2)
  }

  test("string") {
    assert("lee" === (stringJson.name:String))
    assert((stringJson.name:Option[Int]).isEmpty)
    assert((stringJson.name:Option[String]) === Some("lee"))
  }

  test("map") {
    val mapped = 
      stringJson.name map {name:String => name} 
    assert(mapped === Some("lee"))
  }

  test("foreach") {
    var found = false
    stringJson.name foreach {name:String =>
      assert(name === "lee")
      found = true
    }
    assert(found)
  }

  test("flatMap") {
    val flatMapped = 
      stringJson.name flatMap {name:String => Some(name)}
    assert(flatMapped === Some("lee"))
  }

  test("for uses map") {
    val mapped = for (name:String <- stringJson.name) yield {name}
    assert(mapped === Some("lee"))
  }

  test("for uses foreach ") {
    var found = false
    for (name:String <- stringJson.name) {if (name == "lee") found = true}
    assert(found)
  }

  test("for doesn't actually use map/filter here") {
    var found = false
    for (name:String <- stringJson.name; if name == "lee") yield {found = true}
    assert(found === true)
  }

  test("array via apply") {
    val array = arrayJson.array
    val number:Int = array(2)
    assert(number === 4)
  }

  test("array via applyDymamic") {
    val number:Int = arrayJson.array(3)
    assert(number === 5)
  }

  test("optional array via applyDymamic") {
    val noNumber:Option[Int] = arrayJson.array(5)
    assert(noNumber === None)

    val someNumber:Option[Int] = arrayJson.array(1)
    assert(someNumber === Some(1))
  }

  test("char") {
    val charJson = """ { "initial": "j" } """.json
    assert('j' === (charJson.initial:Char))
    assert((stringJson.name:Option[Char]).isEmpty)
    assert((stringJson.name:Either[Exception,Char]).isLeft) 
  }

  test("numeric types") {
    val json = """ {"float":1.5, "num":4 } """.json
    assert(1.5 === (json.float:Double))
    assert(1.5f === (json.float:Float))

    assert(4 === (json.num:Int))
    assert(4L === (json.num:Long))
    assert(4 === (json.num:Short))
    assert(4 === (json.num:Byte))
    assert(4 === (json.num:BigInt))
    assert(4 === (json.num:BigDecimal))
  }

  test("boolean") {
    val reservedJson = """ { "bool": true } """.json
    assert((reservedJson.bool:Boolean) === true)
  }

  test("symbol") {
    val reservedJson = """ { "var": "reserved" } """.json
    assert("reserved" === (reservedJson.`var`:String))
  }

  test("convert to List") {
    import cc.spray.json.DefaultJsonProtocol._
    val list = arrayJson.array.toType[List[Int]]
    assert(list.length === 4)
    assert(list(1) === 1)
  }
}

