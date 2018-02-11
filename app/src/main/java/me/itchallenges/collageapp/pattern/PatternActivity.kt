package me.itchallenges.collageapp.pattern


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import com.azoft.carousellayoutmanager.CarouselLayoutManager
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.azoft.carousellayoutmanager.CenterScrollListener
import com.google.gson.Gson
import me.itchallenges.collageapp.R


class PatternActivity : AppCompatActivity(), PatternView {
    private lateinit var presenter: PatternPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pattern)
        presenter = PatternPresenter(this, PatternDataSource(this, Gson()))
        presenter.loadPatterns()
    }

    override fun showPatternsPicker(patterns: Array<Pattern>, active: Pattern?) {
        val patternsView = findViewById<RecyclerView>(R.id.patterns_picker)
        //val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)//CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true)
        val layoutManager = CarouselLayoutManager(RecyclerView.HORIZONTAL, true)
        layoutManager.setPostLayoutListener(CarouselZoomPostLayoutListener())

        patternsView.layoutManager = layoutManager
        patternsView.setHasFixedSize(true)
        patternsView.adapter = PatternsAdapter(this, patterns)
        patternsView.addOnScrollListener(CenterScrollListener())
    }
}