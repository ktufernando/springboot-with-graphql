package com.springboot.graphql.api.springbootgraphqlapi.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by fer on 13/11/19.
 */
@Table
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Movie {

    @Id
    private String id;
    private String title;
    private String[] directors;
    private String[] actors;
    private String releaseDate;

}
