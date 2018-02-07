package me.itchallenge.collage

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.google.gson.Gson
import me.itchallenge.collage.pattern.Pattern
import me.itchallenge.collage.pattern.PatternDataSource
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class PatternsDataSourceInstrumentedTest {

    companion object {
        private lateinit var patterns: Array<Pattern>
        @BeforeClass
        @JvmStatic
        fun setUp() {
            val appContext = InstrumentationRegistry.getTargetContext()
            patterns = PatternDataSource(appContext, Gson())
                    .getPatterns()
                    .blockingGet()
        }
    }

    @Test
    fun patternsList_CountIsCorrect() {
        Assert.assertEquals(1, patterns.size)
    }

    @Test
    fun patternFirst_NameIsCorrect() {
        val pattern = patterns[0]
        Assert.assertEquals("octo-adventure", pattern.name)
    }

    @Test
    fun patternFirst_PositionsCountIsCorrect() {
        val pattern = patterns[0]
        Assert.assertEquals(5, pattern.positions.size)
    }

    @Test
    fun patternFirst_CoordinatesAreCorrect() {
        val pattern = patterns[0]
        Assert.assertEquals(0, pattern.positions[0].x)
        Assert.assertEquals(0, pattern.positions[0].y)
    }

    @Test
    fun patternFirst_SizesAreCorrect() {
        val pattern = patterns[0]
        Assert.assertEquals(5, pattern.positions[0].width)
        Assert.assertEquals(5, pattern.positions[0].height)
    }
}
