package me.itchallenges.collageapp.pattern

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
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
import me.itchallenges.collageapp.collage.CollageLayout
import me.itchallenges.collageapp.frame.FramesDataSource
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
                GetFramesInteractor(FramesDataSource(), SettingsDataSource(this), ThreadScheduler()))
        presenter.loadPatterns()
    }

    override fun showPatternsPicker(patterns: Array<Pattern>, active: Pattern?) {
        patternsAdapter = PatternsAdapter(this, patterns)
        patternsView.adapter = patternsAdapter
    }

    override fun getSelectedPattern(): Pattern? {
        return patternsAdapter?.getPattern(patternsView.tag as Int)
    }

    override fun showPatternPreview(pattern: Pattern?, frames: List<Bitmap>) {
        pattern?.let {
            collageView.removeAllViews()
            (0 until frames.size)
                    .map { createCollageCellView(frames[it], pattern.positions[it]) }
                    .forEach { collageView.addView(it) }
        }
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