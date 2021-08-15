package com.sonusourav.movies.data.local.dao

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sonusourav.movies.testhelpers.LiveDataObservers
import com.sonusourav.movies.data.local.room.MoviesDatabase
import com.sonusourav.movies.testhelpers.apiFactory.CreditApiFactory
import com.sonusourav.movies.testhelpers.apiFactory.MovieApiFactory
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config
import java.io.IOException


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class CreditsDaoTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var db: MoviesDatabase

    @Before
    fun createDb() {
        MockitoAnnotations.initMocks(this);
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MoviesDatabase::class.java).allowMainThreadQueries().build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun `cast stored in db is same as that of inserted`() {

        val castList = CreditApiFactory.getCastResponse().cast

        db.moviesDao().insertMovie(MovieApiFactory.getMovieWithId(11))
        db.castsDao().insertAllCasts(castList)

        val movie = LiveDataObservers.getValue(db.moviesDao().getMovie(11))
        Assert.assertEquals(castList.size, movie!!.castList.size)
    }

}

