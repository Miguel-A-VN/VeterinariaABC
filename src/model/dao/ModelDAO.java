package model.dao;

import java.sql.SQLException;

import model.dto.PersonDTO;

public abstract class ModelDAO {

	public abstract Object query(String text) throws SQLException;

	public abstract boolean register(Object dto) throws SQLException;

}
