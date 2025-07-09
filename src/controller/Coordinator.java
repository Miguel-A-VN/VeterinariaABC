package controller;

import java.sql.SQLException;
import java.util.ArrayList;

import model.connection.ConnectionDB;
import model.dao.PetDAO;
import model.dao.PersonDAO;
import model.dto.PetDTO;
import model.dto.PersonDTO;

import views.WindowMain;
import views.WindowPerson;
import views.WindowPet;

public class Coordinator {

    private WindowMain winMain;
    private WindowPet winPet;
    private WindowPerson winPerson;

    private PersonDAO myPersonDAO;
    private PetDAO myPetDAO;
    private ConnectionDB myConnectionBD;

    public void setWinMain(WindowMain winMain) {
        this.winMain = winMain;
    }

    public void setWinPets(WindowPet winPet) {
        this.winPet = winPet;
    }

    public void setWinPersons(WindowPerson winPerson) {
        this.winPerson = winPerson;
    }

    public void setMyPersonDAO(PersonDAO myPersonDAO) {
        this.myPersonDAO = myPersonDAO;
    }

    public void setMyPetDAO(PetDAO myPetDAO) {
        this.myPetDAO = myPetDAO;
    }

    public void setMyConnectionBD(ConnectionDB myConnectionBD) {
        this.myConnectionBD = myConnectionBD;
    }

    public void showWindowMain() {
        winMain.setVisible(true);
    }

    public void showWindowPets() {
        winPet.setVisible(true);
    }

    public void showWindowPersons() {
        winPerson.setVisible(true);
    }

    public boolean registerPerson(PersonDTO person) throws SQLException {
        return myPersonDAO.register(person);
    }

    public boolean registerPet(PetDTO pet) throws SQLException {
        return myPetDAO.register(pet);
    }

    public String deletePerson(String document) throws SQLException {
        return myPersonDAO.deletePerson(document);
    }

    public String deletePet(String idOwner, String namePet) {
        return myPetDAO.deletePet(idOwner, namePet);
    }

    public String updatePerson(PersonDTO newPerson) throws SQLException {
        return myPersonDAO.updatePerson(newPerson);
    }

    public String updatePet(PetDTO newPet, String originalName) {
        return myPetDAO.updatePet(newPet, originalName);
    }

    public ArrayList<PetDTO> queryPets(String idOwner) throws SQLException {
        return myPetDAO.queryPets(idOwner);
    }

    public PersonDTO queryPerson(String document) throws SQLException {
        return myPersonDAO.queryPerson(document);
    }

    public ArrayList<PersonDTO> queryPersonsList() {
        return myPersonDAO.queryPersonsList();
    }
}