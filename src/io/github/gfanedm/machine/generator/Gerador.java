package io.github.gfanedm.machine.generator;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;

public class Gerador {

	static final int OP_MIN = 1; // Intervalo de OPCODES -> menor OPCODE
	static final int OP_MAX = 3; // Maior OPCODE
	static final int HAULT = -1; // OPCIDE que representa o final do seu programa -> A ultima instrucao FOR contera essE OPCODE
	static final int TAM_FOR = 10; // Tamanho dos blocos
	static final int REPETICOES = 70; // (0 <= REPETICOES <= 100) Porcentagem que representa a quantidade de blocos de FOR repetidos dentro do NUM_INS
	// O mesmo bloco (FOR) aparecera "REPETICOES(%)" da quantidade total de blocos (FORs), nao necessariamente em sequencia.
	static final int TAM_MEM = 64;  // Tamanho da RAM
	static final int NUM_INS = 1000; // Quantidade de instrucoes -> Maior que o TAM_FOR(vide item 2 das observacoes).
	static final int QUANTIDADE_DE_ENDERECOS = 3; // Tamanho da RAM
	static final int ENDERECO_DE_PALAVRA = 1; // 1:SIM || 2:NAO
	// 1: -> IMPRIME O ENDERECO DA PALAVRA (0 A 3) APOS --- CADA --- ENDERECO DE BLOCO (ATE TAM_RAM-1) vide instrucoes.txt
	// 0: -> IMPRIME APENAS O ENDERECO DE BLOCOS 
	
	/*
	OBSERVACOES:
		 
		. Defina o INTERVALO DE OPCODES das suas instrucoes em "op_min" e "op_max". A instrucao "x" gerada sera "op_min <= x <= op_max"...
		...se assegure que seu "hault" nao se encontra nesse intervalo.
		 
		. A quantidade de instrucoes e' funcao da divisao inteira NUM_INS / TAM_FOR.
		 
		. Este codigo gerara' um arquivo no formato ".txt" com o nome "instrucoes.txt".
		 
		. Em "instrucoes.txt", nao ha espacos apos o ultimo numero de cada linha, nem antes do primeiro...
		...portanto, nao ha' problemas, p. ex., com duplos inputs em variaveis do tipo "istringstream", entre outros erros.
		 
		*/
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("Criando instrucoes");
		gerador();
		System.out.println("DONE!!!");
		
	}
	
	@SuppressWarnings("unused")
	public static void gerador() {
		OutputStream os;
		try {
			os = new FileOutputStream("instrucoes.txt");
		
		OutputStreamWriter osw = new OutputStreamWriter(os);
		BufferedWriter bw = new BufferedWriter(osw);
		
		ArrayList< ArrayList<Integer>> vetor = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> aux = new ArrayList<Integer>();
		
		Random r = new Random();
		
		int quant_for = NUM_INS/TAM_FOR;
		int quant_repeticoes =(int) ((REPETICOES/100.0 + 0.0001)*(quant_for));
		
		ArrayList<Integer> sequence = new ArrayList<Integer>();
		for(int i=0;i<quant_for;i++) {
			sequence.add(i);
		}
		
		for(int i = 0;i<quant_for - quant_repeticoes;i++) {
			int tmp = r.nextInt(quant_for);
			while(tmp >= sequence.size()) {
				tmp -= sequence.size();
			}
			
			sequence.remove(tmp);
		}
		
		for(int i =0;i<TAM_FOR;i++) {
			aux.add(r.nextInt(OP_MAX - OP_MIN + 1) + OP_MIN);
			for(int add =0;add < QUANTIDADE_DE_ENDERECOS ; add++) {
				aux.add(r.nextInt(TAM_MEM));
				if(ENDERECO_DE_PALAVRA != 0) {
					aux.add(r.nextInt(4));
				}
			}
			vetor.add(new ArrayList<Integer>(aux));
			aux.clear();
		}
		
		
		int tamanho = (ENDERECO_DE_PALAVRA == 0 ? QUANTIDADE_DE_ENDERECOS + 1 : QUANTIDADE_DE_ENDERECOS *2 +1);
		boolean end = false, same = false, saida = false;
		for(int a =0 ; a < quant_for;a++) {
			if(!end) {
				if(a == sequence.get(0)) {
					if(a == quant_for -1) {
						vetor.get(TAM_FOR - 1).add(HAULT);
					}
					for(int i =0;i<TAM_FOR;i++) {
						for(int j =0 ;j < tamanho; j++) {
							bw.write(vetor.get(i).get(j).toString());
							bw.write((j == tamanho -1 ? "\n" : " "));
						}
					}
					
					if(sequence.size() > 0) {
						sequence.remove(0);
						if( sequence.size() == 0) {
							end = true;
						}
					}
					same = true;
				}
			}
			
			if(!same) {
				for(int j=0;j < TAM_FOR ; j++) {
					if( j == TAM_FOR -1 && a == quant_for-1) {
						bw.write(String.valueOf(HAULT));
					}else {
						bw.write( String.valueOf(r.nextInt(OP_MAX - OP_MIN+1) + OP_MIN));
					}
					for(int add =0;add <QUANTIDADE_DE_ENDERECOS; add++) {
						bw.write(" ");
						bw.write(String.valueOf(r.nextInt(TAM_MEM)));
						if(ENDERECO_DE_PALAVRA != 0) {
							bw.write(" ");
							bw.write(String.valueOf(r.nextInt(4)));
						}
					}
					bw.newLine();
				}
			}
			same= false;
		}
		bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getLocalizedMessage();
			e.printStackTrace();
		}
		
	}
		
}
