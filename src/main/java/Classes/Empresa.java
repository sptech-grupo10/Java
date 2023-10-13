package Classes;

public class Empresa {
    private Integer idEmpresa;
    private Integer cnpj;
    private String razaoSocial;
    private String email;
    private Integer fkEndereco;
    private Integer fkRepresentante;


    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }


    public Integer getCnpj() {
        return cnpj;
    }

    public void setCnpj(Integer cnpj) {
        this.cnpj = cnpj;
    }


    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
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

    @Override
    public String toString() {
        return "\nComponente{" +
                "id Empresa=" + idEmpresa +
                ", cnpj=" + cnpj +
                ", razão social=" + razaoSocial +
                ", email=" + email +
                ", fk endereço =" + fkEndereco +
                ", fk representante =" +  +fkRepresentante +
                '}';
    }
}
