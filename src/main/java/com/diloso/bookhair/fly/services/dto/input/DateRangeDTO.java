package com.diloso.bookhair.fly.services.dto.input;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "DTO for DateRange", name = "DateRangeDTO")
public class DateRangeDTO implements Serializable {
	
	protected static final long serialVersionUID = 1L;

	@Schema(description = "" + "\r\n"
			+ "Example : 2023-05-02", name = "startDate", type = "string", example = "2023-05-02")
	protected @Getter @Setter String startDate;

	@Schema(description = "" + "\r\n"
			+ "Example : 2023-05-02", name = "endDate", type = "string", example = "2023-05-02")
	protected @Getter @Setter String endDate;

}
