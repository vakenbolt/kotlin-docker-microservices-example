package io.samuelagesilas.nbafinals

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.restassured.RestAssured.given
import io.restassured.http.Header
import io.samuelagesilas.nbafinals.core.Keys
import io.samuelagesilas.nbafinals.core.LocalizedErrorResponse
import io.samuelagesilas.nbafinals.core.PathParameters
import io.samuelagesilas.nbafinals.core.Paths
import io.samuelagesilas.nbafinals.endpoints.*
import io.samuelagesilas.nbafinals.models.ChampionsModel
import io.samuelagesilas.nbafinals.modules.LocalizationManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import java.util.*

@TestInstance(Lifecycle.PER_CLASS)
class ApiIntegrationTests {

    companion object {
        private val jackson = jacksonObjectMapper()

        private fun assertErrorMessage(resultStr: String) {
            val t: TypeReference<LocalizedErrorResponse> = object : TypeReference<LocalizedErrorResponse>() {}
            val resultJson: LocalizedErrorResponse = jackson.readValue<LocalizedErrorResponse>(resultStr, t)
            val errMsg = LocalizationManager().getBundle(Locale.ENGLISH)!!.getString(Keys.NO_RECORDS_FOUND.name)
            assertEquals(errMsg, resultJson.errorMessage)
        }
    }

    private lateinit var authenticationToken: String;
    private lateinit var authorizationHeader: Header;

    @BeforeAll
    fun getAuthenticationToken() {
        val req = AuthenticationRequest("Chicago", "Bulls")
        val resultStr = given().header("Accept-Language", "en")
            .body(jackson.writeValueAsString(req))
            .post(Paths.authenticate)
            .then()
            .statusCode(201)
            .extract()
            .body()
            .asString()
        val t: TypeReference<AuthenticationResponse> = object : TypeReference<AuthenticationResponse>() {}
        val authenticationResponse = jackson.readValue<AuthenticationResponse>(resultStr, t)
        this.authenticationToken = authenticationResponse.bearer
        this.authorizationHeader = Header("Authorization", "Bearer $authenticationToken")

    }

    @Test
    fun `test games-year`() {
        val resultStr = given().header(authorizationHeader)
            .header("Accept-Language", "en")
            .get(Paths.Games.GAMES.replace(":year", "1980"))
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val t: TypeReference<List<ChampionsModel>> = object : TypeReference<List<ChampionsModel>>() {}
        val resultJson: List<ChampionsModel> = jackson.readValue<List<ChampionsModel>>(resultStr, t)
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
        given().header(authorizationHeader)
            .get(Paths.Games.WINS.replace(":year", "1980"))
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
    }

    @Test
    fun `test games-year-wins if year is not in the acceptable range`() {
        val resultStr = given().header(authorizationHeader)
            .get(Paths.Games.WINS.replace(":year", "1979"))
            .then()
            .statusCode(404)
            .extract()
            .body()
            .asString()
        assertErrorMessage(resultStr)
    }

    @Test
    fun `test games-year-losses`() {
        given().header(authorizationHeader)
            .get(Paths.Games.LOSSES.replace(":year", "1980"))
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
    }

    @Test
    fun `test games-year-losses if year is not in the acceptable range`() {
        val resultStr = given().header(authorizationHeader)
            .get(Paths.Games.WINS.replace(":year", "2364"))
            .then()
            .statusCode(404)
            .extract()
            .body()
            .asString()
        assertErrorMessage(resultStr)
    }

    @Test
    fun `test games-year-home`() {
        given().header(authorizationHeader)
            .get(Paths.Games.HOME_GAMES.replace(":year", "1980"))
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
    }

    @Test
    fun `test games-year-home if year is not in the acceptable range`() {
        println(this.authenticationToken)
        val resultStr = given().header(authorizationHeader)
            .get(Paths.Games.WINS.replace(":year", "1901"))
            .then()
            .statusCode(404)
            .extract()
            .body()
            .asString()
        assertErrorMessage(resultStr)
    }

    @Test
    fun `test games-year-away`() {
        given().header(authorizationHeader)
            .get(Paths.Games.AWAY_GAMES.replace(":year", "1980"))
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
    }

    @Test
    fun `test games-year-away if year is not in the acceptable range`() {
        val resultStr = given().header(authorizationHeader)
            .get(Paths.Games.WINS.replace(":year", "1931"))
            .then()
            .statusCode(404)
            .extract()
            .body()
            .asString()
        assertErrorMessage(resultStr)
    }

    @Test
    fun `test healthcheck`() {
        given().get(Paths.healthCheck)
            .then()
            .statusCode(200)
    }

    @Test
    fun `test getYears`() {
        val resultStr = given().header(authorizationHeader)
            .get(Paths.getYears)
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val t: TypeReference<YearsResponse> = object : TypeReference<YearsResponse>() {}
        val resultJson: YearsResponse = jackson.readValue<YearsResponse>(resultStr, t)
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
        val resultStr = given().header(authorizationHeader)
            .get(Paths.getTeams)
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val t: TypeReference<TeamsResponse> = object : TypeReference<TeamsResponse>() {}
        val resultJson: TeamsResponse = jackson.readValue<TeamsResponse>(resultStr, t)
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
    fun `test games by id`() {
        val resultStr = given().header(authorizationHeader)
            .get(Paths.Games.GAME.replace(":${PathParameters.GAME_ID}", "1"))
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val t: TypeReference<ChampionsModel> = object : TypeReference<ChampionsModel>() {}
        val resultJson: ChampionsModel = jackson.readValue<ChampionsModel>(resultStr, t)
        with(resultJson) {
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
    }

    @Test
    fun `test games by id if bad request`() {
        given().header(authorizationHeader)
            .get(Paths.Games.GAME.replace(":${PathParameters.GAME_ID}", "bad"))
            .then()
            .statusCode(400)
    }


    @Test
    fun `test getGamesByTeam with missing payload`() {
        given().header(authorizationHeader)
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
        val resultStr = given().header(authorizationHeader)
                .body(TeamRequest("Cavaliers"))
                .get(Paths.getGamesByTeam)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString()
        val t: TypeReference<List<ChampionsModel>> = object : TypeReference<List<ChampionsModel>>() {}
        val resultJson: List<ChampionsModel> = jackson.readValue<List<ChampionsModel>>(resultStr, t)
        assertEquals(7, resultJson.size)
        resultJson.forEachIndexed { index, championsModel ->
            assertEquals("Cavaliers", championsModel.team)
            assertEquals(index + 1, championsModel.game)
            assertEquals(240, championsModel.mp)
        }
    }
}