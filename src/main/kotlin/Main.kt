import spark.Spark.*



fun main(args: Array<String>) {
    get("/hello") { req, res -> "Hello World In Kotlin " }
}