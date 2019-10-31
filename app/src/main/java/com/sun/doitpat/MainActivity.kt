package com.sun.doitpat

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.sun.doitpat.util.Constants.DEFAULT_CHECK_ID
import com.sun.doitpat.util.Constants.EXTRA
import com.sun.doitpat.util.Constants.ID
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusBarTransparent()
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        openDetailByRequest()
    }

    private fun makeStatusBarTransparent() {
        window.apply {
            setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            statusBarColor = Color.TRANSPARENT
        }
    }

    private fun openDetailByRequest() {
        intent?.getIntExtra(ID, DEFAULT_CHECK_ID)?.let {
            openDetailFragment(it, DEFAULT_CHECK_ID)
        }
        intent?.getIntExtra(EXTRA, DEFAULT_CHECK_ID)?.let {
            openDetailFragment(it, DEFAULT_CHECK_ID)
        }
    }

    private fun openDetailFragment(id: Int, idCompare: Int) {
        if (id != idCompare) {
            val bundle = Bundle()
            bundle.putInt(resources.getString(R.string.title_title), id)
            this.findNavController(R.id.navHostFragment).navigate(R.id.detailFragment, bundle)
        }
    }

    companion object {
        fun getIntent(context: Context, itemId: Int) =
                Intent(context, MainActivity::class.java).apply {
                    putExtra(ID, itemId)
                }
    }
}
