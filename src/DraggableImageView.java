import javafx.event.EventHandler;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;


public class DraggableImageView extends ImageView
{
	DraggableImageView source;
	ImageLocalisee imgLocalisee;

	public DraggableImageView()
	{
		super();
		source = this;
		setEventHandler();
	}
	
	public void setImgLocalisee(String imgLocalisee)
	{
		this.setImage(new ImageLocalisee("Javafx_logo_color.png"));
		this.imgLocalisee = new ImageLocalisee(imgLocalisee);
		super.setImage(new ImageLocalisee(imgLocalisee));
	}
	
	public void setEventHandler()
	{
		this.setOnDragDetected(new EventHandler<MouseEvent>() 
		{
		    public void handle(MouseEvent event) 
		    {
		        /* drag was detected, start a drag-and-drop gesture*/
		        /* allow any transfer mode */
		        Dragboard db = source.startDragAndDrop(TransferMode.ANY);
		        
		        /* Put a string on a dragboard */
		        ClipboardContent content = new ClipboardContent();
		        content.putUrl(imgLocalisee.getURL());
		        db.setContent(content);
		        
		        event.consume();
		    }
		});
		
		this.setOnDragDone(new EventHandler<DragEvent>()
		{
			 public void handle(DragEvent event) 
			 {
				if(event.getTransferMode() == TransferMode.MOVE)
		    	{
		    		((ImageView)event.getSource()).setImage(null);
		    	}
			 }
		});
		
		this.setOnDragDropped(new EventHandler<DragEvent>()
		{
			public void handle(DragEvent event) 
			 {
		    	Dragboard db = event.getDragboard();
		    	boolean success = false;
		    	if (db.hasUrl())
		    	{
			    	ImageView target = (ImageView)event.getTarget();
			    	target.setImage(new ImageLocalisee(db.getUrl()));
			    	success = true;
		    	}
		    	event.setDropCompleted(success);
			 }
		});
		
		this.setOnDragEntered(new EventHandler<DragEvent>()
		{
			 public void handle(DragEvent event) 
			 {
				Dragboard db = event.getDragboard();
		    	if (db.hasUrl())
		    	{
		    		((ImageView)event.getTarget()).setEffect(new Glow(0.8));
		    	}
			 }
		});
		
		this.setOnDragExited(new EventHandler<DragEvent>()
		{
			 public void handle(DragEvent event) 
			 {
				Dragboard db = event.getDragboard();
		    	if (db.hasUrl())
		    	{
		    		((ImageView)event.getTarget()).setEffect(null);
		    	}
			 }
		});
		
		this.setOnDragOver(new EventHandler<DragEvent>()
		{
			 public void handle(DragEvent event) 
			 {
				 ImageView target = (ImageView)event.getTarget();
			    	
		    	if (event.getGestureSource() != target && event.getDragboard().hasUrl())
		    	{
		    		event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
		    	}
			 }
		});
	}
}
