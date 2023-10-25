package Classes;

public class LanHouse {
    private Integer idLanHouse;
    private Integer cnpj;
    private String unidade;
    private String email;
    private Integer fkEndereco;
    private Integer fkRepresentante;
    private Integer fkEmpresa;


    public Integer getIdLanHouse() {
        return idLanHouse;
    }

    public void setIdLanHouse(Integer idLanHouse) {
        this.idLanHouse = idLanHouse;
    }


    public Integer getCnpj() {
        return cnpj;
    }

    public void setCnpj(Integer cnpj) {
        this.cnpj = cnpj;
    }


    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Integer getFkEndereco() {
        return fkEndereco;
    }

    public void setFkEndereco(Integer fkEndereco) {
        this.fkEndereco = fkEndereco;
    }


    public Integer getFkRepresentante() {
        return fkRepresentante;
    }

    public void setFkRepresentante(Integer fkRepresentante) {
        this.fkRepresentante = fkRepresentante;
    }


    public Integer getFkEmpresa() {
        return fkEmpresa;
    }

    public void setFkEmpresa(Integer fkEmpresa) {
        this.fkEmpresa = fkEmpresa;
    }

    @Override
    public String toString() {
        return "\nLanHouse{" +
                "id LanHouse=" + idLanHouse +
                ", cnpj=" + cnpj +
                ", unidade=" + unidade +
                ", email=" + email +
                ", fk endereço =" + fkEndereco +
                ", fk representante =" +  fkRepresentante +
                ", fk empresa =" +  fkEmpresa +
                '}';
    }

}
