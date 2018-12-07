package com.diloso.bookhair.app.negocio.dto.generator;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.diloso.bookhair.app.negocio.utils.Utils;

public class NotifCalendarDTO implements Serializable {
	protected static final long serialVersionUID = 1L;

	protected String nocUID;
	
	protected Date nocDtStart;
	
	protected Date nocDtEnd;
	
	protected Date nocDtCreated;
	
	protected Date nocDtModify;
	
	protected Date nocDtStamp;

	protected String nocOrgName;
	
	protected String nocOrgEmail;
	
	protected String nocDesName;
	
	protected String nocDesEmail;
	
	protected String nocDesEmailCC;
	
	protected String nocLocName;
	
	protected String nocLocEmail;
	
	protected String nocLocTelf;
	
	protected String nocSummary;
	
	protected String nocContent;
	
	protected String nocLocation;
	
	protected String nocTasks;
	
	protected Locale locale;
	
	protected TimeZone timeZone;
	
	public NotifCalendarDTO() {
    }

	
	public String getNocUID() {
		return nocUID;
	}

	public void setNocUID(String nocUID) {
		this.nocUID = nocUID;
	}

	public Date getNocDtStart() {
		return nocDtStart;
	}

	public void setNocDtStart(Date nocDtStart) {
		this.nocDtStart = nocDtStart;
	}

	public Date getNocDtEnd() {
		return nocDtEnd;
	}

	public void setNocDtEnd(Date nocDtEnd) {
		this.nocDtEnd = nocDtEnd;
	}

	public Date getNocDtCreated() {
		return nocDtCreated;
	}

	public void setNocDtCreated(Date nocDtCreated) {
		this.nocDtCreated = nocDtCreated;
	}

	public Date getNocDtModify() {
		return nocDtModify;
	}

	public void setNocDtModify(Date nocDtModify) {
		this.nocDtModify = nocDtModify;
	}
	
	public Date getNocDtStamp() {
		return nocDtStamp;
	}

	public void setNocDtStamp(Date nocDtStamp) {
		this.nocDtStamp = nocDtStamp;
	}

	public String getNocOrgName() {
		return nocOrgName;
	}

	public void setNocOrgName(String nocOrgName) {
		this.nocOrgName = nocOrgName;
	}

	public String getNocOrgEmail() {
		return nocOrgEmail;
	}

	public void setNocOrgEmail(String nocOrgEmail) {
		this.nocOrgEmail = nocOrgEmail;
	}
		
	public String getNocDesName() {
		return nocDesName;
	}

	public void setNocDesName(String nocDesName) {
		this.nocDesName = nocDesName;
	}

	public String getNocDesEmail() {
		return nocDesEmail;
	}

	public void setNocDesEmail(String nocDesEmail) {
		this.nocDesEmail = nocDesEmail;
	}

	public String getNocDesEmailCC() {
		return nocDesEmailCC;
	}

	public void setNocDesEmailCC(String nocDesEmailCC) {
		this.nocDesEmailCC = nocDesEmailCC;
	}

	
	public String getNocLocName() {
		return nocLocName;
	}

	public void setNocLocName(String nocLocName) {
		this.nocLocName = nocLocName;
	}

	public String getNocLocEmail() {
		return nocLocEmail;
	}

	public void setNocLocEmail(String nocLocEmail) {
		this.nocLocEmail = nocLocEmail;
	}

	public String getNocLocTelf() {
		return nocLocTelf;
	}

	public void setNocLocTelf(String nocLocTelf) {
		this.nocLocTelf = nocLocTelf;
	}

	public String getNocSummary() {
		return nocSummary;
	}

	public void setNocSummary(String nocSummary) {
		this.nocSummary = nocSummary;
	}

	public String getNocContent() {
		return nocContent;
	}

	public void setNocContent(String nocContent) {
		this.nocContent = nocContent;
	}

	public String getNocLocation() {
		return nocLocation;
	}

	public void setNocLocation(String nocLocation) {
		this.nocLocation = nocLocation;
	}
	
	public String getNocTasks() {
		return nocTasks;
	}

	public void setNocTasks(String nocTasks) {
		this.nocTasks = nocTasks;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}


	public String getFormatICS(Date date){
		return Utils.getFormatICS(date, getLocale(), getTimeZone());
	}
	
	public String getFormatICSDtStart(){
		return getFormatICS(getNocDtStart());
	}
	
	public String getFormatICSDtEnd(){
		return getFormatICS(getNocDtEnd());
	}
	
	public String getFormatICSDtCreated(){
		return getFormatICS(getNocDtCreated());
	}
	
	public String getFormatICSDtModify(){
		return getFormatICS(getNocDtModify());
	}
	
	public String getFormatICSDtStamp(){
		return getFormatICS(getNocDtStamp());
	}
	
	public String getFormatTextDtStart(){
		return Utils.getFormatText(getNocDtStart(), getLocale(), getTimeZone());
	}
	
}