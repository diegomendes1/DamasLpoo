package interfaces;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MenuScreenController implements Initializable{
	
	 @FXML
	 private Button botaoIniciar;
	 
	 @FXML
	 private Button botaoAjuda;
	 
	 @FXML
	 private Button botaoSair;
	 
	 @FXML
	 private CheckBox isTelaCheia;
	 
	 @FXML
	 private ImageView fundo;
	 
	 @FXML
	 private ImageView titulo;
	 
	 @FXML
	 private TextField jogador1Name;
	 
	 @FXML
	 private TextField jogador2Name;
	 
	 
	public MenuScreenController() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Image imgFundo = new Image("/resources/back1.png");
		fundo.setImage(imgFundo);
		
		Image imgTitulo = new Image("/resources/titulo.png");
		titulo.setImage(imgTitulo);
		
		/*TranslateTransition tt = new TranslateTransition();
		tt.setDuration(Duration.seconds(10));
		tt.setNode(botaoIniciar);
		
		tt.setToX(250);
		
		
		tt.play();*/
		
	}
	
	@FXML
	protected void alternarTelaCheia() {
		
		Stage stage = (Stage) isTelaCheia.getScene().getWindow();
		if(this.isTelaCheia.isSelected()) {
			stage.setFullScreen(true);
		}else {
			stage.setFullScreen(false);
		}
	}
	
	
	//Desejaria transformar os tres metodos em um so, mudando apenas a string do nome. Mas parece que chamar o metodo assim do fxml nao da :(
	@FXML
	protected void abrirCenaJogo(ActionEvent event) throws Exception{
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/interfaces/TelaJogo.fxml"));
		String nome1 = null;
		String nome2 = null;
		
		if(jogador1Name.getText() != null) {
			if(jogador1Name.getText().trim().isEmpty()){
				jogador1Name.setText("Jogador 1");
			}else {
				nome1 = jogador1Name.getText();
			}
		}
		
		if(jogador2Name.getText() != null) {
			if(jogador2Name.getText().trim().isEmpty()){
				jogador2Name.setText("Jogador 2");
			}else {
				nome2 = jogador2Name.getText();
			}
		}
		
		TelaJogoController controller = new TelaJogoController(jogador1Name.getText(), jogador2Name.getText());
		fxmlloader.setController(controller);
		Parent root = (Parent)fxmlloader.load();
		Stage stage = (Stage) ((Node) event.getTarget()).getScene().getWindow();
		Scene cenaJogo = new Scene(root);
		stage.setScene(cenaJogo);
	}
	
	@FXML
	protected void abrirCenaAjuda(ActionEvent event) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("/interfaces/AjudaMenu.fxml"));

		Stage stage = (Stage) ((Node) event.getTarget()).getScene().getWindow();
		Scene cenaAjuda = new Scene(root);
		stage.setScene(cenaAjuda);
	}
	
	@FXML
	protected void fecharJogo(ActionEvent event) throws Exception{
		Stage stage = (Stage) ((Node) event.getTarget()).getScene().getWindow();
		stage.close();
	}
	

}