package io.samuelagesilas.nbafinals.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import io.samuelagesilas.nbafinals.core.ServerConfigPropertyKeys.REDIS_HOST
import io.samuelagesilas.nbafinals.core.ServerConfigPropertyKeys.REDIS_PORT
import redis.clients.jedis.Jedis
import javax.inject.Named

class RedisModule: AbstractModule() {

    @Provides
    @Singleton
    fun providesJedis(@Named(REDIS_HOST) redisHost : String,
                      @Named(REDIS_PORT) redisPort: Int) = Jedis(redisHost, redisPort)
}