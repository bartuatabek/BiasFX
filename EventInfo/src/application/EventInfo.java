package application;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class EventInfo extends Pane {

	//	private static final Coordinate BILKENT = new Coordinate(39.874615, 32.747596);	
	//	private static final Coordinate EE = new Coordinate(39.871999, 32.750920);	
	//	private static final Coordinate EA = new Coordinate(39.871143, 32.750030);
	//	private static final Coordinate EB = new Coordinate(39.871810, 32.749858);
	//	private static final Coordinate LC = new Coordinate(39.869348, 32.750276);
	//	private static final Coordinate LB = new Coordinate(39.869356, 32.750030);
	//	private static final Coordinate LA = new Coordinate(39.869356, 32.749751);
	//	private static final Coordinate G = new Coordinate(39.868731, 32.749606);
	//	private static final Coordinate H = new Coordinate(39.868077, 32.750097);
	//	private static final Coordinate T = new Coordinate(39.868264, 32.749244);
	//	private static final Coordinate A = new Coordinate(39.867614, 32.749437);
	//	private static final Coordinate V = new Coordinate(39.867331, 32.750115);
	//	private static final Coordinate FA = new Coordinate(39.866298, 32.749933);
	//	private static final Coordinate FB = new Coordinate(39.866628, 32.749633);
	//	private static final Coordinate FC = new Coordinate(39.866821, 32.749354);
	//	private static final Coordinate FD = new Coordinate(39.866252, 32.749311);
	//	private static final Coordinate FF = new Coordinate(39.865775, 32.748774);
	//	private static final Coordinate SA = new Coordinate(39.867726, 32.748136);
	//	private static final Coordinate SB = new Coordinate(39.868315, 32.748163);
	//	private static final Coordinate B = new Coordinate(39.868665, 32.748168);
	//	private static final Coordinate M = new Coordinate(39.868035, 32.750148);
	//	private static final Coordinate C = new Coordinate(39.871381, 32.764117);
	//	private static final Coordinate D = new Coordinate(39.870023, 32.765018);
	//	private static final Coordinate NA = new Coordinate(39.870640, 32.764835);

	@FXML
	private JFXTextField eventName;

	@FXML
	private JFXTextArea loc;

	@FXML
	private ImageView mail;

	@FXML
	private ImageView bell;

	@FXML
	private ImageView silent;

	@FXML
	private Label date;

	@FXML
	private Label time;

	@FXML
	private JFXTextArea notes;

	String mailTo = "support@infinity.com";

	public EventInfo(String eventName, String location, String date, String startTime, String endTime) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("EventInfo.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		loader.load();
		setInfo(eventName,location,date,startTime,endTime);
	}

	public void setInfo(String eventName, String location, String date, String startTime, String endTime) {
		SimpleDateFormat month_date = new SimpleDateFormat("MMM");
		Calendar cal = Calendar.getInstance();
		String month = month_date.format(cal.getTime());
		this.eventName.setText(eventName);
		this.loc.setText(location);
		this.date.setText(month + " " + date + ", " + Calendar.getInstance().get(Calendar.YEAR));
		this.time.setText(startTime + " to " + endTime);		
	}

	public void setAlarm(MouseEvent e) {
		if (silent.isVisible()) {
			silent.setVisible(false);
			bell.setVisible(true);
		}
		else {
			silent.setVisible(true);
			bell.setVisible(false);
		}
	}

	public void showNotification() throws IOException, AWTException {
		if (bell.isVisible()) {
			String os = System.getProperty("os.name").toUpperCase();

			if (os.contains("WIN")) {				
				SystemTray tray = SystemTray.getSystemTray();
				Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/resources/icon.png"));
				TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
				trayIcon.setImageAutoSize(true);
				trayIcon.setToolTip("System tray icon demo");
				tray.add(trayIcon);
				trayIcon.displayMessage("Hello, World", "notification demo", MessageType.INFO);
			}

			if (os.contains("MAC")) {
				Runtime.getRuntime().exec(new String[] { "osascript", "-e",
						"display notification \"Make sure you're ready!\""
								+ " with title \"Upcoming Event Alert\" subtitle "
								+ "\"You have an activity coming up next.\" sound name "
								+ "\"Tri-tone\"" });
			}	
		}
	}

	public void setMailAddress(String mail) {
		if (!mail.isEmpty())
			mailTo = mail;
	}

	public void sendMail(MouseEvent e) throws URISyntaxException, IOException {
		Desktop desktop;

		if (Desktop.isDesktopSupported()
				&& (desktop = 
				Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
			URI mailto = new URI("mailto:" + mailTo + 
					"?subject=Upcoming%20Event" + "&body=You%20have%20an%20upcoming%20event%20on%20" + 
					date.getText().replaceAll(" ", "%20") + "%20from%20" + time.getText().replaceAll(" ", "%20") +
					"%20called%20" + eventName.getText().replaceAll(" ", "%20") + "%20at%20" + loc.getText().replaceAll(" ", "%20") + ".");
			desktop.mail(mailto);
		} else {
			throw new RuntimeException();
		}
	}

	//	public void setMap(Coordinate loc) {	
	//		mapView.initializedProperty().addListener((observable, oldValue, newValue) -> {
	//			if (newValue) {
	//				mapView.setCenter(BILKENT);
	//				mapView.addMarker(new Marker(getClass().getResource("/resources/pin.png"), -20, -20).setPosition(loc)
	//						.setVisible(true));
	//			}
	//		});
	//		mapView.initialize();
	//	}

	public void setEditableFalse()
	{
		this.eventName.setEditable(false);
		this.loc.setEditable(false);
	}

	public void setMailInvisible()
	{
		mail.setVisible(false);
	}

	public void setNotes (String note)
	{
		notes.setText(note);
	}

	public void setSilentAndBell (boolean isSilent)
	{
		silent.setVisible(isSilent);
		bell.setVisible(!isSilent);
	}

	public void setEventName (String name)
	{
		this.eventName.setText(name);
	}

	public String getNotes()
	{
		return notes.getText();
	}

	public boolean getSilent()
	{
		return silent.isVisible();
	}

	public String getLocation() {
		return loc.getText();
	}

	public String getEventName() {
		return eventName.getText();
	}
}
