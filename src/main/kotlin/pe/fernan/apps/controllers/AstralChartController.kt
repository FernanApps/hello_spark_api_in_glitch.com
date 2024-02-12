package pe.fernan.apps.controllers

import org.koin.java.KoinJavaComponent
import pe.fernan.apps.domain.use_case.GetAstralChartAllCountryUseCase
import pe.fernan.apps.domain.use_case.GetAstralChartLocationOfStateUseCase
import pe.fernan.apps.domain.use_case.GetAstralChartStateOfCountryUseCase
import pe.fernan.apps.domain.use_case.GetAstralChartUseCase
import spark.Spark

class AstralChartController(
    private val getAstralChartUseCase: GetAstralChartUseCase,
    private val getAstralChartAllCountryUseCase: GetAstralChartAllCountryUseCase,
    private val getAstralChartStateOfCountryUseCase: GetAstralChartStateOfCountryUseCase,
    private val getAstralChartLocationOfStateUseCase: GetAstralChartLocationOfStateUseCase
) {


    fun getCountry(){
        Spark.get("/horoscope-astral-chart-country") { req, res ->
            returnDefault(res) {
                getAstralChartAllCountryUseCase.invoke()
            }
        }

    }

    fun getState(){
        /**
        curl --location 'http://localhost:4567/horoscope-astral-chart-state' \
        --header 'Content-Type: application/x-www-form-urlencoded' \
        --data-urlencode 'codeCountry=87'

         */

        Spark.post("/horoscope-astral-chart-state") { req, res ->
            val codeCountry = req.queryParams("codeCountry").toIntOrNull()
            return@post checkParams(res, codeCountry) {
                returnDefault(res) {
                    getAstralChartStateOfCountryUseCase.invoke(codeCountry!!.toInt())
                }
            }

        }
    }

    fun getLocation() {

        /**
        curl --location 'http://localhost:4567/horoscope-astral-chart-location' \
        --header 'Content-Type: application/x-www-form-urlencoded' \
        --data-urlencode 'codeCountry=7' \
        --data-urlencode 'codeState=10' \


         */

        Spark.post("/horoscope-astral-chart-location") { req, res ->
            val codeCountry = req.queryParams("codeCountry").toIntOrNull()
            val codeState = req.queryParams("codeState").toIntOrNull()
            return@post checkParams(res, codeCountry, codeState) {
                returnDefault(res) {
                    getAstralChartLocationOfStateUseCase.invoke(codeCountry = codeCountry!!, codeState!!)
                }
            }

        }




    }

    fun getAstralChart(){
        /**
        curl --location 'http://localhost:4567/horoscope-astral-chart' \
        --header 'Content-Type: application/x-www-form-urlencoded' \
        --data-urlencode 'codeCountry=7' \
        --data-urlencode 'codeState=50' \
        --data-urlencode 'codeLocation=292219' \
        --data-urlencode 'day=14' \
        --data-urlencode 'month=12' \
        --data-urlencode 'year=1947' \
        --data-urlencode 'hour=10' \
        --data-urlencode 'minutes=45'

         */
        Spark.post("/horoscope-astral-chart") { req, res ->
            val codeCountry = req.queryParams("codeCountry").toIntOrNull()
            val codeState = req.queryParams("codeState").toIntOrNull()
            val codeLocation = req.queryParams("codeLocation").toIntOrNull()
            val day = req.queryParams("day").toIntOrNull()
            val month = req.queryParams("month").toIntOrNull()
            val year = req.queryParams("year").toIntOrNull()
            val hour = req.queryParams("hour").toIntOrNull()
            val minutes = req.queryParams("minutes").toIntOrNull()

            return@post checkParams(
                response = res,
                codeCountry, codeState, codeLocation, day, month, year, hour, minutes
            ) {
                returnDefault(response = res) {
                    getAstralChartUseCase.invoke(
                        codeCountry!!,
                        codeState!!,
                        codeLocation!!,
                        day!!,
                        month!!,
                        year!!,
                        hour!!,
                        minutes!!
                    )
                }
            }

        }
    }




}