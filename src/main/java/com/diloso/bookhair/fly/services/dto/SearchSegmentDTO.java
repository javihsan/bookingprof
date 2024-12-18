package com.diloso.bookhair.fly.services.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "DTO for SearchSegment", name = "SearchSegmentDTO")
public class SearchSegmentDTO implements Serializable {
	
	protected static final long serialVersionUID = 1L;

	private @Getter @Setter String carrierCode;
	private @Getter @Setter AirportInfoDTO departure;
	private @Getter @Setter AirportInfoDTO arrival;

	public SearchSegmentDTO() {
	}
}
