package io.samuelagesilas.nbafinals.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import redis.clients.jedis.Jedis

class RedisModule: AbstractModule() {

    @Provides
    @Singleton
    fun providesJedis(serverConfig: ServerConfig) = Jedis(serverConfig.redisHost, serverConfig.redisPort)
}