package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;
import java.util.List;


public class LocalTaskDTO implements Serializable {

	protected static final long serialVersionUID = 1L;
	
	protected Long id;
	
	protected Integer enabled;
	
	// Local
	protected Long lotLocalId;
	
	// Servicio
	protected Long lotTaskId;
	
	// Nombre del servicio del local en multiIdioma
	protected String lotNameMulti;
	
	// Duración en minutos del servicio del local
	protected Integer lotTaskDuration;
	
	// Duración en minutos del espacio extra posterior al servicio del local que no ocupa tiempo del calendario
	// Contabilizar para calcular si tiene hueco en el horario del Calendar (la fecha de cierre del Calendar)
	// Solo aplicable cuando el servicio es seleccionado en modo individual, o es el último en uno combinado
	protected Integer lotTaskPost;
	
	// Tarifa aplicada
	protected Float lotTaskRate;
	
	// Lista de servicios del local a combinar
	protected List<Long> lotTaskCombiId;
	
	// Duración en minutos en medio (uno por cada dos) de la lista de servicio del local a combinar
	protected List<Integer> lotTaskCombiRes;
	
	// Es servicio invisible para el cliente
	protected Integer lotVisible;
	
	// Nombre del servicio del local, solo para mostrar, no se guarda
	protected String lotName;
	
	// Es servicio del local default, solo para mostrar, no se guarda
	protected Integer lotDefault;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public Long getLotLocalId() {
		return lotLocalId;
	}

	public void setLotLocalId(Long lotLocalId) {
		this.lotLocalId = lotLocalId;
	}

	public Long getLotTaskId() {
		return lotTaskId;
	}

	public void setLotTaskId(Long lotTaskId) {
		this.lotTaskId = lotTaskId;
	}

	public String getLotNameMulti() {
		return lotNameMulti;
	}

	public void setLotNameMulti(String lotNameMulti) {
		this.lotNameMulti = lotNameMulti;
	}
	
	public Integer getLotTaskDuration() {
		return lotTaskDuration;
	}

	public void setLotTaskDuration(Integer lotTaskDuration) {
		this.lotTaskDuration = lotTaskDuration;
	}
	
	public Integer getLotTaskPost() {
		return lotTaskPost;
	}

	public void setLotTaskPost(Integer lotTaskPost) {
		this.lotTaskPost = lotTaskPost;
	}
	
	public Float getLotTaskRate() {
		return lotTaskRate;
	}

	public void setLotTaskRate(Float lotTaskRate) {
		this.lotTaskRate = lotTaskRate;
	}

	public List<Long> getLotTaskCombiId() {
		return lotTaskCombiId;
	}

	public void setLotTaskCombiId(List<Long> lotTaskCombiId) {
		this.lotTaskCombiId = lotTaskCombiId;
	}

	public List<Integer> getLotTaskCombiRes() {
		return lotTaskCombiRes;
	}

	public void setLotTaskCombiRes(List<Integer> lotTaskCombiRes) {
		this.lotTaskCombiRes = lotTaskCombiRes;
	}
	
	public Integer getLotVisible() {
		return lotVisible;
	}

	public void setLotVisible(Integer lotVisible) {
		this.lotVisible = lotVisible;
	}

	public String getLotName() {
		return lotName;
	}

	public void setLotName(String lotName) {
		this.lotName = lotName;
	}

	public Integer getLotDefault() {
		return lotDefault;
	}

	public void setLotDefault(Integer lotDefault) {
		this.lotDefault = lotDefault;
	}
	
	
}
