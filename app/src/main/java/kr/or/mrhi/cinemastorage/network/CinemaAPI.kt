package kr.or.mrhi.cinemastorage.network

import kr.or.mrhi.cinemastorage.BuildConfig
import kr.or.mrhi.cinemastorage.data.cinema.GetCinemaResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CinemaAPI {

    /*local properties에 저장해둔 API_KEY를 build.gradle에서
    * BuilConfig에 저장해두고 정해둔 각각의 이름string으로 불러서사용
    * .gitignore에 properties를 넣어 github에 공유되지 않도록함
    * Get어노테이션을 선언해두어 이 API 호출할때 이용하도록함 */
    @GET("movie/popular")
    fun getPopularCinema(
        @Query("api_key") apiKey: String = BuildConfig.POPULAR_KEY,
        @Query("page") page: Int,
    ): Call<GetCinemaResponse>

    @GET("movie/top_rated")
    fun getTopRatedCinema(
        @Query("api_key") apiKey: String = BuildConfig.TOP_RATE_KEY,
        @Query("page") page: Int,
    ): Call<GetCinemaResponse>

    @GET("movie/upcoming")
    fun getUpcomingCinema(
        @Query("api_key") apiKey: String = BuildConfig.UPCOMING_KEY,
        @Query("page") page: Int,
    ): Call<GetCinemaResponse>
}