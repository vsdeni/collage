package me.itchallenges.collageapp.browse

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.gson.Gson
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.urancompany.indoorapp.executor.ThreadScheduler
import kotlinx.android.synthetic.main.activity_browse.*
import me.itchallenges.collageapp.R
import me.itchallenges.collageapp.collage.CollageDataSource
import me.itchallenges.collageapp.settings.SettingsDataSource


class BrowseActivity : AppCompatActivity(), BrowseScreenView {
    private lateinit var presenter: BrowseScreenPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.screen_browse_title)
        supportActionBar?.setSubtitle(R.string.screen_browse_subtitle)

        presenter = BrowseScreenPresenter(this,
                GetCollageInteractor(CollageDataSource(SettingsDataSource(this)
                        , getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE), Gson()), ThreadScheduler()))

        lifecycle.addObserver(presenter)
    }

    override fun showLoader() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoader() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showCollage(image: Uri) {
        Picasso.with(this)
                .load(image)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(collage_image)
    }

    override fun navigateNext() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun context(): Context =
            this
}