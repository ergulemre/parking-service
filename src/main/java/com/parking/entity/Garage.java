package com.parking.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "garage")
public class Garage {
	
    @Id
    @Column(name="id")
    private Long id;
    @Column(name="vehicle_plate_no")
    private String plateNo;
    @Column(name="available")
    private boolean available;
    @Column(name="color")
    private String color;
 
    public Garage()
    {
    }

    public Garage(Long id, String plateNo, boolean available)
    {
    	this.id = id;
        this.plateNo = plateNo;
        this.available = available;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}	
}
