import java.util.LinkedList;

public class CustoReducao {
    int entrada;
    LinkedList<Reducao> reducao = new LinkedList<Reducao>();

    public CustoReducao(int entrada, Reducao reducao1, Reducao reducao2, Reducao reducao3) {
        set2mm(entrada, reducao1, reducao2);
        this.reducao.add(reducao3);
    }

    public CustoReducao(int entrada, Reducao reducao1, Reducao reducao2) {
        set2mm(entrada, reducao1, reducao2);
    }

    public CustoReducao(int entrada, Reducao reducao1) {
        set1mm(entrada, reducao1);
    }

    public CustoReducao() {}

    public void set1mm(int entrada, Reducao reducao1) {
        this.entrada = entrada;
        this.reducao.add(reducao1);
    }

    public void set2mm(int entrada, Reducao reducao1, Reducao reducao2) {
        set1mm(entrada, reducao1);
        this.reducao.add(reducao2);
    }

    public void set2mm(int reducao2) {
        this.reducao.add(new Reducao(2, reducao2));
    }

    public void set3mm(int reducao3) {
        this.reducao.add(new Reducao(3, reducao3));
    }

    @Override
    public String toString() {
        return this.entrada + "mm - " + this.reducao.get(0).getCusto() + " - " +
                getCustoString(1) + "-" + getCustoString(2);
    }

    public String getCustoString(int idx) {
        return " " + (this.reducao.size() > idx ? this.reducao.get(idx).getCusto() + " " : "x ");
    }

    public int getEntrada() {
        return entrada;
    }

    public void setEntrada(int entrada) {
        this.entrada = entrada;
    }

    public LinkedList<Reducao> getReducoes(){
        return this.reducao;
    }

    public Reducao getCustoPorReducao(int reducao){
        return this.reducao.stream().filter(x -> x.getReducao() == reducao).findFirst().orElseThrow();
    }
}
