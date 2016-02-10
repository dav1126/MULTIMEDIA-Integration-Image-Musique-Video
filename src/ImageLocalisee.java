

import javafx.scene.image.Image;

/**
 * Classe enfant de Image permettant d'avoir un getter sur l'attribut URL
 * @author 0345162
 *
 */
public class ImageLocalisee extends Image{
	
	/**
	 * Url de l'image sous forme de String
	 */
	private final String url;
	
	/**
	 * Constructeur de Imagelocalisee. Utilise le constructeur de son parent
	 * mais conserve l'url sous forme de String en attribut.
	 * @param url
	 */
	public ImageLocalisee(String url) {
        
		super(url);
        this.url = url;
    }

	/**
	 * Retourne le URL de l'image sous forme de String
	 * @return
	 */
    public String getURL() {
        
    	return url;
    }
}
