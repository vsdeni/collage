package me.itchallenges.collageapp.filter

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.azoft.carousellayoutmanager.CarouselLayoutManager
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.azoft.carousellayoutmanager.CenterScrollListener
import com.google.gson.Gson
import com.urancompany.indoorapp.executor.ThreadScheduler
import me.itchallenges.collageapp.R
import me.itchallenges.collageapp.collage.CollageDataSource
import me.itchallenges.collageapp.collage.CollageLayout
import me.itchallenges.collageapp.pattern.Position
import me.itchallenges.collageapp.settings.SettingsDataSource
import java.io.File


class FilterActivity : AppCompatActivity(), FilterView {
    private lateinit var filtersView: RecyclerView
    private lateinit var collageView: CollageLayout

    private lateinit var presenter: FilterPresenter
    private var filtersAdapter: FiltersAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

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
                GetFilterCollageInteractor(SettingsDataSource(this), CollageDataSource(getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE), Gson()), ThreadScheduler()),//TODO
                GetFiltersInteractor(FilterDataSource(),
                        ThreadScheduler()))

        presenter.loadFilters()
    }

    override fun showFiltersPicker(filters: List<Filter>, active: Filter?) {
        filtersAdapter = FiltersAdapter(this, filters)
        filtersView.adapter = filtersAdapter
        filtersView.scrollToPosition(filters.indexOf(active))
    }

    override fun getSelectedFilter(): Filter {
        return filtersAdapter!!.getFilter(filtersView.tag as Int)
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

    override fun getAppliedFilters(): Array<Filter> =
            Array(collageView.childCount,
                    { (collageView.getChildAt(it) as FilterCellView).filter })

    override fun getCheckedCells(): BooleanArray =
            BooleanArray(collageView.childCount,
                    { (collageView.getChildAt(it) as FilterCellView).isChecked() })

    override fun showCollagePreview(collage: CollageFilterViewModel, filters: Array<Filter>, checked: BooleanArray) {
        collageView.removeAllViews()
        (0 until collage.frames.size)
                .map {
                    createCollageCellView(
                            collage.frames[it],
                            collage.pattern.positions[it],
                            filters[it],
                            checked[it])
                }
                .forEach { collageView.addView(it) }
    }

    private fun createCollageCellView(frame: File, position: Position, filter: Filter, checked: Boolean): FilterCellView {
        val cellView = FilterCellView(this, frame, filter, checked, View.OnClickListener { presenter.onCheckedChanged() })
        val params = CollageLayout.CellLayoutParams(position.width, position.height, position.y, position.x)
        cellView.layoutParams = params
        return cellView
    }

    override fun context(): Context =
            applicationContext
}