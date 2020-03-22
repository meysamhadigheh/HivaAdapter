package info.meysam.hivaadaptertest.activities

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

abstract class ParentActivity : AppCompatActivity() {
    var context: Activity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        //set what horizontal_item to show
        setContentViewActivity()

        //if activity has any intent data
        getIntentData()

        //set custom settings for toolbar if needed
        //setToolbar();

        //get all views in activity content
        instantiateViews()

        //set listeners for what views should do
        setViewListeners()

        //set views content (this makes sure all views are instantiated)
        setActivityContent()
    }

    /**
     * don't use setContentView in onCreate
     * use this method instead
     */
    protected abstract fun setContentViewActivity()
    protected abstract fun getIntentData()
    protected abstract fun instantiateViews()
    protected abstract fun setViewListeners()
    protected abstract fun setActivityContent()
    protected abstract fun refreshContent()

    //*********
    //easier stuff
    //********
    protected fun showToast(str: String?) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
    }

    protected fun showToastLong(str: String?) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show()
    }

    //*******************
    //get resources easier
    //*******************
    protected fun getColorRes(color: Int): Int {
        return ContextCompat.getColor(context!!, color)
    }
}