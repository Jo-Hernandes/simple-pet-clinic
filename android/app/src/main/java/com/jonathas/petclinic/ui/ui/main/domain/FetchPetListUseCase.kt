package com.jonathas.petclinic.ui.ui.main.domain

import com.jonathas.httpservice.model.PetModel
import com.jonathas.httpservice.restRepository.PetClinicRepository
import com.jonathas.petclinic.models.PetItemModel

class FetchPetListUseCase(
    private val dataSource: PetClinicRepository
) {

    operator fun invoke() =
        dataSource.fetchPetsData()

    fun mapData(data: List<PetModel>) = data.map { model ->
        PetItemModel(
            model.imageUrl,
            model.contentUrl,
            model.title
        )
    }

}