# GithubExplorer

An android app, where the user can search for repositories on Github by keywords. To use the app, replace the bearer token used in `GraphQLRepository.kt` with a valid Github access token.

Implementation: <br>
The app implements the MVP architecture and uses the Apollo Android Client (https://github.com/apollographql/apollo-android) 
to make queries to the Github GraphQL API v4 (https://developer.github.com/v4/).
