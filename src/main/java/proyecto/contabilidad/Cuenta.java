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