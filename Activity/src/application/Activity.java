package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Activity extends Pane {

	@FXML
	private Label begins;

	@FXML
	private Label ends;

	@FXML
	private Label activityName;

	@FXML
	private Label Location;
	
	@FXML
	private ImageView bell;

	@FXML
	private ImageView silent;

	private boolean is_attending = false;

	
	public Activity(String beginning, String ending, String activityName, String location) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Activity.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		loader.load();

		newActivity(beginning, ending, activityName, location);
	}

	public String getStartingTime() {
		return begins.getText();
	}

	public void setStartingTime(String time) {
		this.begins.setText(time);
	}

	public String getEndingTime() {
		return ends.getText();
	}

	public void setEndingTime(String time) {
		this.ends.setText(time);
	}

	public String getActivityName() {
		return activityName.getText();
	}

	public void setActivityName(String activityName) {
		this.activityName.setText(activityName);
	}

	public String getLocation() {
		return Location.getText();
	}

	public void setLocation(String Location) {
		this.Location.setText(Location);
	}

	public void newActivity(String beginning, String ending, String activityName, String location) {
		setStartingTime(beginning);
		setEndingTime(ending);
		setActivityName(activityName);
		setLocation(location);
	}

	public boolean isAttending() {
		return is_attending;
	}

	public void setAttending(MouseEvent e) {

		if (silent.isVisible()) {
			silent.setVisible(false);
			bell.setVisible(true);
			is_attending = true;
		}
		else {
			silent.setVisible(true);
			bell.setVisible(false);
			is_attending = false;
		}
	}
	
	public void setSilentAndBell (boolean isSilent)
	{
		silent.setVisible(isSilent);
		bell.setVisible(!isSilent);
		is_attending = !isSilent;
	}
	
	public boolean getSilent()
	{
		return silent.isVisible();
	}
}