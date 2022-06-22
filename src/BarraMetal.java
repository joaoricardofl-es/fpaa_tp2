public class BarraMetal {
    int tamanho;
    int tamanhoOriginal;

    public BarraMetal(int tamanho) {
        this.tamanho = tamanho;
        tamanhoOriginal = tamanho;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public void reduzirTamanho(int reducao){
        this.tamanho -= reducao;
    }

    public void voltarAoTamanhoOriginal(){
        tamanho = tamanhoOriginal;
    }
}
