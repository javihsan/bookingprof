package com.diloso.bookhair.app.persist.entities;

import java.util.List;

import com.diloso.bookhair.app.datastore.data.StorableWithModificationTimestamp;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * The persistent class for the LocalTask entity
 * 
 */
@Entity
@Cache
public class LocalTask extends StorableWithModificationTimestamp<Long> { 

	@Id
	protected Long id;
	
	@Index
	protected Integer enabled;
		
	protected Long lotLocalId;
	
	protected Long lotTaskId;
	
	protected String lotNameMulti;
	
	protected Integer lotTaskDuration;
	
	protected Integer lotTaskPost;
	
	protected Float lotTaskRate;
		
	protected List<Long> lotTaskCombiId;

	protected List<Integer> lotTaskCombiRes;
	
	protected Integer lotVisible;
	
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

}
