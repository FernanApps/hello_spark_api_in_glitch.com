package pe.fernan.apps.domain.use_case

import pe.fernan.apps.domain.respositories.AstralChartRepository

class GetAstralChartLocationOfStateUseCase(private val astralRepository: AstralChartRepository) {
    suspend operator fun invoke(codeCountry: Int, codeState: Int) =
        astralRepository.getLocationOfState(codeCountry, codeState = codeState)

}