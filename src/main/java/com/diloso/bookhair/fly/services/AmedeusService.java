package com.diloso.bookhair.fly.services;

import org.springframework.stereotype.Component;

import com.amadeus.Amadeus;

import jakarta.inject.Singleton;

@Component
@Singleton
public class AmedeusService {

	private static String CLIENT_ID = "aa";
	private static String CLIENT_SECRET = "aa";

	protected static final Amadeus amadeusInstance = Amadeus.builder(CLIENT_ID, CLIENT_SECRET).build();

	protected AmedeusService() {
	}

	public Amadeus get() {
		return amadeusInstance;
	}

}
