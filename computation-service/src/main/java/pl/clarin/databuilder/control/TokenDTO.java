package pl.clarin.databuilder.control;

public record TokenDTO(String orth, String lemma, String partOfSpeech, PositionDTO position, boolean ns){};