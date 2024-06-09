package proyecto.contabilidad;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Modal de la tabla diarios
 * @author Carlos Juan González
 */
public class Diario {

    private int id;
    private String nombre;

    public Diario() {

    }
    public Diario(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    public Diario(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Crea mediante peticion a la base de datos una instancia de Diario
     * @param connection Connection: conexion con la base de datos
     * @param diario_id int: id del diario a filtrar
     * @return Diario: diario
     */
    public Diario constructDiario(Connection connection,int diario_id){
        Diario diario;
        try (Statement statement = connection.createStatement()){

            ResultSet rs = statement.executeQuery("select * from diarios where id = "+diario_id);
            rs.next();

            diario = new Diario(rs.getInt(1),rs.getString(2));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return diario;
    }
    /**
     * Crea mediante peticion a la base de datos una lista de nombres de diarios
     * @param connection Connection: conexion con la base de datos
     * @return List<String>: lista de nombres
     */
    public List<String> constructDiarios(Connection connection){
        List<String> diarios = new ArrayList<>();
        try (Statement statement = connection.createStatement()){

            ResultSet rs = statement.executeQuery("select nombre from diarios");
            while (rs.next()){
              diarios.add(rs.getString(1));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return diarios;
    }

    /**
     * Busca en la base de datos el id del diario asociado al nombre pasado por parametros
     * @param connection Connection: conexion con la base de datos
     * @param nombre String: nombre del diario
     * @return int: id del diario
     */
    public int getDiarioIDByName(Connection connection,String nombre){
        int id = 0;
        try (Statement statement = connection.createStatement()){

            ResultSet rs = statement.executeQuery("select id from diarios where nombre = '"+nombre+"'");
            while (rs.next()){
                id = rs.getInt(1);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    /**
     * Ejecuta un insert en la tabla asientos
     * @param connection Connection: conexion con la base de datos
     */
    public void insertDiario(Connection connection){
        try (Statement statement = connection.createStatement()){

            statement.executeUpdate("insert into diarios (nombre) values ('"+nombre+"')");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
