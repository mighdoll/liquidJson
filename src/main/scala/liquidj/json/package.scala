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

package liquidj

/** A convenient way to use json objects from scala using a natural syntax.
  * Built atop spray-json and dynamic dispatch. 
  * 
  * Scala member syntax can be used to access property values, and they are 
  * implicitly converted to the correct type.
  * {{{
  * val n:Int = json.number
  * }}}   
  *
  * For comprehensions are also available:
  * {{{
  * for {n:Int <- json.number} yield {n} 
  * }}}
  *
  * Create a $Json object by using the `json` method against a json encoded `String`
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
  *  @define Json [[liquidj.json.Json]]
  */
package object json
