package Classes;

public class TipoComponente {
    private Integer idTipoComponente;
    private String tipoComponentecol;


    public Integer getIdTipoComponente() {
        return idTipoComponente;
    }

    public void setIdTipoComponente(Integer idTipoComponente) {
        this.idTipoComponente = idTipoComponente;
    }

    public String getTipoComponentecol() {
        return tipoComponentecol;
    }

    public void setTipoComponentecol(String tipoComponentecol) {
        this.tipoComponentecol = tipoComponentecol;
    }




    @Override
    public String toString() {
        return "\nTipo de Componente{" +
                "id Tipo de Componente=" + idTipoComponente +
                ", Tipo de Componente=" + tipoComponentecol+
                '}';
    }


}
