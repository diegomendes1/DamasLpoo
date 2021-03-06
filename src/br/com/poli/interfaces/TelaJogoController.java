package br.com.poli.interfaces;

import java.net.URL;
import java.util.ResourceBundle;
import br.com.poli.CapturaInvalidaException;
import br.com.poli.CapturaMultiplaException;
import br.com.poli.Interface;
import br.com.poli.Jogador;
import br.com.poli.Jogo;
import br.com.poli.MovimentoInvalidoException;
import br.com.poli.componentes.Casa;
import br.com.poli.componentes.Tabuleiro;
import br.com.poli.damIA.JogadorAutonomo;
import br.com.poli.enums.CorPeca;
import br.com.poli.enums.Resultado;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TelaJogoController implements Initializable{
	
	@FXML
	private GridPane tabuleiro;
	@FXML
	private GridPane tabPecas;
	@FXML
	private GridPane efeitosPane;
	@FXML
	private GridPane pecasJogador1;
	@FXML
	private GridPane pecasJogador2;
	@FXML
	private Label qualJogador;
	@FXML 
	private Label tempoQuePassou;
	@FXML
	private AnchorPane gameOverMenu;
	@FXML
	private Text textoResultado;
	@FXML
	private Text jogadorVencedor;
	@FXML
	private Text tempoPassado;
	@FXML
	private Text desistenciaTexto;
	@FXML
	private AnchorPane desistenciaMenu;
	@FXML
	private Label contadorJogadas;
	@FXML
	private Rectangle erroFundo;
	@FXML
	private Text erroTexto;
	
	private Casa casaOrigem;
	private Casa casaDestino;
	private int pecasCapturadasJogador1;
	private int pecasCapturadasJogador2;
	private boolean fimDeJogo = false;
	
	private String jogador1Nome, jogador2Nome;
	
	private Interface jogo;
	private JogadorAutonomo jogador3;
	
	private boolean isUmJogador;

	public TelaJogoController(String jogador1, String jogador2, boolean isUmJogador) {
		this.jogador1Nome = jogador1;
		this.jogador2Nome = jogador2;
		this.isUmJogador = isUmJogador;
	}
	
	public void initialize(URL location, ResourceBundle resources) {
		gameOverMenu.setVisible(false);
		this.contadorJogadas.setText(" ");
		long inicioTempo = System.currentTimeMillis();
		
		new AnimationTimer() {
			long minPassado;
			@Override
			public void handle(long agora) {
				if(!fimDeJogo) {
				long tempoPassado = System.currentTimeMillis() - inicioTempo;
				minPassado = tempoPassado/60000;
				tempoQuePassou.setText("Tempo de Jogo: " + tempoPassado/60000 + ":"+ (tempoPassado/1000 - minPassado*60));
				}
			}
		}.start();
		
		rodandoJogo();
    }
	
	@FXML
	protected void voltarMenu(ActionEvent event) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("/br/com/poli/interfaces/MenuScreen.fxml"));
		Stage stage = (Stage) ((Node) event.getTarget()).getScene().getWindow();
		Scene cenaMenu = new Scene(root);
		stage.setScene(cenaMenu);
	}
	
	@FXML
	protected void abrirCenaJogo(ActionEvent event) throws Exception{
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/br/com/poli/interfaces/TelaJogo.fxml"));
		
		TelaJogoController controller = new TelaJogoController(jogador1Nome, jogador2Nome, isUmJogador);
		fxmlloader.setController(controller);
		Parent root = (Parent)fxmlloader.load();
		Stage stage = (Stage) ((Node) event.getTarget()).getScene().getWindow();
		Scene cenaJogo = new Scene(root);
		stage.setScene(cenaJogo);
	}
	
	public void rodandoJogo() {
        
		tabPecas.setDisable(true);
		
		Tabuleiro tab = new Tabuleiro();
		if(!isUmJogador) {
			Jogador jogador1 = new Jogador(false);
			Jogador jogador2 = new Jogador(false);
			jogador1.setNome(jogador1Nome);
			jogador2.setNome(jogador2Nome);
		tab.gerarTabuleiro(jogador1, jogador2);
		jogo = new Jogo(jogador1, jogador2, null, tab, null, false);
		}else {
			Jogador jogador1 = new Jogador(false);
			jogador3 = new JogadorAutonomo();
			jogador1.setNome(jogador1Nome);
			jogador3.setNome("Computador");
			tab.gerarTabuleiro(jogador1, jogador3);
			jogo = new Jogo(jogador1, jogador3, null, tab, null, true);
			jogador3.setJogo(jogo);
		}
		
		
		
		pecasCapturadasJogador1 = 0;
		pecasCapturadasJogador2 = 0;
		jogo.iniciarPartida();
		mostrarTabuleiro(jogo.getTabuleiro());
		mostrarPecasTabuleiro(jogo.getTabuleiro(), false);
		mostrarPecasCapturadas(true, pecasJogador1, pecasCapturadasJogador1);
		mostrarPecasCapturadas(false, pecasJogador2, pecasCapturadasJogador2);
		limparEfeitos();
		this.qualJogador.setText("Jogue, " + jogo.getJogador1().getNome());
		erroFundo.setVisible(false);
		erroTexto.setVisible(false);
	}
	
	@FXML
	protected void botaoDesistir() {
		desistenciaMenu.setVisible(true);
		qualJogador.setVisible(false);
		desistenciaTexto.setText(jogo.getAtualJogador().getNome() + ", DESEJA DESISTIR DA PARTIDA?");
	}
	
	@FXML
	public void jogadorDesistiu() {
		jogo.isFimDeJogo(true);
		desistenciaMenu.setVisible(false);
		fimDeJogo();
	}
	
	@FXML
	public void naoDesistiu() {
		desistenciaMenu.setVisible(false);
		qualJogador.setVisible(true);
	}
	
	public void setCasaOrigem(Casa casa) {
		this.casaOrigem = casa;
	}
	
	public void setCasaDestino(Casa casa) {
		this.casaDestino = casa;
	}
	
	public void limparEfeitos() {
		efeitosPane.getChildren().clear();
	}
	
	public void mostrarPecasTabuleiro(Tabuleiro tabuleiro, boolean captura) {
		Image imgPecaClara = new Image("/br/com/poli/resources/clara.png");
		Image imgPecaEscura = new Image("/br/com/poli/resources/escura.png");
		Image imgPecaClaraDama = new Image("/br/com/poli/resources/claraDama.png");
		Image imgPecaEscuraDama = new Image("/br/com/poli/resources/escuraDama.png");
		
		DropShadow dropShadow = new DropShadow();
		 dropShadow.setRadius(5.0);
		 dropShadow.setOffsetX(3.0);
		 dropShadow.setOffsetY(3.0);
		 dropShadow.setColor(Color.color(0, 0, 0));
		 
		 GaussianBlur blur = new GaussianBlur();
		 blur.setRadius(50);
		 
		tabPecas.getChildren().clear();
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(tabuleiro.getGrid()[i][j].getPeca() != null) {
						if(tabuleiro.getGrid()[i][j].getPeca().getCor() == CorPeca.ESCURO) {
							ImageView imagemPecaEscura = null;
							if(!tabuleiro.getGrid()[i][j].getPeca().getIsDama()) {
							imagemPecaEscura = new ImageView(imgPecaEscura);
							}else {
								imagemPecaEscura = new ImageView(imgPecaEscuraDama);
							}
							imagemPecaEscura.setDisable(true);
							imagemPecaEscura.setEffect(dropShadow);
							if(captura) {
								if(jogo.getTabuleiro().getCasaGrid(i, j).getOcupada() && 
										jogo.getTabuleiro().getCasaGrid(i, j).getPeca().getJogador() == jogo.getAtualJogador()) {
									if(!jogo.verificarPossibilidadeCapturaCasa(jogo.getTabuleiro().getCasaGrid(i, j))) {
										imagemPecaEscura.setEffect(blur);
									}else if(jogo.getCasaCapturaMultipla() != null) {
										if(jogo.getTabuleiro().getCasaGrid(i, j) != jogo.getCasaCapturaMultipla()) {
										imagemPecaEscura.setEffect(blur);
									}
									}
								}
							}
							
							this.tabPecas.add(imagemPecaEscura, j, i);
							
	    				}else {
	    					ImageView imagemPecaClara = null;
	    					if(!tabuleiro.getGrid()[i][j].getPeca().getIsDama()) {
								imagemPecaClara = new ImageView(imgPecaClara);
								}else {
									imagemPecaClara = new ImageView(imgPecaClaraDama);
								}
	    					imagemPecaClara.setDisable(true);
	    					imagemPecaClara.setEffect(dropShadow);
	    					if(captura) {
		    					if(jogo.getTabuleiro().getCasaGrid(i, j).getOcupada() && 
		    							jogo.getTabuleiro().getCasaGrid(i, j).getPeca().getJogador() == jogo.getAtualJogador()) {
		    						if(!jogo.verificarPossibilidadeCapturaCasa(jogo.getTabuleiro().getCasaGrid(i, j))) {
		    							imagemPecaClara.setEffect(blur);
		    						}
		    					}
		    				}
	    					
	    					this.tabPecas.add(imagemPecaClara, j, i);
	    				}
					}
				}
			
			
		}
	}
	public void mostrarTabuleiro(Tabuleiro tabuleiro) {
		Image imgCasaBranca = new Image("/br/com/poli/resources/white.png");
		Image imgCasaPreta = new Image("/br/com/poli/resources/black.png");
		Image imgCasaSelecionadaP1 = new Image("/br/com/poli/resources/casaSelecionadaP1.png");
		Image imgCasaSelecionadaP2 = new Image("/br/com/poli/resources/casaSelecionadaP2.png");
		
		boolean atualBranca = true;
		for(int i = 0; i < 8; i++) {
			for(int j =0; j < 8; j++) {
				
				ColumnConstraints cc = new ColumnConstraints();
			    cc.setHgrow(Priority.ALWAYS);
			    this.tabuleiro.getColumnConstraints().add(cc);
			    
			    RowConstraints rc = new RowConstraints();
			    rc.setVgrow(Priority.ALWAYS);
			    this.tabuleiro.getRowConstraints().add(rc);
			    
				if(atualBranca) {
					ImageView imagem = new ImageView(imgCasaBranca);
					this.tabuleiro.add(imagem, j, i);
					atualBranca = false;
				}else {
					
					ImageView imagem = new ImageView(imgCasaPreta);
					imagem.setDisable(true);
					Button btn = new Button();
					btn.setPrefSize(60,60);
					btn.setStyle("-fx-focus-color: transparent;");
					this.tabuleiro.add(btn, j, i);
					this.tabuleiro.add(imagem, j, i);
					atualBranca = true;
					
					btn.setOnAction((e) -> {
						if(casaOrigem == null) {
							casaOrigem = tabuleiro.getCasaGrid(GridPane.getRowIndex(btn), GridPane.getColumnIndex(btn));
							if(jogo.getAtualJogador() == jogo.getJogador1()) {
							ImageView imagemSelecionada = new ImageView(imgCasaSelecionadaP2);
							this.efeitosPane.add(imagemSelecionada, casaOrigem.getPosY(), casaOrigem.getPosX());
							}else {
								ImageView imagemSelecionada = new ImageView(imgCasaSelecionadaP1);
								this.efeitosPane.add(imagemSelecionada, casaOrigem.getPosY(), casaOrigem.getPosX());
							}
						}else {
							casaDestino = tabuleiro.getCasaGrid(GridPane.getRowIndex(btn), GridPane.getColumnIndex(btn));
							try {
								
								if(isUmJogador) {
									if(jogo.getAtualJogador() == jogo.getJogador1() && jogo.getCasaCapturaMultipla() == null) {
									jogo.jogar(casaOrigem, casaDestino);
									jogo.trocarJogador();
									}
									//IA
									
									
									jogarIA(jogo);
									
									jogo.trocarJogador();
								}else {
									jogo.jogar(casaOrigem, casaDestino);
									jogo.trocarJogador();
								}
								
								
								
								mostrarPecasTabuleiro(tabuleiro, false);
								limparEfeitos();
								erroFundo.setVisible(false);
								erroTexto.setVisible(false);
							}catch(MovimentoInvalidoException excecao) {
								System.out.println(excecao);
								mostrarPecasTabuleiro(tabuleiro, false);
								limparEfeitos();
								erroFundo.setVisible(true);
								erroTexto.setVisible(true);
								erroTexto.setText(excecao.getMessage());
							}catch(CapturaInvalidaException excecao){
								System.out.println(excecao);
								mostrarPecasTabuleiro(tabuleiro, true);
								limparEfeitos();
								erroFundo.setVisible(true);
								erroTexto.setVisible(true);
								erroTexto.setText(excecao.getMessage());
							}catch(CapturaMultiplaException excecao) {
								System.out.println(excecao);
								mostrarPecasTabuleiro(tabuleiro, true);
								limparEfeitos();
								erroFundo.setVisible(true);
								erroTexto.setVisible(true);
								erroTexto.setText(excecao.getMessage());
							}
							
							
							atualizarPecasCapturadas(jogo);
							mostrarPecasCapturadas(true, pecasJogador1, pecasCapturadasJogador1);
							mostrarPecasCapturadas(false, pecasJogador2, pecasCapturadasJogador2);
							this.qualJogador.setText("Jogue, " + jogo.getAtualJogador().getNome());
							if(jogo.getJogadas() <= 0) {
								this.contadorJogadas.setText(" ");
							}else {
								this.contadorJogadas.setText(20 - (jogo.getJogadas()) + " Jogadas para empate");
							}
							casaOrigem = null;
							casaDestino = null;
							if(jogo.isFimDeJogo(false)) {
								fimDeJogo();
							}
						}
					});
				}
			}
			
			if(atualBranca) {
				atualBranca = false;
			}else {
				atualBranca = true;
			}
		}
		
	}
	
	public void jogarIA(Interface jogo) throws MovimentoInvalidoException, CapturaInvalidaException, CapturaMultiplaException {
		int[] escolha = jogador3.jogarAuto();
		//System.out.println("(" + escolha[1]+","+ escolha[2] + ") (" + escolha[3] + "," + escolha[4] + ")");
		jogo.jogar(jogo.getTabuleiro().getCasaGrid(escolha[1], escolha[2]), jogo.getTabuleiro().getCasaGrid(escolha[3], escolha[4]));
	}
	
	public void fimDeJogo() {
		fimDeJogo = true;
		qualJogador.setVisible(false);
		gameOverMenu.setVisible(true);
		tempoPassado.setText(tempoQuePassou.getText());
		if(jogo.getResultado() == Resultado.empate) {
			textoResultado.setVisible(false);
			jogadorVencedor.setText("EMPATE");
		}else if(jogo.getResultado() == Resultado.comVencedorJogador1 ||
				jogo.getResultado() == Resultado.comVencedorJogador2) {
			jogadorVencedor.setText(jogo.getVencedor().getNome());
		}
	}
	
	public void mostrarPecasCapturadas(boolean isJogador1, GridPane grid, int totalPecas) {
		Image imgPecaClara = new Image("/br/com/poli/resources/clara.png");
		Image imgPecaEscura = new Image("/br/com/poli/resources/escura.png");
		int pecasMostradas = 0;
		
		DropShadow dropShadow = new DropShadow();
		 dropShadow.setRadius(5.0);
		 dropShadow.setOffsetX(3.0);
		 dropShadow.setOffsetY(3.0);
		 dropShadow.setColor(Color.color(0, 0, 0));
		 
		grid.getChildren().clear();
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 2; j++) {
				if(pecasMostradas < totalPecas) {
					pecasMostradas++;
					if(isJogador1) {
						ImageView imagem = new ImageView(imgPecaClara);
						imagem.setFitHeight(48);
						imagem.setFitWidth(48);
						imagem.setEffect(dropShadow);
						grid.add(imagem, j, i);
					}else {
						ImageView imagem = new ImageView(imgPecaEscura);
						imagem.setFitHeight(48);
						imagem.setFitWidth(48);
						imagem.setEffect(dropShadow);
						grid.add(imagem, j, i);
					}
					
				}
			}
		}
	}
	
	public void atualizarPecasCapturadas(Interface jogo) {
		pecasCapturadasJogador1 = jogo.getPecasCapturadas1();
		pecasCapturadasJogador2 = jogo.getPecasCapturadas2();
	}
	
}
