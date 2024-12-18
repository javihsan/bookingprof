package com.diloso.bookhair.fly.services.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "DTO for Location", name = "LocationDTO")
public class LocationDTO implements Serializable {
	
	protected static final long serialVersionUID = 1L;

	private @Getter @Setter String type;
	private @Getter @Setter String name;
	private @Getter @Setter String iataCode;
	private @Getter @Setter String cityCode;

	public LocationDTO() {
	}
}
