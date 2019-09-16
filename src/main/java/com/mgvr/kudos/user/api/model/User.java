package com.mgvr.kudos.user.api.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

@Entity
public class User {
	@Id
	@GeneratedValue
	private int id;
	private String nickName;
	private String realName;
	@JsonIgnore
	private String email;
	@Transient
	private List<Kudo> kudos;

	public List<Kudo> getKudos() {
		return kudos;
	}

	public void setKudos(List<Kudo> kudos) {
		this.kudos = kudos;
	}

	public String getNickName() {
		return nickName;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getId() {
		return id;
	}
}
