public class HashEncadeamento {
    Registro[] tabela;
    int tamanho;
    long colisoes;

    public HashEncadeamento(int tamanho){
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

        if (tabela[pos] != null){
            colisoes++;
            Registro atual = tabela[pos];
            while(atual.getProximo() != null){
                colisoes++;
                atual = atual.getProximo();
            }

            atual.setProximo(reg);
        } else if (tabela[pos] == null){
            tabela[pos] = reg;
        }
    }

    public boolean buscar(int codigo){
        int pos = funcaoHash(codigo);

        if (tabela[pos] == null){
            return false;
        }
        else{
            Registro atual = tabela[pos];

            while(atual != null && atual.getCodigo() != codigo){
                atual = atual.getProximo();
            }

            if (atual == null){
                return false;
            }

            return true;
        }
    }

    public int[] maioresListas() {
        int[] top3 = new int[3];

        for (int i = 0; i < tamanho; i++) {
            int tam = 0;
            Registro atual = tabela[i];
            while (atual != null) {
                tam++;
                atual = atual.getProximo();
            }

            if (tam > top3[0]) {
                top3[2] = top3[1];
                top3[1] = top3[0];
                top3[0] = tam;
            } else if (tam > top3[1]) {
                top3[2] = top3[1];
                top3[1] = tam;
            } else if (tam > top3[2]) {
                top3[2] = tam;
            }
        }
        return top3;
    }

    public int[] calcularGaps() {
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

        int mediaGap = numGaps > 0 ? somaGaps / numGaps : 0;
        return new int[]{minGap == 999999 ? 0 : minGap, maxGap, mediaGap};
    }
}
