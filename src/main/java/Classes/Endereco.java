package Classes;

public class Endereco {
   private Integer idEndereco;
    private Integer cep;
    private String logradouro;
    private String bairro;
    private String numero;
    private String complemento;
    private Integer uf;
    private String cidade;


    public Integer getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(Integer idEndereco) {
        this.idEndereco = idEndereco;
    }


    public Integer getCep() {
        return cep;
    }

    public void setCep(Integer cep) {
        this.cep = cep;
    }


    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }


    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }


    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }


    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }


    public Integer getUf() {
        return uf;
    }

    public void setUf(Integer uf) {
        this.uf = uf;
    }


    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @Override
    public String toString() {
        return "\nEndereço{" +
                "id Endereço=" + idEndereco +
                ", cep=" + cep +
                ", lofradouro=" + logradouro +
                ", bairro=" + bairro +
                ", numero =" + numero +
                ", complemento =" +  complemento +
                ", uf =" +  uf +
                ", cidade =" +  cidade +
                '}';
    }
}
