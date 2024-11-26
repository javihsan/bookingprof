package com.diloso.bookhair.fly.services.dto.input;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
@Schema(
	    description = "DTO for FlightSearch", 
	    name = "FlightSearchDTO")
public class FlightSearchDTO {
	
	@Schema(
		    description = "city/airport IATA code from which the traveler will depart, e.g. BOS for Boston\r\n"
		    		+ "\r\n"
		    		+ "Example : SYD", 
		    name = "originLocationCode", 
		    type = "string", 
		    example = "SYD")
	protected @Getter @Setter String originLocationCode;
	
	@Schema(
		    description = "city/airport IATA code to which the traveler is going, e.g. PAR for Paris\r\n"
		    		+ "\r\n"
		    		+ "Example : BKK", 
		    name = "destinationLocationCode", 
		    type = "string", 
		    example = "BKK")
	protected @Getter @Setter String destinationLocationCode;
	
	@Schema(
		    description = "\r\n"
		    		+ "(query)\r\n"
		    		+ "the date on which the traveler will depart from the origin to go to the destination. Dates are specified in the ISO 8601 YYYY-MM-DD format, e.g. 2017-12-25\r\n"
		    		+ "\r\n"
		    		+ "Example : 2023-05-02", 
		    name = "departureDate", 
		    type = "string", 
		    example = "2023-05-02")
	protected @Getter @Setter String departureDate;
	
	@Schema(
		    description = "\r\n"
		    		+ "(query)\r\n"
		    		+ "the min hour on which the traveler will depart from the origin to go to the destination"
		    		+ "\r\n"
		    		+ "Example : 12", 
		    name = "minDepartureHour", 
		    type = "string", 
		    example = "12")
	protected @Getter @Setter String minDepartureHour;
	
	@Schema(
		    description = "\r\n"
		    		+ "(query)\r\n"
		    		+ "the maximum hour on which the traveler will depart from the origin to go to the destination"
		    		+ "\r\n"
		    		+ "Example : 12", 
		    name = "maxDepartureHour", 
		    type = "string", 
		    example = "12")
	protected @Getter @Setter String maxDepartureHour;
	
	@Schema(
		    description = "\r\n"
		    		+ "(query)\r\n"
		    		+ "the min hour on which the traveler will depart from the destination to go to the origin"
		    		+ "\r\n"
		    		+ "Example : 12", 
		    name = "minReturnHour", 
		    type = "string", 
		    example = "12")
	protected @Getter @Setter String minReturnHour;
	
	@Schema(
		    description = "\r\n"
		    		+ "(query)\r\n"
		    		+ "the maximum hour on which the traveler will depart from the destination to go to the origin"
		    		+ "\r\n"
		    		+ "Example : 12", 
		    name = "maxReturneHour", 
		    type = "string", 
		    example = "12")
	protected @Getter @Setter String maxReturnHour;
	
	@Schema(
		    description = "the date on which the traveler will depart from the destination to return to the origin. If this parameter is not specified, only one-way itineraries are found. If this parameter is specified, only round-trip itineraries are found. Dates are specified in the ISO 8601 YYYY-MM-DD format, e.g. 2018-02-28", 
		    name = "returnDate", 
		    type = "string", 
		    example = "2018-02-28")
	protected @Getter @Setter String returnDate;
	
	@Schema(
		    description = "\r\n"
		    		+ "(query)\r\n"
		    		+ "the number of adult travelers (age 12 or older on date of departure).\r\n"
		    		+ "\r\n"
		    		+ "The total number of seated travelers (adult and children) can not exceed 9.\r\n"
		    		+ "\r\n"
		    		+ "Default value : 1", 
		    name = "adults", 
		    type = "integer", 
		    example = "1")
	protected @Getter @Setter Integer adults;
	
	@Schema(
		    description = "\r\n"
		    		+ "integer\r\n"
		    		+ "(query)\r\n"
		    		+ "the number of child travelers (older than age 2 and younger than age 12 on date of departure) who will each have their own separate seat. If specified, this number should be greater than or equal to 0\r\n"
		    		+ "\r\n"
		    		+ "The total number of seated travelers (adult and children) can not exceed 9.", 
		    name = "children", 
		    type = "integer", 
		    example = "1")
	protected @Getter @Setter Integer children;
	
	@Schema(
		    description = "\r\n"
		    		+ "(query)\r\n"
		    		+ "the number of infant travelers (whose age is less or equal to 2 on date of departure). Infants travel on the lap of an adult traveler, and thus the number of infants must not exceed the number of adults. If specified, this number should be greater than or equal to 0", 
		    name = "infants", 
		    type = "integer", 
		    example = "1")
	protected @Getter @Setter Integer infants;
	
	@Schema(
		    description = "This option ensures that the system will only consider these airlines. This can not be cumulated with parameter excludedAirlineCodes.\r\n"
		    		+ "\r\n"
		    		+ "Airlines are specified as IATA airline codes and are comma-separated, e.g. 6X,7X,8X", 
		    name = "includedAirlineCodes", 
		    type = "string", 
		    example = "6X,7X,8X")
	protected @Getter @Setter String includedAirlineCodes;
	
	@Schema(
		    description = "This option ensures that the system will ignore these airlines. This can not be cumulated with parameter includedAirlineCodes.\r\n"
		    		+ "\r\n"
		    		+ "Airlines are specified as IATA airline codes and are comma-separated, e.g. 6X,7X,8X", 
		    name = "excludedAirlineCodes", 
		    type = "string", 
		    example = "6X,7X,8X")
	protected @Getter @Setter String excludedAirlineCodes;
	
	@Schema(
		    description = "if set to true, the search will find only flights going from the origin to the destination with no stop in between\r\n"
		    		+ "\r\n"
		    		+ "Default value : false", 
		    name = "nonStop", 
		    type = "boolean", 
		    example = "false")
	protected @Getter @Setter Boolean nonStop;
	
	@Schema(
		    description = "the preferred currency for the flight offers. Currency is specified in the ISO 4217 format, e.g. EUR for Euro", 
		    name = "currencyCode", 
		    type = "string", 
		    example = "EUR")
	protected @Getter @Setter String currencyCode;
	
	@Schema(
		    description = "most of the flight time should be spent in a cabin of this quality or higher. The accepted travel class is economy, premium economy, business or first class. If no travel class is specified, the search considers any travel class\r\n"
		    		+ "\r\n"
		    		+ "Available values : ECONOMY, PREMIUM_ECONOMY, BUSINESS, FIRST", 
		    name = "travelClass", 
		    type = "string", 
		    example = "ECONOMY")
	protected @Getter @Setter String travelClass;
	
	@Schema(
		    description = "maximum price per traveler. By default, no limit is applied. If specified, the value should be a positive number with no decimals", 
		    name = "maxPrice", 
		    type = "integer", 
		    example = "200")
	protected @Getter @Setter Integer maxPrice;
	
	@Schema(
		    description = "maximum number of flight offers to return. If specified, the value should be greater than or equal to 1\r\n"
		    		+ "\r\n"
		    		+ "Default value : 250", 
		    name = "max", 
		    type = "integer", 
		    example = "250")
	protected @Getter @Setter Integer max;
	
}
