package pl.clarin.chronocorpus.geographicalpropernames.entity;

import java.util.concurrent.atomic.AtomicInteger;

public record LocationCount(AtomicInteger count, double lon, double lat) {}
