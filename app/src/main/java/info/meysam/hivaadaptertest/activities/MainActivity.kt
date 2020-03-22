package info.meysam.hivaadaptertest.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import info.meysam.hivaadapter.*
import info.meysam.hivaadaptertest.R
import info.meysam.hivaadaptertest.model.HeaderItem
import info.meysam.hivaadaptertest.model.Movie
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : ParentActivity() {

    var adapter: HivaRecyclerAdapter? = null
    var movies = ArrayList<Any?>()
    var lord = "http://www.taosmemory.com/movies/poster/2002/51.jpg"
    var titanic = "https://i.pinimg.com/originals/44/55/d9/4455d96357fb041d1cf3c8a5264ed593.jpg"


    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private var currentAnimator: Animator? = null

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private var shortAnimationDuration: Int = 300

    override fun setContentViewActivity() {

        setContentView(R.layout.activity_main)

    }

    override fun getIntentData() {


    }


    override fun instantiateViews() {

    }

    override fun setViewListeners() {

    }

    override fun setActivityContent() {

        movies.add(HeaderItem())

        for (i in 0..99) {
            movies.add(Movie("Titanic", "Jack, Rose ...", titanic))
            movies.add(Movie("Lord Of The Rings", "Gandalf, Frodo, Bilbo ...", lord))
        }


        movies.add(2, HeaderItem())
        movies.add(5, HeaderItem())
        movies.add(10, HeaderItem())
        movies.add(13, HeaderItem())


        adapter = HivaRecyclerAdapter()
        recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        adapter?.items = movies

        adapter?.makeClassHeader(HeaderItem::class.java)

        recycler.adapter = adapter

        recycler.addItemDecoration(ItemDecoration(), 0)


        adapter?.setOnItemClickListener(Movie::class.java) { item, holder ->

            val intent = Intent(context, MovieDetailsActivity::class.java)

            intent.putExtra("details", Gson().toJson(item))

            startActivity(intent)


        }


        adapter?.setItemsListener(Movie::class.java, object : Movie.ItemListener {
            override fun onImageClickListener(
                item: Movie?,
                it: View
            ) {

                // zoomImageFromThumb(it, (it as ImageView).drawable)
                zoomImageFromThumb(it, item?.imgUrl)


                // Retrieve and cache the system's default "short" animation time.
                shortAnimationDuration =
                    resources.getInteger(android.R.integer.config_shortAnimTime)


            }

        })


    }

    override fun refreshContent() {

    }

    private fun zoomImageFromThumb(thumbView: View, imageResId: String?) {

        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        currentAnimator?.cancel()

        // Load the high-resolution "zoomed-in" image.
        Glide.with(context!!).load(imageResId).into(expanded_image)
        //expandedImageView.setImageDrawable(imageResId)

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBoundsInt)
        findViewById<View>(R.id.container)
            .getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        val startScale: Float
        if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
            // Extend start bounds horizontally
            startScale = startBounds.height() / finalBounds.height()
            val startWidth: Float = startScale * finalBounds.width()
            val deltaWidth: Float = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            // Extend start bounds vertically
            startScale = startBounds.width() / finalBounds.width()
            val startHeight: Float = startScale * finalBounds.height()
            val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.alpha = 0f
        expanded_image.visibility = View.VISIBLE

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expanded_image.pivotX = 0f
        expanded_image.pivotY = 0f

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        currentAnimator = AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(
                    expanded_image,
                    View.X,
                    startBounds.left,
                    finalBounds.left
                )
            ).apply {
                with(
                    ObjectAnimator.ofFloat(
                        expanded_image,
                        View.Y,
                        startBounds.top,
                        finalBounds.top
                    )
                )
                with(ObjectAnimator.ofFloat(expanded_image, View.SCALE_X, startScale, 1f))
                with(ObjectAnimator.ofFloat(expanded_image, View.SCALE_Y, startScale, 1f))
            }
            duration = shortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {


                override fun onAnimationStart(animation: Animator?) {
                    supportActionBar?.hide()
                }


                override fun onAnimationEnd(animation: Animator) {
                    currentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    currentAnimator = null
                }
            })
            start()
        }

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        expanded_image.setOnClickListener {
            currentAnimator?.cancel()

            // Animate the four positioning/sizing properties in parallel,
            // back to their original values.
            currentAnimator = AnimatorSet().apply {
                play(ObjectAnimator.ofFloat(expanded_image, View.X, startBounds.left)).apply {
                    with(ObjectAnimator.ofFloat(expanded_image, View.Y, startBounds.top))
                    with(ObjectAnimator.ofFloat(expanded_image, View.SCALE_X, startScale))
                    with(ObjectAnimator.ofFloat(expanded_image, View.SCALE_Y, startScale))
                }
                duration = shortAnimationDuration.toLong()
                interpolator = DecelerateInterpolator()
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationStart(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        supportActionBar?.show()
                        thumbView.alpha = 1f
                        expanded_image.visibility = View.GONE
                        currentAnimator = null
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        thumbView.alpha = 1f
                        expanded_image.visibility = View.GONE
                        currentAnimator = null
                    }
                })
                start()
            }
        }
    }

    override fun onBackPressed() {


        if (expanded_image.visibility == View.VISIBLE) {

            expanded_image.performClick()

        } else {
            super.onBackPressed()


        }
    }

}
