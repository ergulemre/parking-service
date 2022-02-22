package com.parking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.parking.entity.Garage;

@Repository
@Transactional
public interface GarageRepository extends JpaRepository<Garage, Long> {
	
	@Query(value = "select * from garage g where g.available = true", nativeQuery = true)
    List<Garage> findAvailableSlots();
	
	@Modifying
	@Query(value = "update garage set available=false , vehicle_plate_no=:plateNo, color=:color where id=:id", nativeQuery = true)
    public void updateSlot(@Param("plateNo") String plateNo , @Param("id") long id, @Param("color") String color);
	
	@Modifying
	@Query(value="update garage set available=true,vehicle_plate_no=null where id=:id", nativeQuery=true)
	public void updateDeletedSlot(@Param("id") String id);
}
