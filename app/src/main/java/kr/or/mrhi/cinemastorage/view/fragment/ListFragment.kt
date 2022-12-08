package kr.or.mrhi.cinemastorage.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.or.mrhi.cinemastorage.data.cinema.Cinema
import kr.or.mrhi.cinemastorage.data.cinema.CinemaRepository
import kr.or.mrhi.cinemastorage.databinding.FragmentListBinding
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity.Companion.MOVIE_BACKDROP
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity.Companion.MOVIE_OVERVIEW
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity.Companion.MOVIE_POSTER
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity.Companion.MOVIE_RATING
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity.Companion.MOVIE_RELEASE_DATE
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity.Companion.MOVIE_TITLE
import kr.or.mrhi.cinemastorage.view.adapter.ListAdapter

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding

    private val cinemaList = listOf<Cinema>()

    /*어댑터에서 람다식으로 표현해두고 itemview클릭하면 invoke함수로 해당 함수를 호출*/
    private val popularAdapter = ListAdapter(cinemaList) { cinema -> showCinemaDetail(cinema) }

    private val topRatedAdapter = ListAdapter(cinemaList) { cinema -> showCinemaDetail(cinema) }

    private val upcomingAdapter = ListAdapter(cinemaList) { cinema -> showCinemaDetail(cinema) }

    private var position = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)

        binding.apply {
            getPopularCinema()
            getTopRatedCinema()
            getUpcomingCinema()
            setRecyclerView(recyclerViewPopular, popularAdapter)
            setRecyclerView(recyclerViewTopRated, topRatedAdapter)
            setRecyclerView(recyclerViewUpcoming, upcomingAdapter)
            setVideoView()
        }
        return binding.root
    }

    /*인텐트로 영화 정보를 putExtra*/
    private fun showCinemaDetail(cinema: Cinema) {
        val intent = Intent(requireContext(), ListDetailActivity::class.java)
        intent.putExtra(MOVIE_BACKDROP, cinema.backdrop)
        intent.putExtra(MOVIE_POSTER, cinema.poster)
        intent.putExtra(MOVIE_TITLE, cinema.title)
        intent.putExtra(MOVIE_RELEASE_DATE, cinema.release)
        intent.putExtra(MOVIE_RATING, cinema.rating)
        intent.putExtra(MOVIE_OVERVIEW, cinema.overview)
        startActivity(intent)
    }

    /*api 로 정보를 성공적으로 불러왔을 경우 어댑터에 데이터리스트를 업데이트 하도록함*/
    private fun getPopularCinema() {
        CinemaRepository.getPopularCinema(onSuccess = ::onPopularCinemaFetched, onError = ::onError)
    }

    private fun onPopularCinemaFetched(cinema: List<Cinema>) {
        popularAdapter.updateCinema(cinema)
        Log.d("ListFragment", "Cinema : $cinema")
    }

    private fun getTopRatedCinema() {
        CinemaRepository.getTopRatedCinema(
            onSuccess = ::onTopRatedCinemaFetched, onError = ::onError
        )
    }

    private fun onTopRatedCinemaFetched(cinema: List<Cinema>) {
        topRatedAdapter.updateCinema(cinema)
        Log.d("ListFragment", "Cinema : $cinema")
    }

    private fun getUpcomingCinema() {
        CinemaRepository.getUpcomingCinema(
            onSuccess = ::onUpcomingCinemaFetched, onError = ::onError
        )
    }

    private fun onUpcomingCinemaFetched(cinema: List<Cinema>) {
        upcomingAdapter.updateCinema(cinema)
        Log.d("ListFragment", "Cinema : $cinema")
    }

    private fun onError() {
        Toast.makeText(
            requireContext(), "Please check your internet connection", Toast.LENGTH_SHORT
        ).show()
    }

    /*videoView자리에 재생하려는 비디오의 URI로 샛팅. */
    private fun setVideoView() {
        binding.videoView.apply {
            setVideoURI(Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))
            requestFocus() /*파일 재생 준비*/
            setOnPreparedListener { start() } /*재생가능한 상태로 로딩되면 시작*/
            setOnCompletionListener { start() } /*미디어 재생 완료되었을때 시작*/
        }
    }

    /*리사이클러뷰를 horizontal로 셋팅*/
    private fun setRecyclerView(recyclerView: RecyclerView, listAdapter: ListAdapter) {
        recyclerView.apply {
            adapter = listAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    /*액티비티 생명주기별로 비디오 재생상태를 설정*/
    override fun onResume() {
        super.onResume()
        binding.videoView.apply {
            if (!isPlaying) {
                seekTo(position)
                start()
            }
        }
    }

    override fun onPause() {
        binding.videoView.apply {
            position = currentPosition
            pause()
        }
        super.onPause()
    }

    override fun onStop() {
        binding.videoView.apply { if (isPlaying) stopPlayback() }
        super.onStop()
    }

}