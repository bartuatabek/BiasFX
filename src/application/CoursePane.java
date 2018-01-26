package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.chart.ChartData;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import javafx.util.Duration;

public class CoursePane extends Pane {

	@SuppressWarnings("unchecked")
	public CoursePane(String courseName) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("CoursePane.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		loader.load();

		setCourseName(courseName);

		grades.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
		grades.setPageFactory(new Callback<Integer, Node>() {
			@Override
			public Node call(Integer pageIndex) {                     
				try {
					return createPage(pageIndex);
				} catch (IOException e) {
					return null;
				}
			}});

		overallGraph.setBackgroundColor(Color.web("transparent"));
		LineChart.getData().addAll(assessments, ass);

		deletePane.setCache(true);
		deletePane.setCacheShape(true);
		deletePane.setCacheHint(CacheHint.SPEED);

		secondary.setCache(true);
		secondary.setCacheShape(true);
		secondary.setCacheHint(CacheHint.SPEED);

		blur1.setCache(true);
		blur1.setCacheHint(CacheHint.SPEED);

		blur2.setCache(true);
		blur2.setCacheHint(CacheHint.SPEED);

		// in case user drops node in blank space in root:
		boxes = new ArrayList<VBox>();
		boxes.add(examGrades);
		boxes.add(assessmentGrades);
		boxes.add(assessmentGrades);

		for (VBox box : boxes) {
			box.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
				@Override
				public void handle(MouseDragEvent event) {
					int indexOfDraggingNode = box.getChildren().size() - 1 ;
					rotateNodes(box, indexOfDraggingNode, indexOfDropTarget);
					removePreview(box);

					for (Node n : box.getChildren()) {
						n.setOpacity(1.0);
					}
					grade_buffer = false;
					event.consume();
				}
			});
		}

		Timeline gradeCheck = new Timeline(new KeyFrame(Duration.seconds(0.1), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ArrayList<GradeCell> toBeRemoved = new ArrayList<GradeCell>();

				if (!grade_buffer) {
					for (VBox box : boxes) {
						for (Node n : box.getChildren()) {
							if (((GradeCell) n).willRemoved()) {
								toBeRemoved.add((GradeCell) n);
							}}
						box.getChildren().removeAll(toBeRemoved);

					}}}}));

		gradeCheck.setCycleCount(Timeline.INDEFINITE);
		gradeCheck.play();
	}

	private	ArrayList<VBox> boxes;

	private boolean grade_buffer = false;
	private boolean deleted = false;

	public void pressed(MouseEvent e) {
		if (e.getSource().equals(plus))
			plus.setOpacity(0.8);
		else if (e.getSource().equals(detail))
			detail.setOpacity(0.8);
		else if (e.getSource().equals(check))
			check.setOpacity(0.8);
		else if (e.getSource().equals(delete1))
			delete1.setOpacity(0.8);
	}

	public void released(MouseEvent e) {
		plus.setOpacity(1.0);
		detail.setOpacity(1.0);
		check.setOpacity(1.0);	
		delete1.setOpacity(1.0);
	}

	@FXML
	private Pane main;

	@FXML
	private Pane secondary;


	@FXML
	private ImageView blur1;

	@FXML
	private ImageView blur2;

	@FXML
	private Region arrowDetector;

	@FXML
	private ImageView arrow;

	@FXML
	private Rectangle arrowL;

	@FXML
	private Rectangle arrowR;

	public void express_deploy(MouseEvent e) throws Exception {
		SnapshotParameters param = new SnapshotParameters();
		param.setViewport(new Rectangle2D(0,0,360,680));
		WritableImage snapshot = main.snapshot(param, null);
		blur1.setImage(snapshot);
		blur2.setImage(snapshot);


		blur1.setVisible(true);
		blur2.setVisible(true);
		FadeTransition fade1 = new FadeTransition(new Duration(125), blur1);
		fade1.setToValue(0.5);	
		FadeTransition fade2 = new FadeTransition(new Duration(125), blur2);
		fade2.setToValue(0.5);	

		fade1.play();
		fade2.play();

		fast_deploy();
	}

	public void fast_deploy() throws Exception {

		TranslateTransition deploy = new TranslateTransition(new Duration(250), secondary);
		deploy.setToY(0);

		FadeTransition fadeL = new FadeTransition(new Duration(250), arrowL);
		fadeL.setToValue(1.0);			
		fadeL.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				RotateTransition rotateL = new RotateTransition(new Duration(125), arrowL);
				rotateL.setToAngle(20.0);
				rotateL.setDelay(new Duration(100));
				rotateL.play();		
			}});

		FadeTransition fadeR = new FadeTransition(new Duration(250), arrowR);
		fadeR.setToValue(1.0);			
		fadeR.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				RotateTransition rotateL = new RotateTransition(new Duration(125), arrowR);
				rotateL.setToAngle(-20.0);
				rotateL.setDelay(new Duration(100));
				rotateL.play();		
			}});

		FadeTransition fade1 = new FadeTransition(new Duration(250), blur1);
		fade1.setToValue(1.0);	
		FadeTransition fade2 = new FadeTransition(new Duration(250), blur2);
		fade2.setToValue(1.0);	

		deploy.play();
		fadeL.play();
		fadeR.play();
		fade1.play();
		fade2.play();
		setGraphs();

	}

	public void peekArrow(MouseEvent e) {
		arrow.setVisible(true);
		TranslateTransition peek = new TranslateTransition(new Duration(125), arrow);
		peek.setToY(0);
		peek.play();
	}

	public void hideArrow(MouseEvent e) {
		TranslateTransition hide = new TranslateTransition(new Duration(125), arrow);
		hide.setToY(30);
		hide.play();
		hide.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				arrow.setVisible(false);
			}});
	}

	public void hideSecondary(MouseEvent e) {
		TranslateTransition hide = new TranslateTransition(new Duration(250), secondary);
		hide.setToY(680);		

		RotateTransition rotateL = new RotateTransition(new Duration(125), arrowL);
		rotateL.setToAngle(0.0);
		rotateL.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				FadeTransition fadeL = new FadeTransition(new Duration(500), arrowL);
				fadeL.setToValue(0.0);
				fadeL.play();
			}});

		RotateTransition rotateR = new RotateTransition(new Duration(125), arrowR);
		rotateR.setToAngle(0.0);
		rotateR.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				FadeTransition fadeR = new FadeTransition(new Duration(500), arrowR);
				fadeR.setToValue(0.0);
				fadeR.play();
			}});

		FadeTransition fade1 = new FadeTransition(new Duration(125), blur1);
		fade1.setToValue(0.0);
		fade1.setDelay(new Duration(125));
		fade1.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				blur1.setVisible(false);
			}});

		FadeTransition fade2 = new FadeTransition(new Duration(125), blur2);
		fade2.setToValue(0.0);
		fade2.setDelay(new Duration(125));
		fade2.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				blur2.setVisible(false);
			}});

		hide.play();
		fade1.play();
		fade2.play();
		rotateL.play();
		rotateR.play();
	}

	public void hide(ScrollEvent e) {
		if (secondary.getTranslateY() > 600) {
			TranslateTransition deploy = new TranslateTransition(new Duration(250), secondary);
			deploy.setToY(680);	

			FadeTransition fade1 = new FadeTransition(new Duration(125), blur1);
			fade1.setToValue(0.0);	
			FadeTransition fade2 = new FadeTransition(new Duration(125), blur2);
			fade2.setToValue(0.0);	

			deploy.play();
			fade1.play();
			fade2.play();
		}

		else if (secondary.getTranslateY() <= 340) {
			TranslateTransition deploy = new TranslateTransition(new Duration(250), secondary);
			deploy.setToY(0);
			deploy.play();
		}

	}

	public void deploy(ScrollEvent e) throws Exception {

		if (e.getSceneY() < 298 || e.getSceneY() > 513) {
			if (e.getDeltaY() < 0) {
				if (secondary.getTranslateY() > 600) {
					secondary.setTranslateY(secondary.getTranslateY() - -e.getDeltaY());

					SnapshotParameters param = new SnapshotParameters();
					param.setViewport(new Rectangle2D(0,0,360,680));
					WritableImage snapshot = main.snapshot(param, null);
					blur1.setImage(snapshot);
					blur2.setImage(snapshot);

					blur1.setVisible(true);
					blur2.setVisible(true);
					FadeTransition fade1 = new FadeTransition(new Duration(125), blur1);
					fade1.setToValue(0.5);	
					FadeTransition fade2 = new FadeTransition(new Duration(125), blur2);
					fade2.setToValue(0.5);	

					fade1.play();
					fade2.play();
				}

				else if (secondary.getTranslateY() <= 600) {
					fast_deploy();
				}
			}

			else if (e.getDeltaY() > 0) {
				if (secondary.getTranslateY() < 340) {
					secondary.setTranslateY(secondary.getTranslateY() + e.getDeltaY());

					RotateTransition rotateL = new RotateTransition(new Duration(125), arrowL);
					rotateL.setToAngle(0.0);
					rotateL.setDelay(new Duration(50));
					rotateL.setOnFinished(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {
							FadeTransition fadeL = new FadeTransition(new Duration(250), arrowL);
							fadeL.setToValue(0.0);
							fadeL.setDelay(new Duration(100));
							fadeL.play();
						}});

					RotateTransition rotateR = new RotateTransition(new Duration(125), arrowR);
					rotateR.setToAngle(0.0);
					rotateR.setDelay(new Duration(50));
					rotateR.setOnFinished(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {
							FadeTransition fadeR = new FadeTransition(new Duration(250), arrowR);
							fadeR.setToValue(0.0);
							fadeR.setDelay(new Duration(100));
							fadeR.play();
						}});

					rotateL.play();
					rotateR.play();
				}

				else if (secondary.getTranslateY() >= 340) {
					TranslateTransition deploy = new TranslateTransition(new Duration(250), secondary);
					deploy.setToY(680);

					FadeTransition fade1 = new FadeTransition(new Duration(250), blur1);
					fade1.setToValue(0.0);	
					fade1.setOnFinished(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {
							blur1.setVisible(false);
						}});

					FadeTransition fade2 = new FadeTransition(new Duration(250), blur2);
					fade2.setToValue(0.0);
					fade2.setOnFinished(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {
							blur2.setVisible(false);
						}});

					deploy.play();
					fade1.play();
					fade2.play();
				}
			}
		}
	}

	@FXML
	private Label delete1;

	@FXML
	private Label delete2;

	@FXML
	private Label cancel;

	@FXML
	private Pane deletePane;

	public boolean isDeleted() {
		return deleted;
	}

	public void delete(MouseEvent e) {
		deleted = true;
	}

	public void deployDelete(MouseEvent e) {
		deletePane.setVisible(true);
		FadeTransition fade = new FadeTransition(new Duration(125), deletePane);
		fade.setToValue(1.0);
		fade.play();
	}

	public void hideDelete(MouseEvent e) {
		FadeTransition fade = new FadeTransition(new Duration(125), deletePane);
		fade.setToValue(0.0);
		fade.play();
		fade.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				deletePane.setVisible(false);
			}});	
	}

	@FXML
	private Label courseName;

	public void setCourseName(String courseName) {
		this.courseName.setText(courseName);
	}

	@FXML 
	private ImageView plus;

	@FXML 
	private ImageView detail;

	@FXML 
	private ImageView check;

	@FXML
	private Label gradeTitle;

	@FXML
	private Pagination grades;

	@FXML
	private VBox overallGrades = new VBox(5);

	@FXML
	private VBox examGrades = new VBox(5);

	@FXML
	private VBox assessmentGrades = new VBox(5);

	@FXML
	private VBox assignmentGrades = new VBox(5);

	private int slotNo = 0;

	public void swipe(ScrollEvent e) {
		if (e.getDeltaX() < 0 && grades.getCurrentPageIndex()!= 0) {
			grades.setCurrentPageIndex(grades.getCurrentPageIndex() - 1);
		}

		else if (e.getDeltaX() > 0 && grades.getCurrentPageIndex()!= 3) {
			grades.setCurrentPageIndex(grades.getCurrentPageIndex() + 1);
		}

		if (e.getDeltaY() == 0) {
			FadeTransition fade = new FadeTransition(new Duration(125), gradeTitle);
			fade.setToValue(0.5);
			fade.setAutoReverse(true);
			fade.setCycleCount(2);
			fade.play();

			if (grades.getCurrentPageIndex() == 0)
				gradeTitle.setText("Grades");

			else if (grades.getCurrentPageIndex() == 1)
				gradeTitle.setText("Exam Grades");

			else if (grades.getCurrentPageIndex() == 2)
				gradeTitle.setText("Assessment Grades");

			else if (grades.getCurrentPageIndex() == 3)
				gradeTitle.setText("Assignment Grades");

			if (grades.getCurrentPageIndex() == 0 && check.isVisible())
				plus.setOpacity(0.25);
			else
				plus.setOpacity(1.0);
		}
	}

	public ScrollPane createPage(int pageIndex) throws IOException { 

		ScrollPane pane; 

		overallGrades.setStyle("-fx-background-color: #262626;");
		overallGrades.setFillWidth(false);
		overallGrades.setPrefHeight(190);
		overallGrades.setPrefWidth(331);

		examGrades.setStyle("-fx-background-color: #262626;");
		examGrades.setFillWidth(false);
		examGrades.setPrefHeight(190);
		examGrades.setPrefWidth(331);

		assessmentGrades.setStyle("-fx-background-color: #262626;");
		assessmentGrades.setFillWidth(false);
		assessmentGrades.setPrefHeight(190);
		assessmentGrades.setPrefWidth(331);

		assignmentGrades.setStyle("-fx-background-color: #262626;");
		assignmentGrades.setFillWidth(false);
		assignmentGrades.setPrefHeight(190);
		assignmentGrades.setPrefWidth(331);

		if (pageIndex == 0) {
			pane = new ScrollPane(overallGrades);
		}

		else if (pageIndex == 1) {
			pane = new ScrollPane(examGrades);
		}

		else if (pageIndex == 2) {
			pane = new ScrollPane(assessmentGrades);
		}

		else {
			pane = new ScrollPane(assignmentGrades);
		}

		pane.setVbarPolicy(ScrollBarPolicy.NEVER);
		pane.setHbarPolicy(ScrollBarPolicy.NEVER);
		pane.setStyle("-fx-background-color: #262626;");

		return pane;
	}

	private int indexOfDropTarget;

	private void addGradeWithDragging(final VBox root, final GradeCell cell) {

		cell.setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				indexOfDropTarget = root.getChildren().indexOf(cell);			
				addPreview(root,cell);
				cell.setOpacity(0.0);
				cell.toFront();
				cell.startFullDrag();
				grade_buffer = true;
			}
		});

		cell.setOnMouseDragEntered(new EventHandler<MouseDragEvent>() {
			@Override
			public void handle(MouseDragEvent event) {
				((GradeCell) cell).hideCell();
			}
		});

		cell.setOnMouseDragExited(new EventHandler<MouseDragEvent>() {
			@Override
			public void handle(MouseDragEvent event) {
				((GradeCell) cell).showCell();
			}
		});

		cell.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
			@Override
			public void handle(MouseDragEvent event) {
				int indexOfDraggingNode = root.getChildren().indexOf(event.getGestureSource());
				int indexOfDropTarget = root.getChildren().indexOf(cell);
				rotateNodes(root, indexOfDraggingNode, indexOfDropTarget);
				removePreview(root);
				cell.setOpacity(1.0);
				event.consume();
				grade_buffer = false;
			}
		});

		cell.setOnMouseReleased(event -> {
			Point2D mouseLoc = new Point2D(event.getScreenX(), event.getScreenY());
			Bounds bounds = root.getBoundsInLocal();
			Bounds screenBounds = root.localToScreen(bounds);
			int x = (int) screenBounds.getMinX();
			int y = (int) screenBounds.getMinY();
			int width = (int) screenBounds.getWidth();
			int height = (int) screenBounds.getHeight();
			Rectangle2D windowBounds = new Rectangle2D(x, y, width, height);  
			if (! windowBounds.contains(mouseLoc)) {
				int indexOfDraggingNode = root.getChildren().indexOf(event.getSource());
				rotateNodes(root, indexOfDraggingNode, indexOfDropTarget);
				removePreview(root);
				cell.setOpacity(1.0);
				grade_buffer = false;
				event.consume();
			}
		});

		root.getChildren().add(cell);
	}

	private void rotateNodes(final VBox root, final int indexOfDraggingNode, final int indexOfDropTarget) {
		if (indexOfDraggingNode >= 0 && indexOfDropTarget >= 0) {
			final Node node = root.getChildren().remove(indexOfDraggingNode);
			node.setOpacity(1.0);
			root.getChildren().add(indexOfDropTarget, node);
		}
	}

	private void addPreview(final VBox root, final Node cell) {
		SnapshotParameters param = new SnapshotParameters();
		param.setViewport(new Rectangle2D(0,0,331,27.16));
		ImageView imageView = new ImageView(cell.snapshot(param, null));
		imageView.setEffect(new DropShadow());
		imageView.setOpacity(0.8);
		imageView.setManaged(false);
		imageView.setMouseTransparent(true);
		root.getChildren().add(imageView);
		root.setUserData(imageView);
		root.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getY() >= 0 && event.getY() < root.getHeight() - ((Pane) cell).getHeight() * 2)
					imageView.relocate(0, event.getY());		
			}
		});
	}

	private void removePreview(final VBox root) {
		root.getChildren().remove(root.getUserData());
		root.setOnMouseDragged(null);
		root.setUserData(null);
	}

	public void addGrade(MouseEvent e) throws IOException {
		if (slotNo < 100) {

			if (grades.getCurrentPageIndex() == 1) 
				addGradeWithDragging(examGrades, new GradeCell(new ArrayList<String>(Arrays.asList("", "", ""))));
			else if (grades.getCurrentPageIndex() == 2) 
				addGradeWithDragging(assessmentGrades, new GradeCell(new ArrayList<String>(Arrays.asList("", "", ""))));	
			else if (grades.getCurrentPageIndex() == 3) 
				addGradeWithDragging(assignmentGrades, new GradeCell(new ArrayList<String>(Arrays.asList("", "", ""))));


			detail.setVisible(false);
			check.setVisible(true);
			slotNo++;
		}
		else {
			plus.setDisable(true);
			plus.setOpacity(0.5);
		}
	}

	public void showDetail(MouseEvent e) {
		detail.setVisible(false);
		plus.setVisible(true);
		check.setVisible(true);

		if (grades.getCurrentPageIndex() == 0) {
			grades.setCurrentPageIndex(1);
			gradeTitle.setText("Exam Grades");
		}

		for (Node n : examGrades.getChildren()) {
			((GradeCell) n).show();
		}

		for (Node n : examGrades.getChildren()) {
			((GradeCell) n).show();
		}

		for (Node n : assessmentGrades.getChildren()) {
			((GradeCell) n).show();
		}

		for (Node n : assignmentGrades.getChildren()) {
			((GradeCell) n).show();
		}
	}

	private ArrayList<Double> assesments = new ArrayList<Double>();
	private ArrayList<Double> assignments = new ArrayList<Double>();
	private ArrayList<Double> exams = new ArrayList<Double>();
	private ArrayList<Double> overall = new ArrayList<Double>();

	// saves the grades and draws the complementary graphs

	public boolean check(VBox gradeBox) throws IOException {

		ArrayList<GradeCell> toBeRemoved = new ArrayList<GradeCell>();

		for (Node n : gradeBox.getChildren()) {
			if (((GradeCell) n).isEmpty())
				toBeRemoved.add((GradeCell) n);

			else if (((GradeCell) n).getScore() != 0 && !((GradeCell) n).hasError()) {
				overallGrades.getChildren().add(new GradeCell(((GradeCell) n).save()));

				if (gradeBox.equals(examGrades))
					exams.add(((GradeCell) n).getScore());
				else if (gradeBox.equals(assessmentGrades))
					assesments.add(((GradeCell) n).getScore());
				else if (gradeBox.equals(assignmentGrades))
					assignments.add(((GradeCell) n).getScore());

				overall.add(((GradeCell) n).getScore());
				((GradeCell) n).hide();
			}

			else {
				((GradeCell) n).getScore();
				((GradeCell) n).hide();
				return false;
			}
		}
		slotNo -= toBeRemoved.size();
		gradeBox.getChildren().removeAll(toBeRemoved);
		return true;
	}

	public void check() throws Exception {

		overallGrades.getChildren().clear();
		boolean is_clear;

		assesments.clear();
		assignments.clear();
		exams.clear();
		overall.clear();

		is_clear = check(examGrades);
		is_clear = check(assessmentGrades);
		is_clear = check(assignmentGrades);

		if (slotNo < 100) {
			plus.setDisable(false);
			plus.setOpacity(1.0);
		}

		if (is_clear) {
			detail.setVisible(true);
			check.setVisible(false);
			plus.setVisible(false);
		}

		setProgressCharts();
	}

	@FXML
	private ImageView examsCircular;

	@FXML
	private ImageView assignmentsCircular;

	@FXML
	private ImageView assesmentsCircular;

	@FXML
	private ImageView overall1;

	@FXML
	private ImageView overall2;

	@FXML
	private ImageView overall3;

	@FXML
	private ImageView eArc;

	@FXML
	private ImageView aArc;

	@FXML
	private ImageView oArc;

	@FXML
	private Label ePoints;

	int singleArc_index = 0;	
	int doubleArcOuter_index = 0;
	int doubleArcInner_index = 0;

	// sets and categorized all progress charts accordingly
	public void setProgressCharts() throws Exception {

		// setting exams chart
		int examScore = 0;

		for (Double e : exams) {
			examScore += e;
		}

		if (examScore > 0) {
			examScore = Math.round(examScore / exams.size());
			ePoints.setText(examScore + "");
			int targetE = examScore;

			if (targetE != singleArc_index )
				singleArc_index = 0;	

			Timeline singleArc = new Timeline(new KeyFrame(Duration.seconds(0.017), new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {	
					if (singleArc_index < targetE) {
						examsCircular.setImage(new Image("/resources/SingleArc/SingleArc" + singleArc_index+ ".png"));
						overall1.setImage(new Image("/resources/TripleArc/TripleArcOuter" + singleArc_index + ".png"));
						singleArc_index++;
					}
				}}));

			singleArc.setCycleCount(101);
			singleArc.play();
		}

		else {
			examsCircular.setImage(new Image("/resources/SingleArc/SingleArc0.png"));
			overall1.setImage(new Image("/resources/TripleArc/TripleArcOuter0.png"));
		}

		// setting assessments chart
		int assesmentsScore = 0;

		for (Double e : assesments) {
			assesmentsScore += e;
		}

		if (assesmentsScore > 0) {
			assesmentsScore = Math.round(assesmentsScore / assesments.size());
			int targetA = assesmentsScore;

			if (targetA != doubleArcOuter_index)
				doubleArcOuter_index = 0;

			Timeline doubleArcOuter = new Timeline(new KeyFrame(Duration.seconds(0.017), new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {	
					if (doubleArcOuter_index < targetA) {
						assesmentsCircular.setImage(new Image("/resources/DoubleArc/DoubleArcOuter" + doubleArcOuter_index+ ".png"));
						overall2.setImage(new Image("/resources/TripleArc/TripleArcMiddle" + doubleArcOuter_index + ".png"));
						doubleArcOuter_index++;
					}
				}}));

			doubleArcOuter.setCycleCount(101);
			doubleArcOuter.play();
		}

		else {
			assesmentsCircular.setImage(new Image("/resources/DoubleArc/DoubleArcOuter0.png"));
			overall2.setImage(new Image("/resources/TripleArc/TripleArcMiddle0.png"));
		}

		// setting assignments chart
		int assignmentsScore = 0;

		for (Double e : assignments) {
			assignmentsScore += e;
		}

		if (assignmentsScore > 0) {
			assignmentsScore = Math.round(assignmentsScore / assignments.size());
			int targetAss = assignmentsScore;

			if (targetAss != doubleArcInner_index)
				doubleArcInner_index = 0;

			Timeline doubleArcInner = new Timeline(new KeyFrame(Duration.seconds(0.017), new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {	
					if (doubleArcInner_index < targetAss) {
						assignmentsCircular.setImage(new Image("/resources/DoubleArc/DoubleArcInner" + doubleArcInner_index + ".png"));
						overall3.setImage(new Image("/resources/TripleArc/TripleArcInner" + doubleArcInner_index + ".png"));		
						doubleArcInner_index++;
					}
				}}));

			doubleArcInner.setCycleCount(101);
			doubleArcInner.play();
		}

		else {
			assignmentsCircular.setImage(new Image("/resources/DoubleArc/DoubleArcInner0.png"));
			overall3.setImage(new Image("/resources/TripleArc/TripleArcInner0.png"));
		}

		// setting graph scores
		int overallScore;

		if (exams.isEmpty() && !assesments.isEmpty() && !assignments.isEmpty())
			overallScore = (assesmentsScore + assignmentsScore) / 2;
		else if (assesments.isEmpty() && !exams.isEmpty() && !assignments.isEmpty())	
			overallScore = (examScore + assignmentsScore) / 2;
		else if (assignments.isEmpty() && !exams.isEmpty() && !assesments.isEmpty())
			overallScore = (examScore + assesmentsScore) / 2;

		else if (exams.isEmpty() && assesments.isEmpty() && !assignments.isEmpty())
			overallScore = assignmentsScore;
		else if (exams.isEmpty() && assignments.isEmpty() && !assesments.isEmpty())
			overallScore = assesmentsScore;
		else if (assesments.isEmpty() && assignments.isEmpty() && !exams.isEmpty())
			overallScore = examScore;
		else
			overallScore = (examScore + assesmentsScore + assignmentsScore) / 3;		

		overallScore = Math.round(overallScore);

		if (exams.isEmpty())
			eScore.setText("–/100 points");
		else
			eScore.setText(examScore + "/100 points");

		if (assesments.isEmpty() && assignments.isEmpty())
			aScore.setText("–/100 points");
		else if (assesments.isEmpty())
			aScore.setText(assignmentsScore + "/100 points");
		else if (assignments.isEmpty())
			aScore.setText(assesmentsScore + "/100 points");
		else
			aScore.setText(Math.round((assesmentsScore + assignmentsScore) / 2) + "/100 points");

		if (exams.isEmpty() && assesments.isEmpty() && assignments.isEmpty())
			oScore.setText("–/100 points");
		else
			oScore.setText(overallScore + "/100 points");

		eArc.setImage(new Image("/resources/TripleArc/TripleArcOuter" + examScore + ".png"));
		aArc.setImage(new Image("/resources/DoubleArc/DoubleArcOuter" + Math.round((assesmentsScore + assignmentsScore) / 2) + ".png"));
		oArc.setImage(new Image("/resources/OverallArc/OverallArc" + overallScore + ".png"));
	}

	@FXML
	private Label eScore;

	@FXML
	private Label aScore;

	@FXML
	private Label oScore;


	@FXML
	private Label eHigh;

	@FXML
	private Label aHigh;

	@FXML
	private Label oHigh;


	@FXML
	private Rectangle eBar1;

	@FXML
	private Rectangle eBar2;

	@FXML
	private Rectangle eBar3;

	@FXML
	private Rectangle eBar4;

	@FXML
	private Rectangle eBar5;

	@FXML
	private Rectangle eBar6;

	@FXML
	private Rectangle eBar7;

	@FXML
	private Rectangle eBar8;

	@FXML
	private Rectangle eBar9;

	@FXML
	private Rectangle eBar10;


	@FXML
	private CategoryAxis x;

	@FXML
	private NumberAxis y;

	@FXML
	private LineChart<?,?> LineChart;

	@SuppressWarnings("rawtypes")
	XYChart.Series assessments = new XYChart.Series();
	@SuppressWarnings("rawtypes")
	XYChart.Series ass = new XYChart.Series();

	@FXML 
	private Tile overallGraph;

	// sets all graphs according to data
	@SuppressWarnings("unchecked")
	public void setGraphs() throws Exception {

		ArrayList<Integer> allHighs = new ArrayList<Integer>();

		// setting exam section
		if (!exams.isEmpty()) {
			eHigh.setText(Math.round(Collections.max(exams)) + " PTS");
			allHighs.add((int) Math.round(Collections.max(exams)));

			int[] graphScore = new int[10];

			for (int i : graphScore) {
				graphScore[i]  = 0;
			}

			for (int i = 0; i < exams.size(); i++) {
				graphScore[i] = (int) Math.round((exams.get(i) * 105) / 100);
			}

			Timeline eGraphAnimator = new Timeline(new KeyFrame(Duration.seconds(0.017), new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {

					if (eBar1.getHeight() < graphScore[0]) {
						eBar1.setHeight(eBar1.getHeight() + 5);
					}

					if (eBar2.getHeight() < graphScore[1]) {
						eBar2.setHeight(eBar2.getHeight() + 5);
					}

					if (eBar3.getHeight() < graphScore[2]) {
						eBar3.setHeight(eBar3.getHeight() + 5);
					}

					if (eBar4.getHeight() < graphScore[3]) {
						eBar4.setHeight(eBar4.getHeight() + 5);
					}

					if (eBar5.getHeight() < graphScore[4]) {
						eBar5.setHeight(eBar5.getHeight() + 5);
					}

					if (eBar6.getHeight() < graphScore[5]) {
						eBar6.setHeight(eBar6.getHeight() + 5);
					}

					if (eBar7.getHeight() < graphScore[6]) {
						eBar7.setHeight(eBar7.getHeight() + 5);
					}

					if (eBar8.getHeight() < graphScore[7]) {
						eBar8.setHeight(eBar8.getHeight() + 5);
					}

					if (eBar9.getHeight() < graphScore[8]) {
						eBar9.setHeight(eBar9.getHeight() + 5);
					}

					if (eBar10.getHeight() < graphScore[9]) {
						eBar10.setHeight(eBar10.getHeight() + 5);
					}
				}}));

			eGraphAnimator.setCycleCount(21);
			eGraphAnimator.play();
		}

		// setting assessments section
		if (!assesments.isEmpty()) {
			aHigh.setText(Math.round(Collections.max(assesments)) + " PTS");
			allHighs.add((int) Math.round(Collections.max(assesments)));

			assessments.getData().clear();
			ass.getData().clear();

			for (int i = 0; i < assesments.size(); i++) {
				assessments.getData().add(new XYChart.Data<String, Double>(i + "", assesments.get(i)));
			}

			for (int i = 0; i < assignments.size(); i++) {
				ass.getData().add(new XYChart.Data<String, Double>(i + "", assignments.get(i)));
			}
		}

		// setting overall section			
		if (!exams.isEmpty() || !assesments.isEmpty())
			oHigh.setText(Math.round(Collections.max(allHighs)) + " PTS");

		overallGraph.setVisible(true);

		ArrayList<ChartData> overallData = new ArrayList<ChartData>();

		for (Double d : overall) 
			overallData.add(new ChartData("Data", d, Tile.MAGENTA));

		if (!overallData.isEmpty() && overallData.size() == 1)
			overallData.add(overallData.get(0));

		overallGraph.setChartData(overallData);
	}

	public ArrayList<ArrayList<ArrayList<String>>> getGrades() {

		ArrayList<ArrayList<ArrayList<String>>> allGrades = new ArrayList<ArrayList<ArrayList<String>>>();
		ArrayList<ArrayList<String>> gradeList = new ArrayList<ArrayList<String>>();

		for (Node n : overallGrades.getChildren()) {
			gradeList.add(((GradeCell) n).save());
		}

		allGrades.add(gradeList);
		gradeList = new ArrayList<ArrayList<String>>();

		for (Node n : examGrades.getChildren()) {
			gradeList.add(((GradeCell) n).save());
		}

		allGrades.add(gradeList);
		gradeList = new ArrayList<ArrayList<String>>();

		for (Node n : assessmentGrades.getChildren()) {
			gradeList.add(((GradeCell) n).save());
		}

		allGrades.add(gradeList);
		gradeList = new ArrayList<ArrayList<String>>();

		for (Node n : assignmentGrades.getChildren()) {
			gradeList.add(((GradeCell) n).save());
		}

		allGrades.add(gradeList);
		return allGrades;
	}

	public void load(ArrayList<ArrayList<ArrayList<String>>> allGrades) throws Exception {
		overallGrades.getChildren().clear();
		examGrades.getChildren().clear();
		assessmentGrades.getChildren().clear();
		assignmentGrades.getChildren().clear();

		for (int i = 0; i < allGrades.get(0).size(); i++) {
			overallGrades.getChildren().add(new GradeCell(allGrades.get(0).get(i)));
		}

		for (int i = 0; i < allGrades.get(1).size(); i++) {
			examGrades.getChildren().add(new GradeCell(allGrades.get(1).get(i)));
		}

		for (int i = 0; i < allGrades.get(2).size(); i++) {
			assessmentGrades.getChildren().add(new GradeCell(allGrades.get(2).get(i)));
		}

		for (int i = 0; i < allGrades.get(3).size(); i++) {
			assignmentGrades.getChildren().add(new GradeCell(allGrades.get(3).get(i)));
		}
	}
}