package com.springboot.graphql.api.springbootgraphqlapi.repository;

import com.springboot.graphql.api.springbootgraphqlapi.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by fer on 13/11/19.
 */
public interface MovieRepository extends JpaRepository<Movie, String> {
}
