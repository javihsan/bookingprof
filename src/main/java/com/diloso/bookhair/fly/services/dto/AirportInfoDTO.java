package com.diloso.bookhair.fly.services.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "DTO for AirportInfo", name = "AirportInfoDTO")
public class AirportInfoDTO {

	private @Getter @Setter String iataCode;
	private @Getter @Setter String terminal;
	private @Getter @Setter String at;

	public AirportInfoDTO() {
	}
}
