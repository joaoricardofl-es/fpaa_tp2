import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.Clock;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws FileNotFoundException {

        System.out.println("main");
        int meta = 4;
        Clock clock = Clock.systemDefaultZone();
        String arquivos[] = {"LaminacaoTeste1.txt",
                "LaminacaoTeste2.txt", "LaminacaoTeste3.txt", "LaminacaoTeste4.txt"};
       //String arquivos[] = {"LaminacaoTeste2.txt"};

        for (String arquivo : arquivos) {

            System.out.println(arquivo);
            LinkedList<CustoReducao> crList = new LinkedList<CustoReducao>();

            readFromFile(crList, arquivo);

            int tamanhoInicial = crList.get(0).getEntrada();
            final BarraMetal barra = new BarraMetal(tamanhoInicial);
            System.out.println("# Problema");
            System.out.println("Para reduzir uma barra de " + barra.getTamanho() + "mm até " + meta + " mm");
            System.out.println();

            algoritmoGuloso(crList, barra, meta, clock);

            programacaoDinamica(crList, barra, meta, clock);

            backTracking(crList, meta, barra, clock);
        }

    }

    public static CustoReducao getCustoReducao(LinkedList<CustoReducao> custoReducao, BarraMetal barra) {
        return getCustoReducao(custoReducao, barra.getTamanho());
    }

    public static CustoReducao getCustoReducao(LinkedList<CustoReducao> custoReducao, int tamanhoBarra) {
        final int tamanho = tamanhoBarra;
        return custoReducao.stream()
                .filter(x -> x.getEntrada() == tamanho)
                .findFirst().get();
    }

    public static int getCustoPorReducao(CustoReducao custoReducao, int reducao) {
        return custoReducao.getCustoPorReducao(reducao).getCusto();
    }

    public static String printNumber(int no) {
        String res = "";
        if (no < 10) res = "  " + no + "  ";
        else if (no < 100) res = "  " + no + " ";
        else res = " " + no + " ";
        return res;
    }

    public static void algoritmoGuloso(LinkedList<CustoReducao> custoReducao, BarraMetal barra, int meta, Clock clock) {
        System.out.println("# Solução - Algoritmo Guloso");
        long inicio, fim;
        inicio = clock.millis();
        int valor = 0;
        do {
            CustoReducao cr = getCustoReducao(custoReducao, barra);

            Reducao reducao = cr.getReducoes().stream()
                    .min(Comparator.comparing(Reducao::custoBeneficio)).get();

            valor += reducao.getCusto();

            System.out.println("Redução da barra " + barra.getTamanho() + "mm de " +
                    reducao.getReducao() + " por R$" + reducao.getCusto() + " - " +
                    "Tamanho atualizado da barra " + (barra.getTamanho() - reducao.getReducao()));
            barra.reduzirTamanho(reducao.getReducao());

        } while (barra.getTamanho() > meta);
        fim = clock.millis();
        System.out.println("O valor para redução " + valor);
        System.out.println("Tempo de execução " + (fim - inicio) + "ms");
        System.out.println();
        barra.voltarAoTamanhoOriginal();
    }

    public static void programacaoDinamica(LinkedList<CustoReducao> custoReducao, BarraMetal barra, int meta, Clock clock) {
        System.out.println("# Solução - Programação Dinâmica");
        long inicio, fim;
        inicio = clock.millis();
        LinkedList<Reducao> reducoes = custoReducao.getFirst().getReducoes();
        int qtdeOpcoes = reducoes.get(reducoes.size() - 1).getReducao();
        int resPD[][] = new int[qtdeOpcoes + 1][barra.getTamanho() - meta + 1];
        CustoReducao custoReducao1;
        StringBuilder[] sb = new StringBuilder[qtdeOpcoes + 2];

        int posJ;
        for (int i = -1; i <= qtdeOpcoes; i++) {
            sb[i + 1] = new StringBuilder();
            for (int j = barra.getTamanho() + 1; j >= meta; j--) {
                posJ = barra.getTamanho() - j;
                if (j == barra.getTamanho()) {
                    if (i >= 0) {
                        resPD[i][posJ] = 0;
                        sb[i + 1].append(printNumber(0));
                    }
                }

                if (i == -1) {
                    if (j == barra.getTamanho() + 1) sb[i + 1].append("     ");
                    if (j <= barra.getTamanho()) sb[i + 1].append(printNumber(j));
                }
                if (i == 0) {
                    if (j == barra.getTamanho() + 1) sb[i + 1].append("     ");
                    if (j < barra.getTamanho()) sb[i + 1].append(" inf ");
                }
                if (i > 0) {
                    if (j == barra.getTamanho() + 1) sb[i + 1].append(printNumber(i));
                    if (j < barra.getTamanho()) {
                        if (i == 1) {
                            custoReducao1 = getCustoReducao(custoReducao, j + 1);
                            resPD[i][posJ] = getCustoPorReducao(custoReducao1, i) + resPD[i][posJ - i];
                            sb[i + 1].append(printNumber(resPD[i][posJ]));
                        }
                        if (i > 1) {
                            resPD[i][posJ] = resPD[i - 1][posJ];
                            if ((barra.getTamanho() - j) % i == 0) {
                                custoReducao1 = getCustoReducao(custoReducao, j + i);
                                resPD[i][posJ] =
                                        Math.min((getCustoPorReducao(custoReducao1, i) + resPD[i][posJ - i]), resPD[i][posJ]);
                            } else if (posJ > 1) {
                                int red = (barra.getTamanho() - j) % i;
                                custoReducao1 = getCustoReducao(custoReducao, j + red);
                                resPD[i][posJ] =
                                        Math.min((getCustoPorReducao(custoReducao1, red) + resPD[i][posJ - red]), resPD[i][posJ]);
                            }
                            sb[i + 1].append(printNumber(resPD[i][posJ]));
                        }

                    }
                }

            }
            System.out.println(sb[i + 1] != null ? sb[i + 1].toString() : "");
        }

        fim = clock.millis();
        System.out.println("Tempo de execução " + (fim - inicio) + "ms");
        System.out.println();
    }


    public static void backTracking(LinkedList<CustoReducao> crList, int meta, BarraMetal barra, Clock clock) {
        System.out.println("# Solução - Back Tracking");
        long inicio, fim;
        StringBuilder sb = new StringBuilder();
        String str = "";
        int min;

        System.out.println("Ordem Original");
        inicio = clock.millis();
        min = backTrackingRec(crList, meta, barra.getTamanho(), 0, 1000, str, true, barra, false);
        System.out.println("O minimo é " + min);
        fim = clock.millis();
        System.out.println("Tempo de execução " + (fim - inicio) + "ms");
        System.out.println();

        System.out.println("Ordem Alterada");
        inicio = clock.millis();
        min = backTrackingRec(crList, meta, barra.getTamanho(), 0, 1000, str, false, barra, false);
        System.out.println("O minimo é " + min);
        fim = clock.millis();
        System.out.println("Tempo de execução " + (fim - inicio) + "ms");
        System.out.println();

    }

    public static int backTrackingRec(LinkedList<CustoReducao> crList, int meta, int tamanhoAtual, int valor,
                                       int minValor, String str, boolean ascOrd, BarraMetal barra, boolean printLog) {
        CustoReducao cr = getCustoReducao(crList, tamanhoAtual);
        int idxReducao = ascOrd ? 0 : cr.getReducoes().size() - 1;
        do {
            Reducao reducao = cr.getReducoes().get(idxReducao);
            int proximoTamanho = tamanhoAtual - reducao.getReducao();
            if (proximoTamanho >= meta) {
                int novoValor = valor + reducao.getCusto();
                if (proximoTamanho == meta && novoValor < minValor) {
                    minValor = novoValor;
                    String res = str + printBT(tamanhoAtual, reducao) + " / valor total de " + novoValor;
                    if(printLog) System.out.println(res);
                    return minValor;
                }
                if (proximoTamanho > meta && novoValor < minValor) {
                    minValor = Math.min(backTrackingRec(crList, meta, proximoTamanho, novoValor, minValor,
                            str + printBT(tamanhoAtual, reducao), ascOrd, barra, printLog), minValor);
                }
            }
            if (ascOrd) idxReducao++;
            else idxReducao--;
        } while (ascOrd ? (cr.getReducoes().size() > idxReducao) : (idxReducao >= 0));
        return minValor;
    }


    public static String printBT(int tamanhoAtual, Reducao reducao) {
        return " / " + tamanhoAtual + "-" + reducao.getReducao();
    }

    private static void readFromFile(LinkedList<CustoReducao> crList, String fileName) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader(fileName));
        StringBuilder sb = new StringBuilder();
        while (in.hasNext()) {
            String entrada = in.next();
            String custo1 = in.next();
            CustoReducao cr2 = new CustoReducao(Integer.valueOf(entrada),
                    new Reducao(1, Integer.valueOf(custo1)));
            String custo2 = in.next();
            if (!custo2.equals("x")) cr2.set2mm(Integer.valueOf(custo2));
            if (in.hasNext()) {
                String custo3 = in.next();
                if (!custo3.equals("x")) cr2.set3mm(Integer.valueOf(custo3));
            }
            crList.add(cr2);
        }
        in.close();

        System.out.println("# Tabela de entrada");
        crList.forEach(x -> System.out.println(x.toString()));
        System.out.println();
    }
}
