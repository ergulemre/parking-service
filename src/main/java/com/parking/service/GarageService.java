package com.parking.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.parking.entity.Garage;
import com.parking.repository.GarageRepository;

@Service
public class GarageService {
	
	final static int parkingCapacity = 10;
	
	@Autowired
	public GarageRepository garageRepository;

	@Autowired
	private Executor executor;
	
    @Async
	public CompletableFuture<String> park(String command) {
    	
		String[] splitCommand = command.split(" ");
		String plateNo = splitCommand[1];
		String color = splitCommand[2];
		String type = splitCommand[3];

		int vehicleLength = lengthOfVehicle(type);
		
		List<Garage> availableSlots = garageRepository.findAvailableSlots();
		
		List<Integer> availableSlotList = new ArrayList<Integer>();
		for(Garage v : availableSlots) {
			availableSlotList.add(v.getId().intValue());
		}
		
		if(availableSlotList.isEmpty() || availableSlotList.get(0)+vehicleLength>10) {
			return CompletableFuture.completedFuture("Garage is full.");
		}
		if(!availableSlotList.isEmpty() && !availableSlotList.contains(Integer.valueOf(1))) {
			garageRepository.updateSlot(null, availableSlotList.get(0), null);
			availableSlotList.remove(0);
		}
		for(int i=1; i<vehicleLength+1; i++) {
			availableSlotList.sort(Comparator.naturalOrder());
            String slot = availableSlotList.get(0).toString();
			garageRepository.updateSlot(plateNo, Long.valueOf(slot), color);
            availableSlotList.remove(0);
		}
		return CompletableFuture.completedFuture("Allocated " + (vehicleLength==1 ? String.valueOf(vehicleLength) + " slot." : String.valueOf(vehicleLength) + " slots."));
		//return "Allocated " + (vehicleLength==1 ? String.valueOf(vehicleLength) + " slot." : String.valueOf(vehicleLength) + " slots.") ;	
    }
	
    
    public int lengthOfVehicle(String type) {
		if(type.equalsIgnoreCase("Car")) return 1;
		if(type.equalsIgnoreCase("Jeep")) return 2;
		if(type.equalsIgnoreCase("Truck")) return 4;
		return 0;
    }
    	
    @Async
    public CompletableFuture<String> garageStatus() {
    	List<Garage> vehicles = garageRepository.findAll();
    	
    	Map<String, List<String>> vehicleSlotMap = prepareAllVehicles(vehicles);
    	String statusResult = "Status:";
    	for(Map.Entry<String, List<String>> entry : vehicleSlotMap.entrySet()) {
    		if(entry.getKey()!=null) {
    			statusResult += "\r\n" +entry.getKey() +" " + entry.getValue();
    		}
    	}
    	return CompletableFuture.completedFuture(statusResult);
    }
    
    public Map<String, List<String>> prepareAllVehicles(List<Garage> vehicles) {
    	Map<String, List<String>> vehicleSlotMap = new LinkedHashMap<String, List<String>>();
    	for(Garage g : vehicles) {
    		if(g.getPlateNo()!=null) {
    			List<String> slotList = new ArrayList<String>();
    			if(vehicleSlotMap.containsKey(g.getPlateNo()+" "+g.getColor())) {
    				slotList = vehicleSlotMap.get(g.getPlateNo()+" "+g.getColor());
    			}
    			slotList.add(String.valueOf(g.getId()));
    			String obj = g.getPlateNo()+" "+g.getColor();
    			vehicleSlotMap.put(obj, slotList);		
    		}
    	}
    	return vehicleSlotMap;
    }
    
    public void leave(String item) {
    	List<Garage> vehicles = garageRepository.findAll();
    	Map<String, List<String>> sortedVehicleList = prepareAllVehicles(vehicles);
    	sortedVehicleList.remove(null);

    	String[] splitIndex = item.split(" ");
    	Integer index = Integer.valueOf(splitIndex[1]);
    	
    	if(sortedVehicleList.size()>=index) {
    		Object firstKey = sortedVehicleList.values().toArray()[index-1];
    		String strSlotList = firstKey.toString();
    		String requiredString = strSlotList.substring(strSlotList.indexOf("[") + 1, strSlotList.indexOf("]"));
    		String[] splitSlots = requiredString.split(",");
    		
    		//the slot which is broken empty deleted.
    		garageRepository.updateDeletedSlot(String.valueOf(Integer.valueOf(splitSlots[0])-1));

    		//update table to make slots available 
        	for(String s : splitSlots) {
        		garageRepository.updateDeletedSlot(s);
        	}
    	}    	    	
    }

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*Map<String, Vehicle> vehicleMap;
	Map<String, String> map2;
    Map<String, ArrayList<String>> map3;
    
	ArrayList<Integer> availableSlotList;
	
	public void park(String vehicleInfo) {
		
		Integer lengthOfVehicle = lengthOfVehicle(vehicleInfo);
		
		if(parkingCapacity==vehicleMap.size()) {
			System.out.println("Park is full.");
		}
		else {
			Collections.sort(availableSlotList);
			for(int i=0; i<lengthOfVehicle; i++) {
				String slot = availableSlotList.get(i).toString();
				Vehicle vehicle = new Vehicle(vehicleInfo);
				vehicleMap.put(slot, vehicle);
				
	            this.map2.put(vehicleInfo, slot);		            
	            availableSlotList.remove(i);
	        }
			System.out.println("Allocated slot number: " + slot);
            System.out.println();
				
		}
	}
	
	public Integer lengthOfVehicle(String vehicleInfos) {
		
		String[] infoArr = vehicleInfos.split(" ");
		if(infoArr[3]=="Car") return 1;
		if(infoArr[3]=="Jeep") return 2;
		if(infoArr[3]=="Truck") return 4;
		return 0;
	}*/
}
