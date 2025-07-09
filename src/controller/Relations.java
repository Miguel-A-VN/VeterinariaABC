package controller;

import model.dao.PetDAO;
import views.WindowMain;
import views.WindowPerson;
import views.WindowPet;
import model.connection.ConnectionDB;
import model.dao.PersonDAO;

public class Relations{
     
	public Relations(){
		
	WindowMain windowMain=new WindowMain();
	WindowPerson windowPersons = new WindowPerson();
	WindowPet windowPet = new WindowPet();
	
	PersonDAO myPersonDAO=new PersonDAO();
	PetDAO myPetDAO = new PetDAO();
     ConnectionDB myConnectionBD=new ConnectionDB();
     Coordinator myCoordinator = new Coordinator();
     
     windowMain.setCoordinator(myCoordinator);
     windowPersons.setCoordinator(myCoordinator);
     windowPet.setCoordinator(myCoordinator);
     
     myPersonDAO.setCoordinator(myCoordinator);
     myPetDAO.setCoordinator(myCoordinator);
     myConnectionBD.setCoordinator(myCoordinator);
     
     myCoordinator.setWinMain(windowMain);
     myCoordinator.setWinPersons(windowPersons);
     myCoordinator.setWinPets(windowPet);
     
     myCoordinator.setMyPersonDAO(myPersonDAO);
     myCoordinator.setMyPetDAO(myPetDAO);
     myCoordinator.setMyConnectionBD(myConnectionBD);
     
     myCoordinator.showWindowMain();;
	}
}
