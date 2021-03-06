package me.itchallenges.collageapp.filter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.azoft.carousellayoutmanager.CarouselLayoutManager
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.azoft.carousellayoutmanager.CenterScrollListener
import kotlinx.android.synthetic.main.activity_filter.*
import me.itchallenges.collageapp.R
import me.itchallenges.collageapp.browse.BrowseActivity
import me.itchallenges.collageapp.collage.CollageLayout
import me.itchallenges.collageapp.di.Injector
import me.itchallenges.collageapp.pattern.Position
import javax.inject.Inject


class FilterActivity : AppCompatActivity(), FilterScreenView {
    private var filtersAdapter: FiltersAdapter? = null

    @Inject
    lateinit var presenter: FilterScreenPresenter

    private var menuNext: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
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
        supportActionBar?.setTitle(R.string.screen_filter_title)
        supportActionBar?.setSubtitle(R.string.screen_filter_subtitle)

        val layoutManager = CarouselLayoutManager(RecyclerView.HORIZONTAL, true)
        layoutManager.setPostLayoutListener(CarouselZoomPostLayoutListener())
        layoutManager.addOnItemSelectionListener { position ->
            if (position != -1) {
                filter_picker.tag = position
                presenter.onFilterChanged()
            }
        }
        filter_picker.layoutManager = layoutManager
        filter_picker.setHasFixedSize(true)
        filter_picker.addOnScrollListener(CenterScrollListener())
    }

    override fun showFiltersPicker(filters: List<Filter>, active: Filter?) {
        filtersAdapter = FiltersAdapter(this, filters)
        filter_picker.adapter = filtersAdapter
        filter_picker.scrollToPosition(filters.indexOf(active))
    }

    override fun getSelectedFilter(): Filter {
        return filtersAdapter!!.getFilter(filter_picker.tag as Int)
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

    override fun navigateNext() {
        startActivity(Intent(this, BrowseActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.filter_menu, menu)
        menuNext = menu.findItem(R.id.menu_next)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_next -> presenter.onNavigateNextClicked()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getAppliedFilters(): Array<Filter> =
            Array(collage_preview.childCount,
                    { (collage_preview.getChildAt(it) as FilterCellView).filter })

    override fun getCheckedCells(): BooleanArray =
            BooleanArray(collage_preview.childCount,
                    { (collage_preview.getChildAt(it) as FilterCellView).isChecked() })

    override fun showCollagePreview(collage: CollageFilterViewModel, filters: Array<Filter>, checked: BooleanArray) {
        collage_preview.removeAllViews()
        (0 until collage.images.size)
                .map {
                    createCollageCellView(
                            collage.images[it],
                            collage.pattern.positions[it],
                            filters[it],
                            checked[it])
                }
                .forEach { collage_preview.addView(it) }
    }

    private fun createCollageCellView(image: Uri, position: Position, filter: Filter, checked: Boolean): FilterCellView {
        val cellView = FilterCellView(this, image, filter, checked, View.OnClickListener { presenter.onCheckedChanged() })
        val params = CollageLayout.CellLayoutParams(position.width, position.height, position.y, position.x)
        cellView.layoutParams = params
        return cellView
    }

    override fun context(): Context =
            applicationContext

    override fun getFilteredImages(): Array<Bitmap> {
        return Array(collage_preview.childCount,
                { (collage_preview.getChildAt(it) as FilterCellView).getBitmap() })
    }
}