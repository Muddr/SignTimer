package com.gtdclan.signtimer.databases;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotNull;

@Entity()
@Table(name = "signtimer_stopsign")
public class DB_StopSign {
	
	@Id
	private int id;
	@NotNull
	private int TimerID;
	@NotNull
	private String sign_world;
	@NotNull
	private int sign_x;
	@NotNull
	private int sign_y;
	@NotNull
	private int sign_z;
	
	public int getId() {
		return this.id;
	}
	
	public String getSign_world() {
		return this.sign_world;
	}
	
	public int getSign_x() {
		return this.sign_x;
	}
	
	public int getSign_y() {
		return this.sign_y;
	}
	
	public int getSign_z() {
		return this.sign_z;
	}
	
	public int getTimerID() {
		return this.TimerID;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setSign_world(String sign_world) {
		this.sign_world = sign_world;
	}
	
	public void setSign_x(int sign_x) {
		this.sign_x = sign_x;
	}
	
	public void setSign_y(int sign_y) {
		this.sign_y = sign_y;
	}
	
	public void setSign_z(int sign_z) {
		this.sign_z = sign_z;
	}
	
	public void setTimerID(int timerID) {
		this.TimerID = timerID;
	}
	
}
