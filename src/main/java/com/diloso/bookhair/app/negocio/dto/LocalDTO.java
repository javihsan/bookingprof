package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;
import java.util.List;


public class LocalDTO extends ResourceDTO implements Serializable {
	protected static final long serialVersionUID = 1L;

	// Permitir reservas por los clientes
	protected Integer locBookingClient;
	
	// Opciones por defecto para los puestos del local
	
	protected String locName;
	
	protected WhereDTO locWhere;
		
	// Espacio de tiempo en minutos entre citas del local
	protected Integer locApoDuration;
	// Espacio de tiempo en minutos restringidos desde la hora actual para reservar de todos los puestos del local
	protected Integer locTimeRestricted;
	// Días desde hoy con agenda abierta de todos los puestos del local
	protected Integer locOpenDays;
	// Numero de personas por reserva aplicable al local
	protected Integer locNumPersonsApo;
	// Permite reservar multiple servicios por persona
	protected Integer locMulServices;
	// Permite seleccionar puesto en la reserva
	protected Integer locSelCalendar;
	// Numero de dias que un usuario puede volver a reservar aplicable al local
	protected Integer locNumUsuDays;
	/* Propiedades aplicables a los dias de la semana 
	  (Horarios de apertura semanal en horas) 
	  del local */
	protected SemanalDiaryDTO locSemanalDiary;
	// Tarea por defecto del local
	protected Long locTaskDefaultId;
	// Lista de idiomas del local
	protected List<LangDTO> locLangs;
	// Si por defecto, en la reserva, el cliente es nuevo o no
	protected Integer locNewClientDefault;
	// Preseleccionado siempre servicio por defecto u obligar a seleccionar servicios cada vez 
	protected Integer locCacheTasks;
	// Contacto del local, copias en email de citas
	protected ProfessionalDTO locRespon;
	// Recibir copia de correo al reservar el cliente
	protected Integer locMailBookign;
	// Sincronizar con Google Calendar
	protected SincroDTO locSinGCalendar;
	// Sincronizar con MailChimp
	protected SincroDTO locSinMChimp;
	
	public LocalDTO() {
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
	
	public WhereDTO getLocWhere() {
		return locWhere;
	}

	public void setLocWhere(WhereDTO locWhere) {
		this.locWhere = locWhere;
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

	public SemanalDiaryDTO getLocSemanalDiary() {
		return locSemanalDiary;
	}

	public void setLocSemanalDiary(SemanalDiaryDTO locSemanalDiary) {
		this.locSemanalDiary = locSemanalDiary;
	}

	public Long getLocTaskDefaultId() {
		return locTaskDefaultId;
	}

	public void setLocTaskDefaultId(Long locTaskDefaultId) {
		this.locTaskDefaultId = locTaskDefaultId;
	}
	
	public List<LangDTO> getLocLangs() {
		return locLangs;
	}

	public void setLocLangs(List<LangDTO> locLangs) {
		this.locLangs = locLangs;
	}

	public String getLocLocation() {
		return getLocWhere()!=null?getLocWhere().getWheAddress()+" - "+getLocWhere().getWheCity()+" - "+getLocWhere().getWheState():"";
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

	public ProfessionalDTO getLocRespon() {
		return locRespon;
	}

	public void setLocRespon(ProfessionalDTO locRespon) {
		this.locRespon = locRespon;
	}

	public Integer getLocMailBookign() {
		return locMailBookign;
	}

	public void setLocMailBookign(Integer locMailBookign) {
		this.locMailBookign = locMailBookign;
	}

	public SincroDTO getLocSinGCalendar() {
		return locSinGCalendar;
	}

	public void setLocSinGCalendar(SincroDTO locSinGCalendar) {
		this.locSinGCalendar = locSinGCalendar;
	}

	public SincroDTO getLocSinMChimp() {
		return locSinMChimp;
	}

	public void setLocSinMChimp(SincroDTO locSinMChimp) {
		this.locSinMChimp = locSinMChimp;
	}
	
	
}