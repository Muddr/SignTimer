package com.gtdclan.signtimer;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotNull;

@Entity()
@Table(name = "signtimer_data")
public class DB {
	
	@Id
	private int id;
	@NotNull
	private String Playername;
	@NotNull
	private long Time;
	
	public int getId() {
		return this.id;
	}
	
	public String getPlayername() {
		return this.Playername;
	}
	
	public long getTime() {
		return this.Time;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setPlayername(String Playername) {
		this.Playername = Playername;
	}
	
	public void setTime(long Time) {
		this.Time = Time;
	}
}
