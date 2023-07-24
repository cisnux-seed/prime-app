package dev.cisnux.prime.dummy

import androidx.paging.LoadState
import androidx.paging.LoadStates
import dev.cisnux.core.domain.models.Movie
import dev.cisnux.core.domain.models.MovieDetail
import dev.cisnux.core.utils.ORIGINAL_IMAGE_URL
import dev.cisnux.core.utils.Failure
import dev.cisnux.core.utils.duration

val dummySuccessLoadState = LoadStates(
    refresh = LoadState.NotLoading(endOfPaginationReached = true),
    append = LoadState.NotLoading(endOfPaginationReached = true),
    prepend = LoadState.NotLoading(endOfPaginationReached = true)
)

val dummyMovies = List(20) {
    Movie(
        id = it,
        title = "Transformers: Rise of the Beasts",
        overview = "When a new threat capable of destroying the entire planet emerges, Optimus Prime and the Autobots must team up with a powerful faction known as the Maximals. With the fate of humanity hanging in the balance, humans Noah and Elena will do whatever it takes to help the Transformers as they engage in the ultimate battle to save Earth.",
        poster = "$ORIGINAL_IMAGE_URL/gPbM0MK8CP8A174rmUwGsADNYKD.jpg",
    )
}

val dummyMovieDetail = MovieDetail(
    id = 2,
    title = "Transformers: Rise of the Beasts",
    voteAverage = 7.331f,
    overview = "When a new threat capable of destroying the entire planet emerges, Optimus Prime and the Autobots must team up with a powerful faction known as the Maximals. With the fate of humanity hanging in the balance, humans Noah and Elena will do whatever it takes to help the Transformers as they engage in the ultimate battle to save Earth.",
    poster = "$ORIGINAL_IMAGE_URL/gPbM0MK8CP8A174rmUwGsADNYKD.jpg",
    genres = "Action, Adventure, Science Fiction",
    duration = 127.duration(),
    isFavorite = false
)

val dummyLoadingLoadState = LoadStates(
    refresh = LoadState.Loading,
    append = LoadState.Loading,
    prepend = LoadState.Loading
)
val dummyConnectionFailure = Failure.ConnectionFailure("no internet connection")
val dummyConnectionFailureLoadState = LoadStates(
    refresh = LoadState.Error(dummyConnectionFailure),
    append = LoadState.Error(dummyConnectionFailure),
    prepend = LoadState.Error(dummyConnectionFailure)
)

val dummyKeywords = List(10) {
    "$it dummy"
}