# Análise de Desempenho de Tabelas Hash

## 1. Visão Geral do Projeto

Este trabalho acadêmico, desenvolvido para a disciplina de Estrutura de Dados, tem como objetivo implementar e realizar uma análise comparativa de desempenho de três diferentes técnicas de tratamento de colisão em tabelas hash. As técnicas implementadas foram:

1.  **Encadeamento Separado:** Uma estratégia que utiliza listas encadeadas para armazenar múltiplos elementos que colidem no mesmo índice da tabela.
2.  **Endereçamento Aberto com Sondagem Linear:** Uma técnica de *rehashing* que, em caso de colisão, busca o próximo espaço livre na tabela de forma sequencial.
3.  **Endereçamento Aberto com Hashing Duplo:** Uma técnica de *rehashing* mais eficiente que utiliza uma segunda função de hash para calcular o "passo" a ser dado na busca por um espaço livre, mitigando o problema de agrupamento.

O desempenho de cada abordagem foi medido em cenários variados de tamanho de tabela e volume de dados, analisando métricas como tempo de inserção, tempo de busca e número de colisões.

**Autores:** 
* Riscala Miguel Fadel Neto
* Victor Valerio Fadel
* Pedro Senes Velloso Ribeiro

## 2. Implementação e Metodologia

### 2.1. Estruturas Implementadas

O projeto foi desenvolvido em Java e estruturado nas seguintes classes:

* `Registro.java`: Classe que representa o objeto a ser armazenado, contendo um código (`int`) e um ponteiro para o próximo registro (utilizado no encadeamento).
* `HashEncadeamento.java`: Implementação da tabela hash com tratamento de colisão por encadeamento separado.
* `HashLinear.java`: Implementação da tabela hash com tratamento de colisão por sondagem linear.
* `HashDuplo.java`: Implementação da tabela hash com tratamento de colisão por hashing duplo.
* `Main.java`: Classe principal responsável por orquestrar os testes, gerar os dados, medir o desempenho e exportar os resultados para arquivos `.csv`.

### 2.2. Funções de Hash Utilizadas

* **Função de Hash Primária (Todos os Métodos):** Para o cálculo do índice inicial, foi utilizada a função de resto da divisão, uma escolha comum e eficiente.
    $$
    \text{hash}(chave) = chave \pmod{\text{tamanho}}
    $$

* **Função de Hash Secundária (Hashing Duplo):** Para o cálculo do passo no Hashing Duplo, foi utilizada a seguinte função, que garante que o passo nunca seja zero e seja relativamente primo ao tamanho da tabela (quando o tamanho é primo).
    $$
    \text{hash}_2(chave) = 1 + (chave \pmod{(\text{tamanho} - 1)})
    $$

### 2.3. Metodologia de Teste

Para garantir uma análise justa e abrangente, a seguinte metodologia foi adotada:

* **Tamanhos da Tabela Hash:** Foram escolhidos três tamanhos distintos, que são números primos para ajudar a minimizar colisões primárias:
    * `10.007`
    * `100.003`
    * `1.000.003`

* **Conjuntos de Dados:** Foram gerados três conjuntos de dados com códigos de registro aleatórios, utilizando uma **seed fixa (`Random(43)`)** para garantir que todas as estruturas fossem testadas exatamente com os mesmos dados, tornando a comparação válida.
    * `10.000` registros
    * `100.000` registros
    * `1.000.000` de registros

* **Métricas Coletadas:**
    * **Tempos de Inserção e Busca:** Medido em milissegundos (`ms`) para a operação completa no conjunto de dados.
    * **Número de Colisões:** Contabiliza o número de "passos" ou "sondagens" necessários para encontrar uma posição livre (na inserção) ou o elemento desejado (na busca).
    * **Maiores Listas (Encadeamento):** Os tamanhos das 3 maiores listas encadeadas.
    * **Análise de Gaps (Endereçamento Aberto):** O tamanho do menor, do maior e a média dos espaços vazios (gaps) entre os elementos na tabela. Esta métrica é relevante primariamente para as técnicas de Endereçamento Aberto (Linear e Duplo), pois mede a distribuição de dados e o agrupamento.

## 3. Resultados e Análise

Os resultados completos dos testes foram exportados para os arquivos `resultado10007.csv`, `resultado100003.csv`, e `resultado1000003.csv`. Abaixo, uma análise dos cenários mais significativos.

### 3.1. Análise do Fator de Carga ($$\lambda$$)

O fator de carga ($$\lambda = \frac{\text{nº de itens}}{\text{tamanho da tabela}}$$) é o principal indicador de desempenho.

**Cenário 1: Baixo Fator de Carga ($$\lambda \approx 0.1$$)**
* **Tabela:** 100.003 | **Dados:** 10.000

| Método | Tempo Inserção (ms) | Tempo Busca (ms) | Colisões |
| :--- | :--- | :--- | :--- |
| Encadeamento | 0.040606 | 0.026720 | 537 |
| Linear Probing | 0.033673 | 0.018625 | 607 |
| Double Hashing | 0.035607 | 0.032912 | 557 |

**Análise:** Com a tabela "vazia", todos os métodos são extremamente rápidos e eficientes. O número de colisões é mínimo, e as diferenças de tempo são insignificantes.

**Cenário 2: Fator de Carga Próximo a 1 ($$\lambda \approx 1$$)**
* **Tabela:** 100.003 | **Dados:** 100.000

| Método | Tempo Inserção (ms) | Tempo Busca (ms) | Colisões |
| :--- | :--- | :--- | :--- |
| Encadeamento | 1.400685 | 0.623192 | 50.420 |
| Linear Probing | 37.793678 | 37.786949 | 17.850.445 |
| Double Hashing | 3.210379 | 3.538386 | 929.663 |

**Análise:** À medida que a tabela enche, as diferenças se tornam gritantes.
* **Sondagem Linear:** Sofre com o fenômeno de **agrupamento primário**. As colisões criam longos *clusters* de dados, fazendo com que o número de sondagens para encontrar um local livre dispare. Seu desempenho é o pior.
* **Hashing Duplo:** Por usar um passo variável, espalha melhor os dados e mitiga o agrupamento, resultando em um número de colisões e tempo muito menores que a Sondagem Linear.
* **Encadeamento:** Continua com um bom desempenho, pois o custo médio de inserção depende apenas do tamanho médio das listas (que aqui é próximo de 1).

**Cenário 3: Fator de Carga Extremo ($$\lambda \gg 1$$)**
* **Tabela:** 10.007 | **Dados:** 1.000.000 ($$\lambda \approx 100$$)

| Método | Tempo Inserção (ms) | Tempo Busca (ms) | Colisões |
| :--- | :--- | :--- | :--- |
| Encadeamento | 475.915009 | 479.770630 | 49.972.599 |
| Linear Probing | 20124.259766 | 20122.208984 | 9.907.632.507 |
| Double Hashing | 22109.380859 | 21982.962891 | 9.906.945.556 |

**Análise:**
* **Endereçamento Aberto (Linear e Duplo):** O desempenho entra em colapso total. Como o número de itens é muito maior que o tamanho da tabela, a tabela fica 100% cheia rapidamente. A partir daí, cada nova tentativa de inserção percorre a tabela inteira em busca de um espaço que não existe. Os tempos de inserção (20-22 segundos) e o número de colisões (na casa dos 9.9 bilhões) refletem essa busca inútil.
* **Encadeamento Separado:** É a **única abordagem viável** neste cenário. O tempo de inserção e busca aumenta, pois as listas encadeadas se tornam longas (a maior lista chegou a 141 elementos), mas o sistema continua funcional.

### 3.2. Gráficos Comparativos (Exemplos)

Para uma visualização clara, gráficos são essenciais. 

**Tempo de Inserção (ms) vs. Volume de Dados (Tabela de 100.003)**

| Volume de Dados | Encadeamento | Sondagem Linear | Hashing Duplo |
| :--- | :--- | :--- | :--- |
| 10.000 | 0.04 | 0.03 | 0.03 |
| 100.000 | 1.40 | 37.79 | 3.21 |
| 1.000.000 | 34.86 | 188419.26 | 202109.40 |

## Comparação Entre Tabelas

![Texto Alternativo](imagesPJBL/comparacao_tabelas.png)

Tabela de 10.007: O Encadeamento é o único viável.

Tabela de 100.003 (Fator de Carga ≈ 1): Aqui vemos a diferença clássica: o Encadeamento é o mais rápido, seguido pelo Hashing Duplo. A Sondagem Linear é visivelmente a mais lenta das três, pois o agrupamento primário força muitas sondagens extras.

Tabela de 1.000.003 (Fator de Carga ≈ 0.1): Com a tabela "vazia", as colisões são raras, e todos os métodos têm desempenho excelente e muito similar.

## Explosão de Desempenho

![Texto Alternativo](imagesPJBL/explosao_desempenho.png)

Encadeamento (Linha Azul): Mostra um crescimento de tempo suave e quase linear. Isso é esperado, pois o custo aumenta à medida que as listas encadeadas crescem, mas o sistema continua funcional e previsível.

Linear e Duplo (Linhas Laranja e Cinza): Mostram uma "explosão" de desempenho. O tempo de inserção é baixo quando há poucos dados, mas dispara de forma exponencial quando o número de registros ultrapassa a capacidade da tabela (100003). Isso acontece porque, uma vez que a tabela está cheia, cada nova tentativa de inserção força o algoritmo a percorrer a tabela inteira em busca de um espaço que não existe. Seu código implementa corretamente essa parada, mas o custo computacional para chegar a essa conclusão é imenso, o que o gráfico captura perfeitamente.

## Resultados da Tabela de Tamanho 1000003

![Texto Alternativo](imagesPJBL/tabela_1000003.png)

Este gráfico mostra o cenário ideal. Como a tabela nunca fica cheia (o fator de carga é no máximo 1), não há explosão de desempenho. Todos os métodos são extremamente rápidos e eficientes. Isso demonstra que, com um dimensionamento adequado, qualquer uma das técnicas pode ser eficaz.

## Resultados da Tabela de Tamanho 100003

![Texto Alternativo](imagesPJBL/tabela_100003.png)

Endereçamento Aberto é inviável quando o número de itens (1 milhão) excede em muito a capacidade da tabela (100 mil).

## Resultados da Tabela de Tamanho 10007

![Texto Alternativo](imagesPJBL/tabela_10007.png)

Este gráfico mostra o cenário de falha catastrófica para o Endereçamento Aberto (Linear e Duplo). Com 100 mil ou 1 milhão de itens em uma tabela de 10 mil posições, o tempo de inserção se torna proibitivo, como esperado. O Encadeamento, por outro lado, continua a funcionar, embora com um tempo maior, pois as listas ficam muito longas.

## resultado 10007

|Metodo|TamTabela|TamDados|TempoInsercao(ms)|TempoBusca(ms)|Colisoes|Lista1|Lista2|Lista3|GapMin|GapMax|GapMedia |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
|Encadeamento|10007|10000|0.663107|0.456078|5048|6|6|6|1|11|1 |
|LinearProbing|10007|10000|1.867393|1.482048|420117|0|0|0|1|4|1 |
|DoubleHashing|10007|10000|0.639423|0.616630|66184|0|0|0|1|1|1 |
|Encadeamento|10007|100000|2.867674|2.850552|499602|26|24|24|0|0|0 |
|LinearProbing|10007|100000|1839.601685|1838.271240|900962580|0|0|0|0|0|0 |
|DoubleHashing|10007|100000|2030.093384|2037.739380|900637826|0|0|0|0|0|0 |
|Encadeamento|10007|1000000|475.915009|479.770630|49972599|141|139|138|0|0|0 |
|LinearProbing|10007|1000000|20124.259766|20122.208984|9907632507|0|0|0|0|0|0 |
|DoubleHashing|10007|1000000|22109.380859|21982.962891|9906945556|0|0|0|0|0|0 |

### Fator de Carga:

10.000 / 10007 ≈ 1 (quase cheia).
100.000 / 10007 ≈ 10 (sobrecarregada).
1.000.000 / 10007 ≈ 100 (extremamente sobrecarregada).


### Tempos de Inserção e Busca:

#### Para 10.000 elementos:

Encadeamento: 0,66 ms (inserção), 0,46 ms (busca) — mais rápido.
DoubleHashing: 0,64 ms (inserção), 0,62 ms (busca) — semelhante ao Encadeamento.
LinearProbing: 1,87 ms (inserção), 1,48 ms (busca) — mais lento devido a mais colisões.


#### Para 100.000 elementos:

Encadeamento: 2,87 ms (inserção), 2,85 ms (busca) — eficiente.
LinearProbing: ~1839 ms (inserção), ~1838 ms (busca) — extremamente lento.
DoubleHashing: ~2030 ms (inserção), ~2038 ms (busca) — igualmente lento.


#### Para 1.000.000 elementos:

Encadeamento: ~476 ms (inserção), ~480 ms (busca) — muito melhor.
LinearProbing: ~20.124 ms (inserção), ~20.122 ms (busca) — impraticável.
DoubleHashing: ~22.109 ms (inserção), ~21.983 ms (busca) — igualmente impraticável.




### Colisões:

Encadeamento: 5.048 (10.000), 499.602 (100.000), 49.972.599 (1.000.000) — aumentam com o tamanho dos dados, mas gerenciados por listas.
LinearProbing: 420.117 (10.000), 900.962.580 (100.000), 9.907.632.507 (1.000.000) — extremamente altas.
DoubleHashing: 66.184 (10.000), 900.637.826 (100.000), 9.906.945.556 (1.000.000) — também altas.


### Comprimento das Listas (Encadeamento):

Listas mais longas crescem com os dados: 6 (10.000), 24–26 (100.000), 138–141 (1.000.000).


### Lacunas (Sondagem):

Para 10.000: LinearProbing (GapMax: 4), DoubleHashing (GapMax: 1) — DoubleHashing tem sequências de sondagem mais curtas.
Para 100.000 e 1.000.000: Lacunas caem para 0 devido à saturação da tabela.

## resultado 100003

|Metodo|TamTabela|TamDados|TempoInsercao(ms)|TempoBusca(ms)|Colisoes|Lista1|Lista2|Lista3|GapMin|GapMax|GapMedia|
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
|Encadeamento|100003|10000|0.040606|0.026720|537|4|3|3|1|110|10|
|LinearProbing|100003|10000|0.033673|0.018625|607|0|0|0|1|110|10|
|DoubleHashing|100003|10000|0.035607|0.032912|557|0|0|0|1|110|9|
|Encadeamento|100003|100000|1.400685|0.623192|50420|7|7|7|1|13|1|
|LinearProbing|100003|100000|37.793678|37.786949|17850445|0|0|0|2|4|3|
|DoubleHashing|100003|100000|3.210379|3.538386|929663|0|0|0|1|1|1|
|Encadeamento|100003|1000000|34.864658|30.009356|5002251|26|26|25|1|1|1|
|LinearProbing|100003|1000000|188419.265625|188285.656250|90017460116|0|0|0|0|0|0|
|DoubleHashing|100003|1000000|202109.406250|204082.109375|90003386794|0|0|0|0|0|0|

### Fator de Carga:

10.000 / 100003 ≈ 0,1 (esparsa).
100.000 / 100003 ≈ 1 (quase cheia).
1.000.000 / 100003 ≈ 10 (sobrecarregada).


### Tempos de Inserção e Busca:

#### Para 10.000 elementos:

LinearProbing: 0,034 ms (inserção), 0,019 ms (busca) — mais rápido devido ao baixo fator de carga.
DoubleHashing: 0,036 ms (inserção), 0,033 ms (busca).
Encadeamento: 0,041 ms (inserção), 0,027 ms (busca).


#### Para 100.000 elementos:

Encadeamento: 1,40 ms (inserção), 0,62 ms (busca) — melhor desempenho.
DoubleHashing: 3,21 ms (inserção), 3,54 ms (busca).
LinearProbing: 37,79 ms (inserção), 37,79 ms (busca) — muito mais lento.


#### Para 1.000.000 elementos:

Encadeamento: 34,86 ms (inserção), 30,01 ms (busca) — muito superior.
DoubleHashing: ~202.109 ms (inserção), ~204.082 ms (busca) — extremamente lento.
LinearProbing: ~188.419 ms (inserção), ~188.286 ms (busca) — igualmente lento.




### Colisões:

Encadeamento: 537 (10.000), 50.420 (100.000), 5.002.251 (1.000.000).
LinearProbing: 607 (10.000), 17.850.445 (100.000), 90.017.460.116 (1.000.000).
DoubleHashing: 557 (10.000), 929.663 (100.000), 90.003.386.794 (1.000.000).


### Comprimento das Listas (Encadeamento):

Listas mais longas: 3–4 (10.000), 7 (100.000), 25–26 (1.000.000).


### Lacunas (Sondagem):

Para 10.000: GapMax 110 para ambos os métodos de sondagem, indicando sequências longas em tabelas esparsas.
Para 100.000: LinearProbing (GapMax: 4), DoubleHashing (GapMax: 1).
Para 1.000.000: Lacunas caem para 0 devido à saturação.

## resultado 1000003

|Metodo|TamTabela|TamDados|TempoInsercao(ms)|TempoBusca(ms)|Colisoes|Lista1|Lista2|Lista3|GapMin|GapMax|GapMedia|
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
|Encadeamento|1000003|10000|0.164159|0.022552|43|2|2|2|1|999|100|
|LinearProbing|1000003|10000|0.149962|0.017202|43|0|0|0|1|999|100|
|DoubleHashing|1000003|10000|0.141336|0.027301|44|0|0|0|1|999|100|
|Encadeamento|1000003|100000|0.494530|0.212960|5003|4|4|4|1|110|10|
|LinearProbing|1000003|100000|0.577356|0.207510|5567|0|0|0|1|110|10|
|DoubleHashing|1000003|100000|0.561016|0.367120|5353|0|0|0|1|109|10|
|Encadeamento|1000003|1000000|11.037131|7.224039|499067|9|8|8|1|14|1|
|LinearProbing|1000003|1000000|662.049316|651.075378|302530120|0|0|0|1|6|1|
|DoubleHashing|1000003|1000000|60.120300|64.680496|11194249|0|0|0|1|1|1|

### Fator de Carga:

10.000 / 1000003 ≈ 0,01 (muito esparsa).
100.000 / 1000003 ≈ 0,1 (esparsa).
1.000.000 / 1000003 ≈ 1 (quase cheia).


### Tempos de Inserção e Busca:

#### Para 10.000 elementos:

DoubleHashing: 0,14 ms (inserção), 0,027 ms (busca).
LinearProbing: 0,15 ms (inserção), 0,017 ms (busca) — busca mais rápida.
Encadeamento: 0,16 ms (inserção), 0,023 ms (busca).


#### Para 100.000 elementos:

Encadeamento: 0,49 ms (inserção), 0,21 ms (busca) — mais rápido.
LinearProbing: 0,58 ms (inserção), 0,21 ms (busca).
DoubleHashing: 0,56 ms (inserção), 0,37 ms (busca).


#### Para 1.000.000 elementos:

Encadeamento: 11,04 ms (inserção), 7,22 ms (busca) — muito superior.
DoubleHashing: 60,12 ms (inserção), 64,68 ms (busca).
LinearProbing: 662,05 ms (inserção), 651,08 ms (busca) — mais lento.




### Colisões:

Encadeamento: 43 (10.000), 5.003 (100.000), 499.067 (1.000.000).
LinearProbing: 43 (10.000), 5.567 (100.000), 302.530.120 (1.000.000).
DoubleHashing: 44 (10.000), 5.353 (100.000), 11.194.249 (1.000.000).


### Comprimento das Listas (Encadeamento):

Listas mais longas: 2 (10.000), 4 (100.000), 8–9 (1.000.000).


### Lacunas (Sondagem):

Para 10.000: GapMax 999 para ambos os métodos (tabela esparsa, sequências longas).
Para 100.000: GapMax 110 (LinearProbing), 109 (DoubleHashing).
Para 1.000.000: LinearProbing (GapMax: 6), DoubleHashing (GapMax: 1).


## 4. Conclusão

Com base na análise dos dados, as seguintes conclusões podem ser tiradas:

1.  **Melhor Desempenho Geral (Endereçamento Aberto):** O **Hashing Duplo** é superior à Sondagem Linear em todos os cenários com fator de carga moderado a alto ($$\lambda > 0.5$$), pois evita o problema de agrupamento primário, mantendo o número de colisões e o tempo de operação significativamente mais baixos.

2.  **Melhor para Cenários Imprevisíveis:** O **Encadeamento Separado** é a técnica mais robusta. Seu desempenho degrada de forma muito mais suave com o aumento do fator de carga e, crucialmente, é a única das três que continua a funcionar de forma eficiente quando o número de elementos excede o tamanho da tabela.

3.  **Pior Desempenho:** A **Sondagem Linear** é a abordagem menos eficiente, exceto em tabelas com baixíssimo fator de carga. Sua simplicidade não compensa a severa degradação de desempenho causada pelo agrupamento primário assim que a tabela começa a encher.

Em suma, a escolha da técnica ideal depende diretamente da aplicação. Para sistemas onde o número máximo de elementos é conhecido e é possível garantir um fator de carga baixo ($$\lambda < 0.7$$), o Hashing Duplo é uma excelente escolha. Para sistemas onde o volume de dados pode crescer indefinidamente ou exceder a capacidade inicial da tabela, o Encadeamento Separado é a única opção confiável.

## 5. Como Executar o Projeto

1.  Clone o repositório.
2.  Certifique-se de ter o JDK (Java Development Kit) instalado.
3.  Navegue até a pasta do projeto e compile todos os arquivos `.java`:
    ```bash
    javac *.java
    ```
4.  Execute a classe principal:
    ```bash
    java Main
    ```
5.  Ao final da execução, os resultados são gerados em arquivos .csv correspondentes aos tamanhos de tabela testados: `resultado10007.csv`, `resultado100003.csv` e `resultado1000003.csv`.
