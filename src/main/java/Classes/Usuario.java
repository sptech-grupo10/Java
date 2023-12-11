package Classes;

public class Usuario extends TipoUsuario {
    private Integer idUsuario;
    private String nome;
    private String sobrenome;
    private String situacao;
    private String email;
    private String senha;
    private Integer fkLanHouse;
    private Integer fkEmpresa;
    private Integer fkTipoUsuario;

    public Usuario(Integer idUsuario, String nome, String sobrenome, String situacao, String email, String senha, Integer fkLanHouse, Integer fkEmpresa, Integer fkTipoUsuario) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.situacao = situacao;
        this.email = email;
        this.senha = senha;
        this.fkLanHouse = fkLanHouse;
        this.fkEmpresa = fkEmpresa;
        this.fkTipoUsuario = fkTipoUsuario;
    }

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
    }

    public String getSenha() {
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

    public boolean senhaSegura() {
        return senha.length() >= 8 && senha.matches(".*\\d.*") && senha.matches(".*[!@#$%^&*()].*");
    }

    public void alterarSenha(String novaSenha) {
        if (senhaSegura(novaSenha)) {
            this.senha = novaSenha;
            System.out.println("Senha alterada com sucesso!");
        } else {
            System.out.println("A nova senha não atende aos critérios de segurança.");
        }
    }

    @Override
    public String toString() {
        return "\nUsuario{" +
                "idUsuario=" + idUsuario +
                ", nome=" + nome +
                ", sobrenome=" + sobrenome +
                ", situacao=" + situacao +
                ", email=" + email +
                ", senha=" + "Salva" +
                ", fkLanHouse=" + fkLanHouse +
                ", fkEmpresa=" + fkEmpresa +
                ", fkTipoUsuario=" + fkTipoUsuario +
                '}';
    }
}