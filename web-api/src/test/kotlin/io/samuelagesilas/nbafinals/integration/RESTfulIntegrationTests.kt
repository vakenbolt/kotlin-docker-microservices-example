package io.samuelagesilas.nbafinals.integration

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import io.restassured.RestAssured.given
import io.samuelagesilas.nbafinals.core.Paths
import io.samuelagesilas.nbafinals.dao.ChampionsModel
import io.samuelagesilas.nbafinals.endpoints.TeamRequest
import io.samuelagesilas.nbafinals.endpoints.TeamsResponse
import io.samuelagesilas.nbafinals.endpoints.YearsResponse

class RESTfulIntegrationTests {

    @Test
    fun `test games-year`() {
        val resultStr = given()
            .get(Paths.Games.GAMES.replace(":year", "1980"))
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val t: TypeReference<List<ChampionsModel>> = object : TypeReference<List<ChampionsModel>>() {}
        val resultJson: List<ChampionsModel> = jacksonObjectMapper().readValue<List<ChampionsModel>>(resultStr, t)
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

    @Test
    fun `test games-year-wins`() {
        val resultStr = given()
            .get(Paths.Games.WINS.replace(":year", "1980"))
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        println(resultStr)
    }

    @Test
    fun `test games-year-losses`() {
        val resultStr = given()
            .get(Paths.Games.LOSSES.replace(":year", "1980"))
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        println(resultStr)
    }

    @Test
    fun `test games-year-home`() {
        val resultStr = given()
            .get(Paths.Games.HOME_GAMES.replace(":year", "1980"))
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        println(resultStr)
    }

    @Test
    fun `test games-year-away`() {
        val resultStr = given()
            .get(Paths.Games.AWAY_GAMES.replace(":year", "1980"))
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        println(resultStr)
    }

    @Test
    fun `test healthcheck`() {
        given()
            .get(Paths.healthCheck)
            .then()
            .statusCode(200)
    }

    @Test
    fun `test getYears`() {
        val resultStr = given()
            .get(Paths.getYears)
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val t: TypeReference<YearsResponse> = object : TypeReference<YearsResponse>() {}
        val resultJson: YearsResponse = jacksonObjectMapper().readValue<YearsResponse>(resultStr, t)
        with(resultJson.years) {
            assertEquals(1980, this[0])
            assertEquals(1985, this[5])
            assertEquals(1990, this[10])
            assertEquals(1995, this[15])
            assertEquals(2000, this[20])
            assertEquals(2005, this[25])
            assertEquals(2010, this[30])
            assertEquals(2015, this[35])
            assertEquals(2018, this[38])
        }
    }

    @Test
    fun `test getTeams`() {
        val resultStr = given()
            .get(Paths.getTeams)
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val t: TypeReference<TeamsResponse> = object : TypeReference<TeamsResponse>() {}
        val resultJson: TeamsResponse = jacksonObjectMapper().readValue<TeamsResponse>(resultStr, t)
        with(resultJson.teams) {
            assertEquals("Bulls", this[0])
            assertEquals("Cavaliers", this[1])
            assertEquals("Celtics", this[2])
            assertEquals("Heat", this[3])
            assertEquals("Lakers", this[4])
            assertEquals("Mavericks", this[5])
            assertEquals("Pistons", this[6])
            assertEquals("Rockets", this[7])
            assertEquals("Sixers", this[8])
            assertEquals("Spurs", this[9])
            assertEquals("Warriors", this[10])
        }
    }


    @Test
    fun `test getGamesByTeam with missing payload`() {
        val resultStr = given()
            .get(Paths.getGamesByTeam)
            .then()
            .statusCode(500)
    }

//    @Test
//    fun `test getGamesByTeam with team not in champions table`() {
//        val resultStr = given()
//            .body(TeamRequest("Space Jams"))
//            .get(Paths.getGamesByTeam)
//            .then()
//            .statusCode(500)
//    }

    @Test
    fun `test getGamesByTeam`() {
        val resultStr = given()
            .body(TeamRequest("Cavaliers"))
            .get(Paths.getGamesByTeam)
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val t: TypeReference<List<ChampionsModel>> = object : TypeReference<List<ChampionsModel>>() {}
        val resultJson: List<ChampionsModel> = jacksonObjectMapper().readValue<List<ChampionsModel>>(resultStr, t)
        assertEquals(7, resultJson.size)
        resultJson.forEachIndexed { index, championsModel ->
            assertEquals("Cavaliers", championsModel.team)
            assertEquals(index + 1, championsModel.game)
            assertEquals(240, championsModel.mp)
        }
    }
}