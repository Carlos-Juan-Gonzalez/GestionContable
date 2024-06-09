package proyecto.contabilidad;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase Modal de la tabla Usuario
 * @author Carlos Juan González
 */
public class Usuario {

    private int id;
    private int permiso_id;
    private int diario_id;
    private String nombre;
    private String contraseña;

    public Usuario() {

    }
    public Usuario(int id,int permiso_id,int diario_id, String nombre, String contraseña) {
        this.id = id;
        this.permiso_id = permiso_id;
        this.diario_id = diario_id;
        this.nombre = nombre;
        this.contraseña = contraseña;
    }
    public Usuario(int permiso_id,int diario_id, String nombre,String contraseña) {
        this.permiso_id = permiso_id;
        this.diario_id = diario_id;
        this.nombre = nombre;
        this.contraseña = contraseña;
    }


    /**
     * Cambia la contraseña inicial del administrador de la aplicacion
     * @param connection Connection: conexion con la base de datos
     * @param contraseña String: contraseña del usuario admin
     */
    public void cambiarContraseñaAdmin(Connection connection,String contraseña){

        try(Statement statement = connection.createStatement()){
            statement.execute("use gestorcontabledb;");
            statement.executeUpdate("update usuarios set contraseña = '"+ contraseña +"' where id = 1");
        }catch (SQLException e){
            System.out.println(e);
        }
    }

    /**
     * Comprueba que los datos pasados por parametros coinciden con los
     * guardados en la base de datos
     * @param conexion Connection: conexion con la base de datos
     * @param usuario String: nombre de usuario a comprobar
     * @param password String: contraseña a comprobar
     * @return boolean: true si los datos coincicen, false si no
     */
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

    /**
     * Ejecuta un insert en la tabla usuarios
     * @param connection Connection: conexion con la base de datos
     */
    public void insertUsuario(Connection connection){
        try(Statement statement = connection.createStatement()){
            statement.executeUpdate("insert into usuarios (permiso_id,diario_id,usuario,contraseña) values " +
                    "("+permiso_id+","+diario_id+",'"+nombre+"','"+contraseña+"')");
        }catch (SQLException e){
            System.out.println(e);
        }
    }

    /**
     * Crea mediante peticion a la base de datos una instancia de Usuario
     * @param connection Connection: conexion con la base de datos
     * @param user String: nombre de usuario a instanciar
     * @return Usuario: instancia de Usuario creada
     */
    public Usuario constructUsuario(Connection connection,String user){
        Usuario usuario;
        try (Statement statement = connection.createStatement()){

            ResultSet rs = statement.executeQuery("select * from usuarios where usuario = '"+user+"'");
            rs.next();

            usuario = new Usuario(rs.getInt(1),rs.getInt(2),rs.getInt(3),
                    rs.getString(4),rs.getString(5));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return usuario;
    }

    /**
     * Comprueba la existencia de un registro en la base de datos que
     * coincida con los valores pasados por parametros
     * @param connection Connection: conexion con la base de datos
     * @param user String: nombre del usuario
     * @param diario_id int: id de diario
     * @return boolean: true si existe, false si no
     */
    public boolean usuarioExiste(Connection connection,String user,int diario_id){
        try (Statement statement = connection.createStatement()){

            ResultSet rs = statement.executeQuery("select * from usuarios where usuario = '"+user+"' and diario_id = "+diario_id);

            if (rs.next()){
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * Comprueba la existencia de un registro con la contraseña igual
     * a la pasada por parametros
     * @param connection Connection: conexion con la base de datos
     * @param contraseña String: contraseña del usuario a comprobar
     * @return boolean: true si existe, false si no
     */
    public boolean contraseñaExiste(Connection connection,String contraseña){
        try (Statement statement = connection.createStatement()){

            ResultSet rs = statement.executeQuery("select * from usuarios where contraseña = '"+contraseña+"'");

            if (rs.next()){
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
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
