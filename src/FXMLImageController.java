import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class FXMLImageController implements Initializable{

	@FXML
    private GridPane blurGridPane;
	
    @FXML
    private Label angleLabel;

    @FXML
    private Slider angleSlider;

    @FXML
    private TextField transparenceTextField;

    @FXML
    private Slider transparenceSlider;

    @FXML
    private CheckBox blurCheckBox;

    @FXML
    private Spinner<Integer> blurHeightSpinner;

    @FXML
    private Spinner<Integer> blurWidthSpinner;

    @FXML
    private Spinner<Integer> blurIterationsSpinner;

    @FXML
    private Button resetButton;

    @FXML
    private Button quitterButton;
    
    @FXML
    private StackPane imgStackPane;
    
    DraggableImageView dragImgView;
    
    BooleanProperty changement = new SimpleBooleanProperty(false);

    @FXML
    void quitter() 
    {
    	System.exit(0);
    }

    @FXML
    void reset() 
    {
    	angleSlider.setValue(0);
    	transparenceSlider.setValue(0);
    	blurHeightSpinner.getValueFactory().setValue(5);
    	blurWidthSpinner.getValueFactory().setValue(5);
    	blurIterationsSpinner.getValueFactory().setValue(1);
    	blurCheckBox.setSelected(false);
    	changement.set(false);
    	resetButton.setDisable(true);
    }
    
    @SuppressWarnings("unchecked")
	public void initialize(URL location, ResourceBundle resources)
    {
    
    	//Set the default blur values
    	SpinnerValueFactory spFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 5);
    	SpinnerValueFactory spFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 5);
    	SpinnerValueFactory spFactory3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 1);
    	blurHeightSpinner.setValueFactory(spFactory1);
    	blurWidthSpinner.setValueFactory(spFactory2);
    	blurIterationsSpinner.setValueFactory(spFactory3);
    	
    	//Set the draggable image
    	dragImgView = new DraggableImageView();
    	imgStackPane.getChildren().add(dragImgView);
    	dragImgView.setImgLocalisee("Javafx_logo_color.png");
    	dragImgView.setPreserveRatio(true);
    	dragImgView.getStyleClass().add("image");
    	
    	//Add listeners to the angle slider
    	angleSlider.valueProperty().addListener((observable, oldValue, newValue) -> 
    	angleLabel.setText(String.valueOf(Math.round(angleSlider.getValue())) + "°")
    			);
    	angleSlider.valueProperty().addListener((observable, oldValue, newValue) -> 
    	dragImgView.setRotate(angleSlider.getValue())
    			);
    	
    	angleLabel.textProperty().addListener(new ChangeListener()
    	{
			@Override
			public void changed(ObservableValue observable,
					Object oldValue, Object newValue)
			{
					changement.set(true);
			}	
		});	
    	
    	//Bind the opacity controls 	
    	transparenceSlider.valueProperty().addListener(new ChangeListener<Number>()
    	{
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue)
			{
					transparenceTextField.setText(String.valueOf(Math.round((Double)newValue)));
					changement.set(true);
			}	
		});	
    	
    	//Empecher le textfield de transparence d'accepter une valeur invalide
    	transparenceTextField.textProperty().addListener(new ChangeListener()
    	{
			@Override
			public void changed(ObservableValue observable,
					Object oldValue, Object newValue)
			{
				try
				{
					NumberFormat format = NumberFormat.getInstance(Locale.CANADA_FRENCH);
			        Number number = null;
					
					number = format.parse((String)newValue);
					
			        double d = number.doubleValue();
					dragImgView.setOpacity(1-d/100);
					transparenceSlider.setValue(d);
				}
				catch (Exception e)
				{
					transparenceTextField.setText((String)oldValue);
					transparenceSlider.setValue(Double.parseDouble((String)oldValue));
					System.out.println("Valeur d'opacité invalide");
				}	
			}	
		});	
    	
    	BoxBlur boxBlur = new BoxBlur();
    	
    	//Bind the blur checkbox to the blur gridpane disableProperty
    	blurCheckBox.selectedProperty().addListener(new ChangeListener()
    			{
					@Override
					public void changed(ObservableValue observable,
							Object oldValue, Object newValue)
					{
						if (blurCheckBox.isSelected())
						{
							blurGridPane.setDisable(false);
							dragImgView.setEffect(boxBlur);
						}
						else
						{
							blurGridPane.setDisable(true);
							dragImgView.setEffect(null);
						}
					}									
    			});
    	
    	//Listener pour empecher la saisie de valeur invalide dans les textfields des spinners
    	ChangeListener blurSpinnerChangeListenner = new ChangeListener<Object>()
    	{
			@Override
			public void changed(ObservableValue observable,
					Object oldValue, Object newValue)
			{
				try
				{
					if (observable == blurHeightSpinner.editorProperty().getValue().textProperty())
						blurHeightSpinner.getValueFactory().setValue(Integer.parseInt((String)newValue));
					else
						blurWidthSpinner.getValueFactory().setValue(Integer.parseInt((String)newValue));	
				}
				catch (Exception e)
				{
					if(observable == blurHeightSpinner.editorProperty().getValue().textProperty())
					{
						blurHeightSpinner.editorProperty().getValue().setText((String)oldValue);
						blurHeightSpinner.getValueFactory().setValue(Integer.parseInt((String)oldValue));
						System.out.println("Valeur de blur invalide.");
					}
					
					if(observable == blurWidthSpinner.editorProperty().getValue().textProperty())
					{
						blurWidthSpinner.editorProperty().getValue().setText((String)oldValue);
						blurWidthSpinner.getValueFactory().setValue(Integer.parseInt((String)oldValue));
						System.out.println("Valeur de blur invalide.");
					}
				}	
			}	
		};	
    	
		//Empecher la saisie de valeur invalide dans les textfields des spinners
    	blurHeightSpinner.editorProperty().getValue().textProperty().addListener(blurSpinnerChangeListenner);
    	blurWidthSpinner.editorProperty().getValue().textProperty().addListener(blurSpinnerChangeListenner);
    	blurIterationsSpinner.editorProperty().getValue().textProperty().addListener(new ChangeListener()
		{
			@Override
			public void changed(ObservableValue observable,
					Object oldValue, Object newValue)
			{
				try
				{
					blurIterationsSpinner.getValueFactory().setValue(Integer.parseInt((String)newValue));
				}
				catch (Exception e)
				{
						blurIterationsSpinner.editorProperty().getValue().setText((String)oldValue);
						blurIterationsSpinner.getValueFactory().setValue(Integer.parseInt((String)oldValue));
						System.out.println("Valeur de blur invalide.");
				}	
			}									
		});
    	
    	//Binder les spinner à la valeur de blur de l'image
    	blurHeightSpinner.valueProperty().addListener(new ChangeListener()
    			{
					@Override
					public void changed(ObservableValue observable,
							Object oldValue, Object newValue)
					{
						boxBlur.setHeight((Integer)newValue);
						changement.set(true);
					}									
    			});
    	
    	blurWidthSpinner.valueProperty().addListener(new ChangeListener()
    			{
					@Override
					public void changed(ObservableValue observable,
							Object oldValue, Object newValue)
					{
						boxBlur.setWidth((Integer)newValue);
						changement.set(true);
					}									
    			});
    	
    	blurIterationsSpinner.valueProperty().addListener(new ChangeListener()
		{
			@Override
			public void changed(ObservableValue observable,
					Object oldValue, Object newValue)
			{
				boxBlur.setIterations((Integer)newValue);
				changement.set(true);
			}									
		});
    	
    	changement.addListener(new ChangeListener()
    	{
			@Override
			public void changed(ObservableValue observable,
					Object oldValue, Object newValue)
			{
					if (changement.get())
					{
						resetButton.setDisable(false);
					}
					else
					{
						resetButton.setDisable(true);	
					}
			}	
		});	
    	
    	//Context menu
    	final ContextMenu contextMenu = new ContextMenu();
    	MenuItem item1 = new MenuItem("Effacer l'image");
    	item1.setOnAction(new EventHandler<ActionEvent>() {
    	    public void handle(ActionEvent e) {
    	    	dragImgView.setImgLocalisee("no_image.png");
    	    }
    	});
    	contextMenu.getItems().add(item1);
    	
    	dragImgView.setOnMouseClicked(new EventHandler<MouseEvent>() {
    		@Override
    	    public void handle(MouseEvent e) {
    	        contextMenu.show(dragImgView, Side.TOP, dragImgView.getFitWidth()/2, dragImgView.getFitHeight()/2);
    	    }
    	});
    	
    	//Responsivity: Bind the image size with its container size
    	dragImgView.fitHeightProperty().bind(imgStackPane.heightProperty());
    	dragImgView.fitWidthProperty().bind(imgStackPane.widthProperty());
    
    	
    }
}
