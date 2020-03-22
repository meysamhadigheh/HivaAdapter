package info.meysam.hivaadaptertest.model

import android.graphics.Color
import info.meysam.hivaadapter.ItemBinder
import info.meysam.hivaadapter.ItemHolder
import info.meysam.hivaadaptertest.R
import java.util.*

/**
 * Created by ashkan on 7/26/18.
 */
class HeaderItem : ItemBinder {
    var color = 0

    override fun bindToHolder(binder: ItemHolder, listener: Any?) {
        binder.itemView.setBackgroundColor(color)
    }

    override fun getResourceId(): Int {
        return R.layout.item_header
    }

    init {
        val rnd = Random()
        color =
            Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }
}