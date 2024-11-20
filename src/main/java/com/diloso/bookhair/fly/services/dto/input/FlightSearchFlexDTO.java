package com.diloso.bookhair.fly.services.dto.input;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "DTO for FlightSearchFlex", name = "FlightSearchFlexDTO")
public class FlightSearchFlexDTO extends FlightSearchDTO {

	@Schema(description = "" + "\r\n"
			+ "Example : 2023-05-02", name = "dateRangers", type = "list", example = "2023-05-02")
	protected @Getter @Setter List<DateRangeDTO> dateRangers;
	
	@Schema(description = "number of days the route lasts" , name = "numNonWorkingDays", type = "integer", example = "2")
	protected @Getter @Setter Integer numDays;
	
	@Schema(description = "the number of non-working days included in the route" , name = "numNonWorkingDays", type = "integer", example = "2")
	protected @Getter @Setter Integer numNonWorkingDays;

}
