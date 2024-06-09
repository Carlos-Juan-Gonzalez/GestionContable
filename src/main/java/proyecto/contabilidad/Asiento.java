package proyecto.contabilidad;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Modal de la tabla anotaciones
 * @author Carlos Juan González
 */
public class Asiento {

    private int id;
    private int diario_id;
    private int numero;
    private String fecha;
    private String descripcion;
    private int balance;

    public Asiento(){

    }
    public Asiento(int diario_id, int numero){
        this.diario_id = diario_id;
        this.numero = numero;
    }
    public Asiento(int id,int diario_id,int numero, String fecha, String descripcion,int balance) {
        this.id = id;
        this.diario_id = diario_id;
        this.numero = numero;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.balance = balance;
    }
    public Asiento(int id,int diario_id, int numero, String fecha, String descripcion) {
        this.id = id;
        this.diario_id = diario_id;
        this.numero = numero;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    /**
     * Crea mediante peticion a la base de datos una lista de asientos
     * que coincidan con el id pasado por parametros
     * @param connection Connection: conexion con la base de datos
     * @param id int: id del diario a filtrar
     * @return List<Asiento>: lista de asientos
     */
    public List<Asiento> constructAsientos(Connection connection,int id){
        List<Asiento> asientos = new ArrayList<Asiento>();
        try (Statement statement = connection.createStatement()){

            ResultSet rs = statement.executeQuery("select * from asientos where diario_id = "+id);
            while (rs.next()){
                asientos.add(new Asiento(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getString(4),
                        rs.getString(5),getBalanceAsiento(connection,rs.getInt(1))));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return asientos;
    }

    /**
     * Ejecuta un update en la tabla asientos cambiando los campos
     * fecha y descripcion
     * @param connection Connection: conexion con la base de datos
     */
    public void updateAsiento(Connection connection){
        try (Statement statement = connection.createStatement()){
            statement.executeUpdate("update asientos set fecha = '"+fecha+"', descripcion = '"+descripcion+"' where id = "+id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Ejecuta un delete en la tabla asientos
     * @param connection Connection: conexion con la base de datos
     */
    public void deleteAsiento(Connection connection){
        try (Statement statement = connection.createStatement()){
            statement.executeUpdate("delete from asientos where id = "+id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Ejecuta un insert en la tabla asientos
     * @param connection Connection: conexion con la base de datos
     */
    public void insertAsiento(Connection connection){
        try (Statement statement = connection.createStatement()){
             statement.executeUpdate("insert into asientos (diario_id,numero,fecha,descripcion) values " +
                    "("+diario_id+","+numero+",'"+fecha+"','"+descripcion+"')");
             ResultSet rs = statement.executeQuery("select id from asientos where numero = "+numero+" and fecha = '"+fecha+"'");

             while (rs.next()){
                 this.id = rs.getInt(1);
             }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calcula el balance del asiento con id pasado por parametros
     * @param connection Connection: conexion con la base de datos
     * @param id int: id a buscar
     * @return int: balance de la cuenta
     */
    public int getBalanceAsiento(Connection connection,int id){
        int balance = 0;
        try (Statement statement = connection.createStatement()){

            ResultSet rs = statement.executeQuery("select debe from anotaciones where asiento_id = "+id);

            while (rs.next()){
                if(rs.getInt(1) != 0){
                    balance += rs.getInt(1);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return balance;
    }

    public int getId(){ return id; }
    public void setId(int id){this.id = id;}
    public int getDiario_id() {return diario_id;}
    public void setDiario_id(int diario_id) {
        this.diario_id = diario_id;
    }
    public int getNumero() {
        return numero;
    }
    public void setNumero(int numero) {
        this.numero = numero;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public int getBalance() {
        return balance;
    }
    public void setBalance(int balance) {
        this.balance = balance;
    }


}
