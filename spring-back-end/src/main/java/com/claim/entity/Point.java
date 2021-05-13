package com.claim.entity;

import javax.persistence.*;

@Entity
@Table(name="points")
public class Point {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column
	private String pointName;
	@Column
	private String Latitude;
	@Column
	private String Longitude;
	@Column
	private String elevation;
	@Column
	private String code;
	@Column
	private String attributes;
	@OneToOne
	private UploadFiles file;
	
	public String getPointName() {
		return pointName;
	}
	public void setPointName(String pointName) {
		this.pointName = pointName;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	public String getElevation() {
		return elevation;
	}
	public void setElevation(String elevation) {
		this.elevation = elevation;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAttributes() {
		return attributes;
	}
	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public UploadFiles getFile() {
		return file;
	}
	public void setFile(UploadFiles file) {
		this.file = file;
	}
	
	

}
