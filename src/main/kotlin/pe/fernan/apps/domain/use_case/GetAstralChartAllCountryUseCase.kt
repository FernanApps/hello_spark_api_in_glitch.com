package pe.fernan.apps.domain.use_case

import pe.fernan.apps.domain.model.AstralChart
import pe.fernan.apps.domain.respositories.AstralChartRepository
import pe.fernan.apps.utils.DataState

class GetAstralChartAllCountryUseCase(private val astralRepository: AstralChartRepository){
    suspend operator fun invoke() = astralRepository.getCountryOfBirth()

}