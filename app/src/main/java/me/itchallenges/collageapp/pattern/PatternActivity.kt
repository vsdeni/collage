package me.itchallenges.collageapp.pattern

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.azoft.carousellayoutmanager.CarouselLayoutManager
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.azoft.carousellayoutmanager.CenterScrollListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.gson.Gson
import com.urancompany.indoorapp.executor.ThreadScheduler
import me.itchallenges.collageapp.R
import me.itchallenges.collageapp.collage.CollageDataSource
import me.itchallenges.collageapp.collage.CollageLayout
import me.itchallenges.collageapp.filter.FilterActivity
import me.itchallenges.collageapp.settings.SettingsDataSource

class PatternActivity : AppCompatActivity(), PatternView {
    private lateinit var patternsView: RecyclerView
    private lateinit var collageView: CollageLayout

    private lateinit var presenter: PatternPresenter
    private var patternsAdapter: PatternsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pattern)
        patternsView = findViewById(R.id.patterns_picker)
        collageView = findViewById(R.id.collage_preview)

        val layoutManager = CarouselLayoutManager(RecyclerView.HORIZONTAL, true)
        layoutManager.setPostLayoutListener(CarouselZoomPostLayoutListener())
        layoutManager.addOnItemSelectionListener { position ->
            if (position != -1) {
                patternsView.tag = position
                presenter.onPatternChanged()
            }
        }
        patternsView.layoutManager = layoutManager
        patternsView.setHasFixedSize(true)
        patternsView.addOnScrollListener(CenterScrollListener())

        presenter = PatternPresenter(this,
                GetPatternsInteractor(PatternDataSource(this, Gson()), ThreadScheduler()),
                GetImagesInteractor(CollageDataSource(getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE), Gson()), SettingsDataSource(this), ThreadScheduler()),
                SaveSelectedPatternInteractor(CollageDataSource(getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE), Gson()), ThreadScheduler()))
        presenter.loadPatterns()
    }

    override fun showLoader() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoader() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMessage(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showPatternsPicker(patterns: Array<Pattern>, active: Pattern?) {
        patternsAdapter = PatternsAdapter(this, patterns)
        patternsView.adapter = patternsAdapter
    }

    override fun getSelectedPattern(): Pattern {
        return patternsAdapter?.getPattern(patternsView.tag as Int)!!
    }

    override fun showCollagePreview(pattern: Pattern, frames: List<Bitmap>) {
        collageView.removeAllViews()
        (0 until frames.size)
                .map { createCollageCellView(frames[it], pattern.positions[it]) }
                .forEach { collageView.addView(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.pattern_menu, menu)
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

    private fun createCollageCellView(frame: Bitmap, position: Position): View {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.item_collage_pattern, collageView, false)
        val image = view.findViewById<ImageView>(R.id.collage_image)
        val params = CollageLayout.CellLayoutParams(position.width, position.height, position.y, position.x)
        Glide.with(this).load(frame)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image)
        view.layoutParams = params
        return view
    }
}