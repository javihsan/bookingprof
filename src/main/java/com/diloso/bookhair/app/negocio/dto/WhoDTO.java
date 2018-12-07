package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;
import java.util.Date;

public class WhoDTO extends ResourceDTO implements Serializable {
	protected static final long serialVersionUID = 1L;

	protected String whoName;
	
	protected String whoSurname;
	
	protected String whoEmail;
	
	protected String whoTelf1;
	
	protected String whoTelf2;
	
	protected String whoDesc;
	// 0 MA - 1 FE
	protected Integer whoGender;
	
	protected Date whoBirthday;
	
	protected WhereDTO whoWhere;
	
	public WhoDTO() {
    }

	public String getWhoName() {
		return whoName;
	}

	public void setWhoName(String whoName) {
		this.whoName = whoName;
	}

	public String getWhoSurname() {
		return whoSurname;
	}

	public void setWhoSurname(String whoSurname) {
		this.whoSurname = whoSurname;
	}

	public String getWhoEmail() {
		return whoEmail;
	}

	public void setWhoEmail(String whoEmail) {
		this.whoEmail = whoEmail;
	}

	public String getWhoTelf1() {
		return whoTelf1;
	}

	public void setWhoTelf1(String whoTelf1) {
		this.whoTelf1 = whoTelf1;
	}

	public String getWhoTelf2() {
		return whoTelf2;
	}

	public void setWhoTelf2(String whoTelf2) {
		this.whoTelf2 = whoTelf2;
	}
	
	public String getWhoDesc() {
		return whoDesc;
	}

	public void setWhoDesc(String whoDesc) {
		this.whoDesc = whoDesc;
	}

	public Integer getWhoGender() {
		return whoGender;
	}

	public void setWhoGender(Integer whoGender) {
		this.whoGender = whoGender;
	}
	
	public Date getWhoBirthday() {
		return whoBirthday;
	}

	public void setWhoBirthday(Date whoBirthday) {
		this.whoBirthday = whoBirthday;
	}

	public WhereDTO getWhoWhere() {
		return whoWhere;
	}

	public void setWhoWhere(WhereDTO whoWhere) {
		this.whoWhere = whoWhere;
	}
	
	public String getWhoTelf() {
		String res = "";
		if (getWhoTelf1()!=null){
			res += getWhoTelf1();
		}
		if (getWhoTelf2()!=null){
			if (res.length()>=0){
				res += " - ";
			}
			res += getWhoTelf2();
		}
		return res;
	}
	
}