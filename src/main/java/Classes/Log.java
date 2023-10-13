package Classes;

import java.time.format.DateTimeFormatter;

public class Log {
    Integer idLog;
    String valor;
    Object dataHora;
    Integer statusLog;
    Integer fkComponente;
    Integer fkTipoComponente;

    public Integer getIdLog() {
        return idLog;
    }

    public void setIdLog(Integer idLog) {
        this.idLog = idLog;
    }

        public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

        public Object getDataHora() {
        return dataHora;
    }

    public void setDataHora(Integer dataHora) {
        this.dataHora = dataHora;
    }

        public Integer getStatusLog() {
        return statusLog;
    }

    public void setStatusLog(Integer statusLog) {
        this.statusLog = statusLog;
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
        return "\nLog{" +
                "id log=" + idLog +
                "valor=" + valor +
                "data e hora=" + dataHora +
                "status de log=" + statusLog +
                ", fk componentes=" + fkComponente+
                ", fk tipo de componentes=" + fkTipoComponente +
                '}';
    }

}
