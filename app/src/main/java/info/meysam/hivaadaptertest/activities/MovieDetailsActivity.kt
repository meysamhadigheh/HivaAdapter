package info.meysam.hivaadaptertest.activities

import com.bumptech.glide.Glide
import com.google.gson.Gson
import info.meysam.hivaadaptertest.R
import info.meysam.hivaadaptertest.model.Movie
import kotlinx.android.synthetic.main.activity_movie_details.*


class MovieDetailsActivity : ParentActivity() {


    var movieDetails = Movie()


    override fun setContentViewActivity() {

        setContentView(R.layout.activity_movie_details)

    }

    override fun getIntentData() {


        movieDetails = Gson().fromJson(intent.getStringExtra("details"), Movie::class.java)

    }

    override fun instantiateViews() {

    }

    override fun setViewListeners() {

    }

    override fun setActivityContent() {


        context?.let { Glide.with(it).load(movieDetails.imgUrl).into(movie_image) }
        movie_name.text = movieDetails.name
        movie_stars.text = movieDetails.stars
        details.text = movieDetails.notice?.name


    }

    override fun refreshContent() {

    }
}
