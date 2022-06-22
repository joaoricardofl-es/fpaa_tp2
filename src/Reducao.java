public class Reducao {
    int reducao;
    int custo;

    public Reducao(int reducao, int custo) {
        this.reducao = reducao;
        this.custo = custo;
    }

    public int getReducao() {
        return reducao;
    }

    public void setReducao(int reducao) {
        this.reducao = reducao;
    }

    public int getCusto() {
        return custo;
    }

    public void setCusto(int custo) {
        this.custo = custo;
    }

    public double custoBeneficio(){
        return (double) this.custo/this.reducao;
    }
}
