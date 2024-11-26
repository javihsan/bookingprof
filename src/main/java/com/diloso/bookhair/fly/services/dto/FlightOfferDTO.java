package com.diloso.bookhair.fly.services.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "DTO for FlightOffer", name = "FlightOfferDTO")
public class FlightOfferDTO {

	private @Getter @Setter List<ItineraryDTO> itineraries;
	private @Getter @Setter Float price;
	private @Getter @Setter int numberOfBookableSeats;
	private @Getter @Setter String departureDate;
	private @Getter @Setter String returnDate;
	private @Getter @Setter List<String> carriersCode;
	private @Getter @Setter String urlSkyscanner;
}
