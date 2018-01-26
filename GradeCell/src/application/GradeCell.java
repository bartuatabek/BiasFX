package application;

import java.io.IOException;
import java.util.ArrayList;

import com.jfoenix.controls.JFXTextField;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class GradeCell extends Pane {

	@FXML
	private Pane gradePane;

	@FXML
	private JFXTextField name;

	@FXML
	private JFXTextField grade;

	@FXML
	private JFXTextField boundary;

	@FXML
	private Label score;

	@FXML
	private ImageView remove;

	@FXML
	private ImageView drag;

	@FXML
	private Line l1;

	@FXML
	private Line l2;

	private boolean deleted = false;
	private boolean dragable = true;
	private boolean in_phase1 = true;
	private boolean error = false;

	public GradeCell(ArrayList<String> data) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Grade.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		loader.load();
		addGrade(data.get(0), data.get(1), data.get(2));
	}

	public void addGrade(String name, String grade, String boundary) {
		setName(name);
		setGrade(grade);
		setBoundary(boundary);

		if (!name.isEmpty() && !grade.isEmpty() && !boundary.isEmpty()) {
			getScore();
			hide();
		}			
	}

	public ArrayList<String> save() {
		ArrayList<String> grade = new ArrayList<String>();
		if (getName().isEmpty())
			grade.add("");
		else
			grade.add(getName());
		
		if (getGrade().isEmpty())
			grade.add("");
		else
			grade.add(getGrade());
		
		if (getBoundary().isEmpty())
			grade.add("");
		else
			grade.add(getBoundary());
		return grade;
	}

	public void hideCell() {
		gradePane.setVisible(false);
	}

	public void showCell() {
		gradePane.setVisible(true);
	}
	
	public void setName(String name) {
		this.name.setText(name);
	}

	public String getName() {
		return name.getText();
	}

	public void setGrade(String grade) {
		this.grade.setText(grade);
	}

	public String getGrade() {
		return grade.getText();
	}

	public void setBoundary(String boundary) {
		this.boundary.setText(boundary);
	}

	public String getBoundary() {
		return boundary.getText();
	}

	public boolean isEmpty() {
		if (getName().isEmpty() && getGrade().isEmpty() && getBoundary().isEmpty())
			return true;
		else
			return false;
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

	public void show() {

		TranslateTransition s1 = new TranslateTransition(new Duration(125), name);
		s1.setToX(25);	
		s1.play();

		TranslateTransition s2 = new TranslateTransition(new Duration(125), score);
		s2.setToX(0);	
		s2.play();

		FadeTransition f1 = new FadeTransition(new Duration(125), remove);
		f1.setToValue(1.0);
		f1.play();

		FadeTransition f2 = new FadeTransition(new Duration(125), drag);
		f2.setToValue(1.0);
		f2.play();
	}

	public void hide() {

		if (in_phase1 && !hasError()) {

			l1.setVisible(false);
			l2.setVisible(false);

			grade.setVisible(false);
			boundary.setVisible(false);

			score.setText(grade.getText() + "/" + boundary.getText());
			score.setVisible(true);

			name.setEditable(false);
			in_phase1 = false;
		}

		else {
			TranslateTransition s1 = new TranslateTransition(new Duration(125), name);
			s1.setToX(0);	
			s1.play();

			TranslateTransition s2 = new TranslateTransition(new Duration(125), score);
			s2.setToX(31);	
			s2.play();

			FadeTransition f1 = new FadeTransition(new Duration(125), remove);
			f1.setToValue(0.0);
			f1.play();

			FadeTransition f2 = new FadeTransition(new Duration(125), drag);
			f2.setToValue(0.0);
			f2.play();
		}
	}

	public void pressed(MouseEvent e) {
		if (e.getSource().equals(remove))
			remove.setOpacity(0.8);
		else if (e.getSource().equals(drag))
			drag.setOpacity(0.8);
	}

	public void released(MouseEvent e) {
		remove.setOpacity(1.0);
		drag.setOpacity(1.0);
	}

	public double getScore() {

		if (!name.getText().isEmpty() && !grade.getText().isEmpty() && !boundary.getText().isEmpty()) {	
			name.setStyle("-fx-text-fill: white; -fx-prompt-text-fill: #C7C7CC;");

			if (Double.parseDouble(grade.getText()) < 0 || Double.parseDouble(grade.getText()) > Double.parseDouble(boundary.getText())) {
				grade.setStyle("-fx-text-fill: #FF3B30;");
				boundary.setStyle("-fx-text-fill: #FF3B30;");
				error = true;
			}

			else {
				grade.setStyle("-fx-text-fill: white; -fx-prompt-text-fill: #C7C7CC;");
				boundary.setStyle("-fx-text-fill: white; -fx-prompt-text-fill: #C7C7CC;");
				error = false;
				// calculating the score
				double score = (Double.parseDouble(grade.getText()) * 100) / Double.parseDouble(boundary.getText());
				return score;
			}
		}

		if (name.getText().isEmpty()) {
			error = true;
			name.setStyle("-fx-prompt-text-fill: #FF3B30;");
		}

		if (grade.getText().isEmpty()) {
			error = true;
			grade.setStyle("-fx-prompt-text-fill: #FF3B30;");
		}

		if (boundary.getText().isEmpty()) {
			error = true;
			boundary.setStyle("-fx-prompt-text-fill: #FF3B30;");
		}

		return 0;
	}

	public boolean hasError() {
		return error;
	}
}