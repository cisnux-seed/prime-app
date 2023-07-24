package dev.cisnux.core.dummy

import arrow.core.Either
import dev.cisnux.core.data.sources.remotes.responses.GenreResponse
import dev.cisnux.core.data.sources.remotes.responses.KeywordResponseItem
import dev.cisnux.core.data.sources.remotes.responses.MovieDetailResponse
import dev.cisnux.core.data.sources.remotes.responses.MovieResponseItem

const val dummyNotFoundMovieJson = """
{
    "success": false,
    "status_code": 34,
    "status_message": "The resource you requested could not be found."
}
"""

const val dummyMoviesJson = """
{
    "page": 1,
    "results": [
        {
            "adult": false,
            "backdrop_path": "/qWQSnedj0LCUjWNp9fLcMtfgadp.jpg",
            "genre_ids": [
                28,
                12,
                878
            ],
            "id": 667538,
            "original_language": "en",
            "original_title": "Transformers: Rise of the Beasts",
            "overview": "When a new threat capable of destroying the entire planet emerges, Optimus Prime and the Autobots must team up with a powerful faction known as the Maximals. With the fate of humanity hanging in the balance, humans Noah and Elena will do whatever it takes to help the Transformers as they engage in the ultimate battle to save Earth.",
            "popularity": 7985.481,
            "poster_path": "/gPbM0MK8CP8A174rmUwGsADNYKD.jpg",
            "release_date": "2023-06-06",
            "title": "Transformers: Rise of the Beasts",
            "video": false,
            "vote_average": 7.331,
            "vote_count": 1479
        }
    ],
    "total_pages": 1,
    "total_results": 1
}
"""

const val dummyEmptyMoviesJson = """
    {
    "dates": {
        "maximum": "2023-07-26",
        "minimum": "2023-06-08"
    },
    "page": 1,
    "results": []
}
"""

const val dummyMovieDetailJson = """
    {
    "adult": false,
    "backdrop_path": "/qWQSnedj0LCUjWNp9fLcMtfgadp.jpg",
    "belongs_to_collection": {
        "id": 939352,
        "name": "Transformers: Rise of the Beasts Collection",
        "poster_path": "/6sAdtwp5LV0jlNVhefTMEsjP7py.jpg",
        "backdrop_path": null
    },
    "budget": 195000000,
    "genres": [
        {
            "id": 28,
            "name": "Action"
        },
        {
            "id": 12,
            "name": "Adventure"
        },
        {
            "id": 878,
            "name": "Science Fiction"
        }
    ],
    "homepage": "https://www.transformersmovie.com",
    "id": 667538,
    "imdb_id": "tt5090568",
    "original_language": "en",
    "original_title": "Transformers: Rise of the Beasts",
    "overview": "When a new threat capable of destroying the entire planet emerges, Optimus Prime and the Autobots must team up with a powerful faction known as the Maximals. With the fate of humanity hanging in the balance, humans Noah and Elena will do whatever it takes to help the Transformers as they engage in the ultimate battle to save Earth.",
    "popularity": 7985.481,
    "poster_path": "/gPbM0MK8CP8A174rmUwGsADNYKD.jpg",
    "production_companies": [
        {
            "id": 82819,
            "logo_path": "/gXfFl9pRPaoaq14jybEn1pHeldr.png",
            "name": "Skydance",
            "origin_country": "US"
        },
        {
            "id": 4,
            "logo_path": "/gz66EfNoYPqHTYI4q9UEN4CbHRc.png",
            "name": "Paramount",
            "origin_country": "US"
        },
        {
            "id": 435,
            "logo_path": "/AjzK0s2w1GtLfR4hqCjVSYi0Sr8.png",
            "name": "di Bonaventura Pictures",
            "origin_country": "US"
        },
        {
            "id": 6734,
            "logo_path": null,
            "name": "Bay Films",
            "origin_country": "US"
        },
        {
            "id": 114732,
            "logo_path": "/tNCbisMxO5mX2X2bOQxHHQZVYnT.png",
            "name": "New Republic Pictures",
            "origin_country": "US"
        },
        {
            "id": 38831,
            "logo_path": null,
            "name": "Tom DeSanto/Don Murphy Production",
            "origin_country": ""
        },
        {
            "id": 2598,
            "logo_path": "/i42C5gRq7XqlG4S9vkchuJZfrBn.png",
            "name": "Hasbro",
            "origin_country": "US"
        }
    ],
    "production_countries": [
        {
            "iso_3166_1": "US",
            "name": "United States of America"
        }
    ],
    "release_date": "2023-06-06",
    "revenue": 420965000,
    "runtime": 127,
    "spoken_languages": [
        {
            "english_name": "Quechua",
            "iso_639_1": "qu",
            "name": ""
        },
        {
            "english_name": "Spanish",
            "iso_639_1": "es",
            "name": "Español"
        },
        {
            "english_name": "English",
            "iso_639_1": "en",
            "name": "English"
        }
    ],
    "status": "Released",
    "tagline": "Unite or fall.",
    "title": "Transformers: Rise of the Beasts",
    "video": false,
    "vote_average": 7.331,
    "vote_count": 1497
}
"""

val dummyMovieResponses = Either.Right(
    listOf(
        MovieResponseItem(
            id = 667538,
            title = "Transformers: Rise of the Beasts",
            posterPath = "/gPbM0MK8CP8A174rmUwGsADNYKD.jpg",
            overview = "When a new threat capable of destroying the entire planet emerges, Optimus Prime and the Autobots must team up with a powerful faction known as the Maximals. With the fate of humanity hanging in the balance, humans Noah and Elena will do whatever it takes to help the Transformers as they engage in the ultimate battle to save Earth.",
        )
    )
)
val dummyMovieDetailResponse = Either.Right(
    MovieDetailResponse(
        id = 667538,
        title = "Transformers: Rise of the Beasts",
        voteAverage = 7.331f,
        overview = "When a new threat capable of destroying the entire planet emerges, Optimus Prime and the Autobots must team up with a powerful faction known as the Maximals. With the fate of humanity hanging in the balance, humans Noah and Elena will do whatever it takes to help the Transformers as they engage in the ultimate battle to save Earth.",
        posterPath = "/gPbM0MK8CP8A174rmUwGsADNYKD.jpg",
        genres = listOf(
            GenreResponse(
                id = 28,
                name = "Action",
            ),
            GenreResponse(
                id = 12,
                name = "Adventure",
            ),
            GenreResponse(
                id = 878,
                name = "Science Fiction"
            )
        ),
        runtime = 127
    )
)

const val jsonServerErrorString = """
{
    "success": false,
    "status_code": 37,
    "status_message": "Internal Server Error"
}
"""

const val jsonKeywordsString = """
{
    "page": 1,
    "results": [
        {
            "id": 193415,
            "name": "testify"
        },
        {
            "id": 235996,
            "name": "standardized tests"
        }
    ],
    "total_pages": 3,
    "total_results": 2
}
"""
const val jsonEmptyKeywordsString = """
{
    "page": 1,
    "results": [],
    "total_pages": 3,
    "total_results": 2
}
"""

val fakeKeywordResponses = Either.Right(
    listOf(
        KeywordResponseItem(
            id = 193415,
            name = "testify"
        ),
        KeywordResponseItem(
            id = 235996,
            name = "standardized tests"
        )
    )
)