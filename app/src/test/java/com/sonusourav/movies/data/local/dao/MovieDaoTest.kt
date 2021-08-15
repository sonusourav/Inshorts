package com.sonusourav.movies.data.local.dao

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sonusourav.movies.data.local.room.MoviesDatabase
import com.sonusourav.movies.testhelpers.LiveDataObservers
import com.sonusourav.movies.testhelpers.apiFactory.MovieApiFactory
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config
import java.io.IOException


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MovieDaoTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var db: MoviesDatabase
    private lateinit var moviesDao: MoviesDao

    @Before
    fun createDb() {
        MockitoAnnotations.initMocks(this);
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MoviesDatabase::class.java).allowMainThreadQueries().build()

        moviesDao = db.moviesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun `movie stored in db is same as that of inserted`() {
        val movie = MovieApiFactory.getMovie()

        db.moviesDao().insertMovie(movie)

        val movieFromDB = LiveDataObservers.getValue(db.moviesDao().getMovie(movie.id))!!.movie
        Assert.assertEquals(movie, movieFromDB)

    }

    @Test
    fun `movie stored as favourite after marking favourite`() {
        val movie = MovieApiFactory.getMovie()

        db.moviesDao().insertMovie(movie)
        db.moviesDao().bookmarkMovie(movie.id)

        val movieFromDB = LiveDataObservers.getValue(db.moviesDao().getMovie(movie.id))!!.movie

        assert(movieFromDB!!.isFavorite)
    }

    @Test
    fun `movie stored as unfavourite after marking unfavourite`() {
        val movie = MovieApiFactory.getFavouriteMovie()

        db.moviesDao().insertMovie(movie)
        db.moviesDao().unbookmarkMovie(movie.id)

        val movieFromDB = LiveDataObservers.getValue(db.moviesDao().getMovie(movie.id))!!.movie

        assert(!movieFromDB!!.isFavorite)
    }

    @Test
    fun `movies are getting stored as favourite`() {

        db.moviesDao().insertMovie(MovieApiFactory.getMovieWithId(11))
        db.moviesDao().insertMovie(MovieApiFactory.getMovieWithId(12))
        db.moviesDao().insertMovie(MovieApiFactory.getMovieWithId(13))
        db.moviesDao().insertMovie(MovieApiFactory.getMovieWithId(14))
        db.moviesDao().insertMovie(MovieApiFactory.getMovieWithId(15))

        db.moviesDao().bookmarkMovie(12)
        db.moviesDao().bookmarkMovie(15)

        val favMovies = LiveDataObservers.getValue(db.moviesDao().allBookmarkedMovies!!)!!

        assert(favMovies.size==2)
    }
}

