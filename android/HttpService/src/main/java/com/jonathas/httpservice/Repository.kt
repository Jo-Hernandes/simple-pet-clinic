package com.jonathas.httpservice

interface Repository {

    fun fetchConfig() : Unit

    fun fetchPetsData() : Unit

}