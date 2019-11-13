package com.springboot.graphql.api.springbootgraphqlapi.service.fetcher;

import com.springboot.graphql.api.springbootgraphqlapi.model.Movie;
import com.springboot.graphql.api.springbootgraphqlapi.repository.MovieRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by fer on 13/11/19.
 */
@Component
public class AllMoviesDataFetcher implements DataFetcher<List<Movie>> {
    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<Movie> get(DataFetchingEnvironment dataFetchingEnvironment) {
        return movieRepository.findAll();
    }
}
