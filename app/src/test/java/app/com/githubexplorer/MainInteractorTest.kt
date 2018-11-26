package app.com.githubexplorer

import app.com.githubexplorer.data.GraphQLManager
import app.com.githubexplorer.main.MainInteractor
import com.apollographql.apollo.ApolloCall
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.assertEquals
import org.junit.Test




/**
 * Created by panhvu on 25.11.18.
 */

class MainInteractorTest {

    @Test
    fun testGetRepositories() {
        // given response from GraphQLManager
        // given certain keyword
        // check if interactor.getRepositories(keyword) returns a consumable Observable<Response>

        val mockApolloCall = mock<ApolloCall<RepositoryQuery.Data>>()
        val mockGraphQLManager = mock<GraphQLManager> {
            on { getRepositoriesCall(any()) }.then { mockApolloCall }
        }

        val testInteractor = MainInteractor(mockGraphQLManager)
        assertEquals(mockApolloCall, testInteractor.getRepositories("test"))
    }

}


