package application;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Main extends Application {

	boolean bool = true; 

	@Override
	public void start(Stage primaryStage) {
		try {
			final VBox root = new VBox(10);
			root.setAlignment(Pos.TOP_CENTER);
			root.setPrefHeight(400);
			root.setStyle("-fx-background-color: #000000;");
			final ScrollPane scroller = new ScrollPane();
			scroller.setStyle("-fx-background-color: #000000;");
			scroller.setContent(root);
			final Scene scene = new Scene(scroller,413,400);

			for (int i = 1; i <= 3; i++) {
				final Assignment cell = new Assignment();
				addWithDragging(root, cell);
			}

			
			// in case user drops node in blank space in root:
			root.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
				@Override
				public void handle(MouseDragEvent event) {
					int indexOfDraggingNode = root.getChildren().indexOf(event.getGestureSource());
					rotateNodes(root, indexOfDraggingNode, indexOfDropTarget);
					removePreview(root);
					event.consume();
				}
			});
						
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	int indexOfDropTarget;
	
	private void addWithDragging(final VBox root, final Assignment cell) {

		cell.setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				indexOfDropTarget = root.getChildren().indexOf(cell);			
				addPreview(root,cell);
				cell.setOpacity(0.0);
				cell.toFront();
				cell.startFullDrag();
			}
		});

		cell.setOnMouseDragEntered(new EventHandler<MouseDragEvent>() {
			@Override
			public void handle(MouseDragEvent event) {
				((Assignment) cell).hide();
			}
		});

		cell.setOnMouseDragExited(new EventHandler<MouseDragEvent>() {
			@Override
			public void handle(MouseDragEvent event) {
				((Assignment) cell).show();
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
		ImageView imageView = new ImageView(cell.snapshot(null, null));
		imageView.setEffect(new DropShadow());
		imageView.setOpacity(0.8);
		imageView.setManaged(false);
		imageView.setMouseTransparent(true);
		root.getChildren().add(imageView);
		root.setUserData(imageView);
		root.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getY() >= 0 && event.getY() < root.getHeight() - ((AnchorPane) cell).getHeight())
					imageView.relocate(0, event.getY());		
			}
		});
	}

	private void removePreview(final VBox root) {
		root.getChildren().remove(root.getUserData());
		root.setOnMouseDragged(null);
		root.setUserData(null);
	}

	public static void main(String[] args) {
		launch(args);
	}
}