package proyecto.contabilidad;

import java.io.*;
import java.sql.*;


public class Conexion {
    final private String url = "jdbc:mysql://localhost:3306/";
    final private String user = "root";
    final private String password = "root";
    private Connection conexion;


    /**
     * Crea una conexion base al gestor de base de datos local
     * y lo settea al atributo de clase 'conexion'
     */
    public void setBaseConnection(){
        try {
            conexion = DriverManager.getConnection(url,user,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * crea una conexion con la base de datos con las credenciales
     * pasadas por parametros
     * @param user String: nombre de usuario
     * @param password String: contraseña del usuario
     * @return Connection: conexion establecida
     */
    public Connection setConnection(String user, String password){
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection((url+"gestorcontabledb"),user,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conexion;
    }

    /**
     * Comprueba si la base de datos esta creada
     * @return boolean: true si existe, false si no existe
     */
    public boolean isCreated(){
        try (Statement statement = conexion.createStatement()){
            ResultSet resultSet = statement.executeQuery("show databases like 'gestorcontabledb' ");

            // Comprobar si la base de datos existe
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println();
        }
        //Si falla devuelve false
        return false;
    }


    /**
     * Ejecuta el sql del archivo 'GestorContable.sql' para crear
     * las tablas e insertar el usuario admin y privilegios
     */
    public void createDB(){

        try(Statement statement = conexion.createStatement();) {
            File file = new File(Conexion.class.getResource("SQL/GestorContable.sql").toURI());
            FileReader lector = new FileReader(file);
            BufferedReader lectorBuffereado = new BufferedReader(lector);

            do {
                statement.executeUpdate(lectorBuffereado.readLine());
            }while(lectorBuffereado.ready());
            //cerramos los lectores
            lectorBuffereado.close();
            lector.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Ejecuta el sql del archivo 'DiarioPruebas.sql' para
     * insertar el contenido de el diario de pruebas
     */
    public void populatePruebaDB(){

        try(Statement statement = conexion.createStatement();) {
            File file = new File(Conexion.class.getResource("SQL/DiarioPruebas.sql").toURI());
            FileReader lector = new FileReader(file);
            BufferedReader lectorBuffereado = new BufferedReader(lector);

            do {
                statement.executeUpdate(lectorBuffereado.readLine());
            }while(lectorBuffereado.ready());
            //cerramos los lectores
            lectorBuffereado.close();
            lector.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean isFirstLoging(){
        try(Statement statement = conexion.createStatement()){
            statement.execute("use gestorcontabledb");
            ResultSet resultSet = (ResultSet) statement.executeQuery("select * from usuarios where contraseña = 'admin'");
            if(resultSet.next()){
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return true;
    }

    public void createNewUser(String usuario, String password){
        try(Statement statement = conexion.createStatement()) {
            statement.execute("use gestorcontabledb");
            statement.execute("create user if not exists '"+usuario+"'@'localhost' IDENTIFIED by '"+password+"';");
            statement.execute("grant all privileges on gestorContableDB.* to '"+usuario+"'@'localhost'");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }
}
