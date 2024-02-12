package pe.fernan.apps.data.network

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.internal.http2.ConnectionShutdownException
import okhttp3.logging.HttpLoggingInterceptor
import pe.fernan.apps.utils.NetworkResult
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object HoroscopeClient {
    val baseUrl = "https://www.losarcanos.com"
    private val countryOfBirthUrl = "$baseUrl/carta-astral-2.php"
    private val stateOfCountryUrl = "$baseUrl/carta-astral-3.php"
    private val locationOfStateUrl = "$baseUrl/carta-astral-5.php"
    private val astralChartUrl = "$baseUrl/carta-astral-resu-m.php"

    private val defaultUserAgent = "Mozilla/5.0 (Windows NT 10.0; rv:109.0) Gecko/20100101 Firefox/119.0"


    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build()

    fun get(url: String): Response {
        val request = Request.Builder()
            .url(url)
            .build()
        return client.newCall(request).execute()
    }

    fun post(url: String, body: String, bodyRequestBuilder: Request.Builder.() -> Unit): Response {
        val mediaType = "application/x-www-form-urlencoded".toMediaType()
        val body = body.toRequestBody(mediaType)
        val request = Request.Builder()
            .url(url)
            .post(body).apply {
                bodyRequestBuilder.invoke(this)
            }
            .addHeader("User-Agent", defaultUserAgent)
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()

        return client.newCall(request).execute()
    }


    suspend fun handleApi(
        execute: suspend () -> Response
    ): NetworkResult<String> {
        return try {
            val response = execute()
            val body = response.body
            if (response.isSuccessful && body != null) {
                NetworkResult.Success(body.string() ?: "")
            } else {
                NetworkResult.Error(code = response.code, message = response.message)
            }
        } catch (e: Exception) {
            var msg = ""
            when (e) {
                is SocketTimeoutException -> {
                    msg = "Timeout - Please check your internet connection"
                }

                is UnknownHostException -> {
                    msg = "Unable to make a connection. Please check your internet"
                }

                is ConnectionShutdownException -> {
                    msg = "Connection shutdown. Please check your internet"
                }

                is IOException -> {
                    msg = "Server is unreachable, please try again later."
                }

                is IllegalStateException -> {
                    msg = "${e.message}"
                }

                else -> {
                    msg = "${e.message}"
                }
            }
            NetworkResult.Exception(Error(msg))
        }
    }


    suspend fun getPageCountryOfBirth(): NetworkResult<String> {
        return handleApi { get(countryOfBirthUrl) }
    }

    suspend fun getStateOfCountry(codeCountry: Int): NetworkResult<String> {
        val body = "nombre=e&dia=01&mes=01&ano=1970&hora=00&minutos=00&pais=$codeCountry&action="
        return handleApi {
            post(stateOfCountryUrl, body) {
                this.addHeader(
                    "Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8"
                )
                    .addHeader("Accept-Language", "en-US,en;q=0.5")
                    .addHeader("Origin", baseUrl)
                    .addHeader("DNT", "1")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Referer", countryOfBirthUrl)
                    .addHeader("Upgrade-Insecure-Requests", "1")
                    .addHeader("Sec-Fetch-Dest", "document")
                    .addHeader("Sec-Fetch-Mode", "navigate")
                    .addHeader("Sec-Fetch-Site", "same-origin")
                    .addHeader("Sec-Fetch-User", "?1")
                    .addHeader("TE", "trailers")

            }
        }

    }

    suspend fun getLocationOfState(codeCountry: Int, codeState: Int): NetworkResult<String> {
        val body =
            "ciudad=${codeState}&action=&nombre=e&dia=01&mes=01&ano=1970&hora=00&minutos=00&pais=${codeCountry}&V5="

        return handleApi {
            post(locationOfStateUrl, body) {
                this.addHeader(
                    "Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8"
                )
                    .addHeader("Accept-Language", "en-US,en;q=0.5")
                    .addHeader("Referer", stateOfCountryUrl)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Origin", baseUrl)
                    .addHeader("DNT", "1")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Upgrade-Insecure-Requests", "1")
                    .addHeader("Sec-Fetch-Dest", "document")
                    .addHeader("Sec-Fetch-Mode", "navigate")
                    .addHeader("Sec-Fetch-Site", "same-origin")
                    .addHeader("Sec-Fetch-User", "?1")
                    .addHeader("TE", "trailers")

            }
        }

    }


    suspend fun getAstralChart(
        codeCountry: Int,
        codeState: Int,
        codeLocation: Int,
        day: String,
        month: String,
        year: String,
        hour: String,
        minutes: String
    ): NetworkResult<String> {
        val body =
            "ciudad=${codeLocation}&" +
                    "action=&" +
                    "nombre=e&" +
                    "dia=$day&" +
                    "mes=$month&" +
                    "ano=$year&" +
                    "hora=$hour&" +
                    "minutos=$minutes&" +
                    "estado=$codeState&" +
                    "pais=$codeCountry&" +
                    "geoloc=2&" +
                    "V5="

        return handleApi { post(url = astralChartUrl, body = body){
            this.addHeader(
                "Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8"
            )
            .addHeader("Accept-Language", "en-US,en;q=0.5")
            .addHeader("Origin", baseUrl)
            .addHeader("DNT", "1")
            .addHeader("Connection", "keep-alive")
            .addHeader("Referer", locationOfStateUrl)
            .addHeader("Upgrade-Insecure-Requests", "1")
            .addHeader("Sec-Fetch-Dest", "document")
            .addHeader("Sec-Fetch-Mode", "navigate")
            .addHeader("Sec-Fetch-Site", "same-origin")
            .addHeader("Sec-Fetch-User", "?1")
        } }

    }


}