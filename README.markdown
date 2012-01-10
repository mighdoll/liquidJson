A convenient way to use JSON objects from scala using a natural syntax. Built atop spray-json and dynamic dispatch.

Scala member syntax can be used to access property values, and they are implicitly converted to the correct type.
    val n:Int = json.number

For comprehensions are also available:
    for {n:Int <- json.number} yield {n}

Create a Json object by using the json method against a json encoded String
    import liquidj.json.Implicits._
    val json = """
      { "number": 9,
        "nested":
           { "child": true }
        "array": [5,4,3]
      }""".json

