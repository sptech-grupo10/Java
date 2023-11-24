package Classes;

public class TipoComponente {
    private Integer idTipoComponente;
    private String tipoComponente;

    public TipoComponente(String tipoComponente) {
        this.tipoComponente = tipoComponente;
    }

    public Integer getIdTipoComponente() {
        return idTipoComponente;
    }

    public void setIdTipoComponente(Integer idTipoComponente) {
        this.idTipoComponente = idTipoComponente;
    }

    public String getTipoComponente() {
        return tipoComponente;
    }

    public void setTipoComponente(String tipoComponentecol) {
        this.tipoComponente = tipoComponente;
    }




    @Override
    public String toString() {
        return "\nTipo de Componente{" +
                "id Tipo de Componente=" + idTipoComponente +
                ", Tipo de Componente=" + tipoComponente+
                '}';
    }


}
