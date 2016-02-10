
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.text.Position;

import com.sun.javafx.scene.control.SelectedCellsMap;

import javafx.animation.Animation.Status;
import javafx.animation.FillTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class FXMLMusiqueController implements Initializable
{
	@FXML
    private AnchorPane animAnchorPane;
	
	@FXML
	private VBox panneauControle;
	
	@FXML
	private Label chansonLabel;

	@FXML
	private Label volumeLabel;

	@FXML
	private Slider volumeSlider;

	@FXML
	private Label lectureLabel;

	@FXML
	private Button reculerButton;

	@FXML
	private ProgressBar progressBar;

	@FXML
	private Button avancerButton;

	@FXML
	private Button jouerButton;

	@FXML
	private Button pauseButton;

	@FXML
	private Button stopButton;

	@FXML
	private TextArea textArea;

	@FXML
	private CheckBox boucleCheckBox;

	@FXML
	private StackPane animStackPane;
	
	@FXML
    private Rectangle rectangle;
	
	MediaPlayer mediaPlayer;
	
	Media media;
	
	URL song;
	
	FillTransition ft;
	
	DecimalFormat df = new DecimalFormat("###");

	@FXML
	void avancer()
	{
		mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(30)));
	}

	@FXML
	void jouer()
	{
		mediaPlayer.play();
	}

	@FXML
	void pause()
	{
		mediaPlayer.pause();
	}

	@FXML
	void reculer()
	{
		mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(Duration.seconds(30)));
	}

	@FXML
	void rejouerEnBoucleOnOff()
	{

	}

	@FXML
	void stop()
	{
		mediaPlayer.stop();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		song = getClass().getResource("Kalimba.mp3");
		media = new Media(song.toString());
		mediaPlayer = new MediaPlayer(media);
		setFileName();
		
		pauseButton.setDisable(true);
		stopButton.setDisable(true);
		mediaPlayer.setVolume(0.216);
		volumeSlider.setValue(60);
		volumeSlider.setMin(20);
		volumeSlider.setMax(100);
		
		setDnD();
		manageNewMedia();
		
		//Ajustement du volume et du volumeLabel à l'aide du slider
		volumeSlider.valueProperty().addListener(new ChangeListener<Object>()
		{
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue)
			{					
				double volumeRatio = (((double)newValue-20)/80)*100;
				volumeLabel.setText(String.valueOf(df.format(volumeRatio) + "%"));
				mediaPlayer.setVolume(Math.pow((double)newValue/100, 3));
				ft.setRate(((double)newValue/100)*2 + 0.5);
			}	
		});
		
		ft = new FillTransition(Duration.millis(3000), rectangle, Color.BLACK, Color.WHITE);
		ft.setAutoReverse(true);
		ft.setCycleCount(-1);
		rectangle.setBlendMode(BlendMode.DIFFERENCE);
		animAnchorPane.prefWidthProperty().bind(animStackPane.widthProperty());
		animAnchorPane.prefHeightProperty().bind(animStackPane.heightProperty());
		rectangle.heightProperty().bind(animAnchorPane.heightProperty());
		rectangle.widthProperty().bind(animAnchorPane.widthProperty());
		
	}
	
	private void updateMetadata()
	{
		String textAreaMsg = "";
		ObservableMap<String, Object> metadonneesMap = media.getMetadata();
		for (Map.Entry<String, Object> entry : metadonneesMap.entrySet())
		{
			if (!entry.getKey().contains("raw"))
			{
				textAreaMsg += entry.getKey() + ": " + entry.getValue() + "\n";
			}
		}
		if (textAreaMsg.equals(""))
			textArea.setText("Aucune métadonnée disponible");
		else
		{
			textArea.setText(textAreaMsg);
			textArea.selectPositionCaret(0);
		}
	}
	
	private void setFileName()
	{
		System.out.println(media.getSource());
		String[] songURLSplit = media.getSource().split("/");
		String name = songURLSplit[songURLSplit.length-1].replace("%20"," ");
		chansonLabel.setText(name);
	}
	
	public String getFormattedCurrentTrackTime()
	{
		Duration currentTime = mediaPlayer.getCurrentTime();
		String currentTimeMin = String.valueOf(currentTime.toMinutes());
		String[] currentTimeMinSplit = currentTimeMin.split("\\.");
		String currentTimeMinWhole = currentTimeMinSplit[0];
		
		String currentTimeSec = df.format(currentTime.toSeconds() % 60);
		if (Integer.parseInt(currentTimeSec) < 10)
		{
			currentTimeSec = "0" + currentTimeSec;
		}
		String formattedCurrentTime = currentTimeMinWhole + ":" + currentTimeSec;
		return formattedCurrentTime;
	}
	
	public String getFormattedTrackDuration()
	{
		Duration trackDuration = mediaPlayer.getMedia().getDuration();
		String durationMin = String.valueOf(trackDuration.toMinutes());
		String[] durationMinSplit = durationMin.split("\\.");
		String durationMinWhole = durationMinSplit[0];
		
		String durationSec = df.format(trackDuration.toSeconds() % 60);
		if (Integer.parseInt(durationSec) < 10)
		{
			durationSec = "0" + durationSec;
		}
		String formattedDuration = durationMinWhole + ":" + durationSec;
		return formattedDuration;
	}
	
	public void setDnD()
	{
		panneauControle.setOnDragDropped(new EventHandler<DragEvent>()
		{
			public void handle(DragEvent event) 
			 {
		    	Dragboard db = event.getDragboard();
		    	if (db.hasUrl() && (db.getUrl().contains(".mp3") || db.getUrl().contains(".wav")))
		    	{
		    		File file = db.getFiles().get(0);
			    	try
					{
			    		media = new Media(file.toURI().toURL().toExternalForm());
			    		if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING))
			    		{	System.out.println("blablabla");
			    			mediaPlayer.dispose();
							mediaPlayer = new MediaPlayer(media);
							manageNewMedia();
							mediaPlayer.setVolume(Math.pow((double)volumeSlider.getValue()/100, 3));
							mediaPlayer.play();
			    		}
			    		else
			    		{
			    			mediaPlayer.dispose();
			    			mediaPlayer = new MediaPlayer(media);
			    			manageNewMedia();	
			    		}						
						
					} catch (MalformedURLException e)
			    	{
						e.printStackTrace();
					}	
			    	setFileName();
		    	}
		    	else
		    	{
		    		Alert alert = new Alert(AlertType.ERROR);
		    		alert.setTitle("Erreur");
		    		alert.setHeaderText(null);
		    		alert.setContentText("Seuls les fichiers .mp3 et .wav sont accepté");
		    		alert.show();
		    	}
			 }
		});
		
		panneauControle.setOnDragEntered(new EventHandler<DragEvent>()
		{
			 public void handle(DragEvent event) 
			 {
				Dragboard db = event.getDragboard();
		    	if (db.hasUrl() && (db.getUrl().contains(".mp3") || db.getUrl().contains(".wav")))
		    	{
		    		((VBox)event.getTarget()).setEffect(new Glow(0.8));
		    	}
			 }
		});
		
		panneauControle.setOnDragExited(new EventHandler<DragEvent>()
		{
			 public void handle(DragEvent event) 
			 {
				Dragboard db = event.getDragboard();
		    	if (db.hasUrl() && (db.getUrl().contains(".mp3") || db.getUrl().contains(".wav")))
		    	{
		    		((VBox)event.getTarget()).setEffect(null);
		    	}
			 }
		});
		
		panneauControle.setOnDragOver(new EventHandler<DragEvent>()
		{
			 public void handle(DragEvent event) 
			 {
				Dragboard db = event.getDragboard();
			    	
		    	if (event.getGestureSource() != event.getTarget() && db.hasUrl() && (db.getUrl().contains(".mp3") || db.getUrl().contains(".wav")))
		    	{
		    		event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
		    	}
			 }
		});
	}
	
	public void manageNewMedia()
	{
		//Listener pour disableProperty des bouttons en fonction du statut (PLAY, PAUSE, STOP) du MediaPlayer
			mediaPlayer.statusProperty().addListener(new ChangeListener<Object>()
			{
				@Override
				public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue)
				{
					if (mediaPlayer.getStatus().equals(MediaPlayer.Status.READY))
					{					
						updateMetadata();					
						lectureLabel.setText(getFormattedCurrentTrackTime() + "/" + getFormattedTrackDuration());
						avancerButton.setDisable(true);
						reculerButton.setDisable(true);
						jouerButton.requestFocus();
					}
					
					else if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING))
					{
						jouerButton.setDisable(true);
						stopButton.setDisable(false);
						pauseButton.setDisable(false);
						avancerButton.setDisable(false);
						reculerButton.setDisable(false);
						ft.play();
					}
					else if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PAUSED))
					{
						jouerButton.setDisable(false);
						stopButton.setDisable(false);
						pauseButton.setDisable(true);
						jouerButton.requestFocus();
						avancerButton.setDisable(true);
						reculerButton.setDisable(true);
						ft.pause();
					}
					else if (mediaPlayer.getStatus().equals(MediaPlayer.Status.STOPPED))
					{
						jouerButton.setDisable(false);
						stopButton.setDisable(true);
						pauseButton.setDisable(true);
						avancerButton.setDisable(true);
						reculerButton.setDisable(true);
						jouerButton.requestFocus();
						ft.pause();
					}				
				}
			});
			
			//Bind entre le currentTime et la barre de progression et son label
			mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Object>()
			{
				@Override
				public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue)
				{
					lectureLabel.setText(getFormattedCurrentTrackTime() + "/" + getFormattedTrackDuration());
					progressBar.setProgress(mediaPlayer.getCurrentTime().toMillis()/mediaPlayer.getMedia().getDuration().toMillis());
				}				
			});
				
			//Gérer la fin de la chanson selon l'option de boucle
			When whenBoucle = Bindings.when(boucleCheckBox.selectedProperty());
			mediaPlayer.cycleCountProperty().bind(whenBoucle.then(MediaPlayer.INDEFINITE).otherwise(1));
			
			mediaPlayer.setOnEndOfMedia(new Runnable() 
			{
	            public void run() 
		        {
		            if (mediaPlayer.getCycleCount() == 1)
		            {
		            	stopButton.fire();
		            	mediaPlayer.seek(Duration.ZERO);
		            }
	            }
	         });
	}
}
