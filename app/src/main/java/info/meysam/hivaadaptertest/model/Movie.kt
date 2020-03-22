package info.meysam.hivaadaptertest.model

import android.view.View
import com.bumptech.glide.Glide
import info.meysam.hivaadapter.ItemBinder
import info.meysam.hivaadapter.ItemHolder
import info.meysam.hivaadaptertest.R
import kotlinx.android.synthetic.main.item_movie.view.*

/**
 * Created by ashkan on 6/28/18.
 */
class Movie : ItemBinder {
    var name: String? = null
    var stars: String? = null
    var imgUrl: String? = null
    var notice: Notice? = null

    constructor(name: String?, stars: String?, imgUrl: String?) {
        this.name = name
        this.stars = stars
        this.imgUrl = imgUrl
        notice = Notice()
        notice!!.id = 11
        notice!!.name = "this is a good movie"
    }

    constructor()


    override fun bindToHolder(binder: ItemHolder, listener: Any?) {

        binder.itemView.movie_name.text = name
        binder.itemView.movie_stars.text = stars
        Glide.with(binder.context!!)
            .load(imgUrl)
            .into(binder.itemView.movie_image)


        binder.itemView.movie_image.setOnClickListener {

            (listener as ItemListener).onImageClickListener(this, it)
        }
    }

    override fun getResourceId(): Int {
        return R.layout.item_movie


    }

    interface ItemListener {
        fun onImageClickListener(
            item: Movie?,
            it: View
        )
    }
}