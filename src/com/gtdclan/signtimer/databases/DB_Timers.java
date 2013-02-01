package com.gtdclan.signtimer.databases;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotNull;

@Entity()
@Table(name = "signtimer_names")
public class DB_Timers {
	
	@Id
	private int id;
	@NotNull
	private String Timername;
	@NotNull
	private Boolean enabled;
	
	public Boolean getEnabled() {
		return this.enabled;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getTimername() {
		return this.Timername;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setTimername(String Timername) {
		this.Timername = Timername;
	}
}
