package com.githang.dinny.app

import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        ActivityCtrl.newTestIntent(this)
        ActivityCtrl.newTestIntent(
            this, arrayListOf(title), ParcelObject("parcel object"),
            longArrayOf(1L), 1.toByte(), doubleArrayOf(1.0), title, booleanArrayOf(true, false), 1, charArrayOf('a'),
            byteArrayOf(2), arrayOf(ParcelObject("parcel array") as Parcelable), intent.extras,
            arrayOf(title), floatArrayOf(1f), 2.0, intArrayOf(3), arrayOf("string"), shortArrayOf(123), true, "string",
            1L, 'a', SerializableObject(), 3f, 4, intent, intent.extras
        )
        ActivityCtrl.startTestActivity(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
