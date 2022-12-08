package kr.or.mrhi.cinemastorage.view.activity.review

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import kr.or.mrhi.cinemastorage.databinding.ActivityPersonalDetailBinding
import kr.or.mrhi.cinemastorage.view.adapter.PersonalAdapter.Companion.PERSONAL_BACKDROP
import kr.or.mrhi.cinemastorage.view.adapter.PersonalAdapter.Companion.PERSONAL_COMMENT
import kr.or.mrhi.cinemastorage.view.adapter.PersonalAdapter.Companion.PERSONAL_DATE
import kr.or.mrhi.cinemastorage.view.adapter.PersonalAdapter.Companion.PERSONAL_POSTER
import kr.or.mrhi.cinemastorage.view.adapter.PersonalAdapter.Companion.PERSONAL_RATING
import kr.or.mrhi.cinemastorage.view.adapter.PersonalAdapter.Companion.PERSONAL_REVIEWER
import kr.or.mrhi.cinemastorage.view.adapter.PersonalAdapter.Companion.PERSONAL_TITLE

class PersonalDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            getReviewDetail()
            returnTo()
        }
    }

    private fun returnTo() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
    /*리스트에서 아이템을 클릭하면서 실행한 intent.putExtra로 해당영화 데이터 넘겨받음*/
    private fun getReviewDetail() {
        val extras = intent.extras
        if (extras != null) setReviewDetail(extras)
        else finish()
    }

    private fun setReviewDetail(extras: Bundle) {
        extras.getString(PERSONAL_POSTER)?.let { poster ->
            Glide.with(this).load("https://image.tmdb.org/t/p/w342$poster").transform(CenterCrop())
                .into(binding.ivPoster)
        }
        extras.getString(PERSONAL_BACKDROP)?.let { backdrop ->
            Glide.with(this).load("https://image.tmdb.org/t/p/w1280$backdrop")
                .transform(CenterCrop()).into(binding.ivBackdrop)
        }
        binding.tvReviewerContent.text = extras.getString(PERSONAL_REVIEWER, "")
        binding.tvDateContent.text = extras.getString(PERSONAL_DATE, "")
        binding.tvTitleContent.text = extras.getString(PERSONAL_TITLE, "")
        binding.tvCommentContent.text = extras.getString(PERSONAL_COMMENT, "")
        binding.ratingBar.rating = extras.getFloat(PERSONAL_RATING, 0f)
    }

}