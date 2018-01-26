package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = new AnchorPane();
			EventInfo ei = new EventInfo("Bias Release","1 Infinite Loop", "25", "10 AM", "11 AM");
			ei.setMailAddress("bartu.atabek@icloud.com");
			root.getChildren().add(ei);
			Scene scene = new Scene(root,300,209);
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