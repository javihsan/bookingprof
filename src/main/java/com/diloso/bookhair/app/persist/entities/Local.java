package com.diloso.bookhair.app.persist.entities;

import java.util.List;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;

/**
 * The persistent class for the Local entity
 * 
 */
@Entity
@Cache
public class Local extends Resource { 
	
	protected Integer locBookingClient;
	
	protected String locName;

	protected Long locWhereId;

	protected Integer locApoDuration;
	
	protected Integer locTimeRestricted;

	protected Integer locOpenDays;

	protected Integer locNumPersonsApo;

	protected Integer locMulServices;

	protected Integer locSelCalendar;
	
	protected Integer locNumUsuDays;
	
	protected Long locSemanalDiaryId;
	
	protected Long locTaskDefaultId;
	
	protected List<Long> locLangsId;
	
	protected Integer locNewClientDefault;
	
	protected Integer locCacheTasks;
	
	protected Long locResponId;

	protected Integer locMailBookign;

	protected Long locSinGCalendarId;

	protected Long locSinMChimpId;
	
	public Local() {

	}

	public Integer getLocBookingClient() {
		return locBookingClient;
	}

	public void setLocBookingClient(Integer locBookingClient) {
		this.locBookingClient = locBookingClient;
	}

	public String getLocName() {
		return locName;
	}

	public void setLocName(String locName) {
		this.locName = locName;
	}

	public Long getLocWhereId() {
		return locWhereId;
	}

	public void setLocWhereId(Long locWhereId) {
		this.locWhereId = locWhereId;
	}

	public Integer getLocApoDuration() {
		return locApoDuration;
	}

	public void setLocApoDuration(Integer locApoDuration) {
		this.locApoDuration = locApoDuration;
	}

	
	public Integer getLocTimeRestricted() {
		return locTimeRestricted;
	}

	public void setLocTimeRestricted(Integer locTimeRestricted) {
		this.locTimeRestricted = locTimeRestricted;
	}

	public Integer getLocOpenDays() {
		return locOpenDays;
	}

	public void setLocOpenDays(Integer locOpenDays) {
		this.locOpenDays = locOpenDays;
	}
	
	public Integer getLocNumPersonsApo() {
		return locNumPersonsApo;
	}

	public void setLocNumPersonsApo(Integer locNumPersonsApo) {
		this.locNumPersonsApo = locNumPersonsApo;
	}

	public Integer getLocMulServices() {
		return locMulServices;
	}

	public void setLocMulServices(Integer locMulServices) {
		this.locMulServices = locMulServices;
	}

	public Integer getLocSelCalendar() {
		return locSelCalendar;
	}

	public void setLocSelCalendar(Integer locSelCalendar) {
		this.locSelCalendar = locSelCalendar;
	}

	
	public Integer getLocNumUsuDays() {
		return locNumUsuDays;
	}

	public void setLocNumUsuDays(Integer locNumUsuDays) {
		this.locNumUsuDays = locNumUsuDays;
	}

	public Long getLocSemanalDiaryId() {
		return locSemanalDiaryId;
	}

	public void setLocSemanalDiaryId(Long locSemanalDiaryId) {
		this.locSemanalDiaryId = locSemanalDiaryId;
	}

	public Long getLocTaskDefaultId() {
		return locTaskDefaultId;
	}

	public void setLocTaskDefaultId(Long locTaskDefaultId) {
		this.locTaskDefaultId = locTaskDefaultId;
	}
	
	public List<Long> getLocLangsId() {
		return locLangsId;
	}

	public void setLocLangsId(List<Long> locLangsId) {
		this.locLangsId = locLangsId;
	}

	public Integer getLocNewClientDefault() {
		return locNewClientDefault;
	}

	public void setLocNewClientDefault(Integer locNewClientDefault) {
		this.locNewClientDefault = locNewClientDefault;
	}
	
	public Integer getLocCacheTasks() {
		return locCacheTasks;
	}

	public void setLocCacheTasks(Integer locCacheTasks) {
		this.locCacheTasks = locCacheTasks;
	}
	
	public Long getLocResponId() {
		return locResponId;
	}

	public void setLocResponId(Long locResponId) {
		this.locResponId = locResponId;
	}

	public Integer getLocMailBookign() {
		return locMailBookign;
	}

	public void setLocMailBookign(Integer locMailBookign) {
		this.locMailBookign = locMailBookign;
	}

	public Long getLocSinGCalendarId() {
		return locSinGCalendarId;
	}

	public void setLocSinGCalendarId(Long locSinGCalendarId) {
		this.locSinGCalendarId = locSinGCalendarId;
	}

	public Long getLocSinMChimpId() {
		return locSinMChimpId;
	}

	public void setLocSinMChimpId(Long locSinMChimpId) {
		this.locSinMChimpId = locSinMChimpId;
	}
}