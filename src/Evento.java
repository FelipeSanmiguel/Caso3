public class Evento {

    private String id;
    private int tipo;
    private boolean fin;

    public Evento(String id, int tipo, boolean fin) {
        this.id = id;
        this.tipo = tipo;
        this.fin = fin;
    }

    public boolean getFin() {
        return fin;
    }

    public String getId() {
        return id;
    }

    public int getTipo() {
        return tipo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setFin(boolean fin) {
        this.fin = fin;
    }

    /*
    Asi no la puedo embarrar al crear un enento fin y static para no tener que crear
    un nuevo objeto en la otra clase
    */
    public static Evento crearFin() {
        return new Evento("FIN", -1, true);
    }

    
}