package app.com.githubexplorer.main

import app.com.githubexplorer.RepositoryQuery
import app.com.githubexplorer.network.GraphQLManager
import com.apollographql.apollo.ApolloCall
import com.nhaarman.mockito_kotlin.mock
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
            on { getRepositoriesCall("test") }.then { mockApolloCall }
        }

        val testInteractor = MainInteractor(mockGraphQLManager)
        testInteractor.getRepositories("test")
                .test()
                .assertComplete()
    }

}


