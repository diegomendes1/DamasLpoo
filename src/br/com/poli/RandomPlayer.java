package br.com.poli;

import java.util.ArrayList;
import java.util.Random;
import br.com.poli.enums.CorCasa;
import br.com.poli.enums.CorPeca;

public class RandomPlayer  extends Jogador implements AutoPlayer{
	private ArrayList<int[]> listaPossiveis;
	private ArrayList<int[]> listaPossiveisCaptura;
	
	public RandomPlayer() {
		listaPossiveis = new ArrayList<int[]>();
		listaPossiveisCaptura = new ArrayList<int[]>();
	}
	
	public boolean jogarAuto(Jogo jogo) {
		listaPossiveis.clear();
		listaPossiveisCaptura.clear();
		
		int posX = 0, posY = 0;
		
		verificaCaptura(jogo);
		
		if(listaPossiveisCaptura.size() == 0) {
			iniciarVerificacaoMovimento(jogo);
			if(listaPossiveis.size() == 0) {
				return false;
			}else {
				 Random randomGenerator = new Random();
				 int random = randomGenerator.nextInt(listaPossiveis.size());
				 
				if(jogo.getTabuleiro().getCasaGrid(listaPossiveis.get(random)[1], listaPossiveis.get(random)[2]) != null) {
					jogo.getTabuleiro().executarMovimento(listaPossiveis.get(random)[1], listaPossiveis.get(random)[2], listaPossiveis.get(random)[3], listaPossiveis.get(random)[4]);
					
					System.out.println();
					return true;
				}
			}
		}else {
			Random randomGenerator = new Random();
			int random = randomGenerator.nextInt(listaPossiveisCaptura.size());
			if(jogo.getTabuleiro().getCasaGrid(listaPossiveisCaptura.get(random)[1], listaPossiveisCaptura.get(random)[2]) != null) {
				jogo.capturar(listaPossiveisCaptura.get(random)[1], listaPossiveisCaptura.get(random)[2], listaPossiveisCaptura.get(random)[3], listaPossiveisCaptura.get(random)[4]);
				return true;
			}
		}
		
		return false;
	}
	
	public void iniciarVerificacaoMovimento(Jogo jogo) {
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(jogo.getTabuleiro().getCasaGrid(i, j).getPeca() != null && jogo.getTabuleiro().getCasaGrid(i, j).getPeca().getJogador() != jogo.getAtualJogador()) {
					int[] destino = verificaMovimento(i, j, jogo);
					if(destino != null) {
						int[] opcao = new int[5];
						opcao[1] = i;
						opcao[2] = j;
						opcao[3] = destino[0];
						opcao[4] = destino[1];
						listaPossiveis.add(opcao);
						
					}
				}
			}
		}
	}
	
	public int[] verificaMovimento(int posX, int posY, Jogo jogo) {
		int[] casaPossivel = null;
    	
    			//Verifica a direcao que a peca pode movimentar
    			
    			//Se a pedra for clara
    			if(jogo.getTabuleiro().getCasaGrid(posX, posY).getPeca().getCor() == CorPeca.CLARO) {
    			casaPossivel = null;
    			if(posX > 0 && posY > 0) {
    			//Lado Superior Esquerdo
    				//este if vira true se onde o jogador quer ir esta� vazio e o jogador queria ir para tal casa
    			if(jogo.getTabuleiro().getCasaGrid(posX-1, posY-1).getOcupada() == false) {
    				casaPossivel = new int[] {posX-1,posY-1};
				}
    			}
    			
    			if(posX > 0 && posY < 7) {
    			//Lado Superior Direito
    			if(jogo.getTabuleiro().getCasaGrid(posX-1, posY+1).getOcupada() == false) {
    				casaPossivel = new int[] {posX-1,posY+1};
				}
    			}
    			
    			//Se a pedra for escura
    			}else {
    			if(posX < 7 && posY > 0) {
    			//Lado Inferior Esquerdo
    			if(jogo.getTabuleiro().getCasaGrid(posX+1, posY-1).getOcupada() == false) {
    				casaPossivel = new int[] {posX+1,posY-1};
				}
    			}
    			
    			if(posX < 7 && posY < 7) {
    			//Lado Inferior Direito
    			if(jogo.getTabuleiro().getCasaGrid(posX+1, posY+1).getOcupada() == false) {
    				casaPossivel = new int[] {posX+1,posY+1};
				}
    			}
    			
    			}
    			return casaPossivel;
	}
	
	public void verificaCaptura(Jogo jogo) {
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(jogo.getTabuleiro().getCasaGrid(i, j).getPeca() != null && jogo.getTabuleiro().getCasaGrid(i, j).getPeca().getJogador() != jogo.getAtualJogador()) {
					int[] casaOponente = verificarCapturaCasa(i, j, jogo);
					if(casaOponente != null) {
						int[] opcao = new int[5];
						opcao[1] = i;
						opcao[2] = j;
						opcao[3] = casaOponente[0];
						opcao[4] = casaOponente[1];
						listaPossiveisCaptura.add(opcao);
					}
				}
			}
		}
	}
	
public int[] verificarCapturaCasa(int i, int j, Jogo jogo) {
 		//Casa em que se pode ir depois do movimento ou captura.
 	    int[] capturaPossivel = null;
 			
 		if(jogo.getTabuleiro().getCasaGrid(i, j).getCor() == CorCasa.PRETO && 
 			jogo.getTabuleiro().getCasaGrid(i, j).getPeca() != null &&
 			jogo.getTabuleiro().getCasaGrid(i, j).getPeca().getJogador() != jogo.getAtualJogador()) {
 				
 			//procura uma possivel captura na direcao Superior Esquerda. se existir um lugar, casaPossivel recebe esta casa.
 			capturaPossivel = verificarCapturaSuperiorEsq(i, j, jogo);
 			if(capturaPossivel != null) {
 				return capturaPossivel;
 			}
 			//procura uma possivel captura na direcao Superior Direita. se existir um lugar, casaPossivel recebe esta casa.
 			capturaPossivel = verificarCapturaSuperiorDir(i, j, jogo);
 			if(capturaPossivel != null) {
 				return capturaPossivel;
 			}
 			//procura uma possivel captura na direcao Inferior Esquerda. se existir um lugar, casaPossivel recebe esta casa.
 			capturaPossivel = verificarCapturaInferiorEsq(i, j, jogo);
 			if(capturaPossivel != null) {
 				return capturaPossivel;
 			}
 			//procura uma possivel captura na direcao Inferior Direita. se existir um lugar, casaPossivel recebe esta casa.
 			capturaPossivel = verificarCapturaInferiorDir(i, j, jogo);
 			if(capturaPossivel != null) {
 				return capturaPossivel;
 			}
 		}
 	
 	return capturaPossivel;
 }
 
 public boolean verificarPossibilidadeCapturaCasa(int x, int y, Jogo jogo) {
 	if(verificarCapturaSuperiorEsq(x,y, jogo) != null||
 		verificarCapturaSuperiorDir(x,y, jogo)!= null||
 		verificarCapturaInferiorEsq(x,y, jogo)!= null||
 		verificarCapturaInferiorDir(x,y, jogo)!= null) {
 		return true;
 	}
 	return false;
 }
 
 public boolean verificarCapturaTabuleiro(Jogo jogo) {
 	for(int i = 0; i < 8; i++) {
 		for(int j =0; j < 8; j++) {
 			
 		//Casa em que se pode ir depois do movimento ou captura.
 	    int[] capturaPossivel = null;
 			
 		if(jogo.getTabuleiro().getCasaGrid(i, j).getCor() == CorCasa.PRETO && 
 			jogo.getTabuleiro().getCasaGrid(i, j).getPeca() != null &&
 			jogo.getTabuleiro().getCasaGrid(i, j).getPeca().getJogador() != jogo.getAtualJogador()) {
 				
 			//procura uma possivel captura na direcao Superior Esquerda. se existir um lugar, casaPossivel recebe esta casa.
 			capturaPossivel = verificarCapturaSuperiorEsq(i, j, jogo);
 			if(capturaPossivel != null) {
 				return true;
 			}
 			//procura uma possivel captura na direcao Superior Direita. se existir um lugar, casaPossivel recebe esta casa.
 			capturaPossivel = verificarCapturaSuperiorDir(i, j, jogo);
 			if(capturaPossivel != null) {
 				return true;
 			}
 			//procura uma possivel captura na direcao Inferior Esquerda. se existir um lugar, casaPossivel recebe esta casa.
 			capturaPossivel = verificarCapturaInferiorEsq(i, j, jogo);
 			if(capturaPossivel != null) {
 				return true;
 			}
 			//procura uma possivel captura na direcao Inferior Direita. se existir um lugar, casaPossivel recebe esta casa.
 			capturaPossivel = verificarCapturaInferiorDir(i, j, jogo);
 			if(capturaPossivel != null) {
 				return true;
 			}
 		}
 			
 		}
 	}
 	
 	return false;
 }
 
 //Grupo de metodos para verificar se existe oportunidade de captura em certa direcao.
 public int[] verificarCapturaSuperiorEsq(int posX, int posY, Jogo jogo) {
 	
 	//impede de verificar captura fora do tabuleiro
 	if(posX >= 2 && posY >= 2) {
 		
 	//Se esse if for verdadeiro, a pedra nao � dama
 		if(jogo.getTabuleiro().getCasaGrid(posX, posY).getPeca() != null) {
 	if(jogo.getTabuleiro().getCasaGrid(posX, posY).getPeca().getIsDama() == false) {
 		
     	if(jogo.getTabuleiro().getCasaGrid(posX-1, posY-1).getOcupada() == true && jogo.getTabuleiro().getCasaGrid(posX-1, posY-1).getPeca().getJogador() == jogo.getAtualJogador()) {
 			if(jogo.getTabuleiro().getCasaGrid(posX-2, posY-2).getOcupada() == false) {
 				int[] novaCasa = new int[2];
 				novaCasa[0] = posX-2;
 				novaCasa[1] = posY-2;
 				return novaCasa;
 			}
     	}
     	//Se a pedra for dama, essa parte vai rodar
 	}else {
 		//Mesmo FOR estranho visto no movimento da dama.
 		int j = posY;
 		for(int i = posX-1; i >= 1; i--) {
 			j--;
 			if(j >= 1) {
 				//System.out.println(i+","+ j);
 				if(jogo.getTabuleiro().getCasaGrid(i, j) != jogo.getTabuleiro().getCasaGrid(posX, posY)) {
     			if(jogo.getTabuleiro().getCasaGrid(i, j).getOcupada() == true) {
     				if(jogo.getTabuleiro().getCasaGrid(i, j).getPeca().getJogador() == jogo.getAtualJogador()) {
     					if(jogo.getTabuleiro().getCasaGrid(i-1, j-1).getOcupada() == false) {
     						int[] novaCasa = new int[2];
     		 				novaCasa[0] = i-1;
     		 				novaCasa[1] = j-1;
     						return novaCasa;
     					}
     				}else {
     					break;
     				}
     			}
 			}
     		}
     	}
 	}
 	}else {
 		return null;
 	}
 }else {
 	return null;
 }
 	return null;
 }
 
 public int[] verificarCapturaSuperiorDir(int posX, int posY, Jogo jogo) {
 	//impede de verificar captura fora do tabuleiro
 	if(posX >= 2 && posY <= 5) {
 	//Se esse if for verdadeiro, a pedra nao e' dama
 		
 		if(jogo.getTabuleiro().getCasaGrid(posX, posY).getPeca() != null) {
 	if(jogo.getTabuleiro().getCasaGrid(posX, posY).getPeca().getIsDama() == false) {
 		if(jogo.getTabuleiro().getCasaGrid(posX-1, posY+1).getOcupada() == true && jogo.getTabuleiro().getCasaGrid(posX-1, posY+1).getPeca().getJogador() == jogo.getAtualJogador()) {
 			if(jogo.getTabuleiro().getCasaGrid(posX-2, posY+2).getOcupada() == false) {
 				int[] novaCasa = new int[2];
 				novaCasa[0] = posX-2;
 				novaCasa[1] = posY+2;
 				return novaCasa;
 			}
 		}
 	}else {
 		
 		int j = posY;
 		for(int i = posX-1; i >= 1; i--) {
 			j++;
     		if(j <= 6) {
     			if(jogo.getTabuleiro().getCasaGrid(i, j).getOcupada() == true) {
     				if(jogo.getTabuleiro().getCasaGrid(i, j) != jogo.getTabuleiro().getCasaGrid(posX, posY)) {
     				if(jogo.getTabuleiro().getCasaGrid(i, j).getPeca().getJogador() == jogo.getAtualJogador()) {
     				if(jogo.getTabuleiro().getCasaGrid(i-1, j+1).getOcupada() == false) {
     					int[] novaCasa = new int[2];
 		 				novaCasa[0] = i-1;
 		 				novaCasa[1] = j+1;
     					return novaCasa;
     				}
     				}else {
     					break;
     				}
     			}
     		}
     		}
     	}
 	}
 		}else {
     		return null;
     	}
 	}else {
 		return null;
 	}
 	return null;
 }
 
 public int[] verificarCapturaInferiorEsq(int posX, int posY, Jogo jogo) {
	 
 	//impede de verificar captura fora do tabuleiro
 	if(posX <= 5 && posY >= 2) {
 	//Se esse if for verdadeiro, a pedra nao e' dama
 		if(jogo.getTabuleiro().getCasaGrid(posX, posY).getPeca() != null) {
 			
 		
 	if(jogo.getTabuleiro().getCasaGrid(posX, posY).getPeca().getIsDama() == false) {
 		if(jogo.getTabuleiro().getCasaGrid(posX+1, posY-1).getOcupada() == true && jogo.getTabuleiro().getCasaGrid(posX+1, posY-1).getPeca().getJogador() == jogo.getAtualJogador()) {
 			if(jogo.getTabuleiro().getCasaGrid(posX+2, posY-2).getOcupada() == false) {
 				int[] novaCasa = new int[2];
 				novaCasa[0] = posX+2;
 				novaCasa[1] = posY-2;
 				return novaCasa;
 			}
 		}
 	}else {
 		
 		int j = posY;
 		for(int i = posX+1; i <= 6; i++) {
 			j--;
     		if(j >= 1) {
     			if(jogo.getTabuleiro().getCasaGrid(i, j).getOcupada() == true) {
     				if(jogo.getTabuleiro().getCasaGrid(i, j) != jogo.getTabuleiro().getCasaGrid(posX, posY)) {
     				if(jogo.getTabuleiro().getCasaGrid(i, j).getPeca().getJogador() == jogo.getAtualJogador()) {
     				if(jogo.getTabuleiro().getCasaGrid(i+1, j-1).getOcupada() == false) {
     					int[] novaCasa = new int[2];
 		 				novaCasa[0] = i+1;
 		 				novaCasa[1] = j-1;
     					return novaCasa;
     				}
     				}else {
     					break;
     				}
     			}
     		}
     		}
     	}
 	}
 	
 	}else {
 		return null;
 	}
 	}else {
 		return null;
 	}
 	return null;
 }
 
 public int[] verificarCapturaInferiorDir(int posX, int posY, Jogo jogo) {
 	//impede de verificar captura fora do tabuleiro
 	if(posX <= 5 && posY <= 5) {
 	//Se esse if for verdadeiro, a pedra nao e' dama
 		if(jogo.getTabuleiro().getCasaGrid(posX, posY).getPeca() != null) {
 	if(jogo.getTabuleiro().getCasaGrid(posX, posY).getPeca().getIsDama() == false) {
 		if(jogo.getTabuleiro().getCasaGrid(posX+1, posY+1).getOcupada() == true&& jogo.getTabuleiro().getCasaGrid(posX+1, posY+1).getPeca().getJogador() == jogo.getAtualJogador()) {
 			if(jogo.getTabuleiro().getCasaGrid(posX+2, posY+2).getOcupada() == false) {
 				int[] novaCasa = new int[2];
 				novaCasa[0] = posX+2;
 				novaCasa[1] = posY+2;
 				return novaCasa;
 			}
 		}
 	}else {
 		int j = posY;
 		for(int i = posX+1; i <= 6; i++) {
 			j++;
     		if(j <= 6) {
     			if(jogo.getTabuleiro().getCasaGrid(i, j).getOcupada() == true) {
     				if(jogo.getTabuleiro().getCasaGrid(i, j) != jogo.getTabuleiro().getCasaGrid(posX, posY)) {
     				if(jogo.getTabuleiro().getCasaGrid(i, j).getPeca().getJogador() == jogo.getAtualJogador()) {
     				if(jogo.getTabuleiro().getCasaGrid(i+1, j+1).getOcupada() == false) {
     					int[] novaCasa = new int[2];
 		 				novaCasa[0] = i+1;
 		 				novaCasa[1] = j+1;
     					return novaCasa;
     				}
     				}else {
     					break;
     				}
     			}
     		}
     		}
 		}
 	}
 		}else {
     		return null;
     	}
 	}else {
 		return null;
 	}
 	return null;
 }

@Override
public Jogador vencedor() {
	// TODO Auto-generated method stub
	return null;
}

}
