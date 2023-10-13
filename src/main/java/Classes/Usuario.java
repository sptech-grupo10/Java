package Classes;

public class Usuario {
    private Integer idUsuario;
    private String nome;
    private String sobrenome;
    private String situacao;
    private String email;
    private  String senha;
    private Integer fkLanHouse;
    private Integer fkEmpresa;
    private Integer fkTipoUsuario;


    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }


    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


    public Integer getFkLanHouse() {
        return fkLanHouse;
    }

    public void setFkLanHouse(Integer fkLanHouse) {
        this.fkLanHouse = fkLanHouse;
    }


    public Integer getFkEmpresa() {
        return fkEmpresa;
    }

    public void setFkEmpresa(Integer fkEmpresa) {
        this.fkEmpresa = fkEmpresa;
    }


    public Integer getFkTipoUsuario() {
        return fkTipoUsuario;
    }

    public void setFkTipoUsuario(Integer fkTipoUsuario) {
        this.fkTipoUsuario = fkTipoUsuario;
    }


    @Override
    public String toString() {
        return "\nUsuario{" +
                "id usuario=" + idUsuario +
                ", nome=" + nome +
                ", sobrenome=" + sobrenome +
                ", situação=" + situacao +
                ", email=" + email +
                ", senha="+"Salva"+
                ", fk LanHouse=" + fkLanHouse +
                ", fk empresa =" + fkEmpresa +
                ", fk tipo de usuario=" + fkTipoUsuario +
                '}';
    }



}
