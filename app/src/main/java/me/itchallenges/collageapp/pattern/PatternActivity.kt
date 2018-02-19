package me.itchallenges.collageapp.pattern

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.azoft.carousellayoutmanager.CarouselLayoutManager
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.azoft.carousellayoutmanager.CenterScrollListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pattern.*
import me.itchallenges.collageapp.R
import me.itchallenges.collageapp.collage.CollageLayout
import me.itchallenges.collageapp.di.Injector
import me.itchallenges.collageapp.filter.FilterActivity
import javax.inject.Inject

class PatternActivity : AppCompatActivity(), PatternScreenView {
    private var patternsAdapter: PatternsAdapter? = null

    @Inject
    lateinit var presenter: PatternScreenPresenter

    private var menuNext: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pattern)

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
        supportActionBar?.setTitle(R.string.screen_pattern_title)
        supportActionBar?.setSubtitle(R.string.screen_pattern_subtitle)
        supportActionBar?.displayOptions = (ActionBar.DISPLAY_SHOW_HOME
                or ActionBar.DISPLAY_SHOW_TITLE or ActionBar.DISPLAY_SHOW_CUSTOM)

        val layoutManager = CarouselLayoutManager(RecyclerView.HORIZONTAL, true)
        layoutManager.setPostLayoutListener(CarouselZoomPostLayoutListener())
        layoutManager.addOnItemSelectionListener { position ->
            if (position != -1) {
                patterns_picker.tag = position
                presenter.onPatternChanged()
            }
        }
        patterns_picker.layoutManager = layoutManager
        patterns_picker.setHasFixedSize(true)
        patterns_picker.addOnScrollListener(CenterScrollListener())
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

    override fun showPatternsPicker(patterns: List<Pattern>, active: Pattern?) {

        if (patterns.isEmpty()) {
            no_pattern.visibility = View.VISIBLE
            patterns_picker.visibility = View.GONE
        } else {
            no_pattern.visibility = View.GONE
            patterns_picker.visibility = View.VISIBLE
            patternsAdapter = PatternsAdapter(this, patterns)
            patterns_picker.adapter = patternsAdapter
        }
    }

    override fun getSelectedPattern(): Pattern? {
        return patternsAdapter?.getPattern(patterns_picker.tag as Int)
    }

    override fun showCollagePreview(pattern: Pattern, frames: List<Uri>) {
        collage_preview.removeAllViews()
        (0 until frames.size)
                .map { createCollageCellView(frames[it], pattern.positions[it]) }
                .forEach { collage_preview.addView(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.pattern_menu, menu)
        menuNext = menu.findItem(R.id.menu_next)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_next -> presenter.onNavigateNextClicked()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun navigateNext() {
        startActivity(Intent(this, FilterActivity::class.java))
    }

    private fun createCollageCellView(image: Uri, position: Position): View {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.item_collage_pattern, collage_preview, false)
        val imageView = view.findViewById<ImageView>(R.id.collage_image)
        val params = CollageLayout.CellLayoutParams(position.width, position.height, position.y, position.x)
        Picasso.with(this)
                .load(image)
                .into(imageView)
        view.layoutParams = params
        return view
    }

    override fun context(): Context =
            applicationContext
}