# GithubExplorer

An android app, where the user can search for repositories on Github by name. To access the Github API, make sure the bearer token provided in AppModule for the OkHttpClient is a valid Github access token.

Implementation: <br>
The app implements the MVVM architecture and uses the Apollo Android Client (https://github.com/apollographql/apollo-android) 
to make queries to the Github GraphQL API v4 (https://developer.github.com/v4/).
