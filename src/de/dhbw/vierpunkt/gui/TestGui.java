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
import de.dhbw.vierpunkt.logic.NameListener;
/**
 *
 * @author janaschaub
 */
public class TestGui implements ZugListener,ConnectionErrorListener, GewinnerListener {

	/** Gibt den aktuellen Fuellstand aller Spalten an*/	
	static int[] plaetzeFreiInReihe = new int[7];
	private static List<NameListener> NameListeners = new ArrayList<NameListener>();
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
	public void setNames1(String names1){this.names1 = names1;}
	public void setNames2(String names2){this.names2 = names2;}
	public void setXodero(char xodero) {this.xodero = xodero;}
    public void setColor(Color color) {	this.color = color;}
	
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
		TextField playerInput= new TextField();									// TextField zur Eingabe des Namens
		if(playerInput.getText() != null && ! playerInput.getText().trim().isEmpty()){
			setNames1(playerInput.getText());									// Name darf nicht leer bleiben
		}
		
		Label opponentLab = new Label("Gegner: ");
		TextField opponentInput= new TextField();								// TextField zur Eingabe des GegnerNamens
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
		Slider zeit = new Slider(0, 5000, 100); 			// Slider geht von 0 bis 2 in 1er Abstaenden
		zeit.setMinorTickCount(0);
		zeit.setMajorTickUnit(100); 						// Man kann nur auf den Zahlen 0, 1, 2 landen, nicht dazwischen
		zeit.setSnapToTicks(true); 							// Der Punkt rutzscht zur naechsten Zahl
		zeit.setShowTickMarks(true); 						// Markierungen anzeigen -
		zeit.setOrientation(Orientation.HORIZONTAL); 		// Vertikale Anordnung,standardmaessig horizontal
		zeit.setValue(zugzeit);								// Default Value = 2

		// Einfuegen der Elemente in die linke Box
		boxlinks.getChildren().addAll(spielerfarben, box3, p, einstellungen, bild, platzhalter2);

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
		
		einstellungen.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0){
				// neue Stage
				final Stage dialog = new Stage();
				dialog.setTitle("Einstellungen");
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(primaryStage);
                VBox dialogVbox = new VBox(20);
                dialogVbox.setPadding(new Insets(10, 10, 10, 10));                
                
                // Button zum Popup schließen
                Button ok = new Button("ok");
                
                // Button Action Event
                ok.setOnMouseClicked(new EventHandler<MouseEvent>(){
             	   @Override
                    public void handle(MouseEvent arg0) {
             		   if(app1.getText()!= null){setAppId(app1.getText());}
             		   if(app2.getText()!= null){setAppKey(app2.getText());}
             		   if(app3.getText()!= null){setAppSecret(app3.getText());}
             		   
             		   dialog.close();
                }});
                
                // Einfuegen in die VBox
                dialogVbox.getChildren().addAll(u1, hb4, schnittstelle, hb, pusher, hb1, hb2, hb3, p1, zeitlabel, zeit, ok);
                Scene dialogScene = new Scene(dialogVbox, 500, 800);
                
                setCSS(dialogScene);
                
              
                dialog.setScene(dialogScene);
                dialog.show();	
			}
		});
		
		start.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
            public void handle(MouseEvent arg0) {
				if(spielmodus.getValue()==2){
					spieler = getXodero();
					if(spieler == 'x'){
						gegner = 'o';
					}else{gegner = 'x';}
	            	createGrids_automatisch(spielfeld);
				}else{
					spieler = getXodero();
					if(spieler == 'x'){
						gegner = 'o';
					}else{gegner = 'x';}
					createGrids(spielfeld);}
				
				fireStartEvent(getZugzeit(), getSchnittstelle(), getFileString(), getXodero(), getAppId(), getAppKey(), getAppSecret());
				Thread t1 = new Thread(){
					@Override
					public void run(){
						fireNames(playerInput.getText(), opponentInput.getText());
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
		
		Scene scene = new Scene(root);
		
		// changeTheme Stage
		final Stage changeTheme = new Stage();
		changeTheme.setTitle("Themenwechsel");
		changeTheme.initModality(Modality.APPLICATION_MODAL);
		changeTheme.initOwner(primaryStage);
        VBox themaVbox = new VBox(20);
        themaVbox.setPadding(new Insets(10, 10, 10, 10));                
        
        Label nachricht = new Label();
        nachricht.setText("Das laufende Spiel wird abgebrochen, wenn das Thema gewechselt wird.");
        nachricht.setWrapText(true);
        Button open = new Button("Thema wechseln");
        Button close = new Button("Abbrechen");
        HBox hbox = new HBox();
        hbox.getChildren().addAll(open, close);
        hbox.setAlignment(Pos.BASELINE_CENTER);
        hbox.setSpacing(20);
        
        close.setOnMouseClicked(new EventHandler<MouseEvent>(){
        	@Override
        	public void handle(MouseEvent arg0){
        		changeTheme.close();
        	}
        });
        
        themaVbox.getChildren().addAll(nachricht, hbox);
        Scene themaScene = new Scene(themaVbox, 500, 200);
       
        changeTheme.setScene(themaScene);
        
        
    
 	    /*********************** NEUES SPIEL *****************************/
		menu00.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e) {
				primaryStage.setScene(scene);
				primaryStage.setFullScreen(fullscreen);
				spieler = getXodero();
				if(spieler == 'x'){
					gegner = 'o';
				}else{gegner = 'x';}
				
				if(spielmodus.getValue()==2){
					createGrids_automatisch(spielfeld);
				}else{createGrids(spielfeld);}
				
				}
		});
		/*********************** BEREITS GESPIELTE SPIELE *****************************/
		menu01.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e) {
			bisherigeSpiele();	
			}
		});
		/*********************** SPIEL BEENDEN *****************************/
		menu02.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	onCloseEvent();
		    	}
		});		        
        /*********************** THEMA SWEETS *****************************/
		menu10.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				// wir nur ausgefuehrt, wenn das Thema nicht eh schon das gleiche ist
				if(thema!=4){
					setCSS(themaScene);
					// wenn man bei der Abfrage wirklich das Thema wechseln will
					open.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
			            public void handle(MouseEvent arg0) {
							
							changeTheme.close();
							primaryStage.setScene(scene);
							primaryStage.setFullScreen(fullscreen);
							setColor(Color.PURPLE);
							setImage1(orange);
							setImage2(gruen);
							i1.setImage(orange);
							i2.setImage(gruen);
							scene.getStylesheets().clear();
							scene.getStylesheets().add(TestGui.class.getResource("Sweets.css").toExternalForm());
							if(spielmodus.getValue()==2){
								createGrids_automatisch(spielfeld);
							}else{createGrids(spielfeld);}
							thema = 4;
						}
			        });
					changeTheme.show();
				}
			}
		});
        /*********************** THEMA HALLOWEEN *****************************/
		menu11.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				// wir nur ausgefuehrt, wenn das Thema nicht eh schon das gleiche ist
				if(thema != 1){ 
					setCSS(themaScene);
					open.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
			            public void handle(MouseEvent arg0) {
							
							changeTheme.close();
							primaryStage.setScene(scene);
							primaryStage.setFullScreen(fullscreen);
							scene.getStylesheets().clear();
							scene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());
							setColor(Color.BLACK);
							setImage1(kuerbis);
							setImage2(fledermaus);
							i1.setImage(kuerbis);
							i2.setImage(fledermaus);
							if(spielmodus.getValue()==2){
								createGrids_automatisch(spielfeld);
							}else{createGrids(spielfeld);}
							thema = 1;
						}
			        });
					changeTheme.show();
				} 
			}
		});
        /*********************** THEMA FOOD *****************************/
		menu12.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				// wir nur ausgefuehrt, wenn das Thema nicht eh schon das gleiche ist
				if(thema != 2){
					setCSS(themaScene);
					open.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
			            public void handle(MouseEvent arg0) {
							
							changeTheme.close();
							primaryStage.setScene(scene);
							primaryStage.setFullScreen(fullscreen);
							scene.getStylesheets().clear();
							scene.getStylesheets().add(TestGui.class.getResource("Food.css").toExternalForm());
							setColor(Color.DARKGREEN);
							setImage1(pizza);
							setImage2(burger);
							i1.setImage(pizza);
							i2.setImage(burger);
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
				// wir nur ausgefuehrt, wenn das Thema nicht eh schon das gleiche ist
				if(thema!= 3){
					setCSS(themaScene);
					open.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
			            public void handle(MouseEvent arg0) {
							changeTheme.close();
							primaryStage.setScene(scene);
							primaryStage.setFullScreen(fullscreen);
							scene.getStylesheets().clear();
							scene.getStylesheets().add(TestGui.class.getResource("Sport.css").toExternalForm());
							setColor(Color.CADETBLUE);
							setImage1(basketball);
							setImage2(baseball);
							i1.setImage(basketball);
							i2.setImage(baseball);
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
	        public void handle(KeyEvent ke)
	        {
	            if (ke.getCode().equals(KeyCode.ENTER))
	            {meldung.setText("Bitte Spielernamen eingeben");
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
	    
	    opponentInput.setOnKeyPressed(new EventHandler<KeyEvent>(){
	        @Override
	        public void handle(KeyEvent ke)
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
	         public void handle(KeyEvent evt)
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
            public void handle(MouseEvent arg0) {
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
		scene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());
		createSpielfeld(spielfeld2);
		
		// manuell
		if(spielmodus.getValue() == 1 ){ createGrids(spielfeld);}
		// automatisch
		if(spielmodus.getValue() == 2){createGrids_automatisch(spielfeld);}
		
		loginStage.show();
		
	}
	
	/*********************************************************************************************************************
	 ******************************************* SPIELFELD METHODEN ******************************************************
	 ********************************************************************************************************************/
	
	/******************************* Erzeugt die Zeilen und Spalten der GridPane ****************************************/
	public void createSpielfeld(GridPane spielfeldGrid){
		spielfeldGrid.setId("spielfeld");
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
	    	spielfeld.getChildren().clear();
	    	
	        for(int anzahlzeilen=0;anzahlzeilen<spielfeld.getRowConstraints().size(); anzahlzeilen++){
	            for(int anzahlspalten=0; anzahlspalten<spielfeld.getColumnConstraints().size(); anzahlspalten++){
	            
	            // Darstellung des Rahmens/ der Zellen    
	            Rectangle rect = new Rectangle(l,l);
	            Circle circ = new Circle((l/2)-5);
	            circ.centerXProperty().set(l/2);
	            circ.centerYProperty().set(l/2);
	            Shape cell = Path.subtract(rect, circ);
	            cell.setId("cell");
	            cell.setFill(color);
	            cell.setStroke(color.darker());
	            
	            // Ansicht der Spielsteine
	            ImageView spielstein = new ImageView(image1);
	            spielstein.setImage(image1);
	            spielstein.setId("spielstein" + anzahlspalten);
	            spielstein.setFitWidth(l-10);
	            spielstein.setPreserveRatio(true);  
	            
	            // Vorschau der Spielsteine
	            ImageView vorschauspielstein = new ImageView(image3);
	            vorschauspielstein.setFitWidth(l-10);
	            vorschauspielstein.setPreserveRatio(true); 
	            vorschauspielstein.setOpacity(0.5);
	            
	            spielstein.setTranslateY(-(l*(anzahlzeilen+1)));
	            
	            // Zellen werden gefuellt
	            StackPane stack = new StackPane();
	            stack.getChildren().addAll(cell, vorschauspielstein, spielstein);   // Fuellen der Zelle mit Rahmen, Vorschau oder Spielstein
	            spielfeld.add(stack, anzahlspalten, anzahlzeilen); 
	            }
	         }
	    }

	/************************* Belegung der GridPane mit Feldelementen und MouseEvents **********************************/
	public void createGrids(GridPane spielfeld){
	    	spielfeld.getChildren().clear();
	    	
	        for(int anzahlzeilen=0;anzahlzeilen<spielfeld.getRowConstraints().size(); anzahlzeilen++){
	            for(int anzahlspalten=0; anzahlspalten<spielfeld.getColumnConstraints().size(); anzahlspalten++){
	            
	            // Darstellung des Rahmens/ der Zellen    
	            Rectangle rect = new Rectangle(l,l);
	            Circle circ = new Circle((l/2)-5);
	            circ.centerXProperty().set(l/2);
	            circ.centerYProperty().set(l/2);
	            Shape cell = Path.subtract(rect, circ);
	            cell.setId("cell");
	            cell.setFill(color);
	            cell.setStroke(color.darker());
	           
	            // Ansicht der Spielsteine
	            ImageView spielstein = new ImageView(image1);
	            spielstein.setImage(image1);
	            spielstein.setId("spielstein" + anzahlspalten);
	            spielstein.setFitWidth(l-10);
	            spielstein.setPreserveRatio(true);  
	            
	            // Vorschau der Spielsteine
	            ImageView vorschauspielstein = new ImageView(image3);
	            vorschauspielstein.setImage(image1);
	            vorschauspielstein.setFitWidth(l-10);
	            vorschauspielstein.setPreserveRatio(true); 
	            vorschauspielstein.setOpacity(0.5);
	            
	            /*******************************************************************************************************************
	             *******************************************  ANZEIGE IM SPIELFELD  ************************************************
	             *******************************************************************************************************************/ 
	            
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
	                
	               spielstein.setTranslateY(-(l*(anzahlzeilen+1)));
	            
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
	                	   if(manuellerSpieler == 'x'){
	                		   spielsteinAnzeigen(spielstein, manuellerSpieler);
	                		   manuellerSpieler = 'o';
	                	   }else{
	                		   spielsteinAnzeigen(spielstein, manuellerSpieler);
	                		   manuellerSpieler = 'x';
	                	   }
	                   }
	               });
	              
	               // Setzen der Spielsteine beim Klick auf die entsprechende Vorschau
	               vorschauspielstein.setOnMouseClicked(new EventHandler<MouseEvent>(){
	                   @Override public void handle(MouseEvent arg0) {
	                	   if(manuellerSpieler == 'x'){
	                		   spielsteinAnzeigen(spielstein, manuellerSpieler);
	                		   manuellerSpieler = 'o';
	                	   }else{
	                		   spielsteinAnzeigen(spielstein, manuellerSpieler);
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
	            spielfeld.add(stack, anzahlspalten, anzahlzeilen); 
	            }
	        }
	    }
		
	/*********************************************************************************************************************
	 ******************************************* NEUE STAGES METHODEN ****************************************************
	 ********************************************************************************************************************/

	/********************************** Anzeige der 10 zuletzt gespielten Spiele ****************************************/
	public void bisherigeSpiele(){
		
		// neue Stage
		final Stage spieleStage = new Stage();
		spieleStage.setTitle("Bisherige Spiele");
		spieleStage.initModality(Modality.APPLICATION_MODAL);
		spieleStage.initOwner(primaryStage);
       
        // Elemente der Stage
        Button back = new Button("Zurueck");
        Button play = new Button("Play"); 
        Label spieleLabel = new Label("Bisherige Spiele:");
        Label spielstandanzeige = new Label("Spielstand: ");
        Label spielerLabel = new Label("Spieler: ");
		Label spieler1 = new Label("names1");
		Label spieler2 = new Label("names2");
		Text spielstand_altesSpiel = new Text("3:1");
		Rectangle platzhalter1 = new Rectangle(7 * l, l);
		Rectangle platzhalter2 = new Rectangle(l, l);
		Rectangle platzhalter3 = new Rectangle(l, l);
		Rectangle platzhalter4 = new Rectangle(l, l);
		Rectangle platzhalter5 = new Rectangle(l, l);
		Rectangle platzhalter7 = new Rectangle(l, l*2);
        Slider satz = new Slider(0, 2, 1); 	
       
        HBox hb = new HBox();
        VBox grid = new VBox();
		VBox anzeige = new VBox();
		VBox spieleranzeige = new VBox();
		VBox spieleVbox = new VBox(20);
		hb.getChildren().addAll(spieleranzeige, grid, anzeige);
        grid.getChildren().addAll(platzhalter1, spielfeld2);
		anzeige.getChildren().addAll(satz, platzhalter7, play, platzhalter4);
		spieleranzeige.getChildren().addAll(spielerLabel, spieler1, spieler2, platzhalter2, spielstandanzeige, spielstand_altesSpiel, platzhalter3, back, platzhalter5);
		spieleVbox.getChildren().addAll(spieleLabel, table, hb);
        
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
		satz.setMajorTickUnit(1); 					// Man kann nur auf den Zahlen 0, 1, 2 landen, nicht dazwischen
		satz.setSnapToTicks(true); 						// Der Punkt rutzscht zur naechsten Zahl
		satz.setShowTickMarks(true); 					// Markierungen anzeigen -
		satz.setShowTickLabels(true);
		satz.setOrientation(Orientation.VERTICAL); 	// Vertikale Anordnung,standardmaessig horizontal
		satz.setValue(0);	
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
		anzeige.setAlignment(Pos.BOTTOM_CENTER);
		anzeige.setPrefWidth((breite-(7*l))/2);
		spieleranzeige.setAlignment(Pos.BOTTOM_CENTER);
		spieleranzeige.setPrefWidth((breite-(7*l))/2);
        hb.setAlignment(Pos.BOTTOM_CENTER);
        spieleVbox.setAlignment(Pos.CENTER);
        spieleVbox.setPadding(new Insets(10, 10, 10, 10));   
        
        
        table.setEditable(true);
        
        // Initialisierung der Spalten der Tabelle
        TableColumn<Spiele, String> gameIDCol = new TableColumn<>("Spiele ID");
        gameIDCol.setMinWidth(100);
        gameIDCol.setCellValueFactory(new PropertyValueFactory<Spiele, String>("gameID"));
        
        TableColumn<Spiele, String> player1Col = new TableColumn<>("Spieler 1");
        player1Col.setMinWidth(100);
        player1Col.setCellValueFactory(new PropertyValueFactory<Spiele, String>("player1"));
        
        TableColumn<Spiele, String> player2Col = new TableColumn<>("Spieler 2");
        player2Col.setMinWidth(100);
        player2Col.setCellValueFactory(new PropertyValueFactory<Spiele, String>("player2"));
        
        TableColumn<Spiele, String> winnerCol = new TableColumn<>("Gewinner");
        winnerCol.setMinWidth(100);
        winnerCol.setCellValueFactory(new PropertyValueFactory<Spiele, String>("winner"));
        
        for (int i = 0; i < alleGames[0].length; i++) {
          	try{items.add(new Spiele(alleGames[i][0], alleGames[i][1], alleGames[i][2], alleGames[i][3])); }
            catch(Exception ex){System.out.println("Empty fields or illegal arguments passed.");}
		}
        
        table.setItems(items);
        table.getColumns().addAll(gameIDCol, player1Col, player2Col, winnerCol);
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	          System.out.println(newSelection);  
			  if (newSelection != null) {
	                selectedGame = table.getSelectionModel().getSelectedItem();
	          }
        });
        
		play.setOnMouseClicked(new EventHandler<MouseEvent>(){
	     	   @Override
	            public void handle(MouseEvent arg0) {
	     		   
	     		   	int g_id = Integer.parseInt(selectedGame.getGameID());
	                String[][] alleSaetze = db.getHighscoreMatch(g_id);
	                int m_id = Integer.parseInt(alleSaetze[(int)satz.getValue()][0]);
	                String[][] alleZuege = db.getHighscoreTurn(g_id, m_id);
	                
	                personX = alleZuege[0][2];
	               
	   	     		Thread playThread = new Thread(){
	   	     			@Override
	   	     			public void run(){
		   	     			for (int i = 0; i < alleZuege[0].length; i++) {
	     	                	if(alleZuege[i][4] != null &&alleZuege[i][3]!= null){
	     	                		if(personX.equals(alleZuege[i][2])){
	         	                		spielsteinAnzeigen(getImageView((getNodeByRowColumnIndex(Integer.parseInt(alleZuege[i][4]), Integer.parseInt(alleZuege[i][3]), spielfeld2))), 'x');
	         	                	} else{
	         	                		spielsteinAnzeigen(getImageView((getNodeByRowColumnIndex(Integer.parseInt(alleZuege[i][4]), Integer.parseInt(alleZuege[i][3]), spielfeld2))), 'o');
	         	                	}
	    							try {Thread.sleep(2000);} 
	    							catch (InterruptedException e) {e.printStackTrace();}
	     	                	}
		   	     			}	
	   	     			}
	   	     		};
	   	     		playThread.start();
	   	     	}
	     });
	     		   
        // Button Action Event
        back.setOnMouseClicked(new EventHandler<MouseEvent>(){
     	   @Override
            public void handle(MouseEvent arg0) {
     		   spieleStage.close();
        }});
       
        Scene spieleScene = new Scene(spieleVbox, 1200, 900);
        setCSS(spieleScene);
       
        createGrids_automatisch(spielfeld2);
        spieleStage.setScene(spieleScene);
        spieleStage.setFullScreen(fullscreen);
        spieleStage.show();	
		}
		
	/************************** Abfrage, ob das Fenster wirklich geschlossen werden soll ********************************/
	public void onCloseEvent(){
		final Stage close = new Stage();
    	close.setTitle("Schliessen");
    	close.initModality(Modality.APPLICATION_MODAL);
    	close.initOwner(primaryStage);
        
    	VBox vbox = new VBox(20);
    	HBox hbox = new HBox();
    	Label nachricht = new Label("Wollen Sie wirklich beenden? Angefangene Spiele werden NICHT gespeichert!");
    	Button beenden = new Button("Beenden");
        Button abbrechen = new Button("Abbrechen");
        
        vbox.getChildren().addAll(nachricht, hbox);
        hbox.getChildren().addAll(beenden, abbrechen);
        
        vbox.setPadding(new Insets(10, 10, 10, 10));                
        
        hbox.setAlignment(Pos.BASELINE_CENTER);
        hbox.setSpacing(20);
        nachricht.setWrapText(true);
       
        abbrechen.setOnMouseClicked(new EventHandler<MouseEvent>(){
        	@Override
        	public void handle(MouseEvent arg0){
        		close.close();
        	}
        });
        beenden.setOnMouseClicked(new EventHandler<MouseEvent>(){
        	@Override
        	public void handle(MouseEvent arg0){
        		close.close();
        		Platform.exit();
        	}
        });
       
        Scene themaScene = new Scene(vbox, 500, 200);
        setCSS(themaScene);
        close.setScene(themaScene);
        close.show();
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
                Button ok = new Button("Einstellungen aendern");
                
                ok.setOnMouseClicked(new EventHandler<MouseEvent>() {
        			@Override
                    public void handle(MouseEvent arg0) {
        				connection.close();
        			}
                });
                
                VBox connectionVbox = new VBox(20);
                connectionVbox.getChildren().addAll(meldung, ok);
                connectionVbox.setPadding(new Insets(10, 10, 10, 10)); 
                
                Scene connectionScene = new Scene(connectionVbox, 500, 800);
                setCSS(connectionScene);
                connection.setScene(connectionScene);
                
                connection.show();
            }
        });
		
	}
	
	/******************************** Anzeige, welcher Spieler den Satz gewonnen hat ************************************/
	public void satzgewinner(char gewinner){
		
		satzstatus.setText("Satz beendet");
		
		// neue Stage
		final Stage satz = new Stage();
		satz.setTitle("Satzgewinner");
        satz.initModality(Modality.APPLICATION_MODAL);
        satz.initOwner(primaryStage);
        
        Label meldung = new Label();
        if(gewinner == 'x' && spieler == 'x' || gewinner == 'o' && spieler == 'o'){ meldung.setText(names1 + " hat den Satz gewonnen!");}
        else {meldung.setText(names2 + " hat den Satz gewonnen!");
		}
        
        VBox satzVbox = new VBox(20);
        satzVbox.getChildren().addAll(meldung);
        satzVbox.setPadding(new Insets(10, 10, 10, 10)); 
        
        Scene dialogScene = new Scene(satzVbox, 500, 800);
        setCSS(dialogScene);
        
        satz.setScene(dialogScene);
        
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished( event -> satz.close() );
        delay.play();
        
        satz.show();
	}
	
	/******************************** Anzeige, welcher Spieler das Spiel gewonnen hat ***********************************/
	public static void gewinnermethode(char gewinner){
		
		// neue Stage
		final Stage gewinnerStage = new Stage();
		gewinnerStage.setTitle("Gewinner");
		gewinnerStage.initModality(Modality.APPLICATION_MODAL);
		gewinnerStage.initOwner(primaryStage);
        
        // Button zum Popup schliessen
        Button back = new Button("ok");
        
        // Button Action Event
        back.setOnMouseClicked(new EventHandler<MouseEvent>(){
     	   @Override
            public void handle(MouseEvent arg0) {
     		   gewinnerStage.close();
        }});
        Label gewinnernachricht = new Label();
        if(gewinner == 'x' && spieler == 'x' || gewinner == 'o' && spieler == 'o'){
			gewinnernachricht.setText(names1 + " hat gewonnen!");
		}else {	gewinnernachricht.setText(names2 + " hat gewonnen!");}
        
        VBox vbox = new VBox(20);
        vbox.getChildren().addAll(gewinnernachricht, back);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10, 10, 10, 10));  
        
        Scene gewinnerScene = new Scene(vbox, 800, 600);
        setCSS(gewinnerScene);
      
        gewinnerStage.setFullScreen(false);
        gewinnerStage.setScene(gewinnerScene);
        gewinnerStage.show();
	}
	
	/************************************ Anzeigeanpassungen je nach Spielmodus *****************************************/
	public void changeSpielmodus(Number newValue, Button einstellungen, Button start, Slider spielmodus){
		/** bei automatischem Spiel werden die Buttons "Einstellungen" und "Spiel starten" wieder angezeigt*/
    	if(newValue.intValue() == 2){		
    		einstellungen.setOpacity(1);
    		einstellungen.setDisable(false);
    		start.setOpacity(1);
    		start.setDisable(false);
    		createGrids_automatisch(spielfeld);		// Das Spielfeld wird erzeugt (Grid nicht clickbar)
    	}
    	/** bei manuellem Spiel werden die Buttons "Einstellungen" und "Spiel starten" wieder ausgeblendet*/
    	if(newValue.intValue() == 1){
    		einstellungen.setOpacity(0);
    		einstellungen.setDisable(true);
    		start.setOpacity(0);
    		start.setDisable(true);
    		createGrids(spielfeld);							// Das Spielfeld wird erzeugt (Grid clickbar)
    	}
    	/** bei manuellem Spiel gegen die KI wird eine Meldung ausgegeben*/
    	if(newValue.intValue()==0){
    		
    		final Stage notImpl = new Stage();
    		notImpl.setTitle("Noch nicht Implementiert");
    		notImpl.initModality(Modality.APPLICATION_MODAL);
    		notImpl.initOwner(primaryStage);
	        
	        Label msg = new Label();
	        msg.setText("Diese Funktion wurde noch nicht implementiert. Bitte waehle einen anderen Spielmodus.");
	        msg.setWrapText(true);
	        
	        Button manuell = new Button("Manuell");
	        manuell.setOnMouseClicked(new EventHandler<MouseEvent>(){
	        	@Override
	        	public void handle(MouseEvent arg0){
	        		spielmodus.setValue(1);
	        		notImpl.close();
	        	}
	        });
	        
	        Button auto = new Button("Automatisch");
	        auto.setOnMouseClicked(new EventHandler<MouseEvent>(){
	        	@Override
	        	public void handle(MouseEvent arg0){
	        		spielmodus.setValue(2);
	        		notImpl.close();
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
	        setCSS(themaScene);
	   
	        notImpl.setScene(themaScene);
	        notImpl.show();  
    	}
	}
	
	/*********************************************************************************************************************
	 ******************************************* ZUM SPIELFELD ***********************************************************
	 ********************************************************************************************************************/
	
	/****************************** Setzen des Spielsteins in der Farbe des Spielers ************************************/
    public void setSpielstein(int zeile, int spalte, char amZug){
    	satzstatus.setText("Satz spielen");
    	spielsteinAnzeigen(getImageView((getNodeByRowColumnIndex(zeile, spalte, spielfeld))), amZug);
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
        for (Node node : spielfeld.getChildren()) {
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return (StackPane) node;
            }
        } return null;
    }
    
    /************* ImageView des ausgewaehlten Spielsteins zum veraendern des Spielsteinsymbols *************************/
    public ImageView getImageView (StackPane stack) throws NullPointerException {
    	try{
        ObservableList<Node> list = stack.getChildren();
        return (ImageView)list.get(2);
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
		scene.getStylesheets().clear();
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
	public void addNameListener(NameListener toAdd) {
		NameListeners.add(toAdd);
	}
	
	/********************************                                              **************************************/
	public static void fireNames (String name1, String name2) {
		for (NameListener name: NameListeners) {
			name.startGame(name1, name2);
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
	public void zugGespielt(char sieger) {
		
		// hier kommt die Methode, die bei Spielende aufgerufen werden soll, sieger enthaelt 'x' oder 'o' als char
		gewinnermethode(sieger);
		for (int i = 0; i < plaetzeFreiInReihe.length; i++){
			plaetzeFreiInReihe[i]=5;
		}
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