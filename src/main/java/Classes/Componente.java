package Classes;

public class Componente {
    private Integer idComponente;
    private String nomeComponente;
    private Integer fkMaquina;
    private Integer fkTipoComponente;
    private Integer fkMetrica;

    public Integer getIdComponente(){
        return idComponente;
    }
    public void setIdComponente(Integer idComponente) {
        this.idComponente = idComponente;
    }


    public String getNomeComponente() {
        return nomeComponente;
    }

    public void setNomeComponente(String nomeComponente) {
        this.nomeComponente = nomeComponente;
    }


    public Integer getFkMaquina() {
        return fkMaquina;
    }

    public void setFkMaquina(Integer fkMaquina) {
        this.fkMaquina = fkMaquina;
    }



    public Integer getFkTipoComponente() {
        return fkTipoComponente;
    }

    public void setFkTipoComponente(Integer fkTipoComponente) {
        this.fkTipoComponente = fkTipoComponente;
    }


    public Integer getFkMetrica() {
        return fkMetrica;
    }

    public void setFkMetrica(Integer fkMetrica) {
        this.fkMetrica = fkMetrica;
    }


    @Override
    public String toString() {
        return "\nComponente{" +
                "id Componente=" + idComponente +
                ", nomeComponente=" + nomeComponente +
                ", fk Maquina=" + fkMaquina +
                ", fk TipoComponente=" + fkTipoComponente +
                ", fk Metrica=" + fkMetrica +
                '}';
    }


}
