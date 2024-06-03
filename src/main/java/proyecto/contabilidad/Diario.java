package proyecto.contabilidad;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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
