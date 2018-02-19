package me.itchallenges.collageapp.browse

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_browse.*
import me.itchallenges.collageapp.R
import me.itchallenges.collageapp.di.Injector
import me.itchallenges.collageapp.video.VideoActivity
import javax.inject.Inject


class BrowseActivity : AppCompatActivity(), BrowseScreenView {

    @Inject
    lateinit var presenter: BrowseScreenPresenter

    private var menuNext: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse)

        Injector.INSTANCE.appComponent.inject(this)

        initPresenter()
        initViews()
    }

    private fun initPresenter() {
        presenter.view = this
        lifecycle.addObserver(presenter)
    }

    private fun initViews() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.screen_browse_title)
        supportActionBar?.setSubtitle(R.string.screen_browse_subtitle)
        share.setOnClickListener({ presenter.onShareClicked() })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.browse_menu, menu)
        menuNext = menu.findItem(R.id.menu_next)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_start_over -> presenter.onStartOverClicked()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showLoader() {
        menuNext?.let {
            it.setActionView(R.layout.progressbar)
            it.expandActionView()
        }
    }

    override fun hideLoader() {
        menuNext?.let {
            it.collapseActionView()
            it.actionView = null
        }
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