package Classes;

public class EspecificacoesComponente {
    Integer idEspecificacoesComponente;
    String especificacao;
    String valor;
    Integer fkComponente;
    Integer fkTipoComponente;


    public Integer getIdEspecificacoesComponente() {
        return idEspecificacoesComponente;
    }

    public void setIdEspecificacoesComponente(Integer idEspecificacoesComponente) {
        this.idEspecificacoesComponente = idEspecificacoesComponente;
    }


    public String getEspecificacao() {
        return especificacao;
    }

    public void setEspecificacao(String especificacao) {
        this.especificacao = especificacao;
    }


    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }


    public Integer getFkComponente() {
        return fkComponente;
    }

    public void setFkComponente(Integer fkComponente) {
        this.fkComponente = fkComponente;
    }


    public Integer getFkTipoComponente() {
        return fkTipoComponente;
    }

    public void setFkTipoComponente(Integer fkTipoComponente) {
        this.fkTipoComponente = fkTipoComponente;
    }


    @Override
    public String toString() {
        return "\nEspecificações de Componentes{" +
                "id Especificações de Componentes=" + idEspecificacoesComponente +
                ", especificação=" + especificacao +
                ", valor=" + valor +
                ", fk Componente=" + fkComponente +
                ", Tipo Componente =" + fkTipoComponente +
                '}';
    }
}
