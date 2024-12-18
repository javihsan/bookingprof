package com.diloso.bookhair.fly.services.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "DTO for Itinerary", name = "ItineraryDTO")
public class ItineraryDTO implements Serializable {
	
	protected static final long serialVersionUID = 1L;

	private @Getter @Setter String duration;
	private @Getter @Setter List<SearchSegmentDTO> segments;

	public ItineraryDTO() {
	}

}
