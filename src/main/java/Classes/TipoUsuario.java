package Classes;

public class TipoUsuario {
    private Integer idTipoUsuario;
    private String descTipoUsuario;



    public Integer getIdTipoUsuario() {
        return idTipoUsuario;
    }

    public void setIdTipoUsuario(Integer idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    public String getDescTipoUsuario() {
        return descTipoUsuario;
    }

    public void setDescTipoUsuario(String descTipoUsuario) {
        this.descTipoUsuario = descTipoUsuario;
    }




    @Override
    public String toString() {
        return "\nLivro{" +
                "id Tipo de uduario=" + idTipoUsuario +
                ", descrição do Tipo de Usuario=" + descTipoUsuario+
                '}';
    }
}
