package de.dhbw.vierpunkt.gui;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.util.Duration;
import javafx.util.StringConverter;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.dhbw.vierpunkt.db.ConnectHSQL;
import de.dhbw.vierpunkt.interfaces.GewinnerListener;
import de.dhbw.vierpunkt.interfaces.ParamListener;
import de.dhbw.vierpunkt.interfaces.ZugListener;
import de.dhbw.vierpunkt.logic.LogicListener;
/**
 *
 * @author janaschaub
 */
public class TestGui implements ZugListener, ConnectionErrorListener, GewinnerListener, GameWinnerListener {

	/** Gibt den aktuellen Fuellstand aller Spalten an*/	
	static int[] plaetzeFreiInReihe = new int[7];
	private static List<LogicListener> NameListeners = new ArrayList<LogicListener>();
	private static List<ParamListener> listeners = new ArrayList<ParamListener>();
	
	/** Hauptstage */	
	public static Stage primaryStage = new Stage();					
	
	/** Variablendeklaration fuer die Einstellungsattribute */	
	private String schnittstelle = "pusher";									// Gibt die Art der Schnittstelle an
	private int zugzeit = 1000;													// Gibt die Zugzeit in ms an
	private char xodero = 'x';													// Gibt an, ob Spieler x oder o ist
	private String fileString = new String();									// File fuer die Dateischnittstelle
	
	/** Variablendeklaration und default -initialisierung der Pusher Credentials */	
	private String appId="255967";
	private String appKey="61783ef3dd40e1b399b2";
	private String appSecret="66b722950915220b298c";
	
	/** Variablendeklaration und -initialisierung der Spielerbelegung */
	private static String names1;												// Spielername Spieler (wir)
	private static String names2;												// Spielername Gegner
	private static TextField playerInput;
	private static TextField opponentInput;
	
	public static char spieler = 'x'; 											// Spieler fuer das automatische Spiel
	public char gegner = 'o';													// Gegner fuer das automatische Spiel
	
	public char manuellerSpieler= 'x';											// Spieler fuer das manuelle Spiel
	
	/** Groeßen- und Laengeneinheiten */
	private final int l = 70; 													// Seitenlaenge der Grids
	private double breite = Toolkit.getDefaultToolkit().getScreenSize().width;	// Breite des geoeffneten Fensters in Double
	private boolean fullscreen = true;											// Fuer einfaches Umstellen auf Fullscreen
	
	/** Defaultbelegung des Themas fuer die jeweilige Verknuepfung mit der CSS-Datei */
	private static int thema = 1;
	
	/**Darzustellende Informationen */
	private Text spielstand = new Text("0 : 0");								// Belegung mit Default Werten
	private Text satzstatus = new Text("Spiel noch nicht begonnen");			// Belegung mit Default Werten
	public GridPane spielfeld = new GridPane();									// Deklarieren des Hauptspielfelds
    public GridPane spielfeld2 = new GridPane();								// Deklarieren des Spielfelds fuer die bereits gespielten Spiele
    public Color color = Color.rgb(0, 0, 0);									// Variable fuer die Farbe des Spielfelds
    public Boolean startedGame = false;											// wenn true wird abgefragt, ob Spiel fortgesetzt werden soll
    
	/** Deklarieren der Variablen fuer die Methode bisherigeSpiele()*/
    static Spiele selectedGame;													// Das in der Tabelle ausgewaehlte Spiel
	static String personX;														// Person, die den Zug gemacht hat
	private final TableView<Spiele> table = new TableView<>();					// Tabellenansicht der Spiele
	private ConnectHSQL db = new ConnectHSQL();									// Verknuepfung mit der Datenbank
	private String[][] alleGames = db.getLastTenGames();						// Array fuer die letzten 10 Spiele
    private final ObservableList<Spiele> items = FXCollections.observableArrayList();
	
	/**Erzeugen der verschiedenen Spielsteine*/
    public javafx.scene.image.Image image1 = new javafx.scene.image.Image(getClass().getResource("kuerbis.png").toExternalForm());
	public javafx.scene.image.Image image2 = new javafx.scene.image.Image(getClass().getResource("fledermaus.png").toExternalForm());
    public javafx.scene.image.Image image3 = new javafx.scene.image.Image(getClass().getResource("spielstein_grau.png").toExternalForm());
	public javafx.scene.image.Image kuerbis = new javafx.scene.image.Image(getClass().getResource("kuerbis.png").toExternalForm()); 
    public javafx.scene.image.Image fledermaus = new javafx.scene.image.Image(getClass().getResource("fledermaus.png").toExternalForm()); 
    public javafx.scene.image.Image pizza = new javafx.scene.image.Image(getClass().getResource("pizza.png").toExternalForm());
    public javafx.scene.image.Image burger = new javafx.scene.image.Image(getClass().getResource("burger.png").toExternalForm());
    public javafx.scene.image.Image basketball = new javafx.scene.image.Image(getClass().getResource("basketball.png").toExternalForm());
    public javafx.scene.image.Image baseball = new javafx.scene.image.Image(getClass().getResource("baseball.png").toExternalForm());
    public javafx.scene.image.Image orange = new javafx.scene.image.Image(getClass().getResource("spielstein_orange.png").toExternalForm());
    public javafx.scene.image.Image gruen = new javafx.scene.image.Image(getClass().getResource("spielstein_gruen.png").toExternalForm());
  
    /**Getter und Setter Methoden*/
    public void setSatzstatus(String satzstatus) {this.satzstatus.setText(satzstatus);}
	public void setSpielstand(String spielstand) {this.spielstand.setText(spielstand);}
    public void setZugzeit(int zugzeit) {this.zugzeit = zugzeit;}
    public void setSchnittstelle(String schnittstelle) {this.schnittstelle = schnittstelle;}
    public void setAppId(String appId) {this.appId = appId;}
	public void setAppKey(String appKey) {this.appKey = appKey;}
	public void setAppSecret(String appSecret) {this.appSecret = appSecret;}
    public void setImage1(javafx.scene.image.Image image1) {this.image1 = image1;}
	public void setImage2(javafx.scene.image.Image image2) {this.image2 = image2;}
	public void setNames1(String names1){TestGui.names1 = names1;}
	public void setNames2(String names2){TestGui.names2 = names2;}
	public void setXodero(char xodero) {this.xodero = xodero;}
    public void setColor(Color color) {	this.color = color;}
   	public void setStartedGame(Boolean startedGame) {this.startedGame = startedGame;}

	
    public Text getSpielstand() {return spielstand;}
	public Text getSatzstatus() {return satzstatus;}
	public int getZugzeit() {return zugzeit;}
	public String getSchnittstelle() {return schnittstelle;}
	public String getFileSting(){return fileString;}
	public String getAppId(){return appId;}	
	public String getAppKey(){return appKey;}
	public String getAppSecret(){return appSecret;}
	public javafx.scene.image.Image getImage1() {return image1;}
	public javafx.scene.image.Image getImage2() {return image2;}
	public String getNames1() {return names1;}
	public String getNames2() {return names2;}
	public char getXodero() {return xodero;}
	public Boolean getStartedGame() {return startedGame;}

	/*********************************************************************************************************************
	 ******************************************* START METHODE ***********************************************************
	 ********************************************************************************************************************/
	
	public void start(Stage primaryStage) {
		
		for (int i = 0; i < plaetzeFreiInReihe.length; i++){
			plaetzeFreiInReihe[i]=5;
		}
		
		// Grundlegende Eigenschaften der Stage
		primaryStage.setFullScreen(false); 							// automatisches Oeffnen im Fullscreen
		primaryStage.setTitle("VierPunkt");
		primaryStage.setResizable(true);

		// Layout Boxen
		VBox root = new VBox(); 									// aeusserste Box
		root.setId("root");
		
		HBox content = new HBox();
		content.setPrefWidth(breite); 								// content ueber gesamte Bildschirmbreite
		content.setAlignment(Pos.TOP_CENTER); 						// alle Inhalte werden mittig ausgerichtet

		/************************************************ MENUBAR ******************************************************/
		// MenuBar Hauptkategorien
		final Menu options = new Menu("Optionen");					// Initialisieren der HauptMenuPunkte
		final Menu themes = new Menu("Themen");

		// Menubar, Hauptkategorien setzen
		MenuBar menuBar = new MenuBar();							// Erstellen der MenuBar
		menuBar.setId("menu");
		menuBar.getMenus().addAll(options, themes);					// Hinzufuegen der HauptMenuPunkte

		// Unterkategorien fuer "options"							
		MenuItem menu00 = new MenuItem("neues Spiel");				
		MenuItem menu01 = new MenuItem("bereits gespielte Spiele");
		MenuItem menu02 = new MenuItem("Spiel beenden");
		options.getItems().addAll(menu00, menu01, menu02);			// Punkte unter den MenuPunkt Optionen haengen
		
		// Unterkategorien fuer "themen"
		MenuItem menu10 = new MenuItem("Suessigkeiten");
		MenuItem menu11 = new MenuItem("Halloween");
		MenuItem menu12 = new MenuItem("Food");
		MenuItem menu13 = new MenuItem("Sports");
		themes.getItems().addAll(menu10, menu11, menu12, menu13);	// Punkte unter den MenuPunkt Themen haengen

		/********************************************** UEBERSCHRIFT ***************************************************/

		// Ansicht der Ueberschrift
		ImageView headingImg = new ImageView();						// ImageView fuer das Bild der Ueberschrift
		headingImg.setId("headerImg");
		headingImg.setFitWidth(breite / 2); 						// Ueberschrift soll die Haelfte des Bildschirms breit sein
		headingImg.setFitHeight((breite / 2) / 3.33); 				// Aufrechterhalten des Verhaeltnisses (Quotient: 3.33)
		headingImg.setPreserveRatio(true);			

		// Container, in dem die Ueberschrift platziert wird
		HBox headerHBox = new HBox();								// HBox (Anordnung horizontal)
		headerHBox.setId("titel");									// Id fuer Formatierung mit CSS

		// Rechtecke als Platzhalter, um die Ueberschrift mittig zu platzieren
		Rectangle headingRectLeft = new Rectangle(breite / 4, (breite / 2) / 3.33);
		headingRectLeft.setId("container");
		Rectangle headingRectRight = new Rectangle(breite / 4, (breite / 2) / 3.33);
		headingRectRight.setId("container");

		// Rechtecke und Ueberschrift in den Container einfuegen
		headerHBox.getChildren().addAll(headingRectLeft, headingImg, headingRectRight);

		/******************************************* CONTAINERBOXEN *********************************************************/

		// Hauptcontainer erhaelt den Inhalt
		root.getChildren().addAll(menuBar, headerHBox, content);

		/************************************** CONTAINERBOXEN IN CONTENT **************************************************/
		

		/******* INHALTE DER RECHTEN CONTAINERBOX *********************/
		// Erzeugen der rechten Containerbox
		VBox vbRight = new VBox();												// VBox um Inhalte untereinander anzuordnen
		vbRight.setPrefWidth(breite / 4);
		vbRight.setSpacing(10);
		vbRight.setPadding(new Insets(50, 0, 0, 50));
		
		// Spielstand und Satzstatus anzeigen
		Label spielstandanzeige = new Label("Spielstand: ");
		spielstand.setId("text");
		spielstandanzeige.setPadding(new Insets(20, 0, 0, 0));

		Label satzstatusanzeige = new Label("Satzstatus:");
		satzstatus.setId("text");
		satzstatusanzeige.setPadding(new Insets(20, 0, 0, 0));
		
		// Ueberschrift fuer den Slider
		Label spielmodi = new Label("Spielmodus:");
		spielmodi.setPadding(new Insets(20, 0, 0, 0));
		
		// Slider, um einfach den Spielmodus einstellen zu koennen
		Slider spielmodus = new Slider(0, 2, 1); 			// Slider geht von 0 bis 2 in 1er Abstaenden
		spielmodus.setMinorTickCount(0);
		spielmodus.setMajorTickUnit(1); 					// Man kann nur auf den Zahlen 0, 1, 2 landen, nicht dazwischen
		spielmodus.setSnapToTicks(true); 					// Der Punkt rutzscht zur naechsten Zahl
		spielmodus.setShowTickMarks(true); 					// Markierungen anzeigen -
		spielmodus.setShowTickLabels(true); 				// Beschriftungen anzeigen
		spielmodus.setOrientation(Orientation.VERTICAL); 	// Vertikale Anordnung,standardmaessig horizontal
		spielmodus.setPrefHeight(120); 						// bevorzugte Hoehe des Sliders
		spielmodus.setMinHeight(120); 						// Mindesthoehe
		spielmodus.setValue(2);								// Default Value = 2
		
		// Methode, die die Zahlenbeschriftung durch entsprechenden Text ersetzt und die Rueckgabewerte festlegt
		spielmodus.setLabelFormatter(new StringConverter<Double>() {
			@Override
			public String toString(Double n) {				// Beschriftung an den verschiedenen Units
				if (n == 0)return "gegen den Computer";		
				if (n > 0 && n < 2)return "manuell";
				if (n == 2)return "automatisch";
				return "automatisch";
			}
			@Override
			public Double fromString(String x) {			// Rueckgabewerte der verschiedenen Wahlmoeglichkeiten
				switch (x) {
				case "manuell":return 1d;					
				case "gegen den Computer":return 0d;
				case "automatisch":return 2d;
				default:return 2d;
				}
			}
		});
		
		Button start = new Button("Spiel starten");			
		
		// Platzhalter, damit der nachfolgende Button weiter unten angeordnet wird
		Rectangle platzhalter0 = new Rectangle(10, 40);
		platzhalter0.setOpacity(0);												// nicht sichtbar (100% transparent)

		// Einfuegen der Elemente in die rechte Box
		vbRight.getChildren().addAll(spielstandanzeige, spielstand, satzstatusanzeige, satzstatus, spielmodi, spielmodus, platzhalter0, start);

			
		/******* INHALTE DER LINKEN CONTAINERBOX *********************/
		// Erzeugen der linken Containerbox
		VBox boxlinks = new VBox();
		boxlinks.setId("boxlinks");
		boxlinks.setPrefWidth(breite / 4);
		boxlinks.setAlignment(Pos.BOTTOM_LEFT);
		
		// Ueberschrift Spieler
		Label spielerfarben = new Label("Spieler");
		
		// Name mit Spielstein Spieler
		ImageView i1 = new ImageView(getImage1());								// Darstellung des richtigfarbigen Spielsteins
		i1.setFitWidth(l-20);	i1.setFitHeight(l-20);
		Label s1 = new Label(getNames1());										// Anzeige des beim Start angegebenen Namens
		
		// Name mit Spielstein Gegner
		ImageView i2 = new ImageView(getImage2());
		i2.setFitWidth(l-20);	i2.setFitHeight(l-20);
		Label s2 = new Label(getNames2());
		
		// Platzhalter
		Rectangle p = new Rectangle(20,20);
		p.setOpacity(0);
		Rectangle p1 = new Rectangle(20,20);
		p1.setOpacity(0);
		
		// zur Box Hinzufuegen
		HBox box3 = new HBox();													// HBox, damit die Steine neben den Spielern angezeigt werden
		box3.getChildren().addAll(s1, i1, s2, i2);								
		
		Button einstellungen = new Button("Einstellungen");						// Hier sind alle Einstellungen vorzunehmen
		
		ImageView bild = new ImageView();										// Passend zu jedem Thema ein anderes Bild
		bild.setId("bild");
		bild.setFitWidth(breite / 4); // Breite soll ein Viertel des Fensters betragen
		bild.setPreserveRatio(true); // Das Verhaeltnis soll beibehalten werden
		
		Rectangle platzhalter2 = new Rectangle(l, 2*l);							// Platzhalter, damit das Bild nicht ganz am Boden sitzt
		platzhalter2.setOpacity(0);
				
		
		/******* INHALTE DER MITTLEREN CONTAINERBOX *********************/
		// Erzeugen der mittleren Containerbox
		VBox boxmitte = new VBox();												// VBox, um die GridPane des Spielfelds anzuordnen
		boxmitte.setId("boxmitte");
		boxmitte.setPrefWidth(7 * l);											// Breite an die Anzahl der Spalten angepasst
		boxmitte.setPadding(new Insets(30, 0, 50, 0));
		boxmitte.setMinWidth(7 * l);
		
		Rectangle platzhalter1 = new Rectangle(7 * l, l);						// Platzhalter zum Anzeigen der Spielsteine ueber der Grid
		platzhalter1.setOpacity(0); // Platzhalter nicht sichtbar
		
		// Erzeugen eines GridPanes spielfeld 
		createSpielfeld(spielfeld);
		
		// Einfuegen der Elemente in die mittlere Box
		boxmitte.getChildren().addAll(platzhalter1, spielfeld);
		
		/*********************************************************************************************************************
		 ****************************************************************************************************
		 ********************************************************************************************************************/
		
		
		
		/******* INHALTE DER LOGIN STAGE ************************/
		
		// login Felder
		Label playerLab = new Label("Spieler: ");								
		playerInput= new TextField();									// TextField zur Eingabe des Namens
		if(playerInput.getText() != null && ! playerInput.getText().trim().isEmpty()){
			setNames1(playerInput.getText());									// Name darf nicht leer bleiben
		}
		
		Label opponentLab = new Label("Gegner: ");
		opponentInput= new TextField();								// TextField zur Eingabe des GegnerNamens
		if(opponentInput.getText() != null && ! opponentInput.getText().trim().isEmpty()){
			setNames2(opponentInput.getText());									// Name darf nicht leer bleiben
		}

 		Stage loginStage = new Stage();											//Erzeugen einer neuen Stage als Popup
 		Button login = new Button("Spiel starten");
 		VBox vb = new VBox();
 		vb.setAlignment(Pos.CENTER);
 		vb.setPadding(new Insets(10, 10, 10, 10));
 		
 		Label meldung = new Label("Bitte Spielernamen eingeben");				// Meldung wenn ein Name fehlt
 		meldung.setOpacity(0);													// Anzeige in login EventHandler
 		meldung.setStyle("-fx-font-weight: lighter;");
 		
 		HBox hb5 = new HBox();													
 		playerLab.setPrefWidth(200);
 		hb5.getChildren().addAll(playerLab, playerInput);						// Anordnung Label und Eingabefeld nebeneinander
 		hb5.setSpacing(10);
 		
 		HBox hb6 = new HBox();
 		opponentLab.setPrefWidth(200);
 		hb6.getChildren().addAll(opponentLab, opponentInput);					// Anordnung Label und Eingabefeld nebeneinander
 		hb6.setSpacing(10);
 		
 		Rectangle p2 = new Rectangle(20, 15);									// PLatzhalter (transparent)
 		p2.setOpacity(0);
 		Rectangle p3 = new Rectangle(20, 15);
 		p3.setOpacity(0);
 		
 		vb.getChildren().addAll(hb5, hb6, p2, meldung, p3, login);				// Alle Elemente untereinander in der loginStage
 		Scene scene2 = new Scene(vb, 400, 250);
 		loginStage.setScene(scene2);
 	    scene2.getStylesheets().add(TestGui.class.getResource("Gui.css").toExternalForm()); // CSS-Datei
 	    loginStage.initModality(Modality.APPLICATION_MODAL);
 	    loginStage.setTitle("Spielernamen");
 	    loginStage.setFullScreen(false); 
		
		/******* INHALTE DER EINSTELLUNGEN STAGE ************************/
		
		// Ueberschrift
        Text u1 = new Text("Einstellungen");
        u1.setId("textEinstellungen");
		
		// X oder O
		ToggleGroup group = new ToggleGroup();									// Buttons wie Ein-/Ausschalter
		ToggleButton tb1 = new ToggleButton("X");
		tb1.setToggleGroup(group);
		tb1.setSelected(true);													// Standardauswahl X
		tb1.getStyleClass().add("togglebutton");
		ToggleButton tb2 = new ToggleButton("O");
		tb2.setToggleGroup(group);
		tb2.getStyleClass().add("togglebutton");
		HBox hb4 = new HBox();							
        hb4.getChildren().addAll(tb1, tb2);										// Anordnung nebeneinander in HBox
        hb4.setSpacing(10);
		
		// Schnittstelle
		Label schnittstelle = new Label("Schnittstelle");
		CheckBox file = new CheckBox("File");									// Checkbox zu Auswahl der Schnittstelle 
		file.getStyleClass().add("checkBox");
		Text keinOrdner = new Text("ACHTUNG! Es wurde kein Ordner ausgewaehlt!"); // Fehlermeldung
		keinOrdner.setStyle("-fx-fill: red;");
		keinOrdner.setOpacity(0);												// Fehlermeldung zunaechst nicht sichtbar
		HBox hb = new HBox();
		hb.getChildren().addAll(file, keinOrdner);
		DirectoryChooser chooser = new DirectoryChooser();						// Funktion zum Ordner auswaehlen
		
		CheckBox pusher = new CheckBox("Pusher");
		pusher.getStyleClass().add("checkBox");
		
		// Pusher Credentials													// Eingabefelder fuer die drei Pusher Credentials
        Label cred1 = new Label("App ID:");
        TextField app1 = new TextField ();
        app1.setText(appId);													// Vorbelegung des Textfeldes mit dem Default Wert
        
        HBox hb1 = new HBox();													// Zur Darstellung von Label und Textfeld nebeneinander
        hb1.getChildren().addAll(cred1, app1);
        hb1.setSpacing(10);
        
        Label cred2 = new Label("App Key: ");
        TextField app2 = new TextField ();
        app2.setPrefWidth(200);
        app2.setText(appKey);													// Vorbelegung des Textfeldes mit dem Default Wert
        
        HBox hb2 = new HBox();													// Zur Darstellung von Label und Textfeld nebeneinander
        hb2.getChildren().addAll(cred2, app2);
        hb2.setSpacing(10);
        
        Label cred3 = new Label("App Secret: ");
        TextField app3 = new TextField ();
        app3.setPrefWidth(200);
        app3.setText(appSecret);												// Vorbelegung des Textfeldes mit dem Default Wert
       
        HBox hb3 = new HBox();													// Zur Darstellung von Label und Textfeld nebeneinander
        hb3.getChildren().addAll(cred3, app3);
        hb3.setSpacing(10);
        
        hb1.setOpacity(0);														// Default nicht angezeigt - Aenderung durch Pusher Checkbox
        hb2.setOpacity(0);
        hb3.setOpacity(0);
        
		// Zeit
		Label zeitlabel = new Label("Zugzeit:   " + zugzeit + " ms");		// Anzeige des Default Wertes
		Slider zeit = new Slider(0, 10000, 100); 			// Slider geht von 0 bis 2 in 1er Abstaenden
		zeit.setMinorTickCount(0);
		zeit.setMajorTickUnit(100); 						// Man kann nur auf den Zahlen 0, 1, 2 landen, nicht dazwischen
		zeit.setSnapToTicks(true); 							// Der Punkt rutzscht zur naechsten Zahl
		zeit.setShowTickMarks(true); 						// Markierungen anzeigen -
		zeit.setOrientation(Orientation.HORIZONTAL); 		// Vertikale Anordnung,standardmaessig horizontal
		zeit.setValue(zugzeit);								// Default Value = 2

		// Einfuegen der Elemente in die linke Box
		boxlinks.getChildren().addAll(spielerfarben, box3, p, einstellungen, bild, platzhalter2);
		
		/******* INHALTE DER CHANGETHEME STAGE ************************/
		
		Scene scene = new Scene(root);
		
		// changeTheme Stage
		final Stage changeTheme = new Stage();						// Nachfrage, ob Theme wirklich gewechselt werden soll
		changeTheme.setTitle("Themenwechsel");
		changeTheme.initModality(Modality.APPLICATION_MODAL);
		changeTheme.initOwner(primaryStage);
        VBox themaVbox = new VBox(20);
        themaVbox.setPadding(new Insets(10, 10, 10, 10));                
        
        Label nachricht = new Label();
        nachricht.setText("Das laufende Spiel wird abgebrochen, wenn das Thema gewechselt wird.");
        nachricht.setWrapText(true);								// automatischer Textumbruch
        Button open = new Button("Thema wechseln");					// Thema wird tatsaechlich gewechselt, je in den Themenmethoden
        Button close = new Button("Abbrechen");						// altes Thema wird beibehalten
        HBox hbox = new HBox();
        hbox.getChildren().addAll(open, close);						// Anordnung der buttons nebeneinander
        hbox.setAlignment(Pos.BASELINE_CENTER);
        hbox.setSpacing(20);
        
        close.setOnMouseClicked(new EventHandler<MouseEvent>(){		// neue Stage wird wieder geschlossen
        	@Override
        	public void handle(MouseEvent arg0){
        		changeTheme.close();
        	}
        });
        
        themaVbox.getChildren().addAll(nachricht, hbox);
        Scene themaScene = new Scene(themaVbox, 500, 200);
       
        changeTheme.setScene(themaScene);	

        /******* INHALTE DER SPIELFORTSETZEN STAGE ************************/ 

		// continue Stage
		final Stage continueGame = new Stage();						// Nachfrage, ob Spiel fortgesetzt werden soll
		continueGame.setTitle("Spiel fortsetzen");
		continueGame.initModality(Modality.APPLICATION_MODAL);
		continueGame.initOwner(primaryStage);
        VBox conGameVbox = new VBox(20);
        conGameVbox.setPadding(new Insets(10, 10, 10, 10));   
        conGameVbox.setAlignment(Pos.CENTER);
        
        Label question = new Label();
        question.setText("Es gibt ein bereits angefangenes Spiel. Dieses Spiel fortsetzen?");
        question.setWrapText(true);												// automatischer Textumbruch
        Button continueG = new Button("Ja, Spiel fortsetzen");					// Spiel wird fortgesetzt
        Button skip = new Button("Nein, Spiel verwerfen");						// neues Spiel wird begonnen und altes Spiel verworfen
        HBox conGamehbox = new HBox();
        conGamehbox.getChildren().addAll(continueG, skip);						// Anordnung der buttons nebeneinander
        conGamehbox.setAlignment(Pos.BASELINE_CENTER);
        conGamehbox.setSpacing(20);
        String[][] alleZuege = db.catchWrongState();
        
        continueG.setOnMouseClicked(new EventHandler<MouseEvent>(){				// angefangenes Spiel wird aus der DB geladen
        	@Override
        	public void handle(MouseEvent arg0){
        		
        		continueGame.close();
        		
        		personX = alleZuege[0][2];											// Spieler der den ersten Zug macht
	               
   	     		Thread playThread = new Thread(){
   	     			@Override
   	     			public void run(){
	   	     			for (int i = 0; i < alleZuege[0].length; i++) {
     	                	if(alleZuege[i][4] != null &&alleZuege[i][3]!= null){		// solange keine NullWerte in der Tabelle
     	                		if(personX.equals(alleZuege[i][2])){					// wenn Person die Startperson ist, setzte die Farbe, wenn nicht die andere
         	                		spielsteinAnzeigen(getImageView((getNodeByRowColumnIndex(Integer.parseInt(alleZuege[i][4]), Integer.parseInt(alleZuege[i][3]), spielfeld))), 'x');
         	                	} else{							// Spielstein in der Grid wird gesetzt mit der Spalte und Zeile aus der DB Tabelle Zuege
         	                		spielsteinAnzeigen(getImageView((getNodeByRowColumnIndex(Integer.parseInt(alleZuege[i][4]), Integer.parseInt(alleZuege[i][3]), spielfeld))), 'o');
         	                	}
    							//try {Thread.sleep(2000);} 								// nach jedem angezeigten Zug wird 2 Sekunden gewartet
    							//catch (InterruptedException e) {e.printStackTrace();}
     	                	}
	   	     			}	
   	     			}
   	     		};
   	     		playThread.start();
        		
        	}
        });
        skip.setOnMouseClicked(new EventHandler<MouseEvent>(){					// neue Stage wird wieder geschlossen
        	@Override
        	public void handle(MouseEvent arg0){
        		continueGame.close();
        		db.deleteGame(Integer.parseInt(alleZuege[0][5]), Integer.parseInt(alleZuege[0][0]));
        		
        	}
        });
        
        conGameVbox.getChildren().addAll(question, conGamehbox);
        Scene continueGameScene = new Scene(conGameVbox, 800, 200);
        setCSS(continueGameScene);
       
        continueGame.setScene(continueGameScene);	

        
      
		/******* CONTAINERBOXEN EINFUEGEN ************************/
		content.getChildren().addAll(boxlinks, boxmitte, vbRight);		// Alle drei Container in den Content Teil einfuegen
		
		
		/*********************************************************************************************************************
		 ***************************************************** LISTENER ******************************************************
		 ********************************************************************************************************************/
		// Bei Aenderung des Spielstandes, wird der neue Text (t1) angezeigt
		spielstand.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
            	spielstand.setText(t1);
            	System.out.println("Spielstand hat sich geaendert");
            }
        }); 
		
		// Bei Aenderung des Satzstatzs, wird der neue Text (t1) angezeigt
		satzstatus.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
            	satzstatus.setText(t1);
            	System.out.println("Satzstatus hat sich geaendert");
            }
        }); 
		
		// Bei Aenderung des Spielmodus werden verschiedene Methoden in changeSpielmodus aufgerufen
		spielmodus.valueProperty().addListener(new ChangeListener<Number>() {
		    @Override
		    public void changed(ObservableValue<? extends Number> observable,
		            Number oldValue, Number newValue) {
		    	changeSpielmodus(newValue, einstellungen, start, spielmodus);
		    }
		});
		
		// Bei Aenderung des der Zugzeit, wird die neue Zugzeit angezeigt und durch Start mit uebergeben
		zeit.valueProperty().addListener(new ChangeListener<Number>() {
		    @Override
		    public void changed(ObservableValue<? extends Number> observable,
		            Number oldValue, Number newValue) {
		    	zeitlabel.setText("Zugzeit:   " + newValue + " ms");
		    	setZugzeit(newValue.intValue());
		    }
		});
		
		// Bei Aenderung des ToggleButtons, aendert sich der Wert des eigenen Spielers von x zu o oder andersherum
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov,
		        Toggle toggle, Toggle new_toggle) {
		            if (new_toggle == null)
		                setXodero('o');
		            else
		            	setXodero('x');
		         }
		});
		
		/*********************************************************************************************************************
		 *************************************************** EVENTHANDLER ****************************************************
		 ********************************************************************************************************************/
		// Bei Klicken auf die Pusher Checkbox erscheinen die Credentials Felder
		pusher.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                if(file.isSelected()){
             	   file.setSelected(false); 
             	   keinOrdner.setOpacity(0);					// das Haekchen bei File wird entfernt
             	   pusher.setSelected(true);					// das Haekchen bei Pusher wird gesetzt
                }else{pusher.setSelected(true);}
                setSchnittstelle("pusher");						// Schnittstelle wird auf Pusher gesetzt und bei Start uebergeben
                hb1.setOpacity(1);								// Anzeigen der Credentials
                hb2.setOpacity(1);
                hb3.setOpacity(1);
            }
    	});

		// Bei Klicken auf die File Checkbox erscheind der FileChooser
		file.setOnMouseClicked(new EventHandler<MouseEvent>(){
		     @Override
		     public void handle(MouseEvent arg0) {
		         if(pusher.isSelected()){
		      	   pusher.setSelected(false);   				// das Haekchen bei Pusher wird entfernt
		      	   file.setSelected(true);						// das Haekchen bei File wird gesetzt
		         }else{file.setSelected(true);}
		         setSchnittstelle("file");						// Schnittstelle wird auf File gesetzt und bei start uebergeben
		         hb1.setOpacity(0);
		         hb2.setOpacity(0);
		         hb3.setOpacity(0);
		         
		         File dir = chooser.showDialog(primaryStage);
		 	    if (dir == null) {								// wenn kein Ordner ausgewaehlt wurde wird Meldung angezeigt
		 	        System.out.println("Kein Ordner ausgewählt");
		 	        keinOrdner.setOpacity(1);
		 	    }else{
		 	    	fileString = dir.getPath();					// Pfad des ausgewaehlten Ordners wird gespeichert und in start uebergeben
		 	    	System.out.println(fileString);
		 	    	keinOrdner.setOpacity(0);
		 	    }
		     }
			});
		
		// Bei Clicken auf Einstellungen erscheint ein Popup
		einstellungen.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0){
				// neue Stage
				final Stage dialog = new Stage();							// Erstellen einer neuen Stage als Popup
				dialog.setTitle("Einstellungen");
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(primaryStage);								
                VBox dialogVbox = new VBox(20);
                dialogVbox.setPadding(new Insets(10, 10, 10, 10));                
                
                // Button zum Popup schliessen
                Button ok = new Button("ok");
                
                // Button Action Event
                ok.setOnMouseClicked(new EventHandler<MouseEvent>(){
             	   @Override
                    public void handle(MouseEvent arg0) {					// Speichern der eingegebenen Credentials in den jeweiligen Variablen
             		   if(app1.getText()!= null){setAppId(app1.getText());}
             		   if(app2.getText()!= null){setAppKey(app2.getText());}
             		   if(app3.getText()!= null){setAppSecret(app3.getText());}
             		   
             		   dialog.close();										// Schliessen des Popups
                }});
                
                // Einfuegen in die VBox
                dialogVbox.getChildren().addAll(u1, hb4, schnittstelle, hb, pusher, hb1, hb2, hb3, p1, zeitlabel, zeit, ok);
                Scene dialogScene = new Scene(dialogVbox, 500, 800);
                
                setCSS(dialogScene);										// Scene an aktuelles Thema angepasst
                
              
                dialog.setScene(dialogScene);
                dialog.show();	
			}
		});
		
		// bei Clicken auf Start beginnt der Verbindungsaufbau und das Spiel startet
		start.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
            public void handle(MouseEvent arg0) {
				if(spielmodus.getValue()==2){					// wenn Slider auf automatisch
					spieler = getXodero();
					if(spieler == 'x'){
						gegner = 'o';
					}else{gegner = 'x';}
	            	createGrids_automatisch(spielfeld);			// Spielfeld leer und ohne MouseEvents
	            	if(db.catchWrongState()!=null){
	            		continueGame.show();
	            	}
	            	
				}else{
					spieler = getXodero();
					if(spieler == 'x'){
						gegner = 'o';
					}else{gegner = 'x';}
					createGrids(spielfeld);}					// Spielfeld leer mit MouseEvents
				
				// Uebergabe der Parameter an das PusherInterface
				fireStartEvent(getZugzeit(), getSchnittstelle(), getFileString(), getXodero(), getAppId(), getAppKey(), getAppSecret());
				Thread t1 = new Thread(){
					@Override
					public void run(){
						fireNames(playerInput.getText(), opponentInput.getText());		// Uebergabe der Namen an die KI
					}
				};
				t1.start();
				
				//Diese Methode muss in das Event Match beendet verschoben werden!
				for (int i = 0; i < plaetzeFreiInReihe.length; i++){
					plaetzeFreiInReihe[i]=5;
				}
				
            }
		});
	
		
		/*************************************************************************************************************
		 *************************************************************************************************************
		 ******************************************* EVENTHANDLER FUER DAS MENU **************************************
		 *************************************************************************************************************
		 *************************************************************************************************************/
		
		
 	    /*********************** NEUES SPIEL *****************************/
		menu00.setOnAction(new EventHandler<ActionEvent>(){			
			@Override public void handle(ActionEvent e) {			// je nach eingestelltem Spielmodus wird eine neue Grid erzeugt
				primaryStage.setScene(scene);
				primaryStage.setFullScreen(fullscreen);
				spieler = getXodero();
				if(spieler == 'x'){
					gegner = 'o';
				}else{gegner = 'x';}
				
				if(spielmodus.getValue()==2){
					createGrids_automatisch(spielfeld);				// Spielfeld ohne MouseEvent
				}else{
					spielmodus.setValue(1);
					createGrids(spielfeld);}						// Spielfeld mit MouseEvent
				
				}
		});
		/*********************** BEREITS GESPIELTE SPIELE *****************************/
		menu01.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e) {			
			bisherigeSpiele();										// oeffnet eine neue Stage
			}
		});
		/*********************** SPIEL BEENDEN *****************************/
		menu02.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	onCloseEvent();										// Beendet das Spiel mit einer zuvor geschalteten Abfrage
		    	}
		});		        
        /*********************** THEMA SWEETS *****************************/
		menu10.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				
				if(thema!=4){									// wir nur ausgefuehrt, wenn das Thema nicht eh schon das gleiche ist
					setCSS(themaScene);
					
					open.setOnMouseClicked(new EventHandler<MouseEvent>() {		// wenn man bei der Abfrage wirklich das Thema wechseln will
						@Override
			            public void handle(MouseEvent arg0) {
							
							changeTheme.close();
							primaryStage.setScene(scene);
							primaryStage.setFullScreen(fullscreen);
							setColor(Color.PURPLE);								// Farbe des Spielfeldes
							setImage1(orange);									// Image des Spielsteins
							setImage2(gruen);									// Image des Spielsteins
							i1.setImage(orange);								// Image Spieleranzeige links
							i2.setImage(gruen);									// Image Spieleranzeige links
							scene.getStylesheets().clear();
							scene.getStylesheets().add(TestGui.class.getResource("Sweets.css").toExternalForm());	// CSS-Datei
							if(spielmodus.getValue()==2){
								createGrids_automatisch(spielfeld);
							}else{createGrids(spielfeld);}
							thema = 4;											// Thema aktualisieren
						}
			        });
					changeTheme.show();
				}
			}
		});
        /*********************** THEMA HALLOWEEN *****************************/
		menu11.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				
				if(thema != 1){ 									// wir nur ausgefuehrt, wenn das Thema nicht eh schon das gleiche ist
					setCSS(themaScene);
					open.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
			            public void handle(MouseEvent arg0) {
							
							changeTheme.close();
							primaryStage.setScene(scene);
							primaryStage.setFullScreen(fullscreen);
							scene.getStylesheets().clear();
							scene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());
							setColor(Color.BLACK);					// Farbe des Spielfeldes
							setImage1(kuerbis);						// Image des Spielsteins
							setImage2(fledermaus);					// Image des Spielsteins
							i1.setImage(kuerbis);					// Image Spieleranzeige links
							i2.setImage(fledermaus);				// Image Spieleranzeige links
							if(spielmodus.getValue()==2){
								createGrids_automatisch(spielfeld);
							}else{createGrids(spielfeld);}
							thema = 1;								// Thema aktualisieren
						}
			        });
					changeTheme.show();
				} 
			}
		});
        /*********************** THEMA FOOD *****************************/
		menu12.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				
				if(thema != 2){											// wir nur ausgefuehrt, wenn das Thema nicht eh schon das gleiche ist
					setCSS(themaScene);
					open.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
			            public void handle(MouseEvent arg0) {
							
							changeTheme.close();
							primaryStage.setScene(scene);
							primaryStage.setFullScreen(fullscreen);
							scene.getStylesheets().clear();
							scene.getStylesheets().add(TestGui.class.getResource("Food.css").toExternalForm());
							setColor(Color.DARKGREEN);					// Farbe des Spielfeldes
							setImage1(pizza);							// Image des Spielsteins
							setImage2(burger);							// Image des Spielsteins
							i1.setImage(pizza);							// Image Spieleranzeige links
							i2.setImage(burger);						// Image Spieleranzeige links
							if(spielmodus.getValue()==2){
								createGrids_automatisch(spielfeld);
							}else{createGrids(spielfeld);}
							thema = 2;
						}
			        });
					changeTheme.show();
				}
			}
		});
        /*********************** THEMA SPORT *****************************/
		menu13.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				
				if(thema!= 3){												// wir nur ausgefuehrt, wenn das Thema nicht eh schon das gleiche ist
					setCSS(themaScene);
					open.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
			            public void handle(MouseEvent arg0) {
							changeTheme.close();
							primaryStage.setScene(scene);
							primaryStage.setFullScreen(fullscreen);
							scene.getStylesheets().clear();
							scene.getStylesheets().add(TestGui.class.getResource("Sport.css").toExternalForm());
							setColor(Color.CADETBLUE);						// Farbe des Spielfelds
							setImage1(basketball);							// Image des Spielsteins
							setImage2(baseball);							// Image des Spielsteins
							i1.setImage(basketball);						// Image Spieleranzeige links
							i2.setImage(baseball);							// Image Spieleranzeige links
							if(spielmodus.getValue()==2){
								createGrids_automatisch(spielfeld);
							}else{createGrids(spielfeld);}
							thema = 3;
						}
			        });
					changeTheme.show();
				}
			}
		});
		
		
	    
		/*********************** LOGIN BEI ENTER UND CLICK *****************************/
	    playerInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
	        @Override
	        public void handle(KeyEvent ke)								// TastaturEvent
	        {
	            if (ke.getCode().equals(KeyCode.ENTER))					// Bei ENTER erscheint eine Meldung, wenn
	            {meldung.setText("Bitte Spielernamen eingeben");
				if(playerInput.getText() == null || playerInput.getText().trim().isEmpty() || opponentInput.getText() == null ||  opponentInput.getText().trim().isEmpty()){
					meldung.setOpacity(1);								// einer der beiden Spielernamen noch null ist
				}else{
					if(playerInput.getText().equals(opponentInput.getText())){	
						meldung.setText("Bitte unterschiedliche Spielernamen waehlen.");
						meldung.setOpacity(1);							// oder die Spielernamen identisch sind
					}else{
						loginStage.close();								// bei erfolgreicher Eingabe wird die LoginStage geschlossen
						s1.setText(playerInput.getText());				// die Spielernamen werden gespeichert
						s2.setText(opponentInput.getText());
						primaryStage.show();							// und die HauptStage geoeffnet
						}
			
	            }
	        }
	        }});
	    
	    opponentInput.setOnKeyPressed(new EventHandler<KeyEvent>(){
	        @Override
	        public void handle(KeyEvent ke)							// siehe playerInput
	        {
	            if (ke.getCode().equals(KeyCode.ENTER))
	            {
	            	meldung.setText("Bitte Spielernamen eingeben");
					if(playerInput.getText() == null || playerInput.getText().trim().isEmpty() || opponentInput.getText() == null ||  opponentInput.getText().trim().isEmpty()){
						meldung.setOpacity(1);
					}else{
						if(playerInput.getText().equals(opponentInput.getText())){
							meldung.setText("Bitte unterschiedliche Spielernamen waehlen.");
							meldung.setOpacity(1);
						}else{
							loginStage.close();
							s1.setText(playerInput.getText());
							s2.setText(opponentInput.getText());
							primaryStage.show();
							}
	            }
	        }
	        }});
	    
	    login.setOnKeyPressed(new EventHandler<KeyEvent>() {
	         public void handle(KeyEvent evt)						// siehe playerInput
	         {
	              if (evt.getCode() == KeyCode.ENTER)
	            	  meldung.setText("Bitte Spielernamen eingeben");
					if(playerInput.getText() == null || playerInput.getText().trim().isEmpty() || opponentInput.getText() == null ||  opponentInput.getText().trim().isEmpty()){
						meldung.setOpacity(1);
					}else{
						if(playerInput.getText().equals(opponentInput.getText())){
							meldung.setText("Bitte unterschiedliche Spielernamen waehlen.");
							meldung.setOpacity(1);
						}else{
							loginStage.close();
							s1.setText(playerInput.getText());
							s2.setText(opponentInput.getText());
							primaryStage.show();
							}
				
	         }
	    }});
	    
		login.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
            public void handle(MouseEvent arg0) {							// bei clicken des Buttons, gleiche funktionen wie bei playerInput
				meldung.setText("Bitte Spielernamen eingeben");
				if(playerInput.getText() == null || playerInput.getText().trim().isEmpty() || opponentInput.getText() == null ||  opponentInput.getText().trim().isEmpty()){
					meldung.setOpacity(1);
				}else{
					if(playerInput.getText().equals(opponentInput.getText())){
						meldung.setText("Bitte unterschiedliche Spielernamen waehlen.");
						meldung.setOpacity(1);
					}else{
						loginStage.close();
						s1.setText(playerInput.getText());
						s2.setText(opponentInput.getText());
						primaryStage.show();
						}
				}
            }
		});
	    
		// primary Stage
		primaryStage.setScene(scene);
		primaryStage.setFullScreen(fullscreen);
		scene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm()); // Default CSS-Datei 
		createSpielfeld(spielfeld2);															// Spielfeld fuer bereits gespielte Spiele
		
		// manuell
		if(spielmodus.getValue() == 1 ){ createGrids(spielfeld);}								// Startspielfeld je nach Spielmodus-Einstellung
		// automatisch
		if(spielmodus.getValue() == 2){createGrids_automatisch(spielfeld);}
		
		loginStage.show();																		// Anzeige der loginStage
		
	}
	
	/*********************************************************************************************************************
	 ******************************************* SPIELFELD METHODEN ******************************************************
	 ********************************************************************************************************************/
	
	/******************************* Erzeugt die Zeilen und Spalten der GridPane ****************************************/
	public void createSpielfeld(GridPane spielfeldGrid){
		spielfeldGrid.setId("spielfeld");														// ID fuer CSS
		// Erzeugen der Spalten (7)
		spielfeldGrid.getColumnConstraints().addAll(new ColumnConstraints(l, l, Double.MAX_VALUE),				
				new ColumnConstraints(l, l, Double.MAX_VALUE), new ColumnConstraints(l, l, Double.MAX_VALUE),
				new ColumnConstraints(l, l, Double.MAX_VALUE), new ColumnConstraints(l, l, Double.MAX_VALUE),
				new ColumnConstraints(l, l, Double.MAX_VALUE), new ColumnConstraints(l, l, Double.MAX_VALUE));
		// Erzeugen der Zeilen (6)
		spielfeldGrid.getRowConstraints().addAll(new RowConstraints(l, l, Double.MAX_VALUE),
				new RowConstraints(l, l, Double.MAX_VALUE), new RowConstraints(l, l, Double.MAX_VALUE),
				new RowConstraints(l, l, Double.MAX_VALUE), new RowConstraints(l, l, Double.MAX_VALUE),
				new RowConstraints(l, l, Double.MAX_VALUE));
	}
	
	/*********************************** Belegung der GridPane mit Feldelementen ****************************************/   
	public void createGrids_automatisch(GridPane spielfeld){
	    	spielfeld.getChildren().clear();									// Loeschen der Elemente in der Grid
	    	
	        for(int anzahlzeilen=0;anzahlzeilen<spielfeld.getRowConstraints().size(); anzahlzeilen++){
	            for(int anzahlspalten=0; anzahlspalten<spielfeld.getColumnConstraints().size(); anzahlspalten++){
	            
	            // Darstellung des Rahmens/ der Zellen    						
	            Rectangle rect = new Rectangle(l,l);							// Rechteck in Cellgroesse
	            Circle circ = new Circle((l/2)-5);								// Kreis mit einem Radius der 5px kleiner ist
	            circ.centerXProperty().set(l/2);								
	            circ.centerYProperty().set(l/2);	
	            Shape cell = Path.subtract(rect, circ);							// Erzeugung einer shape aus Rechteck und Kreis
	            cell.setId("cell");												// ID fuer CSS
	            cell.setFill(color);											// Angabe der Farbe mit einer Variablen (je nach Thema anders)
	            cell.setStroke(color.darker());									// Umrandung immer etwas dunkler als die tatsaechliche Farbe
	            
	            // Ansicht der Spielsteine
	            ImageView spielstein = new ImageView(image1);					// ImageViews fuer alle Felder erzeugen
	            spielstein.setImage(image1);									// StartImage fuer den Spielstein waehlen
	            spielstein.setId("spielstein" + anzahlspalten);
	            spielstein.setFitWidth(l-10);									// Groesse genau so gross wie der Kreis
	            spielstein.setPreserveRatio(true);  							// Seitenverhaeltnis beibehalten
	            
	            // Vorschau der Spielsteine
	            ImageView vorschauspielstein = new ImageView(image3);			// weitere ImageVier fuer die Vorschau des Spielsteins beim Hovern
	            vorschauspielstein.setFitWidth(l-10);
	            vorschauspielstein.setPreserveRatio(true); 
	            vorschauspielstein.setOpacity(0.5);								// Vorschau wird halbtransparent angezeigt
	            
	            spielstein.setTranslateY(-(l*(anzahlzeilen+1)));
	            
	            // Zellen werden gefuellt
	            StackPane stack = new StackPane();								
	            stack.getChildren().addAll(cell, vorschauspielstein, spielstein);   // Fuellen der Zelle mit Rahmen, Vorschau und Spielstein
	            spielfeld.add(stack, anzahlspalten, anzahlzeilen); 					// Jede Zelle der Grid wird mit einem Stack gefuellt
	            }
	         }
	    }

	/************************* Belegung der GridPane mit Feldelementen und MouseEvents **********************************/
	public void createGrids(GridPane spielfeld){
	    	spielfeld.getChildren().clear();									// Loeschen der Elemente in der Grid
	    	Thread t2 = new Thread(){
				@Override
				public void run(){
					startManGame(names1, names2);		// Uebergabe der Namen an die KI
				}
			};
			t2.start();
	    	
	    	System.out.println("startManGame gestartet");
	        for(int anzahlzeilen=0;anzahlzeilen<spielfeld.getRowConstraints().size(); anzahlzeilen++){
	            for(int anzahlspalten=0; anzahlspalten<spielfeld.getColumnConstraints().size(); anzahlspalten++){
	            
	            // Darstellung des Rahmens/ der Zellen    
	            Rectangle rect = new Rectangle(l,l);							// Rechteck in Cellgroesse
	            Circle circ = new Circle((l/2)-5);								// Kreis mit einem Radius der 5px kleiner ist
	            circ.centerXProperty().set(l/2);
	            circ.centerYProperty().set(l/2);
	            Shape cell = Path.subtract(rect, circ);							// Erzeugung einer shape aus Rechteck und Kreis
	            cell.setId("cell");												// ID fuer CSS
	            cell.setFill(color);											// Angabe der Farbe mit einer Variablen (je nach Thema anders)
	            cell.setStroke(color.darker());									// Umrandung immer etwas dunkler als die tatsaechliche Farbe
	           
	            // Ansicht der Spielsteine
	            ImageView spielstein = new ImageView(image1);					// ImageViews fuer alle Felder erzeugen
	            spielstein.setImage(image1);									// StartImage fuer den Spielstein waehlen
	            spielstein.setId("spielstein" + anzahlspalten);
	            spielstein.setFitWidth(l-10);									// Groesse genau so gross wie der Kreis
	            spielstein.setPreserveRatio(true);  							// Seitenverhaeltnis beibehalten  
	            
	            // Vorschau der Spielsteine
	            ImageView vorschauspielstein = new ImageView(image3);			// weitere ImageVier fuer die Vorschau des Spielsteins beim Hovern
	            vorschauspielstein.setImage(image1);
	            vorschauspielstein.setFitWidth(l-10);
	            vorschauspielstein.setPreserveRatio(true); 
	            vorschauspielstein.setOpacity(0.5);								// Vorschau wird halbtransparent angezeigt
	            
	            /*******************************************  ANZEIGE IM SPIELFELD  ************************************************/ 
	            
	        	// Methode zur Vorschau - jeweiliger Spielstein wird angezeigt
	               vorschauspielstein.setOnMouseEntered(new EventHandler<MouseEvent>(){
	                   @Override
	                   public void handle(MouseEvent arg0) {
	                       if(manuellerSpieler=='x'){vorschauspielstein.setImage(image1);      
	                       }else{vorschauspielstein.setImage(image2);}
	                   }
	               });
	               
	               // Beim Verlassen des Vorschau-Spielsteins erscheint wieder der graue Hintergrund
	                vorschauspielstein.setOnMouseExited(new EventHandler<MouseEvent>(){
	                   @Override
	                   public void handle(MouseEvent arg0) {
	                       vorschauspielstein.setImage(image3);
	                   }
	               });
	                
	               spielstein.setTranslateY(-(l*(anzahlzeilen+1)));				// Positionieren des Spielsteins oberhalb der Grid
	            
	               // Spielsteine oberhalb des Spielfelds - Anzeige der Vorschau
	               spielstein.setOnMouseEntered(new EventHandler<MouseEvent>(){
	               @Override
	                   public void handle(MouseEvent arg0) {
	                       vorschauspielstein.setImage(image3);
	                       if(manuellerSpieler=='x'){ vorschauspielstein.setImage(image1);
	                       }else{vorschauspielstein.setImage(image2); }
	                   }
	               });
	               
	               // Beim Verlassen des Spielsteins erscheint wieder der graue Hintergrund
	              spielstein.setOnMouseExited(new EventHandler<MouseEvent>(){
	                   @Override
	                   public void handle(MouseEvent arg0) {
	                        vorschauspielstein.setImage(image3);
	                   }
	               });
	              
	              // Setzen der Spielsteine
	              spielstein.setOnMouseClicked(new EventHandler<MouseEvent>(){
	                   @Override public void handle(MouseEvent arg0) { 
	                	   if(manuellerSpieler == 'x'){									// je nach aktuellem Spieler wird die richtige Farbe gesetzt
	                		   System.out.println(playerInput.getText() + " " + GridPane.getColumnIndex(spielstein.getParent()) + " " + GridPane.getRowIndex(spielstein.getParent()));
	                		   spielsteinAnzeigen(spielstein, manuellerSpieler);
	                		   setManTurn(playerInput.getText(), GridPane.getColumnIndex(spielstein.getParent()),GridPane.getRowIndex(spielstein.getParent()));
	                		   manuellerSpieler = 'o';
	                	   }else{
	                		   System.out.println(opponentInput.getText() + " " + GridPane.getColumnIndex(spielstein.getParent()) + " " + GridPane.getRowIndex(spielstein.getParent()));
	                		   spielsteinAnzeigen(spielstein, manuellerSpieler);
	                		   setManTurn(opponentInput.getText(), GridPane.getColumnIndex(spielstein.getParent()), GridPane.getRowIndex(spielstein.getParent()));
	                		    manuellerSpieler = 'x';
	                	   }
	                   }
	               });
	              
	               // Setzen der Spielsteine beim Klick auf die entsprechende Vorschau
	               vorschauspielstein.setOnMouseClicked(new EventHandler<MouseEvent>(){
	                   @Override public void handle(MouseEvent arg0) {
	                	   if(manuellerSpieler == 'x'){
	                		   System.out.println( playerInput.getText() + " " + GridPane.getColumnIndex(spielstein.getParent()) + " " + GridPane.getRowIndex(spielstein.getParent()));
	                		   spielsteinAnzeigen(spielstein, manuellerSpieler);
	                		   setManTurn(playerInput.getText(), GridPane.getColumnIndex(spielstein.getParent()),GridPane.getRowIndex(spielstein.getParent()));
	                		   manuellerSpieler = 'o';
	                	   }else{
	                		   System.out.println(opponentInput.getText()+ " " + GridPane.getColumnIndex(spielstein.getParent()) + " " + GridPane.getRowIndex(spielstein.getParent()));
	                		   spielsteinAnzeigen(spielstein, manuellerSpieler);
	                		   setManTurn(opponentInput.getText(), GridPane.getColumnIndex(spielstein.getParent()), GridPane.getRowIndex(spielstein.getParent()));
	                		   manuellerSpieler = 'x';
	                	   }
	                   }
	               });
	           
	            /*******************************************************************************************************************
	             *******************************************  ZELLEN FUELLEN  ******************************************************
	             *******************************************************************************************************************/
	            // Zellen werden gefuellt
	            StackPane stack = new StackPane();
	            vorschauspielstein.setImage(image3);                         		// Hintergrund grau
	            stack.getChildren().addAll(cell, vorschauspielstein, spielstein);   // Fuellen der Zelle mit Rahmen, Vorschau oder Spielstein
	            spielfeld.add(stack, anzahlspalten, anzahlzeilen); 					// Jede Zelle der Grid wird mit einem Stack gefuellt
	            }
	        }
	    }
		
	/*********************************************************************************************************************
	 ******************************************* NEUE STAGES METHODEN ****************************************************
	 ********************************************************************************************************************/

	/********************************** Anzeige der 10 zuletzt gespielten Spiele ****************************************/
	public void bisherigeSpiele(){
		
		// neue Stage
		final Stage spieleStage = new Stage();										// Neue Stage wird erzeugt
		spieleStage.setTitle("Bisherige Spiele");
		spieleStage.initModality(Modality.APPLICATION_MODAL);
		spieleStage.initOwner(primaryStage);
       
        // Elemente der Stage
        Button back = new Button("Zurueck");										// Zurueck Button schliesst die Stage
        Button play = new Button("Play"); 											// laesst die Zuege anzeigen
        Label spieleLabel = new Label("Bisherige Spiele:");
        Label spielstandanzeige = new Label("Spielstand: ");
        Label spielerLabel = new Label("Spieler: ");
		
		Text spielstand_altesSpiel = new Text("1:0");
		Rectangle platzhalter1 = new Rectangle(7 * l, l);
		Rectangle platzhalter2 = new Rectangle(l, l);
		Rectangle platzhalter3 = new Rectangle(l, l);
		Rectangle platzhalter4 = new Rectangle(l, l);
		Rectangle platzhalter5 = new Rectangle(l, l);
		Rectangle platzhalter7 = new Rectangle(l, l*2);
        Slider satz = new Slider(0, 2, 1); 											// Satz der abgespielt werden soll ist frei waehlbar
       
        HBox hb = new HBox();
        VBox grid = new VBox();
		VBox anzeige = new VBox();
		VBox spieleranzeige = new VBox();
		VBox spieleVbox = new VBox(20);
		hb.getChildren().addAll(spieleranzeige, grid, anzeige);						// 3 Hauptboxen werden nebeneinander angeordnet
        grid.getChildren().addAll(platzhalter1, spielfeld2);						// Das Spielfeld wird durch Platzhalter nach unten verschoben
		anzeige.getChildren().addAll(satz, platzhalter7, play, platzhalter4);		// Anzeige des Sliders und des Play Buttons uebereinander
		
		spieleVbox.getChildren().addAll(spieleLabel, table, hb);					// Anzeigen der Tabelle oberhalb des neuen Spielfeldes
        
        // weitere Details zu den Elementen
        spielstandanzeige.setPadding(new Insets(20, 0, 0, 0));
		platzhalter1.setOpacity(0); // Platzhalter nicht sichtbar
		platzhalter2.setOpacity(0); // Platzhalter nicht sichtbar
		platzhalter3.setOpacity(0); // Platzhalter nicht sichtbar
		platzhalter4.setOpacity(0); // Platzhalter nicht sichtbar
		platzhalter5.setOpacity(0); // Platzhalter nicht sichtbar
		platzhalter7.setOpacity(0); // Platzhalter nicht sichtbar
		// Slider geht von 0 bis 2 in 1er Abstaenden
		satz.setMinorTickCount(0);
		satz.setMajorTickUnit(1); 						// Man kann nur auf den Zahlen 0, 1, 2 landen, nicht dazwischen
		satz.setSnapToTicks(true); 						// Der Punkt rutzscht zur naechsten Zahl
		satz.setShowTickMarks(true); 					// Markierungen anzeigen -
		satz.setShowTickLabels(true);
		satz.setOrientation(Orientation.VERTICAL); 	// Vertikale Anordnung,standardmaessig horizontal
		satz.setValue(0);								// Default Wert ist der erste Satz
		satz.setLabelFormatter(new StringConverter<Double>() {
			@Override
			public String toString(Double n) {
				if (n == 0)return "Satz 1";
				if (n > 0 && n < 2)return "Satz 2";
				if (n == 2)return "Satz 3";
				return "Satz 1";
			}
			@Override
			public Double fromString(String x) {
				switch (x) {
				case "Satz 1":return 0d;
				case "Satz 2":return 1d;
				case "Satz 3":return 2d;
				default:return 2d;
				}
			}
		});
		// VBox und HBox Details
		anzeige.setAlignment(Pos.BOTTOM_CENTER);		// Anordnung unten in der Box und zentriert
		anzeige.setPrefWidth((breite-(7*l))/2);
		spieleranzeige.setAlignment(Pos.BOTTOM_CENTER);
		spieleranzeige.setPrefWidth((breite-(7*l))/2);
        hb.setAlignment(Pos.BOTTOM_CENTER);
        spieleVbox.setAlignment(Pos.CENTER);
        spieleVbox.setPadding(new Insets(10, 10, 10, 10));   
        
        
        table.setEditable(true);
        
        // Initialisierung der Spalten der Tabelle
        TableColumn<Spiele, String> gameIDCol = new TableColumn<>("Spiele ID");				// neue Spalte Spiele ID mit dem Datentyp der Werte aus der Tabelle
        gameIDCol.setMinWidth(100);
        gameIDCol.setCellValueFactory(new PropertyValueFactory<Spiele, String>("gameID"));
        
        TableColumn<Spiele, String> player1Col = new TableColumn<>("Spieler 1");			// neue Spalte Spieler1 mit dem Datentyp der Werte aus der Tabelle
        player1Col.setMinWidth(100);
        player1Col.setCellValueFactory(new PropertyValueFactory<Spiele, String>("player1"));
        
        TableColumn<Spiele, String> player2Col = new TableColumn<>("Gegner");				// neue Spalte Gegner mit dem Datentyp der Werte aus der Tabelle
        player2Col.setMinWidth(100);
        player2Col.setCellValueFactory(new PropertyValueFactory<Spiele, String>("player2"));
        
        TableColumn<Spiele, String> winnerCol = new TableColumn<>("Gewinner");				// neue Spalte Gewinner mit dem Datentyp der Werte aus der Tabelle
        winnerCol.setMinWidth(100);
        winnerCol.setCellValueFactory(new PropertyValueFactory<Spiele, String>("winner"));
		
		for (int row = 0; row < alleGames.length; row++) { // Zeile
			String firstcolumn = null;
			String secondcolumn = null;
			String thridcolumn = null;
			String fourthcolumn = null;
			for (int column = 0; column < alleGames[row].length; column++) { // Spalte Hinzufuegen aller Spielattribute zu der itemlist
				if (column == 0) {
					firstcolumn = alleGames[row][column];
				}
				if (column == 1) {
					secondcolumn = alleGames[row][column];
				}
				if (column == 2) {
					thridcolumn = alleGames[row][column];
				}
				if (column == 3) {
					fourthcolumn = alleGames[row][column];
				}
			}
			try {
				items.add(new Spiele(firstcolumn, secondcolumn, thridcolumn, fourthcolumn));
			} catch (Exception ex) {
				System.out.println("Empty fields or illegal arguments passed.");
			}
		}
        
		
        
        Label spieler1 = new Label("Spieler1");
       
        ImageView i1 = new ImageView(getImage1());								// Darstellung des richtigfarbigen Spielsteins
		i1.setFitWidth(l-20);	i1.setFitHeight(l-20);
		ImageView i2 = new ImageView(getImage2());								// Darstellung des richtigfarbigen Spielsteins
		i2.setFitWidth(l-20);	i2.setFitHeight(l-20);
		Label spieler2 = new Label("Spieler2");
		
	
        table.setItems(items);																// Anzeigen der Zeilen gefuellt mit den Spielen der Datenbank
        table.getColumns().addAll(gameIDCol, player1Col, player2Col, winnerCol);			// Anzeigen der Spalten
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	          System.out.println(newSelection);  
			  if (newSelection != null) {
	                selectedGame = table.getSelectionModel().getSelectedItem();				// Speichern des Games der ausgewaehlten Zeile
	                int g_id = Integer.parseInt(selectedGame.getGameID());					// speichern der GameID des ausgewaehlten Spiels
	                String[][] alleSaetze = db.getHighscoreMatch(g_id);						// Anlegen eines Arrays mit den Saetzen des Spiels
	                int m_id = Integer.parseInt(alleSaetze[(int)satz.getValue()][0]);		// speichern der MatchID nach entsprechendem Slider Wert
	                String[][] alleZuege = db.getHighscoreTurn(g_id, m_id);					// Anlegen eines Arrays mit den Zuegen des Satzes
	                
	                
	                spieler1.setText(alleZuege[0][2]);
	                spieler2.setText(alleZuege[1][2]);
	                
	                play.setOnMouseClicked(new EventHandler<MouseEvent>(){								
	     	     	   @Override
	     	            public void handle(MouseEvent arg0) {
	     	     		   
	     	     		   	
	     	                personX = alleZuege[0][2];												// Spieler der den ersten Zug macht
	     	               
	     	                for (int i = 0; i < plaetzeFreiInReihe.length; i++){
	     	        			plaetzeFreiInReihe[i]=5;
	     	        		}
	     	                
	     	   	     		Thread playThread = new Thread(){
	     	   	     			@Override
	     	   	     			public void run(){
	     		   	     			for (int i = 0; i < alleZuege[0].length; i++) {
	     	     	                	if(alleZuege[i][4] != null &&alleZuege[i][3]!= null){		// solange keine NullWerte in der Tabelle
	     	     	                		if(personX.equals(alleZuege[i][2])){					// wenn Person die Startperson ist, setzte die Farbe, wenn nicht die andere
	     	         	                		//spielsteinAnzeigen(getImageView((getNodeByRowColumnIndex(Integer.parseInt(alleZuege[i][4]), Integer.parseInt(alleZuege[i][3]), spielfeld2))), 'x');
	     	     	                			spielsteinAnzeigen(getImageView((getNodeByRowColumnIndex(plaetzeFreiInReihe[Integer.parseInt(alleZuege[i][3])], Integer.parseInt(alleZuege[i][3]), spielfeld2))), 'x');
	     	     	                			plaetzeFreiInReihe[Integer.parseInt(alleZuege[i][3])]--;
	     	         	                	} else{							// Spielstein in der Grid wird gesetzt mit der Spalte und Zeile aus der DB Tabelle Zuege
	     	         	                		//spielsteinAnzeigen(getImageView((getNodeByRowColumnIndex(Integer.parseInt(alleZuege[i][4]), Integer.parseInt(alleZuege[i][3]), spielfeld2))), 'o');
	     	         	                		spielsteinAnzeigen(getImageView((getNodeByRowColumnIndex(plaetzeFreiInReihe[Integer.parseInt(alleZuege[i][3])], Integer.parseInt(alleZuege[i][3]), spielfeld2))), 'o');
	     	         	                		plaetzeFreiInReihe[Integer.parseInt(alleZuege[i][3])]--;
	     	         	                	}
	     	    							try {Thread.sleep(2000);} 								// nach jedem angezeigten Zug wird 2 Sekunden gewartet
	     	    							catch (InterruptedException e) {e.printStackTrace();}
	     	     	                	}
	     		   	     			}	
	     	   	     			}
	     	   	     		};
	     	   	     		playThread.start();
	     	   	     	}
	     	     });
			  }
        });
        

		HBox hbox1 = new HBox();
		hbox1.getChildren().addAll(spieler1, i1);
		HBox hbox2 = new HBox();
		hbox2.getChildren().addAll(spieler2, i2);
		
		
		spieleranzeige.getChildren().addAll(spielerLabel, hbox1, hbox2, platzhalter2, spielstandanzeige, spielstand_altesSpiel, platzhalter3, back, platzhalter5);
        
		
	     		   
        // Button Action Event
        back.setOnMouseClicked(new EventHandler<MouseEvent>(){
     	   @Override
            public void handle(MouseEvent arg0) {
     		   spieleStage.close();															// die Stage wird geschlossen - wieder zurueck zur HauptStage
        }});
       
        Scene spieleScene = new Scene(spieleVbox, 1200, 900);
        setCSS(spieleScene);																// je nach Thema wird Style (CSS) angepasst
       
        createGrids_automatisch(spielfeld2);												// Spielfeld soll keine MouseEvents haben
        spieleStage.setScene(spieleScene);
        spieleStage.setFullScreen(fullscreen);
        spieleStage.show();	
		}
		
	/************************** Abfrage, ob das Fenster wirklich geschlossen werden soll ********************************/
	public void onCloseEvent(){
		final Stage close = new Stage();										// Erzeugen einer neuen Stage
    	close.setTitle("Schliessen");
    	close.initModality(Modality.APPLICATION_MODAL);
    	close.initOwner(primaryStage);
        
    	VBox vbox = new VBox(20);
    	HBox hbox = new HBox();
    	Label nachricht = new Label("Wollen Sie wirklich beenden? Angefangene Spiele werden NICHT gespeichert!"); // angezeigter Text
    	Button beenden = new Button("Beenden");									// Button zum tatsaechlichen Beenden
        Button abbrechen = new Button("Abbrechen");								// wieder zuruck zur Hauptstage
        
        vbox.getChildren().addAll(nachricht, hbox);								// Nachricht wird oberhalb der Buttons angezeigt
        hbox.getChildren().addAll(beenden, abbrechen);							// Buttons erscheinen nebeneinander
        
        vbox.setPadding(new Insets(10, 10, 10, 10));                
        
        hbox.setAlignment(Pos.BASELINE_CENTER);
        hbox.setSpacing(20);
        nachricht.setWrapText(true);											// automatischer Textumbruch
       
        abbrechen.setOnMouseClicked(new EventHandler<MouseEvent>(){				// Klick auf den Abbrechen Button
        	@Override
        	public void handle(MouseEvent arg0){
        		close.close();													// Stage zur Meldung wird geschlossen - zurueck zur HauptStage
        	}
        });
        beenden.setOnMouseClicked(new EventHandler<MouseEvent>(){				// Klick auf Beenden Button
        	@Override
        	public void handle(MouseEvent arg0){
        		close.close();													// Stage zur Meldung wird geschlossen
        		Platform.exit();												// gesamtes Spiel wird geschlossen
        	}
        });
       
        Scene themaScene = new Scene(vbox, 500, 200);
        setCSS(themaScene);														// Style der Meldung je nach Thema
        close.setScene(themaScene);
        close.show();
	}
	
	/********************* Anzeige, dass ein Fehler bei der Server Connteciton aufgetreten ist **************************/
	public void onServerConnectionError(){
	// neue Stage
			Platform.runLater(new Runnable() { 
	            @Override
	            public void run() {
	            	
	            	final Stage serverConnection = new Stage();							// neue Stage als Popup
	            	serverConnection.setTitle("Verbindungsfehler");
	            	serverConnection.initModality(Modality.APPLICATION_MODAL);
	            	serverConnection.initOwner(primaryStage);
	                     
	                
	                Label meldung = new Label();
	                meldung.setText("Der Server reagiert nicht, bitte das Spiel neu starten");
	                meldung.setWrapText(true);
	                Button ok = new Button("ok");										// Button der das popup schliesst
	                
	                ok.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        			@Override
	                    public void handle(MouseEvent arg0) {
	        				serverConnection.close();									// Schliessen der Stage - zurueck zur Hauptstage
	        			}
	                });
	                
	                VBox serverVbox = new VBox(20);
	                serverVbox.getChildren().addAll(meldung, ok);
	                serverVbox.setPadding(new Insets(10, 10, 10, 10)); 
	                serverVbox.setAlignment(Pos.CENTER);								// Anordnung in der Mitte
	                
	                Scene serverConnectionScene = new Scene(serverVbox, 500, 800);
	                setCSS(serverConnectionScene);											// Style der Meldung je nach Thema
	                serverConnection.setScene(serverConnectionScene);				
	                serverConnection.setFullScreen(false);
	                
	                serverConnection.show();											// Anzeigen der Stage
	            }	
			});
	 }
	
	/********************* Anzeige, dass ein Fehler bei der Pusher Connteciton aufgetreten ist **************************/
	public void onConnectionError(){
		// neue Stage
		Platform.runLater(new Runnable() { 
            @Override
            public void run() {
            	
            	final Stage connection = new Stage();
        		connection.setTitle("Verbindungsfehler");
                connection.initModality(Modality.APPLICATION_MODAL);
                connection.initOwner(primaryStage);
                     
                
                Label meldung = new Label();
                meldung.setText("Der Pusher Server reagiert nicht, bitte Pusher Credentials in den Einstellungen ueberpruefen");
                Button ok = new Button("Einstellungen aendern");				// Button der das popup schliesst
                
                ok.setOnMouseClicked(new EventHandler<MouseEvent>() {
        			@Override
                    public void handle(MouseEvent arg0) {
        				connection.close();										// Schliessen der Stage - zurueck zur Hauptstage
        			}
                });
                
                VBox connectionVbox = new VBox(20);
                connectionVbox.getChildren().addAll(meldung, ok);
                connectionVbox.setPadding(new Insets(10, 10, 10, 10)); 
                
                Scene connectionScene = new Scene(connectionVbox, 500, 800);
                setCSS(connectionScene);											// Style der Meldung je nach Thema
                connection.setScene(connectionScene);
                
                connection.show();
            }
        });
		
	}
	
	/******************************** Anzeige, welcher Spieler den Satz gewonnen hat ************************************/
	public void satzgewinner(char gewinner){
		
		satzstatus.setText("Satz beendet");										// Es gibt nun einen Gewinner - Status wird geaendert
		
		// neue Stage
		final Stage satz = new Stage();											// Erzeugen einer neuen Stage als Popup
		satz.setTitle("Satzgewinner");
        satz.initModality(Modality.APPLICATION_MODAL);
        satz.initOwner(primaryStage);
        
        Label meldung = new Label();
        meldung.setWrapText(true);
        if(gewinner == 'x' && spieler == 'x' || gewinner == 'o' && spieler == 'o'){ meldung.setText("Spieler " + opponentInput.getText() + " hat den Satz gewonnen!");}
        else {meldung.setText("Spieler " + playerInput.getText() + " hat den Satz gewonnen!");				// wenn Spieler gewonnen hat, anzeige seines Namens, sonst Name des Gegners
		}
        
        VBox satzVbox = new VBox(20);
        satzVbox.getChildren().addAll(meldung);
        satzVbox.setPadding(new Insets(10, 10, 10, 10)); 
        
        Scene dialogScene = new Scene(satzVbox, 500, 800);
        setCSS(dialogScene);
        
        satz.setScene(dialogScene);
        
        PauseTransition delay = new PauseTransition(Duration.seconds(5));		// Meldung wird 5 Sekunden angezeigt und schliesst sich dann automatisch
        delay.setOnFinished( event -> satz.close() );
        delay.play();
        
        satz.setFullScreen(false);
        satz.show();
        
        for (int i = 0; i < plaetzeFreiInReihe.length; i++){
			plaetzeFreiInReihe[i]=5;
		}
        createGrids_automatisch(spielfeld);
	}
	
	/******************************** Anzeige, welcher Spieler das Spiel gewonnen hat ***********************************/
	public void gewinnermethode(String gewinner){
		
		// neue Stage
		final Stage gewinnerStage = new Stage();								// neue Stage als Popup, wenn Spiel gewonnen
		gewinnerStage.setTitle("Gewinner");
		gewinnerStage.initModality(Modality.APPLICATION_MODAL);
		gewinnerStage.initOwner(primaryStage);
		
        // Button zum Popup schliessen
        Button back = new Button("ok");
        
        // Button Action Event
        back.setOnMouseClicked(new EventHandler<MouseEvent>(){
     	   @Override
            public void handle(MouseEvent arg0) {
     		   gewinnerStage.close();											// schliessen der PopupStage - zurueck zur HauptStage
        }});	
        Label gewinnernachricht = new Label(gewinner + " hat gewonnen!");
       /* if(gewinner == 'x' && spieler == 'x' || gewinner == 'o' && spieler == 'o'){
			gewinnernachricht.setText(names1 + " hat gewonnen!");				// Name je nach Spieler oder Gegner
		}else {	gewinnernachricht.setText(names2 + " hat gewonnen!");}*/
        
        
        VBox vbox = new VBox(20);
        vbox.getChildren().addAll(gewinnernachricht, back);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10, 10, 10, 10));  
        
        Scene gewinnerScene = new Scene(vbox, 800, 600);
        setCSS(gewinnerScene);													// Style je nach Thema
      
        gewinnerStage.setFullScreen(false);										// Anzeige nicht im Fullscreen
        gewinnerStage.setScene(gewinnerScene);
        gewinnerStage.show();
	}
	
	/************************************ Anzeigeanpassungen je nach Spielmodus *****************************************/
	public void changeSpielmodus(Number newValue, Button einstellungen, Button start, Slider spielmodus){
		/** bei automatischem Spiel werden die Buttons "Einstellungen" und "Spiel starten" wieder angezeigt*/
    	if(newValue.intValue() == 2){				// bei automatischem Spiel
    		einstellungen.setOpacity(1);			// wird der Einstellungsbutton angezeigt
    		einstellungen.setDisable(false);		// und die Funktionen ermoeglicht
    		start.setOpacity(1);					// auch der Start Button wird angezeigt
    		start.setDisable(false);				// und funktioniert
    		createGrids_automatisch(spielfeld);		// Das Spielfeld wird erzeugt (Grid nicht clickbar)
    	}
    	/** bei manuellem Spiel werden die Buttons "Einstellungen" und "Spiel starten" wieder ausgeblendet*/
    	if(newValue.intValue() == 1){				// bei manuellem Spiel
    		einstellungen.setOpacity(0);			// wird der Einstellungsbutton ausgeblendet, da keine Server-Verbindung benoetigt wird
    		einstellungen.setDisable(true);			// und die Funktionen ausgeschaltet
    		start.setOpacity(0);					// auch der Start Button wird ausgeblendet
    		start.setDisable(true);					// und funktioniert nicht mehr
    		createGrids(spielfeld);					// Das Spielfeld wird erzeugt (Grid clickbar)
    	}
    	/** bei manuellem Spiel gegen die KI wird eine Meldung ausgegeben*/
    	if(newValue.intValue()==0){					// beim Spiel gegen den Computer
    		
    		final Stage notImpl = new Stage();								// wird eine neue Stage erzeugt
    		notImpl.setTitle("Noch nicht Implementiert");					
    		notImpl.initModality(Modality.APPLICATION_MODAL);
    		notImpl.initOwner(primaryStage);
	        
	        Label msg = new Label();	
	        msg.setText("Diese Funktion wurde noch nicht implementiert. Bitte waehle einen anderen Spielmodus.");
	        msg.setWrapText(true);											// die die Meldung anzeigt, dass es diese Funktion noch nicht gibt
	        
	        Button manuell = new Button("Manuell");							// Danach hat man die Wahl manuell zu spielen
	        manuell.setOnMouseClicked(new EventHandler<MouseEvent>(){
	        	@Override
	        	public void handle(MouseEvent arg0){
	        		spielmodus.setValue(1);									// Der Spielmodus Silder wird angepasst
	        		notImpl.close();										// und die PopupStage geschlossen
	        	}
	        });
	        
	        Button auto = new Button("Automatisch");						// oder Automatisch
	        auto.setOnMouseClicked(new EventHandler<MouseEvent>(){
	        	@Override
	        	public void handle(MouseEvent arg0){
	        		spielmodus.setValue(2);									// Der Spielmodus Silder wird angepasst
	        		notImpl.close();										// und die PopupStage geschlossen
	        	}
	        });
	      
	        HBox hb = new HBox();
	        hb.getChildren().addAll(manuell, auto);
	        hb.setAlignment(Pos.BASELINE_CENTER);
	        hb.setSpacing(20);
	        
	        VBox vb = new VBox(20);
	        vb.setPadding(new Insets(10, 10, 10, 10)); 
	        vb.getChildren().addAll(msg, hb);
	        
	        Scene themaScene = new Scene(vb, 500, 200);
	        setCSS(themaScene);												// Style je nach ausgewaehltem Thema
	   
	        notImpl.setScene(themaScene);
	        notImpl.show();  
    	}
	}
	
	/*********************************************************************************************************************
	 ******************************************* ZUM SPIELFELD ***********************************************************
	 ********************************************************************************************************************/
	
	/****************************** Setzen des Spielsteins in der Farbe des Spielers ************************************/
    public void setSpielstein(int zeile, int spalte, char amZug){
    	satzstatus.setText("Satz spielen");									// wenn Spielsteine gesetzt werden wird Satzstatus angepasst
    	spielsteinAnzeigen(getImageView((getNodeByRowColumnIndex(zeile, spalte, spielfeld))), amZug);
    					// Aufruf Spielstein Anzeigen mit dem ImageView der Zelle, der Zeile, der Spalte und der Grid, und dem Spieler der am Zug ist
    }
    
    /******************************** Anzeige des Spielsteins in der richtige Farbe *************************************/
    public void spielsteinAnzeigen(ImageView spielstein, char amZug){
    	if(spielstein.getTranslateY()!=0){ 
    		//spielstein.setTranslateY(-(l*(anzahlzeilen+1)));
            final TranslateTransition translateTransition = new TranslateTransition(Duration.millis(300), spielstein);	
            translateTransition.setToY(0);				//Runterfallen der Steine
            translateTransition.play();
            if(amZug=='x'){ spielstein.setImage(image1);
                //System.out.println((int)spielstein.getId().charAt(10)-48 + " " + amZug);  
            }else{ spielstein.setImage(image2);
                //System.out.println((int)spielstein.getId().charAt(10)-48 + " " + amZug);
            }
        }
    }
    
    /******************* Zugriff auf die Elemente in der GridPane an der jeweiligen Stelle ******************************/
    public StackPane getNodeByRowColumnIndex (final int row, final int column, GridPane spielfeld) {
        for (Node node : spielfeld.getChildren()) {				// Geht alle Stacks ind den Zellen der Grid durch
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {		// wenn richtige Zeile und Spalte gefunden
                return (StackPane) node;						// wird die Stack zurueckgegeben
            }
        } return null;
    }
    
    /************* ImageView des ausgewaehlten Spielsteins zum veraendern des Spielsteinsymbols *************************/
    public ImageView getImageView (StackPane stack) throws NullPointerException {
    	try{
        ObservableList<Node> list = stack.getChildren();		// Liste aus Elementen der Stack
        return (ImageView)list.get(2);							// Rueckgabe der ImageView (an 2. Stelle im Stack)
    	} catch (NullPointerException e){
    		e.getMessage();
    		return null;
    	}
    } 
    
	
	/*********************************************************************************************************************
	 ******************************************* HILFSMETHODEN ***********************************************************
	 ********************************************************************************************************************/
  
    /******************************** Auswahl der CSS-Date je nach aktuellem Thema **************************************/
	public static void setCSS(Scene scene){
		scene.getStylesheets().clear();				// CSS-zuruecksetzen und je nach Thema neue CSS-Datei verknuepfen
		if(thema == 1){ scene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());}
        if(thema == 2){ scene.getStylesheets().add(TestGui.class.getResource("Food.css").toExternalForm());}
        if(thema == 3){ scene.getStylesheets().add(TestGui.class.getResource("Sport.css").toExternalForm());}
        if(thema == 4){ scene.getStylesheets().add(TestGui.class.getResource("Sweets.css").toExternalForm());}
	}
 
	/********************************                                              **************************************/
	@Override
	public void zugGespielt(int zug, char amZug)
	{
		setSpielstein(plaetzeFreiInReihe[zug], zug, amZug);
        plaetzeFreiInReihe[zug]--;
		
	}
	
	/********************************                                              **************************************/
	public void addNameListener(LogicListener toAdd) {
		NameListeners.add(toAdd);
	}
	
	/********************************                                              **************************************/
	public static void fireNames (String name1, String name2) {
		for (LogicListener name: NameListeners) {
			name.startGame(name1, name2);
		}
	}
	
	public static void startManGame(String name1, String name2) {
		for (LogicListener game: NameListeners) {
			game.startManGame(name1, name2);
	}
	}
	
	public static void setManTurn(String name, int x, int y) {
		for (LogicListener turn: NameListeners) {
			turn.setManTurn(name, x, y);
	}
	}
	
	/********************************                                              **************************************/
	public void addParamListener(ParamListener toAdd){
		listeners.add(toAdd);
	}
	
	/********************************                                              **************************************/
	public static void fireStartEvent(int Zugzeit, String Schnittstelle, String Kontaktpfad, char spielerKennung, 
									  String AppID, String AppKey, String AppSecret){
		for (ParamListener pl : listeners){
			pl.startParameterAuswerten(Zugzeit, Schnittstelle, Kontaktpfad, spielerKennung, AppID, AppKey, AppSecret);
		}
	}
	
	/********************************                                              **************************************/
	public String getFileString(){
		return fileString;
	}
	
	
	/********************************                                              **************************************/
	@Override
	public void siegerAnzeigen(char sieger)
	{	
		Platform.runLater(new Runnable() { 
            @Override
            public void run() {
            	satzgewinner(sieger);
            }
        });
	}


	
}