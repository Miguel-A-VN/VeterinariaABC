
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import controller.Coordinator;
import model.connection.ConnectionDB;
import model.connection.ConnectionManager;
import model.dto.PersonDTO;

public class PersonDAO extends ModelDAO {

    private ConnectionManager connectionManager = ConnectionManager.getInstance();
    private PreparedStatement preparedStatement = null;
    private Coordinator myCoordinator;

    @Override
    public boolean register(Object dto) throws SQLException {
        Connection connection = connectionManager.getConnection();
        try {
            if (dto instanceof PersonDTO) {
                PersonDTO person = (PersonDTO) dto;
                if (!ConnectionDB.personsMap.containsKey(person.getDocument())) {
                    String query = "INSERT INTO person(document, numberPhone, name) VALUES (?, ?, ?)";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, person.getDocument());
                    preparedStatement.setString(2, person.getNumberPhone());
                    preparedStatement.setString(3, person.getName());
                    preparedStatement.execute();
                    return true;
                } else {
                    return false;
                }
            } else {
                throw new IllegalArgumentException("No es un PersonDTO");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "Ya existe una persona con ese documento");
            return false;
        } catch (Exception e) {
            System.out.println("No se pudo registrar el dato: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            connectionManager.disconnect();
        }
    }

    public PersonDTO queryPerson(String document) throws SQLException {
        PersonDTO person = null;
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;
        String query = "SELECT document, numberPhone, name FROM person WHERE document = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, document);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                person = new PersonDTO();
                person.setDocument(resultSet.getString("document"));
                person.setNumberPhone(resultSet.getString("numberPhone"));
                person.setName(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error en la consulta: " + e.getMessage());
        } finally {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            connectionManager.disconnect();
        }

        return person;
    }

    public ArrayList<PersonDTO> queryPersonsList() {
        PreparedStatement statement = null;
        ResultSet result = null;
        Connection connection = connectionManager.getConnection();
        ArrayList<PersonDTO> personsList = new ArrayList<>();
        String query = "SELECT document, numberPhone, name FROM person";

        try {
            if (connection != null) {
                statement = connection.prepareStatement(query);
                result = statement.executeQuery();

                while (result.next()) {
                    PersonDTO person = new PersonDTO();
                    person.setDocument(result.getString("document"));
                    person.setNumberPhone(result.getString("numberPhone"));
                    person.setName(result.getString("name"));
                    personsList.add(person);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en la consulta de personas: " + e.getMessage());
        } finally {
            try {
                if (result != null) result.close();
                if (statement != null) statement.close();
                connectionManager.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return personsList;
    }

    public String updatePerson(PersonDTO person) throws SQLException {
        String resp = "";
        Connection connection = connectionManager.getConnection();
        String query = "UPDATE person SET name = ?, numberPhone = ? WHERE document = ?";
        try {
            if (connection != null) {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, person.getName());
                preparedStatement.setString(2, person.getNumberPhone());
                preparedStatement.setString(3, person.getDocument());
                int rows = preparedStatement.executeUpdate();
                resp = (rows > 0) ? "ok" : "no_update";
            }
        } catch (Exception e) {
            System.out.println(e);
            resp = "error";
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            connectionManager.disconnect();
        }
        return resp;
    }

    public String deletePerson(String document) throws SQLException {
        Connection connection = connectionManager.getConnection();
        try {
            String query = "DELETE FROM person WHERE document = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, document);
            int rows = preparedStatement.executeUpdate();
            return (rows > 0) ? "ok" : "no_delete";
        } catch (Exception e) {
            System.out.println(e);
            return "error";
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            connectionManager.disconnect();
        }
    }

    public void setCoordinator(Coordinator myCoordinator) {
        this.myCoordinator = myCoordinator;
    }

    @Override
    public Object query(String text) throws SQLException {
        return null;
    }
}