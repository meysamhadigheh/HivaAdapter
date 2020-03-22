package info.meysam.hivaadaptertest.model

import com.google.gson.annotations.SerializedName

/**
 * Created by ashkan on 8/7/18.
 */
class Notice {
    var id = 0

    @SerializedName("title")
    var name: String? = null

}