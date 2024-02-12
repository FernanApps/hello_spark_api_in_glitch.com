package pe.fernan.apps.domain.respositories

import pe.fernan.apps.domain.model.AstralChart
import pe.fernan.apps.domain.model.Country
import pe.fernan.apps.domain.model.Location
import pe.fernan.apps.domain.model.State
import pe.fernan.apps.utils.DataState

interface AstralChartRepository {
    suspend fun getCountryOfBirth(): DataState<List<Country>>
    suspend fun getStateOfCountry(countryCode: Int): DataState<List<State>>
    suspend fun getLocationOfState(codeCountry: Int, codeState: Int): DataState<List<Location>>

    suspend fun getAstralChart(
        codeCountry: Int,
        codeState: Int,
        codeLocation: Int,
        day: Int,
        month: Int,
        year: Int,
        hour: Int,
        minutes: Int
    ): DataState<AstralChart>
}

