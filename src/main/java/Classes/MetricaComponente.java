package Classes;

public class MetricaComponente {
    private Integer idMetricaComponente;
    private Double min;
    private Double max;
    private String unidadeMedida;

    public MetricaComponente(Double min, Double max, String unidadeMedida) {
        this.min = min;
        this.max = max;
        this.unidadeMedida = unidadeMedida;
    }

    public Integer getIdMetricaComponente() {
        return idMetricaComponente;
    }

    public void setIdMetricaComponente(Integer idMetricaComponente) {
        this.idMetricaComponente = idMetricaComponente;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }
}
