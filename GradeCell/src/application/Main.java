package application;
	
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


public class Main extends Application {
	private Pagination pagination;
	 
    public static void main(String[] args) throws Exception {
        launch(args);
    }
 
    public int itemsPerPage() {
        return 10;
    }
 
    public VBox createPage(int pageIndex) throws IOException {        
        VBox box = new VBox(5);
        box.setFillWidth(false);
        
        int page = pageIndex * itemsPerPage();
        
        for (int i = page; i < page + itemsPerPage(); i++) {
            box.getChildren().add(new GradeCell(new ArrayList<String>(Arrays.asList("Quiz #1", "", ""))));
        }
        
        System.out.println(((GradeCell) box.getChildren().get(0)).save());
        System.out.println(((GradeCell) box.getChildren().get(0)).save().size());
        return box;
    }
 
    @Override
    public void start(final Stage stage) {        
        pagination = new Pagination(4, 0);
        pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {                     
				try {
					return createPage(pageIndex);
				} catch (IOException e) {
					return null;
				}}});
 
        AnchorPane anchor = new AnchorPane();
        anchor.getChildren().addAll(pagination);
        Scene scene = new Scene(anchor, 331, 372);
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.show();
    }
}