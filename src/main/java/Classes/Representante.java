package Classes;

public class Representante {
    private Integer idRepresentante;
    private String nome;
    private String telefone;
    private String email;
    private Integer cpf;


    public Integer getIdRepresentante() {
        return idRepresentante;
    }

    public void setIdRepresentante(Integer idRepresentante) {
        this.idRepresentante = idRepresentante;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

        public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone ) {
        this.telefone = telefone;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email= email;
    }

    public Integer getCpf() {
        return cpf;
    }

    public void setCpf(Integer cpf) {
        this.cpf = cpf;
    }






    @Override
    public String toString() {
        return "\nLivro{" +
                "id representante=" + idRepresentante +
                ", nome=" + nome +
                ", telefone=" + telefone +
                ", email=" + email +
                ", cpf=" + cpf +
                '}';
    }


}
