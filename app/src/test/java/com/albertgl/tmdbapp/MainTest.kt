import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.albertgl.tmdbapp.MainActivity
import com.albertgl.tmdbapp.R
import com.albertgl.tmdbapp.view.viewholder.MovieVH
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainTest {

    @Rule
    @JvmField
    val mActivityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testScrolling() {
        onView(withId(R.id.main_recycler)).perform(RecyclerViewActions.scrollToPosition<MovieVH>(5))
    }
}