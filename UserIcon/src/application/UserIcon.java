package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class UserIcon extends VBox {

	@FXML
	private VBox userPane;
	
	@FXML
	private Circle icon;
	
	@FXML
	private Label username;
	
	private boolean selected = false;
	
	public UserIcon(String icon, String username) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("UserIcon.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		loader.load();
		
		if (icon.isEmpty())
			this.icon.setFill(new ImagePattern(new Image("/resources/user.png")));
		else
			setIcon(icon);
		setUsername(username);
	}
	
	public void setIcon(String icon) {
		this.icon.setFill(new ImagePattern(new Image(icon)));
	}
	
	public void setUsername(String username) {
		this.username.setText(username);
	}
	
	public void select(MouseEvent e) {
		selected = true;
	}
	
	public boolean isSelected() {
		return selected;
	}
}