package com.valarao.wordlesolver.model;

import lombok.Value;

import java.util.List;

/**
 * Model to represent a list of guesses attached with entropy scores and information bits.
 */
@Value
public class InformationList {

    List<Information> information;

}
