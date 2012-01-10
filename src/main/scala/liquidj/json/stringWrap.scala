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

/** Extends `String` to include the `json` method. 
  *
  * Here's an example:
  * {{{
  * val name = """ 
  *     { "first": "milo", 
  *       "initial": "j", 
  *       "last": "jones" }
  *   """.json
  * val initial:Char = name.initial 
  * }}}
  */
class JsonStringWrap(jsonString:String) {
  /** Returns a [[liquidj.json.Json]] created by parsing a json string. 
    * Throws an exception if the json string isn't valid json syntax.  */
  def json:Json = Json(jsonString)
}

private[json] trait ImplicitJsonString {
  implicit def jsonStringWrap(s:String) = new JsonStringWrap(s)
}
