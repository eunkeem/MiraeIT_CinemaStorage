package kr.or.mrhi.cinemastorage.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.cinemastorage.dao.ReviewDAO
import kr.or.mrhi.cinemastorage.dao.UserDAO
import kr.or.mrhi.cinemastorage.data.Review
import kr.or.mrhi.cinemastorage.data.User
import kr.or.mrhi.cinemastorage.databinding.ActivityWriteBinding
import kr.or.mrhi.cinemastorage.util.SharedPreferences
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity.Companion.MOVIE_BACKDROP
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity.Companion.MOVIE_POSTER
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity.Companion.MOVIE_RATING
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity.Companion.MOVIE_RELEASE_DATE
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity.Companion.MOVIE_TITLE
import java.text.SimpleDateFormat
import java.util.*

class WriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWriteBinding

    private lateinit var posterPath: String

    private lateinit var backdropPath: String

    private var globalUser: User? = null

    private var loginUser: User? = null

    private var isUser = false

    private var reviewer: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            setCinemaReview()
            setCinemaInfo()
            setUserNickname()
        }
    }

    /*firebase realtimeDB에서 sharedpreference에 저장된 키값과 같은 키의 유저 데이터를 받음*/
    private fun setUserNickname() {
        val userDAO = UserDAO()
        userDAO.databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (children in snapshot.children) {
                    globalUser = children.getValue(User::class.java)!!
                    if (globalUser!!.key == SharedPreferences.getToken(applicationContext)) {
                        loginUser =
                            User(globalUser?.key!!, globalUser?.nickname, globalUser?.password)
                        isUser = true
                    }
                }
                if (isUser) reviewer = loginUser?.nickname
            }

            override fun onCancelled(error: DatabaseError) {
                setToast(error.message)
            }
        })
    }

    /*glide를 이용하여 리뷰입력창에 영화의 정보들을 셋팅*/
    private fun setCinemaInfo() {
        val extras = intent.extras
        extras?.getString(MOVIE_BACKDROP)?.let { backdrop ->
            Glide.with(this).load("https://image.tmdb.org/t/p/w1280$backdrop")
                .transform(CenterCrop()).into(binding.ivBackdrop)
            backdropPath = "https://image.tmdb.org/t/p/w1280$backdrop"
        }
        extras?.getString(MOVIE_POSTER)?.let { poster ->
            Glide.with(this).load("https://image.tmdb.org/t/p/w342$poster").transform(CenterCrop())
                .into(binding.ivPoster)
            posterPath = "https://image.tmdb.org/t/p/w342$poster"
        }
        binding.tvCinemaTitle.text = extras?.getString(MOVIE_TITLE, "")
        binding.tvReleaseDate.text = extras?.getString(MOVIE_RELEASE_DATE, "")
        binding.ratingBarCinema.rating = extras?.getFloat(MOVIE_RATING, 0f)?.div(2)!!
    }

    /*사용자가 입력한 리뷰를 firebase realtimeDB에 저장
    * 유저의 키값을 별도로 저장해두어 유저별로 검색하기 쉽도록 함*/
    private fun setCinemaReview() {
        binding.btnWrite.setOnClickListener {
            val key = SharedPreferences.getToken(applicationContext)
            val title = binding.etTitle.text
            val date = currentDate()
            val comment = binding.etComment.text
            val rating = binding.ratingBar.rating

            val reviewDAO = ReviewDAO()
            val review = Review(
                key,
                reviewer,
                title.toString(),
                date,
                comment.toString(),
                rating.toString(),
                posterPath,
                backdropPath
            )
            if (title.isBlank() || comment.isBlank()) {
                setToast("Please enter your title, image and comment at all")
                return@setOnClickListener
            } else {
                reviewDAO.databaseReference?.child(date)?.setValue(review)?.apply {
                    addOnSuccessListener { setToast("Success to insert data") }
                    addOnFailureListener { setToast("Failed to insert data") }
                }
            }
        }
    }

    private fun currentDate(): String {
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return simpleDateFormat.format(calendar.time)
    }

    private fun setToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}