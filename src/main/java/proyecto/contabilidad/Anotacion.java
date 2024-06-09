package proyecto.contabilidad;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Modal de la tabla anotaciones
 * @author Carlos Juan González
 */
public class Anotacion {
    private int id;
    private int cuenta_id;
    private int codigo_cuenta;
    private int orden;
    private String nombre_cuenta;
    private int asiento_id;
    private int debe;
    private int haber;

    public Anotacion() {

    }
    public Anotacion(int cuenta_id, int asiento_id, int haber, int debe) {
        this.cuenta_id = cuenta_id;
        this.asiento_id = asiento_id;
        this.haber = haber;
        this.debe = debe;
    }
    public Anotacion(int id,int cuenta_id,int debe, int haber,int orden,int codigo_cuenta, String nombre_cuenta) {
        this.id = id;
        this.cuenta_id = cuenta_id;
        this.codigo_cuenta = codigo_cuenta;
        this.nombre_cuenta = nombre_cuenta;
        this.orden = orden;
        this.debe = debe;
        this.haber = haber;
    }

    @Override
    public String toString() {
        return "Anotacion{" +
                "id=" + id +
                ", cuenta_id=" + cuenta_id +
                ", codigo_cuenta=" + codigo_cuenta +
                ", orden=" + orden +
                ", nombre_cuenta='" + nombre_cuenta + '\'' +
                ", asiento_id=" + asiento_id +
                ", debe=" + debe +
                ", haber=" + haber +
                '}';
    }

    /**
     * Crea mediante peticion a la base de datos una lista de anotaciones
     * que coincidan con el id pasado por parametros
     * @param connection Connection: conexion con la base de datos
     * @param id int: id del asiento a filtrar
     * @return
     */
    public List<Anotacion> constructAnotaciones(Connection connection, int id){
        List<Anotacion> anotaciones = new ArrayList<Anotacion>();
        try (Statement statement = connection.createStatement()){

            ResultSet rs = statement.executeQuery("select anotaciones.id, anotaciones.cuenta_id, anotaciones.debe, " +
                    "anotaciones.haber,anotaciones.orden, cuentas.codigo,cuentas.nombre from anotaciones inner join" +
                    " cuentas on anotaciones.cuenta_id = cuentas.id where anotaciones.asiento_id = "+id);

            while (rs.next()){
                anotaciones.add(new Anotacion(rs.getInt(1),rs.getInt(2),rs.getInt(3),
                        rs.getInt(4),rs.getInt(5),rs.getInt(6),rs.getString(7)));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return anotaciones;
    }

    /**
     * Ejecuta un update en la tabla anotaciones cambiando los campos
     * cuenta_id,debe y haber
     * @param connection Connection: conexion con la base de datos
     */
    public void updateAnotacion(Connection connection){
        try (Statement statement = connection.createStatement()){
            statement.executeUpdate("update anotaciones set cuenta_id = "+cuenta_id+", debe = "+debe+", haber = "+haber+ " where id = " +id);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Ejecuta un insert en la tabla anotaciones
     * @param connection Connection: conexion con la base de datos
     * @param id int: id de asiento
     */
    public void insertAnotacion(Connection connection,int id){
        try (Statement statement = connection.createStatement()){
            statement.execute("insert into anotaciones (cuenta_id,asiento_id,orden,debe,haber) values " +
                    "("+cuenta_id+","+id+","+orden+","+debe+","+haber+")");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Ejecuta un delete en la tabla anotaciones
     * @param connection Connection: conexion con la base de datos
     */
    public void deleteAnotacion(Connection connection){
        try (Statement statement = connection.createStatement()){
            statement.executeUpdate("delete from anotaciones where orden = " + orden);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Comprueba la existencia de un registro en la base de datos
     * @param connection Connection: conexion con la base de datos
     * @return boolean: true si existe, false si no
     */
    public boolean exists(Connection connection){
        try (Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery("select * from cuentas where id = "+ id);

            if (rs.next()){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    public int getCuenta_id() {
        return cuenta_id;
    }

    public void setCuenta_id(int cuenta_id) {
        this.cuenta_id = cuenta_id;
    }

    public int getAsiento_id() {
        return asiento_id;
    }

    public void setAsiento_id(int asiento_id) {
        this.asiento_id = asiento_id;
    }

    public int getDebe() {
        return debe;
    }

    public void setDebe(int debe) {
        this.debe = debe;
    }

    public int getHaber() {
        return haber;
    }

    public void setHaber(int haber) {
        this.haber = haber;
    }

    public String getNombre_cuenta() {
        return nombre_cuenta;
    }

    public void setNombre_cuenta(String nombre_cuenta) {
        this.nombre_cuenta = nombre_cuenta;
    }

    public int getCodigo_cuenta() {
        return codigo_cuenta;
    }

    public void setCodigo_cuenta(int codigo_cuenta) {
        this.codigo_cuenta = codigo_cuenta;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }
}
