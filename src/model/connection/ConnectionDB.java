package model.connection;

import java.util.HashMap;

import controller.Coordinator;
import model.dto.PetDTO;
import model.dto.PersonDTO;

public class ConnectionDB {
	public static HashMap<String, PersonDTO> personsMap;
	public static HashMap<String, PetDTO> petsMap;
	private Coordinator myCoordinator;
	
	public ConnectionDB() {
		personsMap=new HashMap<String, PersonDTO>();
		petsMap = new HashMap<String, PetDTO>();
	}

	public void setCoordinator(Coordinator myCoordinator) {
		this.myCoordinator=myCoordinator;
	}

}
