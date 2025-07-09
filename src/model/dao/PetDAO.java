package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import controller.Coordinator;
import model.dto.PetDTO;
import model.connection.ConnectionDB;
import model.connection.ConnectionManager;

public class PetDAO extends ModelDAO {

    private ConnectionManager connectionManager = ConnectionManager.getInstance();
    private PreparedStatement preparedStatement = null;
    private Coordinator myCoordinator;

    @Override
    public boolean register(Object dto) throws SQLException {
        Connection connection = connectionManager.getConnection();
        try {
            if (dto instanceof PetDTO) {
                PetDTO pet = (PetDTO) dto;
                if (!ConnectionDB.petsMap.containsKey(pet.getIdOwner())) {
                    String query = "INSERT INTO pet(id_owner, name, race, sex) VALUES (?, ?, ?, ?)";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, pet.getIdOwner());
                    preparedStatement.setString(2, pet.getName());
                    preparedStatement.setString(3, pet.getRace());
                    preparedStatement.setString(4, pet.getSex());
                    preparedStatement.execute();
                    return true;
                }
            } else {
                throw new IllegalArgumentException("Se esperaba un PetDTO");
            }
            return false;
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "No existe ningún dueño con ese documento");
            return false;
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            connectionManager.disconnect();
        }
    }

    @Override
    public Object query(String text) throws SQLException {
        PetDTO pet = null;
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;
        String query = "SELECT id_owner, name, race, sex FROM pet WHERE id_owner = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, text);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                pet = new PetDTO();
                pet.setIdOwner(resultSet.getString("id_owner"));
                pet.setName(resultSet.getString("name"));
                pet.setRace(resultSet.getString("race"));
                pet.setSex(resultSet.getString("sex"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Hay un error en la consulta: " + e.getMessage());
        } finally {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            connectionManager.disconnect();
        }

        return pet;
    }

    public String deletePet(String idOwner, String namePet) {
        PreparedStatement preparedStatement = null;
        String resp = "";
        Connection connection = connectionManager.getConnection();

        try {
            if (connection != null) {
                String query = "DELETE FROM pet WHERE id_owner = ? AND name = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, idOwner);
                preparedStatement.setString(2, namePet);

                int rows = preparedStatement.executeUpdate();
                resp = (rows > 0) ? "ok" : "no_delete";
            }
        } catch (Exception e) {
            System.out.println(e);
            resp = "error";
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                connectionManager.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return resp;
    }

    public String updatePet(PetDTO pet, String originalName) {
        PreparedStatement preparedStatement = null;
        String resp = "";
        Connection connection = connectionManager.getConnection();

        try {
            if (connection != null) {
                String query = "UPDATE pet SET name = ?, race = ?, sex = ? WHERE id_owner = ? AND name = ?";
                preparedStatement = connection.prepareStatement(query);

                preparedStatement.setString(1, pet.getName());
                preparedStatement.setString(2, pet.getRace());
                preparedStatement.setString(3, pet.getSex());
                preparedStatement.setString(4, pet.getIdOwner());
                preparedStatement.setString(5, originalName);
                int rows = preparedStatement.executeUpdate();
                resp = (rows > 0) ? "ok" : "no_update";
            }
        } catch (Exception e) {
            System.out.println(e);
            resp = "error";
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                connectionManager.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return resp;
    }

    public ArrayList<PetDTO> queryPets(String idOwner) throws SQLException {
        ArrayList<PetDTO> list = new ArrayList<>();
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        String query = "SELECT p.name AS name_owner, p.document AS document, m.name AS name_pet, m.race AS race, m.sex AS sex " +
                "FROM pet m JOIN person p ON m.id_owner = p.document";
        if (idOwner != null && !idOwner.isEmpty()) {
            query += " WHERE m.id_owner = ?";
        }

        try {
            preparedStatement = connection.prepareStatement(query);

            if (idOwner != null && !idOwner.isEmpty()) {
                preparedStatement.setString(1, idOwner);
            }

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                PetDTO pet = new PetDTO();
                pet.setNameOwner(resultSet.getString("name_owner"));
                pet.setIdOwner(resultSet.getString("document"));
                pet.setName(resultSet.getString("name_pet"));
                pet.setRace(resultSet.getString("race"));
                pet.setSex(resultSet.getString("sex"));
                list.add(pet);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            connectionManager.disconnect();
        }

        return list;
    }

    public void setCoordinator(Coordinator myCoordinator) {
        this.myCoordinator = myCoordinator;
    }
}