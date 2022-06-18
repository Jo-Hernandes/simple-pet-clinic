package com.jonathas.httpservice

import com.jonathas.httpservice.restRepository.PetClinicRepository
import com.jonathas.httpservice.restRepository.client.PetClinicClient

class ServiceModule {

    companion object {
        fun getRestServiceRepository() : PetClinicRepository = PetClinicClient().buildClient()
    }

}