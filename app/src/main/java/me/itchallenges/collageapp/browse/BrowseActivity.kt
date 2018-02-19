package me.itchallenges.collageapp.browse

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.Gson
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.urancompany.indoorapp.executor.ThreadScheduler
import kotlinx.android.synthetic.main.activity_browse.*
import me.itchallenges.collageapp.R
import me.itchallenges.collageapp.collage.CollageDataSource
import me.itchallenges.collageapp.settings.SettingsDataSource
import me.itchallenges.collageapp.video.VideoActivity


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
                GetCollageImageInteractor(CollageDataSource(applicationContext, SettingsDataSource(this)
                        , getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE), Gson()), ThreadScheduler()),
                ShareCollageImageInteractor(CollageDataSource(applicationContext, SettingsDataSource(this)
                        , getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE), Gson()), ThreadScheduler()))

        share.setOnClickListener({ presenter.onShareClicked() })

        lifecycle.addObserver(presenter)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.browse_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_start_over -> presenter.onStartOverClicked()
        }
        return super.onOptionsItemSelected(item)
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

    override fun showCollageImage(image: Uri) {
        Picasso.with(this)
                .load(image)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(collage_image)
    }

    override fun getCaption(): String =
            input_message.text.toString()

    override fun navigateNext() {
        val intent = Intent(this, VideoActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun context(): Context =
            this

    override fun showShareDialog(intent: Intent) {
        startActivity(Intent.createChooser(intent, resources.getText(R.string.send_to)))
    }
}