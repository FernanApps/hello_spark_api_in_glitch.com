package pe.fernan.apps.domain.use_case

import pe.fernan.apps.domain.respositories.AstralChartRepository

class GetAstralChartUseCase(private val astralRepository: AstralChartRepository) {
    suspend operator fun invoke(
        codeCountry: Int,
        codeState: Int,
        codeLocation: Int,
        day: Int,
        month: Int,
        year: Int,
        hour: Int,
        minutes: Int
    ) = astralRepository.getAstralChart(
        codeCountry = codeCountry,
        codeState = codeState,
        codeLocation = codeLocation,
        day = day,
        month = month,
        year = year,
        hour = hour,
        minutes = minutes
    )

}