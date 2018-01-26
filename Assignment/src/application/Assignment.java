package application;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class Assignment extends AnchorPane implements ChangeListener<Object>{

	@FXML
	private AnchorPane root;

	@FXML
	private Pane assPane;

	@FXML
	private Label assName;

	@FXML
	private Label assInfo;

	@FXML
	private Label assDue;

	@FXML
	private Label percentage;

	@FXML
	private Label remainingDays;

	@FXML
	private CircularProgressIndicator ring;

	@FXML 
	private JFXSlider progress;

	@FXML 
	private JFXTextField assNameT;

	@FXML 
	private JFXTextField assInfoT;

	@FXML 
	private JFXDatePicker assDueT;

	@FXML
	private JFXButton save;

	@FXML
	private JFXButton cancel;

	@FXML
	private StackPane delAss;

	private boolean isExpanded = false;

	private boolean willRemoved = false;

	public Assignment() throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Assignment.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		loader.load();

		progress.valueProperty().addListener(this);
		assDue.setText(today);
		assDueT.setPromptText(today);
		assDueT.setValue(localDate);
		progress.setValue(0);
	}

	// peek assignment info allows to peek into assignment details
	public void peekAssInfo(MouseEvent e) {

		if (!willRemoved) {
			isExpanded = true;

			Timer animTimer = new Timer();
			animTimer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					if (assPane.getPrefHeight() < 282) {
						assPane.setPrefHeight(assPane.getPrefHeight() + 20);
					} else { this.cancel(); }
				}
			}, 0, (long) 16.6);

			progress.setVisible(true);
			assNameT.setVisible(true);
			assInfoT.setVisible(true);
			assDueT.setVisible(true);
			cancel.setVisible(true);
			save.setVisible(true);
		}
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	LocalDate localDate = LocalDate.now();
	String today = DateTimeFormatter.ofPattern("MM/dd/yyyy").format(localDate);

	// modify assignment info
	public void saveAss(ActionEvent e) {
		if (assNameT.getText().length() > 0) {
			assName.setText(assNameT.getText());
		}
		if (assInfoT.getText().length() > 0) {
			assInfo.setText(assInfoT.getText());
		}

		String date = today;
		if (assDueT.getValue() != null) {
			date = assDueT.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
		}

		today = today.replaceAll("-","/");
		date = date.replaceAll("-","/");

		int remaining = (int) ChronoUnit.DAYS.between(localDate, assDueT.getValue());

		if (remaining < 0) 
			remaining = 0;

		if (date.length() > 0) {
			assDue.setText(date);
			if (remaining > 0)
				remainingDays.setText(remaining + "");
			else 
				remainingDays.setText("0");
		}

		isExpanded = false;

		progress.setVisible(false);
		assNameT.setVisible(false);
		assInfoT.setVisible(false);
		assDueT.setVisible(false);
		cancel.setVisible(false);
		save.setVisible(false);

		Timer animTimer = new Timer();
		animTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				if (assPane.getPrefHeight() > 82) {
					assPane.setPrefHeight(assPane.getPrefHeight() - 20);
				} else { this.cancel(); }
			}
		}, 0, (long) 16.6);
	}

	// cancel assignment modification
	public void cancelAss(ActionEvent e) {

		isExpanded = false;

		progress.setVisible(false);
		assNameT.setVisible(false);
		assInfoT.setVisible(false);
		assDueT.setVisible(false);
		cancel.setVisible(false);
		save.setVisible(false);

		Timer animTimer = new Timer();
		animTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				if (assPane.getPrefHeight() > 82) {
					assPane.setPrefHeight(assPane.getPrefHeight() - 20);
				} else { this.cancel(); }
			}
		}, 0, (long) 16.6);
	}

	// shows remove icon for selected assignments
	public void showDelete(MouseEvent e) {
		if (assPane.isVisible() && e.getSource() == remainingDays) 
			delAss.setVisible(true);
	}

	// hides remove icon for selected assignments
	public void hideDelete(MouseEvent e) {
		if (assPane.isVisible() && e.getSource() == delAss) 
			delAss.setVisible(false);
	}

	public void setRemoved(MouseEvent e) {
		willRemoved = true;
	}

	public boolean willRemoved() {
		return willRemoved;
	}

	@Override
	public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
		percentage.setText("%" + (int) progress.getValue());

		double ringProgress = Double.parseDouble("0." + (int) progress.getValue());

		if ((int) progress.getValue() == 100) {
			ring.setProgress(1.0);
		}

		else if ((int) progress.getValue() > 0 && (int) progress.getValue() < 10) {
			ringProgress = Double.parseDouble("0.0" + (int) progress.getValue());
			ring.setProgress(ringProgress);
		}

		else if (ringProgress > 0) {
			ring.setProgress(ringProgress);
		}

		else {
			ring.setProgress(-1);
		}
	}

	public void load(ArrayList<Object> data) {
		assName.setText((String) data.get(0));
		assNameT.setText((String) data.get(0));

		assInfo.setText((String) data.get(1));
		assInfoT.setText((String) data.get(1));

		assDue.setText((String) data.get(2));
		assDueT.setValue((LocalDate) data.get(4));;

		progress.setValue((int) data.get(3));

		int remaining = (int) ChronoUnit.DAYS.between(localDate, assDueT.getValue());

		if (remaining < 0) 
			remaining = 0;

		remainingDays.setText(remaining + "");
	}

	public ArrayList<Object> save() {
		ArrayList<Object> data = new ArrayList<Object>();

		data.add(assName.getText());
		data.add(assInfo.getText());
		data.add(assDue.getText());
		data.add((int) progress.getValue());
		data.add(assDueT.getValue());

		return data;
	}

	public void hide() {
		assPane.setVisible(false);
	}

	public void show() {
		assPane.setVisible(true);
	}

	public boolean isExpired() {
		if (Integer.parseInt(remainingDays.getText()) == 0)
			return true;
		return false;
	}
}
