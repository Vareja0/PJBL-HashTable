public class HashLinear {
    Registro[] tabela;
    int tamanho;
    long colisoes;

    public HashLinear(int tamanho) {
        this.tamanho = tamanho;
        this.tabela = new Registro[tamanho];
        this.colisoes = 0;
    }

    public int funcaoHash(int key){
        return key % tamanho;
    }

    public void inserir(int codigo){
        Registro reg = new Registro(codigo);
        int pos = funcaoHash(reg.getCodigo());
        int tentativas = 0;


        while(tabela[pos] != null && tabela[pos].getCodigo() != reg.getCodigo()){
            colisoes++;
            tentativas++;
            pos = (pos + 1) % tamanho;

            if (tentativas >= tamanho) {

                return;
            }
        }
        tabela[pos] = reg;
    }

    boolean buscar(int codigo) {
        int pos = funcaoHash(codigo);
        int tentativas = 0;


        while (tabela[pos] != null) {
            if (tabela[pos].getCodigo() == codigo) {
                return true;
            }
            pos = (pos + 1) % tamanho;
            tentativas++;
            if (tentativas >= tamanho) break;
        }
        return false;
    }

    int[] calcularGaps() {
        int minGap = 999999;
        int maxGap = 0;
        int somaGaps = 0;
        int numGaps = 0;
        int gapAtual = 0;
        boolean emGap = false;

        for (int i = 0; i < tamanho; i++) {
            if (tabela[i] == null) {
                if (!emGap) {
                    emGap = true;
                    gapAtual = 1;
                } else {
                    gapAtual++;
                }
            } else {
                if (emGap) {
                    if (gapAtual < minGap) minGap = gapAtual;
                    if (gapAtual > maxGap) maxGap = gapAtual;
                    somaGaps += gapAtual;
                    numGaps++;
                    emGap = false;
                }
            }
        }

        int mediaGap;
        if (numGaps > 0) {
            mediaGap = somaGaps / numGaps;
        } else {
            mediaGap = 0;
        }

        int valorMinGapFinal;

        if (minGap == 999999) {
            valorMinGapFinal = 0;
        } else {
            valorMinGapFinal = minGap;
        }

        return new int[]{valorMinGapFinal, maxGap, mediaGap};
    }
}
