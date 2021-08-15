package com.sonusourav.movies.data.local.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.sonusourav.movies.data.remote.paging.MoviePageKeyedDataSource

class RepoMoviesResult(var data: LiveData<PagedList<Movie>>,
                       var resource: LiveData<Resource<*>>,
                       var sourceLiveData: MutableLiveData<MoviePageKeyedDataSource>)