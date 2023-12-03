package Classes;

public class Componente extends TipoComponente {
    private Integer idComponente;
    private String nomeComponente;
    private Integer fkMaquina;
    private Integer fkTipoComponente;
    private Integer fkMetrica;

    public Componente(String tipoComponente, Integer idComponente, String nomeComponente, Integer fkMaquina, Integer fkTipoComponente, Integer fkMetrica) {
        super(tipoComponente);
        this.idComponente = idComponente;
        this.nomeComponente = nomeComponente;
        this.fkMaquina = fkMaquina;
        this.fkTipoComponente = fkTipoComponente;
        this.fkMetrica = fkMetrica;
    }

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
        return "Componente{" +
                "idComponente=" + idComponente +
                ", nomeComponente='" + nomeComponente + '\'' +
                ", fkMaquina=" + fkMaquina +
                ", fkTipoComponente=" + fkTipoComponente +
                ", fkMetrica=" + fkMetrica +
                '}';
    }


}
