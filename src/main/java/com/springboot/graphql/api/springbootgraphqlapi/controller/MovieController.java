package com.springboot.graphql.api.springbootgraphqlapi.controller;

import graphql.ExecutionResult;
import graphql.GraphQL;
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

    @Autowired
    private GraphQL graphQL;

    @PostMapping
    public ResponseEntity<Object> movies(@RequestBody String query) {
        ExecutionResult execute = graphQL.execute(query);
        return new ResponseEntity<>(execute, HttpStatus.OK);
    }

}
