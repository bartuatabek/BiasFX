package application;

import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class NoteCell extends AnchorPane{

	@FXML
	private ImageView remove;

	@FXML
	private ImageView drag;

	@FXML
	private ImageView icon;

	@FXML
	private Label filename;

	@FXML
	private Pane cell;

	@FXML
	private AnchorPane notePane;

	private boolean on_edit = false;

	private boolean deleted = false;

	private boolean dragable = false;

	private String openfile = "";

	public NoteCell() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("NoteCell.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		loader.load();
	}

	public void edit(boolean editable) {
		Timeline transition = new Timeline(new KeyFrame(Duration.seconds(0.017), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {				
				if (editable) {
					if (icon.getLayoutX() < 40) {
						icon.setLayoutX(icon.getLayoutX() + 5);
					}

					if (filename.getLayoutX() < 80) {
						filename.setLayoutX(filename.getLayoutX() + 5);
					}

					remove.setVisible(true);
					on_edit = true;
				}
				else {
					if (icon.getLayoutX() > 10) {
						icon.setLayoutX(icon.getLayoutX() - 5);
					}

					if (filename.getLayoutX() > 50) {
						filename.setLayoutX(filename.getLayoutX() - 5);
					}

					remove.setVisible(false);
					on_edit = false;
				}
			}
		}));

		transition.setCycleCount(6);
		transition.play();
	}

	public void setName(String name) {
		filename.setText(name);
		String extension = name.substring(name.lastIndexOf(".") + 1);
		setIcon(extension);
	}

	public String getName() {
		return filename.getText();
	}

	public void setIcon(String extension) {
		try {
			icon.setImage(new Image("/resources/" + extension + ".png"));
		} catch (Exception e) {
			icon.setImage(new Image("/resources/file.png"));
		}
	}

	public void delete(MouseEvent e) {
		deleted = true;
	}

	public boolean willRemoved() {
		return deleted;
	}

	public void dragEnabled(MouseEvent e) {
		if (e.getSource().equals(drag)) {
			dragable = true;
		}
		else
			dragable = false;
	}

	public boolean dragable() {
		return dragable;
	}

	public void hide() {
		cell.setVisible(false);
	}

	public void show() {
		cell.setVisible(true);
	}

	public void open(MouseEvent e) {
		if (e.getClickCount() == 2 && !e.isConsumed() && !on_edit) {
			openfile = filename.getText();
			e.consume();
		}
		else
			openfile = "";
	}

	public String getFilename() {
		String name = openfile;
		openfile = "";
		return name;
	}
}
