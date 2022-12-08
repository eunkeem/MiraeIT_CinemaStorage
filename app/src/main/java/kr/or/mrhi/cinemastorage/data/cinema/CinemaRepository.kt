package kr.or.mrhi.cinemastorage.data.cinema

import kr.or.mrhi.cinemastorage.network.CinemaAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CinemaRepository {

    private val cinemaApi: CinemaAPI

    /*CinemaAPI인터페이스를 구현한 retrofit 서비스 객체 생성
    .addConverterFactory함수로 데이터파싱설정
     GsonConverterFactory를 이용해 api의 JSON을 인코딩*/
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        cinemaApi = retrofit.create(CinemaAPI::class.java)
    }

    /*해당API의 객체를 얻어 enqueue함수를 호출
    *onSuccess 매개변수를 람다로 정의해두고 onResponse오버라이딩해서 통신이 성공했을경우 invoke함수로 실행*/
    fun getPopularCinema(
        page: Int = 1,
        onSuccess: (cinema: List<Cinema>) -> Unit,
        onError: () -> Unit
    ) {
        cinemaApi.getPopularCinema(page = page).enqueue(object : Callback<GetCinemaResponse> {
            override fun onResponse(
                call: Call<GetCinemaResponse>,
                response: Response<GetCinemaResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) onSuccess.invoke(responseBody.results)
                    else onError.invoke()
                } else onError.invoke()
            }

            override fun onFailure(call: Call<GetCinemaResponse>, t: Throwable) {
                onError.invoke()
            }
        })
    }

    fun getTopRatedCinema(
        page: Int = 1,
        onSuccess: (cinema: List<Cinema>) -> Unit,
        onError: () -> Unit
    ) {
        cinemaApi.getTopRatedCinema(page = page).enqueue(object : Callback<GetCinemaResponse> {
            override fun onResponse(
                call: Call<GetCinemaResponse>,
                response: Response<GetCinemaResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) onSuccess.invoke(responseBody.results)
                    else onError.invoke()
                } else onError.invoke()
            }

            override fun onFailure(call: Call<GetCinemaResponse>, t: Throwable) {
                onError.invoke()
            }
        })
    }

    fun getUpcomingCinema(
        page: Int = 1,
        onSuccess: (cinema: List<Cinema>) -> Unit,
        onError: () -> Unit
    ) {
        cinemaApi.getUpcomingCinema(page = page).enqueue(object : Callback<GetCinemaResponse> {
            override fun onResponse(
                call: Call<GetCinemaResponse>,
                response: Response<GetCinemaResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) onSuccess.invoke(responseBody.results)
                    else onError.invoke()
                } else onError.invoke()
            }

            override fun onFailure(call: Call<GetCinemaResponse>, t: Throwable) {
                onError.invoke()
            }
        })
    }
}