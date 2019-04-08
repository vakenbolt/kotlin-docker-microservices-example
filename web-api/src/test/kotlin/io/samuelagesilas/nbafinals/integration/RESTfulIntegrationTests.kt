package io.samuelagesilas.nbafinals.integration

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import io.restassured.RestAssured.given
import io.samuelagesilas.nbafinals.dao.ChampionsModel

class RESTfulIntegrationTests {


    @Test
    fun `test games-year`() {
        val resultStr = given()
            .get("http://localhost:8080/games/1980")
            .then()
            .extract()
            .body()
            .asString()
        val a: TypeReference<List<ChampionsModel>> = object : TypeReference<List<ChampionsModel>>() {}
        val resultJson: List<ChampionsModel> = jacksonObjectMapper().readValue<List<ChampionsModel>>(resultStr, a)
        with(resultJson[0]) {
            assertEquals(1980, this.year)
            assertEquals("Lakers", this.team)
            assertEquals(1, this.game)
            assertEquals(true, this.win)
            assertEquals(true, this.home)
            assertEquals(240, this.mp)
            assertEquals(48, this.fg)
            assertEquals(89, this.fga)
            assertEquals(0.539, this.fgp)
            assertEquals(0, this.tp)
            assertEquals(0, this.tpa)
            assertEquals(0.0, this.tpp)
            assertEquals(13, this.ft)
            assertEquals(15, this.fta)
            assertEquals(0.867, this.ftp)
            assertEquals(12, this.orb)
            assertEquals(31, this.drb)
            assertEquals(43, this.trb)
            assertEquals(30, this.ast)
            assertEquals(5, this.stl)
            assertEquals(9, this.blk)
            assertEquals(17, this.tov)
            assertEquals(24, this.pf)
            assertEquals(109, this.pts)
        }
        assertEquals(6, resultJson.size)
        resultJson.forEach { c: ChampionsModel ->
            assertEquals(1980, c.year)
            assertEquals("Lakers", c.team)
        }
        assertEquals(1, resultJson[0].game)
        assertEquals(2, resultJson[1].game)
        assertEquals(3, resultJson[2].game)
        assertEquals(4, resultJson[3].game)
        assertEquals(5, resultJson[4].game)
        assertEquals(6, resultJson[5].game)

        assertEquals(true, resultJson[0].win)
        assertEquals(false, resultJson[1].win)
        assertEquals(true, resultJson[2].win)
        assertEquals(false, resultJson[3].win)
        assertEquals(true, resultJson[4].win)
        assertEquals(true, resultJson[5].win)

        assertEquals(true, resultJson[0].home)
        assertEquals(true, resultJson[1].home)
        assertEquals(false, resultJson[2].home)
        assertEquals(false, resultJson[3].home)
        assertEquals(true, resultJson[4].home)
        assertEquals(false, resultJson[5].home)
    }
}