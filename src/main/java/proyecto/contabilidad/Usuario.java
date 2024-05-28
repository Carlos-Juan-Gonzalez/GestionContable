package proyecto.contabilidad;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Usuario {

    private int id;
    private int permiso_id;
    private int diario_id;
    private String nombre;
    private String contraseña;

    public Usuario() {

    }
    public Usuario(int permiso_id, int id, int diario_id, String nombre, String contraseña) {
        this.permiso_id = permiso_id;
        this.id = id;
        this.diario_id = diario_id;
        this.nombre = nombre;
        this.contraseña = contraseña;
    }


    public void cambiarContraseñaAdmin(Connection connection,String contraseña){

        try(Statement statement = connection.createStatement()){
            statement.execute("use gestorcontabledb;");
            statement.executeUpdate("update usuarios set contraseña = '"+ contraseña +"' where id = 1");
        }catch (SQLException e){
            System.out.println(e);
        }
    }

    public boolean validarLogin(Connection conexion,String usuario,String password){
        try(Statement statement = conexion.createStatement()){
            statement.execute("use gestorcontabledb;");
            ResultSet resultSet = statement.executeQuery("select * from usuarios where usuario = '"+usuario+"' and contraseña = '"+password+"'");
            if (resultSet.next()){
                return true;
            }else {
                return false;
            }
        }catch (SQLException e){
            System.out.println(e);
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPermiso_id() {
        return permiso_id;
    }

    public void setPermiso_id(int permiso_id) {
        this.permiso_id = permiso_id;
    }

    public int getDiario_id() {
        return diario_id;
    }

    public void setDiario_id(int diario_id) {
        this.diario_id = diario_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}
