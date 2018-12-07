package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;
import java.util.Date;

public class RepeatClientDTO extends ResourceDTO implements Serializable {
	protected static final long serialVersionUID = 1L;

	// Repetición
	protected RepeatDTO recRepeat;
	// Cliente
	protected ClientDTO recClient;
	// Fecha en la que se reservó
	protected Date recBookingTime;
	// Quien reserva el evento 1 profesional / 0 cliente 
	protected Integer recBooking;
	
	public RepeatClientDTO() {
    }

	public RepeatDTO getRecRepeat() {
		return recRepeat;
	}

	public void setRecRepeat(RepeatDTO recRepeat) {
		this.recRepeat = recRepeat;
	}

	public ClientDTO getRecClient() {
		return recClient;
	}

	public void setRecClient(ClientDTO recClient) {
		this.recClient = recClient;
	}

	public Date getRecBookingTime() {
		return recBookingTime;
	}

	public void setRecBookingTime(Date recBookingTime) {
		this.recBookingTime = recBookingTime;
	}

	public Integer getRecBooking() {
		return recBooking;
	}

	public void setRecBooking(Integer recBooking) {
		this.recBooking = recBooking;
	}
	
				  
}