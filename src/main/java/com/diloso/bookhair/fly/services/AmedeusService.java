package com.diloso.bookhair.fly.services;

import org.springframework.stereotype.Component;

import com.amadeus.Amadeus;

import jakarta.inject.Singleton;

@Component
@Singleton
public class AmedeusService {

	private static String CLIENT_ID = "wX2W9ZSJywu6qAAIfExkNUDK5GsQjakp";
	private static String CLIENT_SECRET = "UEffPAruWkzXPFi5";

	protected static final Amadeus amadeusInstance = Amadeus.builder(CLIENT_ID, CLIENT_SECRET).build();

	protected AmedeusService() {
	}

	public Amadeus get() {
		return amadeusInstance;
	}

}
