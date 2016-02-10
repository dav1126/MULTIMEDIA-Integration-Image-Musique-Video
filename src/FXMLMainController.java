import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class FXMLMainController implements Initializable
{
    @FXML
    private AnchorPane imageAnchorPane;

    @FXML
    private AnchorPane musiqueAnchorPane;

    @FXML
    private AnchorPane videoAnchorPane;
    
    ImageApp imageApp;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		try
		{
			imageApp = new ImageApp();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		imageAnchorPane.getChildren().add(imageApp.getRoot());
		AnchorPane.setLeftAnchor(imageApp.getRoot(), 0.0);
		AnchorPane.setTopAnchor(imageApp.getRoot(), 10.0);
		AnchorPane.setBottomAnchor(imageApp.getRoot(), 10.0);
		AnchorPane.setRightAnchor(imageApp.getRoot(), 10.0);
		
		FXMLLoader loaderMusique = new FXMLLoader(getClass().getResource("interfaceMusique.fxml"));
		AnchorPane musiqueRoot = null;
		try
		{
			musiqueRoot = (AnchorPane) loaderMusique.load();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		musiqueAnchorPane.getChildren().add(musiqueRoot);
		StackPane musiqueRootChild = (StackPane)musiqueRoot.getChildren().get(1);
		musiqueRootChild.getChildren().get(0).setTranslateY(-40);
		
		AnchorPane.setLeftAnchor(musiqueRoot, 0.0);
		AnchorPane.setTopAnchor(musiqueRoot, 10.0);
		AnchorPane.setBottomAnchor(musiqueRoot, 10.0);
		AnchorPane.setRightAnchor(musiqueRoot, 10.0);
		
		OverlayMediaPlayer videoApp = new OverlayMediaPlayer();
		videoApp.stop();
		videoAnchorPane.getChildren().add(videoApp.getRoot());
		AnchorPane.setLeftAnchor(videoApp.getRoot(), 0.0);
		AnchorPane.setTopAnchor(videoApp.getRoot(), 10.0);
		AnchorPane.setBottomAnchor(videoApp.getRoot(), 10.0);
		AnchorPane.setRightAnchor(videoApp.getRoot(), 10.0);
	} 
}
