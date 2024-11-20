package com.diloso.bookhair.fly.services;

import org.springframework.stereotype.Component;

import com.amadeus.Amadeus;

import jakarta.inject.Singleton;

@Component
@Singleton
public class AmedeusService {

	private static String CLIENT_ID = "TVe8aysiWhlU2N6b1ElkBRaNa9b9GjWF";
	private static String CLIENT_SECRET = "oqKGK7NdRJfbza89";

	protected static final Amadeus amadeusInstance = Amadeus.builder(CLIENT_ID, CLIENT_SECRET).build();

	protected AmedeusService() {
	}

	public Amadeus get() {
		return amadeusInstance;
	}

}
