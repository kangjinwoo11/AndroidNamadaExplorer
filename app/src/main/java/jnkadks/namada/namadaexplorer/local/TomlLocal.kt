package jnkadks.namada.namadaexplorer.local

import jnkadks.namada.namadaexplorer.models.Parameters

interface TomlLocal {
    suspend fun readParameters(): Parameters
}