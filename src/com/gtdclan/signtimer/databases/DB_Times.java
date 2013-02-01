package com.gtdclan.signtimer.databases;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotNull;

@Entity()
@Table(name = "signtimer_data")
public class DB_Times {
	
	@Id
	private int id;
	@NotNull
	private String Playername;
	@NotNull
	private int TimerID;
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
	
	public int getTimerID() {
		return this.TimerID;
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
	
	public void setTimerID(int timerID) {
		this.TimerID = timerID;
	}
}
