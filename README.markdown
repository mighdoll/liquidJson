LiquidJson is a convenient way to use JSON objects from scala using a natural syntax.  It's built on spray-json and dynamic dispatch.
****
   
* Scala member syntax can be used to access property values, and they are implicitly converted to the correct type.

        val n:Int = json.number
        val b:Boolean = json.nested.child
        val four:Int = json.array(1)
    
   
* For comprehensions are also available:

        for {n:Int <- json.number} yield {n}
    
  
* Create a Json object by using the `json` method against a json encoded String

        import liquidj.json.Implicits._
        val json = """
          { "number": 9,
            "nested":
               { "child": true },
            "array": [5,4,3]
          }""".json

