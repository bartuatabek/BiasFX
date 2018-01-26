package application;
	
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			VBox box = new VBox(5);
			box.setAlignment(Pos.TOP_CENTER);
			box.setStyle("-fx-background-color: #000000;");
			ScrollPane scroller = new ScrollPane();
			scroller.setStyle("-fx-background-color: #000000;");
			scroller.setContent(box);
			
			for (int i = 0; i < 20; i++) {
				Activity activity = new Activity("12:30 AM", "1:00 PM", "Activity Name", "Location");
				box.getChildren().add(activity);
			}
			
			Scene scene = new Scene(scroller,374,200);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
