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
import de.dhbw.vierpunkt.interfaces.ParamListener;
import de.dhbw.vierpunkt.interfaces.ZugListener;
import de.dhbw.vierpunkt.objects.NameListener;
/**
 *
 * @author janaschaub
 */
public class TestGui implements ZugListener,ConnectionErrorListener {

	/****** VARIABLENDEKLARATION *****/
	

	
	
	/**
	 * Gibt den aktuellen Fuellstand aller Spalten an
	 */	
	static int[] plaetzeFreiInReihe = new int[7];
	private static List<NameListener> NameListeners = new ArrayList<NameListener>();
	private static List<ParamListener> listeners = new ArrayList<ParamListener>();
	
	public static Stage primaryStage = new Stage();
	
	// Einstellungen
	private String schnittstelle = "pusher";
	private int zugzeit = 2000;
	private char xodero = 'x';
	private String fileString = new String();
	
	// Pusher Credentials
	private String appId="255967";
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	private String appKey="61783ef3dd40e1b399b2";
	private String appSecret="66b722950915220b298c";
	
	// Grid
	private int anzahlzeilen;
	private int anzahlspalten;
	private final int l = 70; 		// Seitenlaenge der Grids - spaeter manuelle Einstellung
	public char spieler = 'x'; 		// Spieler 1
	public char gegner = 'o';
	private double breite = Toolkit.getDefaultToolkit().getScreenSize().width; // Breite des Fensters in Pixeln
	private int thema = 1;
	public char manuellerSpieler= 'x';
	
	// Spielernamen
	private String names1 = "Spieler1";
	private String names2 = "Spieler2";
	
	// Angaben aus anderen Klassen
	private Text spielstand = new Text("0 : 0");
	private Text satzstatus = new Text("Spiel noch nicht begonnen");
	
	
	//Erzeugen der Spielsteine
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
    
    // Variable fuer die Farbe des Spielfelds
    public Color color = Color.rgb(0, 0, 0);
    public GridPane spielfeld = new GridPane();
    
    public GridPane spielfeld2 = new GridPane();
    
    // Getter und Setter Methoden
    public void setColor(Color color) {	this.color = color;}
	public void setImage1(javafx.scene.image.Image image1) {this.image1 = image1;}
	public void setImage2(javafx.scene.image.Image image2) {this.image2 = image2;}
	public void setImage3(javafx.scene.image.Image image3) {this.image3 = image3;}
	public int getZugzeit() {return zugzeit;}
	public void setZugzeit(int zugzeit) {this.zugzeit = zugzeit;}
	public String getSchnittstelle() {return schnittstelle;}
	public void setSchnittstelle(String schnittstelle) {this.schnittstelle = schnittstelle;}
	public String getNames1() {return names1;}
	public void setNames1(String names1){this.names1 = names1;}
	public String getNames2() {return names2;}
	public void setNames2(String names2){this.names2 = names2;}
	public char getXodero() {return xodero;}
	public void setXodero(char xodero) {this.xodero = xodero;}
	public javafx.scene.image.Image getImage1() {return image1;}
	public javafx.scene.image.Image getImage2() {return image2;}
	public void setSatzstatus(String satzstatus) {this.satzstatus.setText(satzstatus);}
	public void setSpielstand(String spielstand) {this.spielstand.setText(spielstand);}
	public Text getSpielstand() {return spielstand;}
	public Text getSatzstatus() {return satzstatus;}
	public String getFileSting(){return fileString;}
	public String getAppId(){return appId;}	
	public String getAppKey(){return appKey;}
	public String getAppSecret(){return appSecret;}

	

	/*********************************************************************************************************************
	 ******************************************* START METHODE *********************************************************
	 ********************************************************************************************************************/
	
	public void start(Stage primaryStage) {
		
		for (int i = 0; i < plaetzeFreiInReihe.length; i++){
			plaetzeFreiInReihe[i]=5;
		}
		
		// Grundlegende Eigenschaften der Stage
		primaryStage.setFullScreen(false); 		// automatisches Oeffnen im Fullscreen
		primaryStage.setTitle("VierPunkt");
		primaryStage.setResizable(true);

		// Layout Boxen
		VBox root = new VBox(); 				// aeusserste Box
		root.setId("root");
		HBox content = new HBox();
		content.setPrefWidth(breite); 			// content ueber gesamte Bildschirmbreite
		content.setAlignment(Pos.TOP_CENTER); 	// alle Inhalte werden mittig ausgerichtet

		/********************************************** MENUBAR ******************************************************/
		// MenuBar Hauptkategorien
		final Menu vierpunkt = new Menu("Optionen");
		final Menu themen = new Menu("Themen");

		// Menubar, Hauptkategorien setzen
		MenuBar menuBar = new MenuBar();
		menuBar.setId("menu");
		menuBar.getMenus().addAll(vierpunkt, themen);

		// Unterkategorien fuer "vierpunkt"
		MenuItem menu11 = new MenuItem("neues Spiel");
		MenuItem menu31 = new MenuItem("bereits gespielte Spiele");
		MenuItem menu13 = new MenuItem("Spiel beenden");
		vierpunkt.getItems().addAll(menu11, menu31, menu13);
		
		// Unterkategorien fuer "themen"
		MenuItem menu21 = new MenuItem("Suessigkeiten");
		MenuItem menu22 = new MenuItem("Halloween");
		MenuItem menu23 = new MenuItem("Food");
		MenuItem menu24 = new MenuItem("Sports");
		themen.getItems().addAll(menu21, menu22, menu23, menu24);

		/******************************************* UEBERSCHRIFT *********************************************************/

		// Ansicht der Ueberschrift
		ImageView ueberschrift = new ImageView();
		ueberschrift.setId("ueberschrift");
		ueberschrift.setFitWidth(breite / 2); // Ueberschrift soll die Haelfte des Bildschirms breit sein
		ueberschrift.setFitHeight((breite / 2) / 3.33); // Aufrechterhalten des Verhaeltnisses (Quotient: 3.33)
		ueberschrift.setPreserveRatio(true);

		// Container, in dem die Ueberschrift platziert wird
		HBox titelbox = new HBox();
		titelbox.setId("titel");

		// Rechtecke als Platzhalter, um die Ueberschrift mittig zu platzieren
		Rectangle containerlinks = new Rectangle(breite / 4, (breite / 2) / 3.33);
		containerlinks.setId("container");
		Rectangle containerrechts = new Rectangle(breite / 4, (breite / 2) / 3.33);
		containerrechts.setId("container");

		// Platzhalter und Ueberschrift in den Container einfuegen
		titelbox.getChildren().addAll(containerlinks, ueberschrift, containerrechts);

		/******************************************* CONTAINERBOXEN *********************************************************/

		// Hauptcontainer erhaelt den Inhalt
		root.getChildren().addAll(menuBar, titelbox, content);

		/******************************************* Sieger Popup ******************************************************/
		VBox boxrechts2 = new VBox();
		boxrechts2.setPrefWidth(breite / 4);
		boxrechts2.setSpacing(10);
		boxrechts2.setPadding(new Insets(50, 0, 0, 50));
		
		Label sieger = new Label("Sieger:");
		Text antwortsieger = new Text("Spieler 1");
		boxrechts2.getChildren().addAll(sieger, antwortsieger);
		

		/******************************************** CONTAINERBOXEN IN CONTENT ************************************************/
		// Erzeugen der rechten Containerbox
		VBox boxrechts = new VBox();
		boxrechts.setPrefWidth(breite / 4);
		boxrechts.setSpacing(10);
		boxrechts.setPadding(new Insets(50, 0, 0, 50));

		/******* INHALTE DER RECHTEN CONTAINERBOX *********************/

		Label spielstandanzeige = new Label("Spielstand: ");
		//Text antwortspielstand = new Text(spielstand);
		spielstand.setId("text");
		spielstandanzeige.setPadding(new Insets(20, 0, 0, 0));

		Label satzstatusanzeige = new Label("Satzstatus:");
		//Text antwortsatzstatus = new Text(satzstatus);
		satzstatus.setId("text");
		satzstatusanzeige.setPadding(new Insets(20, 0, 0, 0));
		
		
		spielstand.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
               
            	System.out.println("Label Text Changed");
            }
        }); 

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
			public String toString(Double n) {
				if (n == 0)return "gegen den Comupter";
				if (n > 0 && n < 2)return "manuell";
				if (n == 2)return "automatisch";
				return "automatisch";
			}
			@Override
			public Double fromString(String x) {
				switch (x) {
				case "manuell":return 1d;
				case "gegen den Computer":return 0d;
				case "automatisch":return 2d;
				default:return 2d;
				}
			}
		});
		Button einstellungen = new Button("Einstellungen");
		Button start = new Button("Spiel starten");
		spielmodus.valueProperty().addListener(new ChangeListener<Number>() {
		    @Override
		    public void changed(ObservableValue<? extends Number> observable,
		            Number oldValue, Number newValue) {
		    	if(newValue.intValue() == 2){
		    		einstellungen.setOpacity(1);
		    		einstellungen.setDisable(false);
		    		start.setOpacity(1);
		    		start.setDisable(false);
		    		createGrids_automatisch(spielfeld);
		    	}
		    	if(newValue.intValue() == 1){
		    		einstellungen.setOpacity(0);
		    		einstellungen.setDisable(true);
		    		start.setOpacity(0);
		    		start.setDisable(true);
		    		createGrids();
		    	}
		    	if(newValue.intValue()==0){
		    		final Stage notImpl = new Stage();
		    		notImpl.setTitle("Noch nicht Implementiert");
		    		notImpl.initModality(Modality.APPLICATION_MODAL);
		    		notImpl.initOwner(primaryStage);
			        VBox themaVbox = new VBox(20);
			        themaVbox.setPadding(new Insets(10, 10, 10, 10));                
			        
			        Label nachricht = new Label();
			        nachricht.setText("Diese Funktion wurde noch nicht implementiert. Bitte waehle einen anderen Spielmodus.");
			        nachricht.setWrapText(true);
			        Button manuell = new Button("Manuell");
			        Button auto = new Button("Automatisch");
			        HBox hbox = new HBox();
			        hbox.getChildren().addAll(manuell, auto);
			        hbox.setAlignment(Pos.BASELINE_CENTER);
			        hbox.setSpacing(20);
			        
			       auto.setOnMouseClicked(new EventHandler<MouseEvent>(){
			        	@Override
			        	public void handle(MouseEvent arg0){
			        		spielmodus.setValue(2);
			        		notImpl.close();
			        	}
			        });
			       manuell.setOnMouseClicked(new EventHandler<MouseEvent>(){
			        	@Override
			        	public void handle(MouseEvent arg0){
			        		spielmodus.setValue(1);
			        		notImpl.close();
			        	}
			        });
			        // Einfuegen in die VBox
			        themaVbox.getChildren().addAll(nachricht, hbox);
			        Scene themaScene = new Scene(themaVbox, 500, 200);
			        
			       /* if(thema == 1){ themaScene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());}
			        if(thema == 2){ themaScene.getStylesheets().add(TestGui.class.getResource("Food.css").toExternalForm());}
			        if(thema == 3){ themaScene.getStylesheets().add(TestGui.class.getResource("Sport.css").toExternalForm());}
			        if(thema == 4){ themaScene.getStylesheets().add(TestGui.class.getResource("Sweets.css").toExternalForm());}
			      */
			        notImpl.setScene(themaScene);
			        notImpl.show();
			        
		    	}

		    }
		});
		
		// Platzhalter, damit der nachfolgende Button weiter unten angeordnet wird
		Rectangle platzhalter0 = new Rectangle(10, 40);
		platzhalter0.setOpacity(0);

		
	
		// Einfuegen der Elemente in die rechte Box
		boxrechts.getChildren().addAll(spielstandanzeige, spielstand, satzstatusanzeige, satzstatus, spielmodi, spielmodus, platzhalter0, start);

		// Erzeugen der mittleren Containerbox
		VBox boxmitte = new VBox();
		boxmitte.setId("boxmitte");
		boxmitte.setPrefWidth(7 * l);
		boxmitte.setPadding(new Insets(30, 0, 50, 0));
		boxmitte.setMinWidth(7 * l);

		/******* INHALTE DER MITTLEREN CONTAINERBOX *********************/
		Rectangle platzhalter1 = new Rectangle(7 * l, l);
		platzhalter1.setOpacity(0); // Platzhalter nicht sichtbar
		
		/*********************************************************************************************************************
		 ******************************************* GRID FUER DAS SPIELFELD *********************************************************
		 ********************************************************************************************************************/
		
		// Erzeugen eines GridPanes spielfeld im uebergeordneten GridPane grid
		spielfeld.setId("spielfeld");
		
		
		// Erzeugen der Spalten (7)
		spielfeld.getColumnConstraints().addAll(new ColumnConstraints(l, l, Double.MAX_VALUE),
				new ColumnConstraints(l, l, Double.MAX_VALUE), new ColumnConstraints(l, l, Double.MAX_VALUE),
				new ColumnConstraints(l, l, Double.MAX_VALUE), new ColumnConstraints(l, l, Double.MAX_VALUE),
				new ColumnConstraints(l, l, Double.MAX_VALUE), new ColumnConstraints(l, l, Double.MAX_VALUE));
		// Erzeugen der Zeilen (6)
		spielfeld.getRowConstraints().addAll(new RowConstraints(l, l, Double.MAX_VALUE),
				new RowConstraints(l, l, Double.MAX_VALUE), new RowConstraints(l, l, Double.MAX_VALUE),
				new RowConstraints(l, l, Double.MAX_VALUE), new RowConstraints(l, l, Double.MAX_VALUE),
				new RowConstraints(l, l, Double.MAX_VALUE));
				

		// Einfuegen der Elemente in die mittlere Box
		boxmitte.getChildren().addAll(platzhalter1, spielfeld);
		
		/******* INHALTE DER LINKEN CONTAINERBOX ************************/
		// Erzeugen der linken Containerbox
		VBox boxlinks = new VBox();
		boxlinks.setId("boxlinks");
		boxlinks.setPrefWidth(breite / 4);
		boxlinks.setAlignment(Pos.BOTTOM_LEFT);
		
		// login Felder
		Label spieler1 = new Label("Spieler: ");
		TextField spielername1 = new TextField();
		if(spielername1.getText() != null && ! spielername1.getText().trim().isEmpty()){
			//names1 = spielername1.getText();
			setNames1(spielername1.getText());
		}
		
		Label spieler2 = new Label("Gegner: ");
		TextField spielername2 = new TextField();
		if(spielername2.getText() != null && ! spielername2.getText().trim().isEmpty()){
			//names2 = spielername2.getText();
			setNames2(spielername2.getText());
		}
		
	
		// Ueberschrift Spieler
		Label spielerfarben = new Label("Spieler");
		// Name mit Spielstein Spieler 1
		ImageView i1 = new ImageView(getImage1());
		i1.setFitWidth(l-20);	i1.setFitHeight(l-20);
		Label s1 = new Label(getNames1());
		// Name mit Spielstein Spieler 2
		ImageView i2 = new ImageView(getImage2());
		i2.setFitWidth(l-20);	i2.setFitHeight(l-20);
		Label s2 = new Label(getNames2());
		// Platzhalter
		Rectangle p = new Rectangle(20,20);
		p.setOpacity(0);
		Rectangle p1 = new Rectangle(20,20);
		p1.setOpacity(0);
		// zur Box Hinzufuegen
		HBox box3 = new HBox();
		box3.getChildren().addAll(s1, i1, s2, i2);
		
		ImageView bild = new ImageView();
		bild.setId("bild");
		bild.setFitWidth(breite / 4); // Breite soll ein Viertel des Fensters betragen
		bild.setPreserveRatio(true); // Das Verhaeltnis soll beibehalten werden
		
		
		Rectangle platzhalter2 = new Rectangle(l, 2*l);
		platzhalter2.setOpacity(0);
		
		/*************************************************************************************************************
		 *************************************************************************************************************
		 ***************************************       Einstellungen       *******************************************
		 *************************************************************************************************************
		 *************************************************************************************************************/
		// Schnittstelle
		Label schnittstelle = new Label("Schnittstelle");
		CheckBox file = new CheckBox("File");
		Text keinOrdner = new Text("ACHTUNG! Es wurde kein Ordner ausgewaehlt!");
		keinOrdner.setStyle("-fx-fill: red;");
		keinOrdner.setOpacity(0);
		HBox hb = new HBox();
		hb.getChildren().addAll(file, keinOrdner);
		
		CheckBox pusher1 = new CheckBox("Pusher");
		
		file.getStyleClass().add("checkBox");
		pusher1.getStyleClass().add("checkBox");
		
		// Zeit
		Slider zeit = new Slider(0, 5000, 100); 			// Slider geht von 0 bis 2 in 1er Abstaenden
		zeit.setMinorTickCount(0);
		zeit.setMajorTickUnit(100); 					// Man kann nur auf den Zahlen 0, 1, 2 landen, nicht dazwischen
		zeit.setSnapToTicks(true); 						// Der Punkt rutzscht zur naechsten Zahl
		zeit.setShowTickMarks(true); 					// Markierungen anzeigen -
		zeit.setOrientation(Orientation.HORIZONTAL); 	// Vertikale Anordnung,standardmaessig horizontal
		zeit.setValue(2000);								// Default Value = 2
		
		Label zeitlabel = new Label("Zugzeit");
		zeit.valueProperty().addListener(new ChangeListener<Number>() {
		    @Override
		    public void changed(ObservableValue<? extends Number> observable,
		            Number oldValue, Number newValue) {
		    	zeitlabel.setText("Zugzeit:   " + newValue + " ms");
		    	setZugzeit(newValue.intValue());
		    }
		});
		
		// Ueberschrift
        Text u1 = new Text("Einstellungen");
        u1.setId("textEinstellungen");
        
        // Pusher Credentials
        Label cred1 = new Label("App ID:");
        TextField app1 = new TextField ();
        //app1.setPromptText(appId);
        app1.setText(appId);
        
        HBox hb1 = new HBox();
        hb1.getChildren().addAll(cred1, app1);
        hb1.setSpacing(10);
        Label cred2 = new Label("App Key: ");
        TextField app2 = new TextField ();
        //app2.setPromptText(appKey);
        app2.setPrefWidth(200);
        app2.setText(appKey);
        
        HBox hb2 = new HBox();
        hb2.getChildren().addAll(cred2, app2);
        hb2.setSpacing(10);
        Label cred3 = new Label("App Secret: ");
        TextField app3 = new TextField ();
        //app3.setPromptText(appSecret);
        app3.setPrefWidth(200);
        app3.setText(appSecret);
       
        HBox hb3 = new HBox();
        hb3.getChildren().addAll(cred3, app3);
        hb3.setSpacing(10);
        
        hb1.setOpacity(0);
        hb2.setOpacity(0);
        hb3.setOpacity(0);
        
    	pusher1.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                if(file.isSelected()){
             	   file.setSelected(false); 
             	   keinOrdner.setOpacity(0);
             	   pusher1.setSelected(true);
                }else{pusher1.setSelected(true);}
                setSchnittstelle("pusher");
                hb1.setOpacity(1);
                hb2.setOpacity(1);
                hb3.setOpacity(1);
            }
    	});
	
		DirectoryChooser chooser = new DirectoryChooser();
	
		file.setOnMouseClicked(new EventHandler<MouseEvent>(){
	     @Override
	     public void handle(MouseEvent arg0) {
	         if(pusher1.isSelected()){
	      	   pusher1.setSelected(false);   
	      	   file.setSelected(true);
	         }else{file.setSelected(true);}
	         setSchnittstelle("file");
	         hb1.setOpacity(0);
	         hb2.setOpacity(0);
	         hb3.setOpacity(0);
	         
	         
	         
	         File dir = chooser.showDialog(primaryStage);
	 	    if (dir == null) {
	 	        System.out.println("Kein Ordner ausgewählt");
	 	        keinOrdner.setOpacity(1);
	 	    }else{
	 	    	fileString = dir.getPath();
	 	    	System.out.println(fileString);
	 	    	keinOrdner.setOpacity(0);
	 	    }
	     }
		});
			
		// X oder O
		ToggleGroup group = new ToggleGroup();
		ToggleButton tb1 = new ToggleButton("X");
		tb1.setToggleGroup(group);
		tb1.setSelected(true);
		tb1.getStyleClass().add("togglebutton");
		ToggleButton tb2 = new ToggleButton("O");
		tb2.setToggleGroup(group);
		tb2.getStyleClass().add("togglebutton");
		HBox hb4 = new HBox();
        hb4.getChildren().addAll(tb1, tb2);
        hb4.setSpacing(10);
       
		
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov,
		        Toggle toggle, Toggle new_toggle) {
		            if (new_toggle == null)
		                setXodero('o');
		            else
		            	setXodero('x');
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
                dialogVbox.getChildren().addAll(u1, hb4, schnittstelle, hb, pusher1, hb1, hb2, hb3, p1, zeitlabel, zeit, ok);
                Scene dialogScene = new Scene(dialogVbox, 500, 800);
                
                if(thema == 1){ dialogScene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());}
                if(thema == 2){ dialogScene.getStylesheets().add(TestGui.class.getResource("Food.css").toExternalForm());}
                if(thema == 3){dialogScene.getStylesheets().add(TestGui.class.getResource("Sport.css").toExternalForm());}
                if(thema == 4){ dialogScene.getStylesheets().add(TestGui.class.getResource("Sweets.css").toExternalForm());}
              
                dialog.setScene(dialogScene);
                dialog.show();	
			}
		});


		// Einfuegen der Elemente in die linke Box
		boxlinks.getChildren().addAll(spielerfarben, box3, p, einstellungen, bild, platzhalter2);

		/******* CONTAINERBOXEN EINFUEGEN ************************/
		content.getChildren().addAll(boxlinks, boxmitte, boxrechts);

		/*************************************************************************************************************
		 *************************************************************************************************************
		 ******************************************* EVENTHANDLER FUER DAS MENU **************************************
		 *************************************************************************************************************
		 *************************************************************************************************************/
		
		Scene scene = new Scene(root);
		
		menu13.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) { Platform.exit();}
		});
		
		menu11.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e) {
				primaryStage.setScene(scene);
				// primaryStage.setFullScreen(true);
				spieler = getXodero();
				if(spieler == 'x'){
					gegner = 'o';
				}else{gegner = 'x';}
				
				createGrids();
				}
		});
		
		// neue Stage
				final Stage changetheme = new Stage();
				changetheme.setTitle("Themenwechsel");
				changetheme.initModality(Modality.APPLICATION_MODAL);
				changetheme.initOwner(primaryStage);
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
		        		changetheme.close();
		        	}
		        });
		        // Einfuegen in die VBox
		        themaVbox.getChildren().addAll(nachricht, hbox);
		        Scene themaScene = new Scene(themaVbox, 500, 200);
		        
		       /* if(thema == 1){ themaScene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());}
		        if(thema == 2){ themaScene.getStylesheets().add(TestGui.class.getResource("Food.css").toExternalForm());}
		        if(thema == 3){ themaScene.getStylesheets().add(TestGui.class.getResource("Sport.css").toExternalForm());}
		        if(thema == 4){ themaScene.getStylesheets().add(TestGui.class.getResource("Sweets.css").toExternalForm());}
		      */
		        changetheme.setScene(themaScene);
		        
		      //  changetheme.show();
		
		menu21.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				if(thema!=4){
					themaScene.getStylesheets().clear();
					if(thema == 1){ themaScene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());}
			        if(thema == 2){ themaScene.getStylesheets().add(TestGui.class.getResource("Food.css").toExternalForm());}
			        if(thema == 3){ themaScene.getStylesheets().add(TestGui.class.getResource("Sport.css").toExternalForm());}
			        
					
					open.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
			            public void handle(MouseEvent arg0) {
							
							changetheme.close();
							primaryStage.setScene(scene);
							primaryStage.setFullScreen(false);
							setColor(Color.PURPLE);
							setImage1(orange);
							setImage2(gruen);
							i1.setImage(orange);
							i2.setImage(gruen);
							scene.getStylesheets().clear();
							scene.getStylesheets().add(TestGui.class.getResource("Sweets.css").toExternalForm());
							createGrids();
							thema = 4;
						}
			        });
					
					changetheme.show();
				
				}
				
			}
		});
		
		menu22.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				if(thema != 1){ 
					themaScene.getStylesheets().clear();
					if(thema == 2){ themaScene.getStylesheets().add(TestGui.class.getResource("Food.css").toExternalForm());}
			        if(thema == 3){ themaScene.getStylesheets().add(TestGui.class.getResource("Sport.css").toExternalForm());}
			        if(thema == 4){ themaScene.getStylesheets().add(TestGui.class.getResource("Sweets.css").toExternalForm());}
					open.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
			            public void handle(MouseEvent arg0) {
							
							changetheme.close();
							primaryStage.setScene(scene);
							primaryStage.setFullScreen(false);
							scene.getStylesheets().clear();
							scene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());
							setColor(Color.BLACK);
							setImage1(kuerbis);
							setImage2(fledermaus);
							i1.setImage(kuerbis);
							i2.setImage(fledermaus);
							createGrids();
							thema = 1;
						}
			        });
					changetheme.show();
				}
		        
			}
		});
		
		menu23.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				if(thema != 2){
					themaScene.getStylesheets().clear();
					if(thema == 1){ themaScene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());}
			        if(thema == 3){ themaScene.getStylesheets().add(TestGui.class.getResource("Sport.css").toExternalForm());}
			        if(thema == 4){ themaScene.getStylesheets().add(TestGui.class.getResource("Sweets.css").toExternalForm());}
					
					open.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
			            public void handle(MouseEvent arg0) {
							
							changetheme.close();
							primaryStage.setScene(scene);
							primaryStage.setFullScreen(false);
							scene.getStylesheets().clear();
							scene.getStylesheets().add(TestGui.class.getResource("Food.css").toExternalForm());
							setColor(Color.DARKGREEN);
							setImage1(pizza);
							setImage2(burger);
							i1.setImage(pizza);
							i2.setImage(burger);
							createGrids();
							thema = 2;
						}
			        });
					changetheme.show();
				
				}
			}
		});
		
		menu24.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				if(thema!= 3){
					themaScene.getStylesheets().clear();
					if(thema == 1){ themaScene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());}
			        if(thema == 2){ themaScene.getStylesheets().add(TestGui.class.getResource("Food.css").toExternalForm());}
			        if(thema == 4){ themaScene.getStylesheets().add(TestGui.class.getResource("Sweets.css").toExternalForm());}
					
					
					open.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
			            public void handle(MouseEvent arg0) {
							changetheme.close();
							primaryStage.setScene(scene);
							primaryStage.setFullScreen(false);
							scene.getStylesheets().clear();
							scene.getStylesheets().add(TestGui.class.getResource("Sport.css").toExternalForm());
							setColor(Color.CADETBLUE);
							setImage1(basketball);
							setImage2(baseball);
							i1.setImage(basketball);
							i2.setImage(baseball);
							createGrids();
							thema = 3;
						}
			        });
					changetheme.show();
				}
			}
		});
		
		/*************************************************************************************************************
		 *************************************************************************************************************
		 **************************************** Methodenaufruf je nach Slider **************************************
		 *************************************************************************************************************
		 *************************************************************************************************************/
		// manuell
		if(spielmodus.getValue() == 1 ){
			createGrids();
		}
		
		// automatisch
		if(spielmodus.getValue() == 2){
			createGrids_automatisch(spielfeld);
		}
		
		/*************************************************************************************************************
		 *************************************************************************************************************
		 *********************************************** diverse Stages **********************************************
		 *************************************************************************************************************
		 *************************************************************************************************************/
		
		// Login Stage
		Stage loginStage = new Stage();
		Button login = new Button("Spiel starten");
		VBox vb = new VBox();
		vb.setAlignment(Pos.CENTER);
		vb.setPadding(new Insets(10, 10, 10, 10));
		
		Label meldung = new Label("Bitte Spielernamen eingeben");
		meldung.setOpacity(0);
		meldung.setStyle("-fx-font-weight: lighter;");
		
		HBox hb5 = new HBox();
		spieler1.setPrefWidth(200);
		hb5.getChildren().addAll(spieler1, spielername1);
		hb5.setSpacing(10);
		
		HBox hb6 = new HBox();
		spieler2.setPrefWidth(200);
		hb6.getChildren().addAll(spieler2, spielername2);
		hb6.setSpacing(10);
		
		Rectangle p2 = new Rectangle(20, 15);
		p2.setOpacity(0);
		Rectangle p3 = new Rectangle(20, 15);
		p3.setOpacity(0);
		
		vb.getChildren().addAll(hb5, hb6, p2, meldung, p3, login);
		Scene scene2 = new Scene(vb, 400, 250);
		loginStage.setScene(scene2);
	    scene2.getStylesheets().add(TestGui.class.getResource("Gui.css").toExternalForm());
	    loginStage.initModality(Modality.APPLICATION_MODAL);
	    loginStage.setTitle("Spielernamen");
	    loginStage.setFullScreen(false);
	    
	    
	    // Login bei Enter, egal in welchem Feld man ist
	    spielername1.setOnKeyPressed(new EventHandler<KeyEvent>()
	    {
	        @Override
	        public void handle(KeyEvent ke)
	        {
	            if (ke.getCode().equals(KeyCode.ENTER))
	            {meldung.setText("Bitte Spielernamen eingeben");
				if(spielername1.getText() == null || spielername1.getText().trim().isEmpty() || spielername2.getText() == null ||  spielername2.getText().trim().isEmpty()){
					meldung.setOpacity(1);
				}else{
					if(spielername1.getText().equals(spielername2.getText())){
						meldung.setText("Bitte unterschiedliche Spielernamen waehlen.");
						meldung.setOpacity(1);
					}else{
						loginStage.close();
						s1.setText(spielername1.getText());
						s2.setText(spielername2.getText());
						primaryStage.show();
						}
			
	            }
	        }
	        }});
	    
	    spielername2.setOnKeyPressed(new EventHandler<KeyEvent>()
	    {
	        @Override
	        public void handle(KeyEvent ke)
	        {
	            if (ke.getCode().equals(KeyCode.ENTER))
	            {
	            	meldung.setText("Bitte Spielernamen eingeben");
					if(spielername1.getText() == null || spielername1.getText().trim().isEmpty() || spielername2.getText() == null ||  spielername2.getText().trim().isEmpty()){
						meldung.setOpacity(1);
					}else{
						if(spielername1.getText().equals(spielername2.getText())){
							meldung.setText("Bitte unterschiedliche Spielernamen waehlen.");
							meldung.setOpacity(1);
						}else{
							loginStage.close();
							s1.setText(spielername1.getText());
							s2.setText(spielername2.getText());
							primaryStage.show();
							}
				
	            }
	        }
	        }});
	    
	    login.setOnKeyPressed(new EventHandler<KeyEvent>()
	    {
	         public void handle(KeyEvent evt)
	         {
	              if (evt.getCode() == KeyCode.ENTER)
	            	  meldung.setText("Bitte Spielernamen eingeben");
					if(spielername1.getText() == null || spielername1.getText().trim().isEmpty() || spielername2.getText() == null ||  spielername2.getText().trim().isEmpty()){
						meldung.setOpacity(1);
					}else{
						if(spielername1.getText().equals(spielername2.getText())){
							meldung.setText("Bitte unterschiedliche Spielernamen waehlen.");
							meldung.setOpacity(1);
						}else{
							loginStage.close();
							s1.setText(spielername1.getText());
							s2.setText(spielername2.getText());
							primaryStage.show();
							}
				
	         }
	    }});
	    
		login.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
            public void handle(MouseEvent arg0) {
				meldung.setText("Bitte Spielernamen eingeben");
				if(spielername1.getText() == null || spielername1.getText().trim().isEmpty() || spielername2.getText() == null ||  spielername2.getText().trim().isEmpty()){
					meldung.setOpacity(1);
				}else{
					if(spielername1.getText().equals(spielername2.getText())){
						meldung.setText("Bitte unterschiedliche Spielernamen waehlen.");
						meldung.setOpacity(1);
					}else{
						loginStage.close();
						s1.setText(spielername1.getText());
						s2.setText(spielername2.getText());
						primaryStage.show();
						}
			
					
				}
            }
		});
	    
		// Gewinnermeldung
		/*Stage gewinnermeldung = new Stage();
		FlowPane panegewinner = new FlowPane();
		Label gewinnernachricht = new Label();
		panegewinner.setPadding(new Insets(10, 10, 10, 10));
		panegewinner.getChildren().addAll(gewinnernachricht);
		Scene meldung1 = new Scene(panegewinner);
		gewinnermeldung.setScene(meldung1);		*/
		 
		// primary Stage
		primaryStage.setScene(scene);
		scene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());
		spielfeld2.setId("spielfeld");
		
		
		// Erzeugen der Spalten (7)
		spielfeld2.getColumnConstraints().addAll(new ColumnConstraints(l, l, Double.MAX_VALUE),
				new ColumnConstraints(l, l, Double.MAX_VALUE), new ColumnConstraints(l, l, Double.MAX_VALUE),
				new ColumnConstraints(l, l, Double.MAX_VALUE), new ColumnConstraints(l, l, Double.MAX_VALUE),
				new ColumnConstraints(l, l, Double.MAX_VALUE), new ColumnConstraints(l, l, Double.MAX_VALUE));
		// Erzeugen der Zeilen (6)
		spielfeld2.getRowConstraints().addAll(new RowConstraints(l, l, Double.MAX_VALUE),
				new RowConstraints(l, l, Double.MAX_VALUE), new RowConstraints(l, l, Double.MAX_VALUE),
				new RowConstraints(l, l, Double.MAX_VALUE), new RowConstraints(l, l, Double.MAX_VALUE),
				new RowConstraints(l, l, Double.MAX_VALUE));
		
		
		//Beispieldaten
		
		
		
		/*aAllGames[0][0] = "123";
		aAllGames[0][1] = "Leon";
		aAllGames[0][2] = "Phil";
		aAllGames[0][3] = "Phil";
		aAllGames[0][4] = "3:0";
		aAllGames[1][0] = "456";
		aAllGames[1][1] = "Tim";
		aAllGames[1][2] = "Tobi";
		aAllGames[1][3] = "Tim";
		aAllGames[1][4] = "2:1";
		
		String[][] alleSaetze = new String[20][3]; 
		alleSaetze[0][0] = "Satz1";
		alleSaetze[0][1] = "123";
		alleSaetze[0][2] = "irgendwas";

		String[][] alleZuege = new String[20][5]; 
		alleZuege[0][0] = "Zug1";
		alleZuege[0][1] = "123";
		alleZuege[0][2] = "Leon";
		alleZuege[0][3] = "1";
		alleZuege[0][4] = "0";
		*/
		
		
		
		
	    menu31.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e) {
			bisherigeSpiele();	
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
					createGrids();}
				System.out.println(getAppId() + " " + getAppKey() +" "+ getAppSecret());
				fireStartEvent(getZugzeit(), getSchnittstelle(), getFileString(), getXodero(), getAppId(), getAppKey(), getAppSecret() /*app1.getText(), app2.getText(), app3.getText()*/);
				fireNames(spielername1.getText(), spielername2.getText());
				
				//Diese Methode muss in das Event Match beendet verschoben werden!
				for (int i = 0; i < plaetzeFreiInReihe.length; i++){
					plaetzeFreiInReihe[i]=5;
				}
				//satzgewinner(1);
				//System.out.println(getNames1() + names2);
				//gewinnermethode(1, spielername1.getText(), spielername2.getText());
				
            }
		});
	
		loginStage.show();
		
	}
	/*********************************************************************************************************************
	 *********************************************************************************************************************
	 **********************************************************************************************************************
	 *********************************************************************************************************************
     *******************************************  SPIELFELD ERZEUGEN METHODE  ********************************************
     ********************************************************************************************************************/
	 static Spiele selectedGame;
	 static String personX;
	public void bisherigeSpiele(){
		
		ConnectHSQL db = new ConnectHSQL();
		String[][] alleGames = db.getLastTenGames();
		
		//String[][] alleSaetze = db.getHighscoreMatch(G_ID);
		
		
		// neue Stage
		final Stage spieleStage = new Stage();
		spieleStage.setTitle("Bisherige Spiele");
		spieleStage.initModality(Modality.APPLICATION_MODAL);
		spieleStage.initOwner(primaryStage);
        VBox spieleVbox = new VBox(20);
        spieleVbox.setPadding(new Insets(10, 10, 10, 10));   
        spieleVbox.setAlignment(Pos.CENTER);
        
        // Button zum Popup schliessen
        Button back = new Button("Zurueck");
        
        Label spieleLabel = new Label("Bisherige Spiele:");
       
       
        TableView<Spiele> table = new TableView<>();
       // table.setEditable(true);


        TableColumn<Spiele, String> gameIDCol = new TableColumn<>("Spiele ID");
        gameIDCol.setCellValueFactory(new PropertyValueFactory<>("gameID"));
        gameIDCol.setMinWidth(250);
        gameIDCol.setId("spalte0");
        TableColumn<Spiele, String> player1Col = new TableColumn<>("Spieler 1");
        player1Col.setCellValueFactory(new PropertyValueFactory<>("spieler1"));
        player1Col.setMinWidth(250);
        TableColumn<Spiele, String> player2Col = new TableColumn<>("Spieler 2");
        player2Col.setCellValueFactory(new PropertyValueFactory<>("spieler2"));
        player2Col.setMinWidth(250);
        TableColumn<Spiele, String> winnerCol = new TableColumn<>("Gewinner");
        winnerCol.setCellValueFactory(new PropertyValueFactory<>("winner"));
        winnerCol.setMinWidth(250);
        TableColumn<Spiele, String> pointsCol = new TableColumn<>("Punkte");
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));
        pointsCol.setMinWidth(250);
        table.getColumns().addAll(gameIDCol, player1Col, player2Col, winnerCol, pointsCol);
       
        ObservableList<Spiele> items = FXCollections.observableArrayList();
        
        for (int i = 0; i < 10; i++) {
        	String game = alleGames[i][0];
        	String player1 = alleGames[i][1];
        	String player2 = alleGames[i][2];
        	String winner = alleGames[i][3];
        	String points = alleGames[i][4];
        	items.add(new Spiele(game, player1, player2, winner, points));
        	System.out.println(alleGames[i][0]+ alleGames[i][1]+ alleGames[i][2]+ alleGames[i][3]+ alleGames[i][4]);
		}
        table.setItems(items);
        
        Slider satz = new Slider(0, 2, 1); 	
       
       
        
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	          System.out.println(newSelection);  
			  if (newSelection != null) {
	                selectedGame = table.getSelectionModel().getSelectedItem();
	                System.out.println(selectedGame);
	                }});
        
		Button play = new Button("Play"); 
		play.setOnMouseClicked(new EventHandler<MouseEvent>(){
	     	   @Override
	            public void handle(MouseEvent arg0) {
	     		   			int g_id = Integer.parseInt(selectedGame.getGameID());
	     	                
	     	                String[][] alleSaetze = db.getHighscoreMatch(g_id);
	     	                int m_id = Integer.parseInt(alleSaetze[(int)satz.getValue()][0]);
	     	                
	     	                System.out.println(g_id + " " + m_id);
	     	                	
	     	                String[][] alleZuege = db.getHighscoreTurn(g_id, m_id);
	     	                personX = alleZuege[0][2];
	     	                for (int i = 0; i < alleZuege[0].length; i++) {
	     	                	
	     	                	System.out.println(personX);
	     	                	if(personX.equals(alleZuege[i][2])){
	     	                		spielsteinAnzeigen(getImageView((getNodeByRowColumnIndex(Integer.parseInt(alleZuege[i][4]), Integer.parseInt(alleZuege[i][3]), spielfeld2))), 'x');
	     	                	} else{
	     	                		spielsteinAnzeigen(getImageView((getNodeByRowColumnIndex(Integer.parseInt(alleZuege[i][4]), Integer.parseInt(alleZuege[i][3]), spielfeld2))), 'o');
	     	                	}
	     	                	
								/*try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}*/
							}
	     	            }
	     	        });
	     		   
        // Button Action Event
        back.setOnMouseClicked(new EventHandler<MouseEvent>(){
     	   @Override
            public void handle(MouseEvent arg0) {
     		   spieleStage.close();
        }});
        
        HBox hb = new HBox();
        Label spielstandanzeige = new Label("Spielstand: ");
		Text spielstand_altesSpiel = new Text("3:1");
		spielstandanzeige.setPadding(new Insets(20, 0, 0, 0));
		Label spieler = new Label("Spieler: ");
		Label spieler1 = new Label("names1");
		Label spieler2 = new Label("names2");
		Rectangle platzhalter1 = new Rectangle(7 * l, l);
		platzhalter1.setOpacity(0); // Platzhalter nicht sichtbar
		
		Rectangle platzhalter2 = new Rectangle(l, l);
		platzhalter2.setOpacity(0); // Platzhalter nicht sichtbar
		
		Rectangle platzhalter3 = new Rectangle(l, l);
		platzhalter3.setOpacity(0); // Platzhalter nicht sichtbar
		Rectangle platzhalter4 = new Rectangle(l, l);
		platzhalter4.setOpacity(0); // Platzhalter nicht sichtbar
		Rectangle platzhalter5 = new Rectangle(l, l);
		platzhalter5.setOpacity(0); // Platzhalter nicht sichtbar
		Rectangle platzhalter7 = new Rectangle(l, l*2);
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
		
		VBox grid = new VBox();
		grid.getChildren().addAll(platzhalter1, spielfeld2);
		
		VBox anzeige = new VBox();
		anzeige.setAlignment(Pos.BOTTOM_CENTER);
		anzeige.setPrefWidth((breite-(7*l))/2);
		anzeige.getChildren().addAll(satz, platzhalter7, play, platzhalter4);
		
		VBox spieleranzeige = new VBox();
		spieleranzeige.setAlignment(Pos.BOTTOM_CENTER);
		spieleranzeige.setPrefWidth((breite-(7*l))/2);
		spieleranzeige.getChildren().addAll(spieler, spieler1, spieler2, platzhalter2, spielstandanzeige, spielstand_altesSpiel, platzhalter3, back, platzhalter5);
        
		hb.getChildren().addAll(spieleranzeige, grid, anzeige);
        hb.setAlignment(Pos.BOTTOM_CENTER);
        // Einfuegen in die VBox
        spieleVbox.getChildren().addAll(spieleLabel, table, hb);
        Scene spieleScene = new Scene(spieleVbox, 1200, 900);
        if(thema == 1){ spieleScene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());}
        if(thema == 2){ spieleScene.getStylesheets().add(TestGui.class.getResource("Food.css").toExternalForm());}
        if(thema == 3){spieleScene.getStylesheets().add(TestGui.class.getResource("Sport.css").toExternalForm());}
        if(thema == 4){ spieleScene.getStylesheets().add(TestGui.class.getResource("Sweets.css").toExternalForm());}
      
        createGrids_automatisch(spielfeld2);
        spieleStage.setScene(spieleScene);
        //spieleStage.setFullScreen(true);
        spieleStage.show();	
		}
		
	
	
	public void onConnectionError(){
		// neue Stage
		final Stage connection = new Stage();
		connection.setTitle("Verbindungsfehler");
        connection.initModality(Modality.APPLICATION_MODAL);
        connection.initOwner(primaryStage);
        VBox connectionVbox = new VBox(20);
        connectionVbox.setPadding(new Insets(10, 10, 10, 10));                
        
        Label meldung = new Label();
        meldung.setText("Der Pusher Server reagiert nicht, bitte Pusher Credentials in den Einstellungen ueberpruefen");
        Button ok = new Button("Einstellungen aendern");
        
        ok.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
            public void handle(MouseEvent arg0) {
				connection.close();
			}
        });
        // Einfuegen in die VBox
        connectionVbox.getChildren().addAll(meldung, ok);
        Scene connectionScene = new Scene(connectionVbox, 500, 800);
        
        if(thema == 1){ connectionScene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());}
        if(thema == 2){ connectionScene.getStylesheets().add(TestGui.class.getResource("Food.css").toExternalForm());}
        if(thema == 3){connectionScene.getStylesheets().add(TestGui.class.getResource("Sport.css").toExternalForm());}
        if(thema == 4){ connectionScene.getStylesheets().add(TestGui.class.getResource("Sweets.css").toExternalForm());}
      
        connection.setScene(connectionScene);
        
        connection.show();
        
	}
	
	public void satzgewinner(int spieler){
		// neue Stage
		final Stage satz = new Stage();
		satz.setTitle("Satzgewinner");
        satz.initModality(Modality.APPLICATION_MODAL);
        satz.initOwner(primaryStage);
        VBox satzVbox = new VBox(20);
        satzVbox.setPadding(new Insets(10, 10, 10, 10));                
        
        Label meldung = new Label();
        
        if(spieler==1){ meldung.setText(names1 + " hat den Satz gewonnen!");}
        else if (spieler == 2) {meldung.setText(names2 + " hat den Satz gewonnen!");
		}else{meldung.setText("Der Satz ist unentschieden ausgegangen.");}
        
        
        // Einfuegen in die VBox
        satzVbox.getChildren().addAll(meldung);
        Scene dialogScene = new Scene(satzVbox, 500, 800);
        
        if(thema == 1){ dialogScene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());}
        if(thema == 2){ dialogScene.getStylesheets().add(TestGui.class.getResource("Food.css").toExternalForm());}
        if(thema == 3){ dialogScene.getStylesheets().add(TestGui.class.getResource("Sport.css").toExternalForm());}
        if(thema == 4){ dialogScene.getStylesheets().add(TestGui.class.getResource("Sweets.css").toExternalForm());}
      
        satz.setScene(dialogScene);
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished( event -> satz.close() );
       
        
        delay.play();
        satz.show();
        
	}
	
	
	public void gewinnermethode(int spieler, String names1, String names2){
		
		Label gewinnernachricht = new Label();
		
		// neue Stage
		final Stage gewinnerStage = new Stage();
		gewinnerStage.setTitle("Gewinner");
		gewinnerStage.initModality(Modality.APPLICATION_MODAL);
		gewinnerStage.initOwner(primaryStage);
        VBox gewinnerVbox = new VBox(20);
        gewinnerVbox.setAlignment(Pos.CENTER);
        gewinnerVbox.setPadding(new Insets(10, 10, 10, 10));                
        
        // Button zum Popup schliessen
        Button back = new Button("ok");
        
        // Button Action Event
        back.setOnMouseClicked(new EventHandler<MouseEvent>(){
     	   @Override
            public void handle(MouseEvent arg0) {
     		   gewinnerStage.close();
        }});
        if(spieler == 1){
			gewinnernachricht.setText(names1 + " hat gewonnen!");
		}else {
			if(spieler == 2){
				gewinnernachricht.setText(names2 + " hat gewonnen!");
			}else{
				gewinnernachricht.setText("Das spiel ist unentschieden ausgegangen.");
			}}
        // Einfuegen in die VBox
        gewinnerVbox.getChildren().addAll(gewinnernachricht, back);
        Scene gewinnerScene = new Scene(gewinnerVbox, 800, 600);
        if(thema == 1){ gewinnerScene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());}
        if(thema == 2){ gewinnerScene.getStylesheets().add(TestGui.class.getResource("Food.css").toExternalForm());}
        if(thema == 3){ gewinnerScene.getStylesheets().add(TestGui.class.getResource("Sport.css").toExternalForm());}
        if(thema == 4){ gewinnerScene.getStylesheets().add(TestGui.class.getResource("Sweets.css").toExternalForm());}
       
        
        gewinnerStage.setFullScreen(false);
        gewinnerStage.setScene(gewinnerScene);
        gewinnerStage.show();	
		
	}
	

	
	
    public void createGrids_automatisch(GridPane spielfeld){
    	spielfeld.getChildren().clear();
        for(anzahlzeilen=0;anzahlzeilen<spielfeld.getRowConstraints().size(); anzahlzeilen++){
            for(anzahlspalten=0; anzahlspalten<spielfeld.getColumnConstraints().size(); anzahlspalten++){
            
            // Darstellung des Rahmens/ der Zellen    
            Rectangle rect = new Rectangle(l,l);
            Circle circ = new Circle((l/2)-5);
            circ.centerXProperty().set(l/2);
            circ.centerYProperty().set(l/2);
            Shape cell = Path.subtract(rect, circ);
            cell.setId("cell");
            cell.setFill(color);
            cell.setStroke(color.darker());
            
            /*******************************************************************************************************************
             *******************************************  SPIELSTEINE  *********************************************************
             *******************************************************************************************************************/            
            
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

	public void createGrids(){
    	spielfeld.getChildren().clear();
        for(anzahlzeilen=0;anzahlzeilen<spielfeld.getRowConstraints().size(); anzahlzeilen++){
            for(anzahlspalten=0; anzahlspalten<spielfeld.getColumnConstraints().size(); anzahlspalten++){
            
            // Darstellung des Rahmens/ der Zellen    
            Rectangle rect = new Rectangle(l,l);
            Circle circ = new Circle((l/2)-5);
            circ.centerXProperty().set(l/2);
            circ.centerYProperty().set(l/2);
            Shape cell = Path.subtract(rect, circ);
            cell.setId("cell");
            cell.setFill(color);
            cell.setStroke(color.darker());
            
            /*******************************************************************************************************************
             *******************************************  SPIELSTEINE  *********************************************************
             *******************************************************************************************************************/            
            
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
                       if(spieler=='x'){vorschauspielstein.setImage(image1);      
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
                       if(spieler=='x'){ vorschauspielstein.setImage(image1);
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
                		   manuellerSpieler = 'o';
                	   }else{
                		   manuellerSpieler = 'x';
                	   }
                	   spielsteinAnzeigen(spielstein, manuellerSpieler);
                	   
                	   }
               });
              
               // Setzen der Spielsteine beim Klick auf die entsprechende Vorschau
               vorschauspielstein.setOnMouseClicked(new EventHandler<MouseEvent>(){
                   @Override public void handle(MouseEvent arg0) {
                	   spielsteinAnzeigen(spielstein, manuellerSpieler);
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
	
    public void setSpielstein(int zeile, int spalte, char amZug){
    	spielsteinAnzeigen(getImageView((getNodeByRowColumnIndex(zeile, spalte, spielfeld))), amZug);
    }
    
    public void spielsteinAnzeigen(ImageView spielstein, char amZug){
    	if(spielstein.getTranslateY()!=0){ 
    		//spielstein.setTranslateY(-(l*(anzahlzeilen+1)));
            final TranslateTransition translateTransition = new TranslateTransition(Duration.millis(300), spielstein);
            translateTransition.setToY(0);				//Runterfallen der Steine
            translateTransition.play();
            if(amZug=='x'){
                spielstein.setImage(image1);
                System.out.println((int)spielstein.getId().charAt(10)-48 + " " + spieler);
                
            }else{
                spielstein.setImage(image2);
                System.out.println((int)spielstein.getId().charAt(10)-48 + " " + spieler);
               
            }
        }
    }
    
    public StackPane getNodeByRowColumnIndex (final int row, final int column, GridPane spielfeld) {
        for (Node node : spielfeld.getChildren()) {
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return (StackPane) node;
            }
        } return null;
    }
    
    public ImageView getImageView (StackPane stack) {
        ObservableList<Node> list = stack.getChildren();
        return (ImageView)list.get(2);
    }
    
    
	/*@Override
	public void zugGespielt(int zug)
	{
		setSpielstein(plaetzeFreiInReihe[zug], zug);
        plaetzeFreiInReihe[zug]--;
        
	}*/
	@Override
	public void zugGespielt(int zug, char amZug)
	{
		setSpielstein(plaetzeFreiInReihe[zug], zug, amZug);
        plaetzeFreiInReihe[zug]--;
		// hier kommt die Methode, die bei Spielende aufgerufen werden soll, sieger enthält 'x' oder 'o' als char
	}
	
	public void addNameListener(NameListener toAdd) {
		NameListeners.add(toAdd);
	}
	
	public static void fireNames (String name1, String name2) {
		for (NameListener name: NameListeners) {
			name.startGame(name1, name2);
		}
	}
	
	public void addParamListener(ParamListener toAdd){
		listeners.add(toAdd);
	}
	
	
	public static void fireStartEvent(int Zugzeit, String Schnittstelle, String Kontaktpfad, char spielerKennung, 
									  String AppID, String AppKey, String AppSecret){
		for (ParamListener pl : listeners){
			pl.startParameterAuswerten(Zugzeit, Schnittstelle, Kontaktpfad, spielerKennung, AppID, AppKey, AppSecret);
		}
	}
	
	public String getFileString(){
		return fileString;
	}
	
	@Override
	public void zugGespielt(char sieger) {
		// TODO Auto-generated method stub
		
	}
 
}

               
            