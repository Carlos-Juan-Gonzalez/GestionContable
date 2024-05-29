package proyecto.contabilidad;

public class Anotacion {
    private int cuenta_id;
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
}
