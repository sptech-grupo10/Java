package Classes;

public class Maquina {
    Integer idMaquina;
    String nomeMaquina;
    Integer fkSala;
    Integer fkLanHouse;


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

    public Integer getFkSala() {
        return fkSala;
    }

    public void setFkSala(Integer fkSala) {
        this.fkSala = fkSala;
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
                ", sala=" + fkSala +
                ", Lan House=" + fkLanHouse +
                '}';
    }


}
