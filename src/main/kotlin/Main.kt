import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent
import pe.fernan.apps.controllers.AstralChartController
import pe.fernan.apps.data.AstralChartRepositoryImp
import pe.fernan.apps.data.network.HoroscopeClient
import pe.fernan.apps.data.network.HoroscopeClient.getAstralChart
import pe.fernan.apps.domain.respositories.AstralChartRepository
import pe.fernan.apps.domain.use_case.GetAstralChartAllCountryUseCase
import pe.fernan.apps.domain.use_case.GetAstralChartLocationOfStateUseCase
import pe.fernan.apps.domain.use_case.GetAstralChartStateOfCountryUseCase
import pe.fernan.apps.domain.use_case.GetAstralChartUseCase
import spark.Spark
import spark.Spark.internalServerError
import spark.Spark.notFound
import java.text.SimpleDateFormat
import java.util.*


private val astralModule = module {
    single { HoroscopeClient }

    //factory<AstralChartRepository>{ AstralChartRepositoryImp(get()) }
    single<AstralChartRepository> { AstralChartRepositoryImp(get()) }

    // UseCases
    // singleOf(::GetAstralChartExampleUseCase)
    singleOf(::GetAstralChartUseCase)
    singleOf(::GetAstralChartAllCountryUseCase)
    singleOf(::GetAstralChartStateOfCountryUseCase)
    singleOf(::GetAstralChartLocationOfStateUseCase)

    singleOf(::AstralChartController)

}


fun main(args: Array<String>) {
    org.koin.core.context.startKoin {
        modules(astralModule)
    }
    val astralChartController: AstralChartController by KoinJavaComponent.inject(
        AstralChartController::class.java
    )

    Spark.get("/a") { req, res ->
        res.type("application/json")
        "{\"message\":\"Hello Horoscope Api\"}"
    }

    Spark.get("/") { req, res ->
        res.type("text/html")
        """
            <!doctype html>
            <html>
            <head>
            <meta charset="UTF-8">
            <script src="https://cdnjs.cloudflare.com/ajax/libs/markdown-it/13.0.1/markdown-it.min.js" integrity="sha512-SYfDUYPg5xspsG6OOpXU366G8SZsdHOhqk/icdrYJ2E/WKZxPxze7d2HD3AyXpT7U22PZ5y74xRpqZ6A2bJ+kQ==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>


            <script>
            window.onload = function() {
              var md = window.markdownit();
              var div = document.getElementsByClassName('markdown');
              for(var i = 0; i < div.length; i++) {
                var content = div[i].innerHTML;
                document.getElementsByClassName('markdown')[i].innerHTML = md.render(content);
              }
            }
            </script>

            </head>
            <body>


            <div class="markdown">
            
            # Documentación de la API Horóscopo

            ## Horóscopo - Carta Astral 

            ### Códigos de Paises
            - **Ruta:** `/horoscope-astral-chart-country`
            - **Método:** GET

            **Ejemplo de respuesta:** 
            ```json
            	[
            	  {
            	    "id": 100,
            	    "name": "ESPAÑA"
            	  },
            	  {
            	    "id": 5,
            	    "name": "ARGENTINA"
            	  },
            	  ....
            	]
            ```


            Esta ruta devuelve todos los codigo de paises

            ### Códigos de Estados

            - **Ruta:** `/horoscope-astral-chart-state`
            - **Método:** POST
            - **Parámetros:**
              - `codeCountry`: Código del país

            Este endpoint devuelve todos los codigo de estados de un pais. 

            **Ejemplo de solicitud:**

            ```bash
            curl --location '/horoscope-astral-chart-state' \
            --header 'Content-Type: application/x-www-form-urlencoded' \
            --data-urlencode 'codeCountry=87'
            ```
            **Ejemplo de respuesta:**
            ```json
            	[
            	  {
            	    "id": 496,
            	    "name": "Amazonas"
            	  },
            	  {
            	    "id": 497,
            	    "name": "Ancash"
            	  },
            	  ...
            	]
            ```

            ### Códigos de Lugar

            - **Ruta:** `/horoscope-astral-chart-location`
            - **Método:** POST
            - **Parámetros:**
              - `codeCountry`: Código del país
              - `codeState`: Código del estado seleccionado

            Este endpoint devuelve todos los codigo de los lugares de un estado de un pais. 

            **Ejemplo de solicitud:**

            ```bash
            curl --location 'http://localhost:4567/horoscope-astral-chart-location' \  
            --header 'Content-Type: application/x-www-form-urlencoded' \  
            --data-urlencode 'codeCountry=7' \  
            --data-urlencode 'codeState=10' \
            ```
            **Ejemplo de respuesta:**
            ```json
            	[
            	  {
            	    "id": 247829,
            	    "name": "Acachare"
            	  },
            	  {
            	    "id": 247831,
            	    "name": "Acacoto"
            	  },
            	  ...
            	]
            ```

            ### Carta Astral

            - **Ruta:** `/horoscope-astral-chart`
            - **Método:** POST
            - **Descripción:** Este endpoint devuelve la carta astral para una fecha y hora específicas, en una ubicación determinada dentro de un país y estado especificados.
            - **Parámetros:**
              - `codeCountry`: Código del país
              - `codeState`: Código del estado
              - `codeLocation`: Código de la ubicación
              - `day`: Día
              - `month`: Mes
              - `year`: Año
              - `hour`: Hora
              - `minutes`: Minutos

            **Ejemplo de solicitud:**

            ```bash
            curl --location '/horoscope-astral-chart' \
            --header 'Content-Type: application/x-www-form-urlencoded' \
            --data-urlencode 'codeCountry=7' \
            --data-urlencode 'codeState=50' \
            --data-urlencode 'codeLocation=292219' \
            --data-urlencode 'day=14' \
            --data-urlencode 'month=12' \
            --data-urlencode 'year=1947' \
            --data-urlencode 'hour=10' \
            --data-urlencode 'minutes=45'
            ```
            **Ejemplo de respuesta:**
            ```json
            	{
            	  "image": "https://example.com/image.img",
            	  "params": {
            	    "Nacimiento": "Martes 4 Enero 1997",
            	    "Lugar": "Várzea Redonda - Beja - Portugal",
            	    "Long": "08W30 Lat:37N34",
            	    "Hora nacimiento": "20:45",
            	    "Tiempo Universal": "19:45",
            	    "Signo Solar": "Capricornio",
            	    "Signo Lunar": "Aries",
            	    "Signo Ascendente": "Leo",
            	    "Fortuna": "Geminis",
            	    "Lilith": "Leo"
            	  }
            	}
            ```
            # Working....



            </div>

            </body>
            </html>
        """.trimIndent()
    }


    astralChartController.apply {
        getCountry()
        getState()
        getLocation()
        getAstralChart()
    }




    notFound { req, res ->
        res.type("application/json")
        "{\"message\":\"Not Found 404\"}"
    }
    internalServerError { req, res ->
        res.type("application/json")
        "{\"message\":\"Error internal 500 \"}"
    }
}

private fun defaultHtmlWithMarkDown(markdownText: String): String = """
            <!doctype html>
            <html>
            <head>

            <script src="https://cdnjs.cloudflare.com/ajax/libs/markdown-it/13.0.1/markdown-it.min.js" integrity="sha512-SYfDUYPg5xspsG6OOpXU366G8SZsdHOhqk/icdrYJ2E/WKZxPxze7d2HD3AyXpT7U22PZ5y74xRpqZ6A2bJ+kQ==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>


            <script>
            window.onload = function() {
              var md = window.markdownit();
              var div = document.getElementsByClassName('markdown');
              for(var i = 0; i < div.length; i++) {
                var content = div[i].innerHTML;
                document.getElementsByClassName('markdown')[i].innerHTML = md.render(content);
              }
            }
            </script>

            </head>
            <body>


            <div class="markdown">
            $markdownText
            </div>

            </body>
            </html>
        """.trimIndent()


fun Long.toFormattedDate(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    val sdf = SimpleDateFormat(pattern)
    val date = Date(this)
    return sdf.format(date)
}


