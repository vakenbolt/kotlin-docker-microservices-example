package io.samuelagesilas.nbafinals

import com.nhaarman.mockito_kotlin.*
import io.samuelagesilas.nbafinals.core.ApiException
import io.samuelagesilas.nbafinals.core.ApiExceptionFactory
import io.samuelagesilas.nbafinals.core.ResolverResponse
import io.samuelagesilas.nbafinals.dao.ChampionsDAO
import io.samuelagesilas.nbafinals.endpoints.NBAChampionsResolver
import io.samuelagesilas.nbafinals.endpoints.TeamsResponse
import io.samuelagesilas.nbafinals.endpoints.YearsResponse
import io.samuelagesilas.nbafinals.models.ChampionsModel
import io.samuelagesilas.nbafinals.modules.LocalizationManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class TestNBAChampionsResolver {
    private val championsModel = ChampionsModel(1,
                                                1980,
                                                "Bulls",
                                                1,
                                                true,
                                                false,
                                                10,
                                                15,
                                                20,
                                                30.5,
                                                35,
                                                40,
                                                40.5,
                                                45,
                                                50,
                                                50.5,
                                                55,
                                                60,
                                                65,
                                                70,
                                                80,
                                                85,
                                                90,
                                                100,
                                                150)
    private val apiExceptionFactory = ApiExceptionFactory(LocalizationManager())
    private val locale = Locale.ENGLISH
    private val championsDAO = mock<ChampionsDAO> {
        on { selectAllGamesByYear(any()) } doReturn listOf(championsModel)
        on { selectGameById(any()) } doReturn championsModel
        on { selectAllGamesWonByYear(any()) } doReturn listOf(championsModel)
        on { selectAllGamesLostByYear(any()) } doReturn listOf(championsModel)
        on { selectAllHomeGamesByYear(any()) } doReturn listOf(championsModel)
        on { selectAllAwayGamesByYear(any()) } doReturn listOf(championsModel)
        on { selectAllGamesByTeamName(any()) } doReturn listOf(championsModel)
        on { selectAllGamesByTeamNameAndYear(any(), any()) } doReturn listOf(championsModel)
    }
    private val errorMessage = "There was no records found for the given request."

    @Test
    fun `test selectAllChampionshipYears`() {
        val resolver = NBAChampionsResolver(championsDAO, apiExceptionFactory)
        val response: ResolverResponse<YearsResponse> = resolver.selectAllChampionshipYears()
        verify(championsDAO, times(1)).selectAllChampionshipYears()
        assertTrue(response.data is YearsResponse)
        assertEquals(200, response.status.code())
    }

    @Test
    fun `test selectChampionshipTeams`() {
        val resolver = NBAChampionsResolver(championsDAO, apiExceptionFactory)
        val response: ResolverResponse<TeamsResponse> = resolver.selectChampionshipTeams()
        verify(championsDAO, times(1)).selectChampionshipTeams()
        assertTrue(response.data is TeamsResponse)
        assertEquals(200, response.status.code())
    }

    @Test
    fun `test selectAllGamesByYear`() {
        val year = 1980
        val resolver = NBAChampionsResolver(championsDAO, apiExceptionFactory)
        val response: ResolverResponse<List<ChampionsModel>> = resolver.selectAllGamesByYear(year, locale)
        verify(championsDAO, times(1)).selectAllGamesByYear(year)
        assertTrue(response.data is List)
        assertEquals(200, response.status.code())
    }

    @Test
    fun `test selectAllGamesByYear is result is empty`() {
        val year = 1980
        val championsDAO = mock<ChampionsDAO> {}
        val resolver = NBAChampionsResolver(championsDAO, apiExceptionFactory)
        val err = assertThrows<ApiException> {
            resolver.selectAllGamesByYear(year, locale)
        }
        verify(championsDAO, times(1)).selectAllGamesByYear(year)
        assertEquals(404, err.statusCode.code())
        assertEquals(errorMessage, err.message)
    }

    @Test
    fun `test selectGameById`() {
        val gameId = 1
        val resolver = NBAChampionsResolver(championsDAO, apiExceptionFactory)
        val response: ResolverResponse<ChampionsModel> = resolver.selectGameById(gameId, locale)
        verify(championsDAO, times(1)).selectGameById(gameId)
        assertTrue(response.data is ChampionsModel)
        assertEquals(200, response.status.code())
    }

    @Test
    fun `test selectGameById if result is empty`() {
        val gameId = 1
        val championsDAO = mock<ChampionsDAO> {}
        val resolver = NBAChampionsResolver(championsDAO, apiExceptionFactory)
        val err = assertThrows<ApiException> {
            resolver.selectGameById(gameId, locale)
        }
        verify(championsDAO, times(1)).selectGameById(gameId)
        assertEquals(404, err.statusCode.code())
        assertEquals(errorMessage, err.message)
    }

    @Test
    fun `test selectAllGamesWonByYear`() {
        val year = 1990
        val resolver = NBAChampionsResolver(championsDAO, apiExceptionFactory)
        val response: ResolverResponse<List<ChampionsModel>> = resolver.selectAllGamesWonByYear(year, locale)
        verify(championsDAO, times(1)).selectAllGamesWonByYear(year)
        assertTrue(response.data is List<ChampionsModel>)
        assertEquals(200, response.status.code())
    }

    @Test
    fun `test selectAllGamesWonByYear if result is empty`() {
        val year = 1991
        val championsDAO = mock<ChampionsDAO> {}
        val resolver = NBAChampionsResolver(championsDAO, apiExceptionFactory)
        val err = assertThrows<ApiException> {
            resolver.selectAllGamesWonByYear(year, locale)
        }
        verify(championsDAO, times(1)).selectAllGamesWonByYear(year)
        assertEquals(404, err.statusCode.code())
        assertEquals(errorMessage, err.message)
    }

    @Test
    fun `test selectAllGamesLostByYear`() {
        val year = 1985
        val resolver = NBAChampionsResolver(championsDAO, apiExceptionFactory)
        val response: ResolverResponse<List<ChampionsModel>> = resolver.selectAllGamesLostByYear(year, locale)
        verify(championsDAO, times(1)).selectAllGamesLostByYear(year)
        assertTrue(response.data is List<ChampionsModel>)
        assertEquals(200, response.status.code())
    }

    @Test
    fun `test selectAllGamesLostByYear if result is empty`() {
        val year = 1992
        val championsDAO = mock<ChampionsDAO> {}
        val resolver = NBAChampionsResolver(championsDAO, apiExceptionFactory)
        val err = assertThrows<ApiException> {
            resolver.selectAllGamesLostByYear(year, locale)
        }
        verify(championsDAO, times(1)).selectAllGamesLostByYear(year)
        assertEquals(404, err.statusCode.code())
        assertEquals(errorMessage, err.message)
    }

    @Test
    fun `test selectAllHomeGamesByYear`() {
        val year = 1983
        val resolver = NBAChampionsResolver(championsDAO, apiExceptionFactory)
        val response: ResolverResponse<List<ChampionsModel>> = resolver.selectAllHomeGamesByYear(year, locale)
        verify(championsDAO, times(1)).selectAllHomeGamesByYear(year)
        assertTrue(response.data is List<ChampionsModel>)
        assertEquals(200, response.status.code())
    }

    @Test
    fun `test selectAllHomeGamesByYear if result is empty`() {
        val year = 1993
        val championsDAO = mock<ChampionsDAO> {}
        val resolver = NBAChampionsResolver(championsDAO, apiExceptionFactory)
        val err = assertThrows<ApiException> {
            resolver.selectAllGamesLostByYear(year, locale)
        }
        verify(championsDAO, times(1)).selectAllGamesLostByYear(year)
        assertEquals(404, err.statusCode.code())
        assertEquals(errorMessage, err.message)
    }

    @Test
    fun `test selectAllAwayGamesByYear`() {
        val year = 1986
        val resolver = NBAChampionsResolver(championsDAO, apiExceptionFactory)
        val response: ResolverResponse<List<ChampionsModel>> = resolver.selectAllAwayGamesByYear(year, locale)
        verify(championsDAO, times(1)).selectAllAwayGamesByYear(year)
        assertTrue(response.data is List<ChampionsModel>)
        assertEquals(200, response.status.code())
    }

    @Test
    fun `test selectAllAwayGamesByYear if result is empty`() {
        val year = 1996
        val championsDAO = mock<ChampionsDAO> {}
        val resolver = NBAChampionsResolver(championsDAO, apiExceptionFactory)
        val err = assertThrows<ApiException> {
            resolver.selectAllGamesLostByYear(year, locale)
        }
        verify(championsDAO, times(1)).selectAllGamesLostByYear(year)
        assertEquals(404, err.statusCode.code())
        assertEquals(errorMessage, err.message)
    }

    @Test
    fun `test selectAllGamesByTeamName`() {
        val team = "Bulls"
        val resolver = NBAChampionsResolver(championsDAO, apiExceptionFactory)
        val response: ResolverResponse<List<ChampionsModel>> = resolver.selectAllGamesByTeamName(team, locale)
        verify(championsDAO, times(1)).selectAllGamesByTeamName(team)
        assertTrue(response.data is List<ChampionsModel>)
        assertEquals(200, response.status.code())
    }

    @Test
    fun `test selectAllGamesByTeamName if result is empty`() {
        val year = 1996
        val championsDAO = mock<ChampionsDAO> {}
        val resolver = NBAChampionsResolver(championsDAO, apiExceptionFactory)
        val err = assertThrows<ApiException> {
            resolver.selectAllGamesLostByYear(year, locale)
        }
        verify(championsDAO, times(1)).selectAllGamesLostByYear(year)
        assertEquals(404, err.statusCode.code())
        assertEquals(errorMessage, err.message)
    }
}