public class HashDuplo {
    Registro[] tabela;
    int tamanho;
    long colisoes;

    public HashDuplo(int tamanho) {
        this.tamanho = tamanho;
        this.tabela = new Registro[tamanho];
        this.colisoes = 0;
    }

    int hash1(long chave) {
        return (int)(chave % tamanho);
    }

    int hash2(long chave) {
        return 1 + (int)(chave % (tamanho - 1));
    }

    void inserir(int codigo) {
        Registro reg = new Registro(codigo);
        int pos = hash1(codigo);
        int passo = hash2(codigo);
        int tentativas = 0;

        while (tabela[pos] != null) {
            colisoes++;
            pos = (pos + passo) % tamanho;
            tentativas++;
            if (tentativas >= tamanho) {

                return;
            }
        }

        tabela[pos] = reg;
    }

    boolean buscar(int codigo) {
        int pos = hash1(codigo);
        int passo = hash2(codigo);
        int tentativas = 0;

        while (tabela[pos] != null) {
            if (tabela[pos].getCodigo() == codigo) {
                return true;
            }
            pos = (pos + passo) % tamanho;
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