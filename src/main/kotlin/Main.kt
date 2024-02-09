import kotlinx.coroutines.*
import spark.Spark.*
import java.text.SimpleDateFormat
import java.util.*


fun main(args: Array<String>) {
    val applicationScope = CoroutineScope(Dispatchers.Default)

    // port(8080)
    get("/hello") { req, res ->
       "Hello World In Kotlin - Spark"
    }

    get("/hello-coroutine") { req, res ->
        val response = runBlocking {
            val initialTime = System.currentTimeMillis().toFormattedDate()
            delay(10000)
            val endTime = System.currentTimeMillis().toFormattedDate()
            "Hello World In Kotlin - Spark \n Initial Time : $initialTime \n End time : $endTime"
        }
        response
    }
}


fun Long.toFormattedDate(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    val sdf = SimpleDateFormat(pattern)
    val date = Date(this)
    return sdf.format(date)
}