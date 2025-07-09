package model.connection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {

    public Properties properties = new Properties();
    private String nameDB = "";
    private String user = "";
    private String password = "";
    private String url = "";

    private Connection connection = null;
    private static ConnectionManager instance;

    private ConnectionManager() {
        connect();
    }

    private void loadCredentials() {
        try {
            properties.load(new FileInputStream("src/properties/archivo.properties"));
            nameDB = properties.getProperty("db.name_db");
            user = properties.getProperty("db.user");
            password = properties.getProperty("db.password");
            url = "jdbc:mysql://localhost:3306/" + nameDB + "?useUnicode=true&use"
                    + "JDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&"
                    + "serverTimezone=UTC";
        } catch (FileNotFoundException e) {
            System.out.println("Error, El archivo no existe");
        } catch (IOException e) {
            System.out.println("Error, No se puede leer el archivo");
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            loadCredentials();
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            if (connection != null) {
                System.out.println("Conexion Exitosa a la BD: " + nameDB);
            } else {
                System.out.println("**NO SE PUDO CONECTAR " + nameDB);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Ocurre una ClassNotFoundException : " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Ocurre una SQLException: " + e.getMessage());
            System.out.println("Verifique que Mysql esté encendido...");
        }
    }

    public static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar el estado de la conexión: " + e.getMessage());
        }
        return connection;
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Closed connection.");
            } catch (SQLException e) {
                System.out.println("Error closing the connection: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }
}