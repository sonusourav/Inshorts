package com.sonusourav.movies.data.local.dao

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sonusourav.movies.testhelpers.LiveDataObservers
import com.sonusourav.movies.data.local.room.MoviesDatabase
import com.sonusourav.movies.testhelpers.apiFactory.MovieApiFactory
import com.sonusourav.movies.testhelpers.apiFactory.TrailerApiFactory
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config
import java.io.IOException


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class TrailerDaoTest {

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
    fun `Trailer stored in db is same as that of inserted`() {

        val trailerList = TrailerApiFactory.getTrailerResponse().trailers

        db.moviesDao().insertMovie(MovieApiFactory.getMovieWithId(11))
        db.trailersDao().insertAllTrailers(trailerList)

        val movie = LiveDataObservers.getValue(db.moviesDao().getMovie(11))
        Assert.assertEquals(trailerList.size, movie!!.trailers.size)
    }

}

