package me.itchallenges.collageapp.filter

import android.content.Context
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
import me.itchallenges.collageapp.collage.CollageDataSource
import me.itchallenges.collageapp.collage.CollageLayout
import me.itchallenges.collageapp.pattern.Position
import me.itchallenges.collageapp.settings.SettingsDataSource


class FilterActivity : AppCompatActivity(), FilterView {
    private lateinit var filtersView: RecyclerView
    private lateinit var collageView: CollageLayout

    private lateinit var presenter: FilterPresenter
    private var filtersAdapter: FiltersAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        filtersView = findViewById(R.id.filter_picker)
        collageView = findViewById(R.id.collage_preview)

        val layoutManager = CarouselLayoutManager(RecyclerView.HORIZONTAL, true)
        layoutManager.setPostLayoutListener(CarouselZoomPostLayoutListener())
        layoutManager.addOnItemSelectionListener { position ->
            if (position != -1) {
                filtersView.tag = position
                presenter.onFilterChanged()
            }
        }
        filtersView.layoutManager = layoutManager
        filtersView.setHasFixedSize(true)
        filtersView.addOnScrollListener(CenterScrollListener())

        presenter = FilterPresenter(this,
                GetCollageInteractor(SettingsDataSource(this), CollageDataSource(getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE), Gson()), ThreadScheduler()),//TODO
                GetFiltersInteractor(FilterDataSource(),
                        ThreadScheduler()))

        presenter.loadFilters()
    }

    override fun showFiltersPicker(filters: List<Filter>, active: Filter?) {
        filtersAdapter = FiltersAdapter(this, filters)
        filtersView.adapter = filtersAdapter
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

    override fun navigateNext() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSelectedFilter(): Filter {
        return filtersAdapter?.getFilter(filtersView.tag as Int)!!
    }

    override fun getSelectedImages(): List<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCollagePreview(collage: CollageFilterViewModel) {
        collageView.removeAllViews()
        (0 until collage.frames.size)
                .map { createCollageCellView(collage.frames[it], collage.pattern.positions[it]) }
                .forEach { collageView.addView(it) }
    }

    private fun createCollageCellView(frame: Bitmap, position: Position): View {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.item_collage_filter, collageView, false)
        val image = view.findViewById<ImageView>(R.id.collage_image)
        val params = CollageLayout.CellLayoutParams(position.width, position.height, position.y, position.x)
        Glide.with(this).load(frame)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image)
        view.layoutParams = params
        return view
    }
}