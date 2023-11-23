package Classes;

public class Maquina {
    private Integer idMaquina;
    private String nomeMaquina;
    private Integer fkLanHouse;

    public Maquina(String nomeMaquina, Integer fkLanHouse) {
        this.nomeMaquina = nomeMaquina;
        this.fkLanHouse = fkLanHouse;
    }

    public Integer getIdMaquina() {
        return idMaquina;
    }

    public void setIdMaquina(Integer idMaquina) {
        this.idMaquina = idMaquina;
    }

    public String getNomeMaquina() {
        return nomeMaquina;
    }

    public void setNomeMaquina(String nomeMaquina) {
        this.nomeMaquina = nomeMaquina;
    }

    public Integer getFkLanHouse() {
        return fkLanHouse;
    }

    public void setFkLanHouse(Integer fkLanHouse) {
        this.fkLanHouse = fkLanHouse;
    }



    @Override
    public String toString() {
        return "\nMaquina{" +
                "id maquina=" + idMaquina +
                ", nome de maquina=" + nomeMaquina +
                ", Lan House=" + fkLanHouse +
                '}';
    }


}
