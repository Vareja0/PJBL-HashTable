public class Registro {
    private int codigo;
    private Registro proximo;

    public Registro(int codigo) {
        this.codigo = codigo;
        this.proximo = null;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Registro getProximo() {
        return proximo;
    }

    public void setProximo(Registro proximo) {
        this.proximo = proximo;
    }
}
