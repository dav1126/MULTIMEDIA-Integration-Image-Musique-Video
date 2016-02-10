import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AppLauncher extends Application
{
	public static void main(String[] args)
	{		
			Application.launch(args);		
	}
	
	public void start(Stage pStage) throws Exception
	{		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("mainInterface.fxml"));
		TabPane root = (TabPane) loader.load();
			
		Scene scene = new Scene(root);
		pStage.setScene(scene);
		pStage.setTitle("TP2 - Intégration - David St-Pierre ");
		pStage.show();		
	}
}
