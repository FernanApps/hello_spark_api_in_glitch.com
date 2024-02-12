package pe.fernan.apps.data

import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject
import pe.fernan.apps.data.model.AstralCharDiv
import pe.fernan.apps.data.model.CountryDiv
import pe.fernan.apps.data.model.StateDiv
import pe.fernan.apps.data.network.HoroscopeClient
import pe.fernan.apps.data.utils.JSPoonExt.toHtmlAdapter
import pe.fernan.apps.domain.model.AstralChart
import pe.fernan.apps.domain.model.Country
import pe.fernan.apps.domain.model.Location
import pe.fernan.apps.domain.model.State
import pe.fernan.apps.domain.respositories.AstralChartRepository
import pe.fernan.apps.utils.DataState
import pe.fernan.apps.utils.toDataState

class AstralChartRepositoryImp(private val client: HoroscopeClient) : AstralChartRepository {
    override suspend fun getCountryOfBirth(): DataState<List<Country>> {
        val responseNetworkResult = client.getPageCountryOfBirth().toDataState()
        if (responseNetworkResult is DataState.Error) {
            return responseNetworkResult
        }

        val pageString = (responseNetworkResult as DataState.Success).data
        val countryDiv = pageString.toHtmlAdapter<CountryDiv>()
        if (countryDiv == null) {
            return DataState.Error("Error parse")
        }
        val countryList = countryDiv.selectorCss?.cssList?.mapNotNull {
            if (it.text != null && it.value != null) {
                it.value?.toIntOrNull() ?: return@mapNotNull null
                return@mapNotNull Country(id = it.value!!.toInt(), name = it.text!!)
            }
            null
        }?.associateBy { it.id }?.values?.toList().orEmpty()
        return DataState.Success(countryList)

    }

    override suspend fun getStateOfCountry(codeCountry: Int): DataState<List<State>> {
        val responseNetworkResult = client.getStateOfCountry(codeCountry).toDataState()
        if (responseNetworkResult is DataState.Error) {
            return responseNetworkResult
        }

        val pageString = (responseNetworkResult as DataState.Success).data
        val stateDiv = pageString.toHtmlAdapter<StateDiv>()
        if (stateDiv == null) {
            return DataState.Error("Error parse")
        }
        val stateList = stateDiv.selectorCss?.cssList?.mapNotNull {
            if (it.text != null && it.value != null) {
                it.value?.toIntOrNull() ?: return@mapNotNull null
                return@mapNotNull State(id = it.value!!.toInt(), name = it.text!!)
            }
            null
        }?.associateBy { it.id }?.values?.toList().orEmpty()
        return DataState.Success(stateList)

    }

    override suspend fun getLocationOfState(codeCountry: Int, codeState: Int): DataState<List<Location>> {
        val responseNetworkResult = client.getLocationOfState(codeCountry, codeState = codeState).toDataState()
        if (responseNetworkResult is DataState.Error) {
            return responseNetworkResult
        }

        val pageString = (responseNetworkResult as DataState.Success).data
        val locationDiv = pageString.toHtmlAdapter<StateDiv>()
        if (locationDiv == null) {
            return DataState.Error("Error parse")
        }
        val locationList = locationDiv.selectorCss?.cssList?.mapNotNull {
            if (it.text != null && it.value != null) {
                it.value?.toIntOrNull() ?: return@mapNotNull null
                return@mapNotNull Location(id = it.value!!.toInt(), name = it.text!!)
            }
            null
        }?.associateBy { it.id }?.values?.toList().orEmpty()
        return DataState.Success(locationList)

    }


    override suspend fun getAstralChart(
        codeCountry: Int,
        codeState: Int,
        codeLocation: Int,
        day: Int,
        month: Int,
        year: Int,
        hour: Int,
        minutes: Int
    ): DataState<AstralChart> {
        val dayString = if (day < 10) "0$day" else day.toString()
        val monthString = if (month < 10) "0$month" else month.toString()

        val hourString = if (hour < 10) "0$hour" else hour.toString()
        val minutesString = if (minutes < 10) "0$minutes" else minutes.toString()

        val responseNetworkResult =
            client.getAstralChart(
                codeCountry = codeCountry,
                codeState = codeState,
                codeLocation = codeLocation,
                day = dayString,
                month = monthString,
                year = year.toString(),
                hour = hourString,
                minutes = minutesString
            ).toDataState()

        if (responseNetworkResult is DataState.Error) {
            return responseNetworkResult
        }

        val pageString = (responseNetworkResult as DataState.Success).data
        val astralCharDiv = pageString.toHtmlAdapter<AstralCharDiv>()
        if (astralCharDiv == null) {
            return DataState.Error("Error parse")
        }
        val astralChart = astralCharDiv.let {
            val fixImageUrl =
                if (it.image.isNullOrEmpty()) {
                    ""
                } else if (it.image!!.startsWith("http")) {
                    it.image!!
                } else {
                    client.baseUrl + it.image
                }
            AstralChart(image = fixImageUrl, params = it.dataList)
        }
        return DataState.Success(astralChart)


    }
}




fun main(): Unit = runBlocking {




    //return@runBlocking
    val astralRepository = AstralChartRepositoryImp(HoroscopeClient)
    //val list = astralRepository.getCountryOfBirth()
    //val list = astralRepository.getStateOfCountry(100)


    val astralChart = astralRepository.getAstralChart(
        codeCountry = 87,
        codeState = 515,
        codeLocation = 249519,
        day = 1,
        month = 21,
        year = 1997,
        hour = 12,
        minutes = 45
    )



    when (astralChart) {
        is DataState.Error -> println(astralChart.message)
        is DataState.Success -> println(astralChart.data)
    }
    return@runBlocking
    val list = astralRepository.getLocationOfState(87, codeState = 496)

    when (list) {
        is DataState.Error -> println(list.message)
        is DataState.Success -> list.data.map { println("Model ${it.id} = ${it.name}") }
    }

}