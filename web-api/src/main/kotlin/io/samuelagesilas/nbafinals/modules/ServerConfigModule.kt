package io.samuelagesilas.nbafinals.modules

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import java.io.FileInputStream
import java.io.IOException
import java.util.*

class ServerConfigModule constructor(private val serverConfigFileLocation: String) : AbstractModule() {

    @Throws(IOException::class)
    override fun configure() {
        val properties = Properties()
        properties.load(FileInputStream(serverConfigFileLocation))
        Names.bindProperties(binder(), properties)
    }
}