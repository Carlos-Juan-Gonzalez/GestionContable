package proyecto.contabilidad;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {
    private int id;
    private int codigo;
    private String nombre;

    public Cuenta(){

    }
    public Cuenta(int id,int codigo, String nombre) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
    }
    public Cuenta(int codigo, String nombre) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public List<Cuenta> constructCuentas(Connection connection, int id){
        List<Cuenta> cuentas = new ArrayList<Cuenta>();
        try (Statement statement = connection.createStatement()){

            ResultSet rs = statement.executeQuery("select id,codigo,nombre from cuentas where diario_id="+id);

            while (rs.next()){
                cuentas.add(new Cuenta(rs.getInt(1),rs.getInt(2), rs.getString(3)));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return cuentas;
    }

    public void insertCuenta(Connection connection, int id){
        try (Statement statement = connection.createStatement()){

            statement.executeUpdate("insert into cuentas (diario_id,codigo,nombre) values ("+id+","+codigo+",'"+nombre+"')");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public int getCuentaID(Connection connection, int codigo){
        int id = 0;
        try (Statement statement = connection.createStatement()){

            ResultSet rs = statement.executeQuery("select id from cuentas where codigo ="+ codigo);

            while (rs.next()){
                id = rs.getInt(1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    public boolean cuentaExiste(Connection connection, int diario_id,int codigo){
        try (Statement statement = connection.createStatement()){

            ResultSet rs = statement.executeQuery("select * from cuentas where diario_id="+diario_id+" and codigo = " +codigo);

            if (rs.next()){
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return String.valueOf(codigo);
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
