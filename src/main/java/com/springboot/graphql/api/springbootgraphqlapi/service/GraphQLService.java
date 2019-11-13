package com.springboot.graphql.api.springbootgraphqlapi.service;

import com.google.common.collect.Lists;
import com.springboot.graphql.api.springbootgraphqlapi.model.Movie;
import com.springboot.graphql.api.springbootgraphqlapi.repository.MovieRepository;
import com.springboot.graphql.api.springbootgraphqlapi.service.fetcher.AllMoviesDataFetcher;
import com.springboot.graphql.api.springbootgraphqlapi.service.fetcher.MovieDataFetcher;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * Created by fer on 13/11/19.
 */
@Service
public class GraphQLService {

    private static Logger logger = LoggerFactory.getLogger(GraphQLService.class);

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private AllMoviesDataFetcher allMoviesDataFetcher;

    @Autowired
    private MovieDataFetcher movieDataFetcher;

    @Value("classpath:movies.graphql")
    Resource resource;

    private GraphQL graphQL;


    @PostConstruct
    private void loadSchema() throws IOException {
        logger.info("Entering loadSchema@GraphQLService");
        loadDataIntoHSQL();
        //Get the graphql file
        File file = resource.getFile();
        //Parse SchemaF
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(file);
        RuntimeWiring runtimeWiring = buildRuntimeWiring();
        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
        graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private void loadDataIntoHSQL() {
        Stream.of(
                Movie.builder().id("1001").title("Guason").actors(new String[]{"Joaquin Phoenix", "Robert De Niro"}).directors(new String[]{"Todd Phillips"}).releaseDate("3 de octubre de 2019").build(),
                Movie.builder().id("1002").title("Avengers: Endgame").actors(new String[]{"Robert Downey Jr.", "Scarlett Johansson", "Chris Evans"}).directors(new String[]{"Anthony Russo", "Joe Russo"}).releaseDate("22 de abril de 2019").build()

            ).forEach(movie -> movieRepository.save(movie));
    }

    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("allMovies", allMoviesDataFetcher)
                        .dataFetcher("movie", movieDataFetcher))
                        .build();
    }

    public GraphQL getGraphQL(){
        return graphQL;
    }
}
