package com.example.norush.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AIPredictionResponse(
    @JsonProperty("route_id")
    String routeId,

    @JsonProperty("stop_id")
    String stopId,

    @JsonProperty("predicted_congestion")
    double predictedCongestion,

    @JsonProperty("compartment")
    String compartment,

    @JsonProperty("predicted_congestion_compartment")

    String predictedCongestionCompartment,

    @JsonProperty("datetime")
    String datetime
) {}
