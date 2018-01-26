package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class CalendarEvent extends StackPane {

	@FXML
	private StackPane event;

	@FXML
	private Rectangle rect1;

	@FXML
	private Rectangle rect2;

	@FXML
	private Label label1;

	@FXML
	private Label label2;
	
	@FXML
	private Label label3;

	private String info = "New Event";

	private String time = "00:00 AM";
	
	private String location = "Location";

	private double startHour = 0;
	private double startMin = 0;
	private double myHeight = (1250.0 / 24.0);
	private boolean fixed;
	private boolean deleted;

	private Boolean isFocused = false;

	public CalendarEvent() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Event.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		loader.load();
	}

	public void setInfo(String eventName) {
		info = eventName;	

		if (event.getHeight() < 50) {
			label1.setText(info);
			label2.setVisible(false);
		}

		else {
			label1.setText(time);
			label2.setText(info);
			label2.setVisible(true);
		}
	}

	public void setTime(String startTime) {
		time = startTime;

		if (event.getHeight() < 50) {
			label1.setText(info);
			label2.setVisible(false);
		}

		else {
			label1.setText(time);
			label2.setText(info);
			label2.setVisible(true);
		}
	}
	
	public void setLocation(String location) {
		this.location = location;
		label3.setText(location);
		
		if (event.getHeight() < 65) {
			label3.setVisible(false);
		}

		else {
			label3.setVisible(true);
		}
	}

	public String getInfo() {
		return info;
	}

	public String getTime() {
		return time;
	}
	
	public String getLocation() {
		return location;
	}

	public boolean getFixed() {
		return fixed;
	}

	public double getStartHour() {
		return startHour;
	}

	public double getStartMin() {
		return startMin;
	}

	public double getMyHeight() {
		return myHeight;
	}

	public void generateCustomEvents (Double startHour, Double startMin, Double height, String info) {
		this.startHour = startHour;
		this.startMin = startMin;
		this.myHeight = height;

		if (this.startMin < 0)
			this.startMin = 0;

		String start = "";
		String ampm;

		if (startHour >= 12)
			ampm = "PM";
		else
			ampm = "AM";


		if (startHour < 10)
			start = "0" + startHour.intValue();
		else {
			if (startHour.intValue() % 12 != 0)
				start = "" + startHour.intValue() % 12;
			else
				start = "" + startHour.intValue();
		}

		if (startMin < 10)
			start = start + ":0" + startMin.intValue() + " " + ampm;
		else
			start = start + ":" + startMin.intValue() + " " + ampm;

		this.time = start;
		this.info = info;

		event.setLayoutX(0);
		event.setLayoutY(startHour * 1250.0 / 24.0 + startMin * 1250.0 / 1440.0);
		event.setPrefHeight(height);
		rect1.setHeight(height);
		rect2.setHeight(height);
		event.setVisible(true);

		if (myHeight < 50) {
			label1.setText(info);
			label2.setVisible(false);
		}
		else {
			label1.setText(time);
			label2.setText(info);
			label1.setVisible(true);
			label2.setVisible(true);
		}
		
		if (event.getHeight() < 65) {
			label3.setVisible(false);
		}

		else {
			label3.setText(location);
			label3.setVisible(true);
		}
	}


	public void generateEventAutomatically (Double start, Double end, String time, String info) {
		this.fixed = true;
		this.time = time;
		this.info = info;
		label1.setText(time);
		label2.setText(info);
		label1.setVisible(true);
		label2.setVisible(true);

		event.setLayoutX(0);
		event.setLayoutY(start * 1250.0 / 24.0 + 40.0 * 1250.0 / 1440.0);
		event.setPrefHeight(1250.0 / 24.0 * ((end - start) * 60.0 - 10.0) / 60.0);
		rect1.setHeight(1250.0 / 24.0 * ((end - start) * 60.0 - 10.0) / 60.0);
		rect2.setHeight(1250.0 / 24.0 * ((end - start) * 60.0 - 10.0) / 60.0);
		event.setVisible(true);
	}

	public void generateEventOnClick(Double Y) {

		this.startHour = (int) ((Y) / (1250.0 / 24.0));
		this.startMin = (int) ((Y * 1440) / 1250) % 60;
		this.myHeight = (1250.0 / 24.0);



		if (this.startMin < 0)
			this.startMin = 0;

		event.setLayoutX(0);
		event.setLayoutY(Math.round(Y));
		event.setPrefHeight((1250.0 / 24.0));
		rect1.setHeight((1250.0 / 24.0));
		rect2.setHeight((1250.0 / 24.0));
		event.setVisible(true);

		label1.setText(time);
		label2.setText(info);
		label1.setVisible(true);
		label2.setVisible(true);
	}

	public void resize(MouseEvent e)  { 
		if (!fixed) {
			if (!isFocused && e.getSceneY() >= 0 && e.getSceneY() <= 1250) {
				double delta = e.getSceneY() - firstMouseLocation;

				if (height + delta >= 20) {
					event.setPrefHeight(height + delta);
					rect1.setHeight(height + delta);
					rect2.setHeight(height + delta);

					this.myHeight = (int) event.getPrefHeight();
				}

				String hour;
				String min;
				String ampm;

				if (startHour >= 12)
					ampm = "PM";
				else
					ampm = "AM";

				if (this.startHour % 12 == 0)
					hour = "" + (int) startHour;
				else
					hour = "" + (int) startHour % 12;


				if (startMin < 10)
					min = "0" + (int)startMin;
				else
					min = "" + (int)startMin;

				setTime(hour + ":" + min + " " + ampm);



				if (event.getHeight() < 50) {
					label1.setText(info);
					label2.setVisible(false);
				}

				else {
					label1.setText(time);
					label2.setVisible(true);
				}
				
				if (event.getHeight() < 65) {
					label3.setVisible(false);
				}

				else {
					label3.setText(location);
					label3.setVisible(true);
				}
			}
		}
	}

	public void move(MouseEvent e)  {
		if (!fixed) {
			if (isFocused && e.getSceneY() >= 0 && e.getSceneY() <= 1250) {
				double delta = e.getSceneY() - firstMouseLocation;
				event.setLayoutY(eventLocation + delta);

				this.startHour = (int) (event.getLayoutY() / (1250.0 / 24.0));
				this.startMin = (int) (((event.getLayoutY() * 1440) / 1250) % 60) ;

				if (this.startMin < 0)
					this.startMin = 0;

				String hour;
				String min;
				String ampm;

				if (startHour >= 12)
					ampm = "PM";
				else
					ampm = "AM";

				if (this.startHour % 12 == 0)
					hour = "" + (int) startHour;
				else
					hour = "" + (int) startHour % 12;

				if (startMin < 10)
					min = "0" + (int)startMin;
				else
					min = "" + (int)startMin;

				setTime(hour + ":" + min + " " + ampm);

				if (event.getHeight() < 50) {
					label1.setText(info);
					label2.setVisible(false);
				}

				else {
					label1.setText(time);
					label2.setVisible(true);
				}
				
				if (event.getHeight() < 65) {
					label3.setVisible(false);
				}

				else {
					label3.setText(location);
					label3.setVisible(true);
				}

			}
		}

	}

	private double height;
	private double firstMouseLocation;
	private double eventLocation;

	public void recordCoordinates (MouseEvent e){
		height = event.getPrefHeight();
		firstMouseLocation = e.getSceneY();
		eventLocation = event.getLayoutY();
	}

	public void changeCursor(MouseEvent e) {
		if (e.getY() <= 2 || e.getY() == event.getHeight())
			event.setCursor(Cursor.V_RESIZE);
		else
			event.setCursor(Cursor.DEFAULT);
	}

	public void setFocus(MouseEvent e) {
		if (isFocused) {
			isFocused = false;
			removeFocus();
		}
		else {
			isFocused = true;
			rect1.setOpacity(0.8);
			rect2.setOpacity(0.0);	
		}
	}

	public void	removeFocus() {
		rect1.setOpacity(0.2);
		rect2.setOpacity(0.8);
		isFocused = false;
	}

	public Boolean getFocus() {
		return isFocused;
	}

	public void setColor(Color color) {
		rect1.setFill(color);
		rect2.setFill(color);
	}
	
	public boolean getDeleted()
	{
		return deleted;
	}

	public void deleteEvent() {
		if(!fixed && isFocused) {
			deleted = true;		
		}
	}
}