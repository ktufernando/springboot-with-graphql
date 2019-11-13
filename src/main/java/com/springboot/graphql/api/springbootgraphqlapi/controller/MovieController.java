package com.springboot.graphql.api.springbootgraphqlapi.controller;

import graphql.ExecutionResult;
import graphql.GraphQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by fer on 13/11/19.
 */

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private static Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private GraphQL graphQL;

    @PostMapping
    public ResponseEntity<Object> getAllBooks(@RequestBody String query){
        logger.info("Entering getAllMovies@MovieController");
        ExecutionResult execute = graphQL.execute(query);
        return new ResponseEntity<>(execute, HttpStatus.OK);
    }

}
