import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

class ColonizeDialog extends JDialog implements ActionListener, LineParser {
  /** CONSTANTS **/
  public static final String OKAY = "Okay";
  public static final String CANCEL = "Cancel";
  public static final int START_SCRIPT = 1;
  public static final int COLONIZE = 2;
  public static final int GET_HOPS_TO = 3;
  public static final int GET_HOPS_FROM = 4;
  public static final int GET_FUEL = 5;
  public static final int UNLOAD_ALL = 6;
  public static final int WARP_TO_COLOS = 7;
  public static final int WARP_HOME = 8;
  public static final int DUMP_COLOS = 9;

  public static final String FUEL_ORE = "Fuel Ore";
  public static final String ORGANICS = "Organics";
  public static final String EQUIPMENT = "Equipment";
  public static final String COLONISTS = "Colonists";
  public static final int NUM_FUEL_ORE = 1;
  public static final int NUM_ORGANICS = 2;
  public static final int NUM_EQUIPMENT = 3;


  /** INSTANCE VARIABLES **/
  JButton butOkay, butCancel;
  JCheckBox jcbTwarp;
  JTextField txtFrom, txtTo, txtPlanetFrom, txtPlanetTo, txtPlanetFuel, txtTimes;
  JComboBox jcbPlanetTo, jcbPlanetFuel;
  JRadioButton rbFuelOre, rbOrganics, rbEquipment, rbColonists; //rbTWYes, rbTWNo, 
  JRadioButton rbJobFuelOre, rbJobOrganics, rbJobEquipment;
  ButtonGroup bgItem, bgJobs;  //bgTwarp
  ScriptWindow theScriptWindow;
  TWA theTWA;

  String strSectorFrom, strSectorTo;
  String strPlanetFrom, strPlanetTo, strPlanetFuel;
  String strColoJob;
  int iTimes;
  int iTotalTimes;
  int iState;

  int iFuelToFromRequired;
  int iFuelFromToRequired;
  int iTotalFuelRequired;

  int iColosMoved;
  int iTotalColosMoved;

  boolean isFinished;
  boolean moveForFuel;
  

  /**
    * ColonizeDialog
    **/
  public ColonizeDialog(Frame owner, TWA theTWA) {
    super(owner);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    this.theTWA = theTWA;
    makeWindow();

  }

  /**
    * makeWindow
    **/
  private void makeWindow() {
    JPanel pnlMiddle;
    JPanel pnlItems;
    JPanel pnlJobs;
    JPanel pnlResources;
    JPanel pnlButtons;
    JPanel pnlTemp, pnlLeft, pnlRight;

    this.getContentPane().setLayout(new BorderLayout());

    pnlMiddle = new JPanel(new BorderLayout());
      pnlLeft = new JPanel(new GridLayout(6, 1));
      pnlRight = new JPanel(new GridLayout(6, 1));


      pnlTemp = new JPanel();
      pnlTemp.add(new JLabel("Transport From Sector:   ", SwingConstants.LEFT));
      pnlLeft.add(pnlTemp);

      pnlTemp = new JPanel();
      txtFrom = new JTextField(5);
      txtFrom.setText(theTWA.getBBS().getGameData().getSectorFrom());
      pnlTemp.add(txtFrom);
      pnlRight.add(pnlTemp);

      pnlTemp = new JPanel();
      pnlTemp.setAlignmentX(LEFT_ALIGNMENT);
      pnlTemp.add(new JLabel("Planet:", SwingConstants.LEFT));
      pnlLeft.add(pnlTemp);

      pnlTemp = new JPanel();
      txtPlanetFrom = new JTextField(5);
      txtPlanetFrom.setText(theTWA.getBBS().getGameData().getPlanetFrom());
      pnlTemp.add(txtPlanetFrom);
      pnlRight.add(pnlTemp);


      pnlTemp = new JPanel();
      pnlTemp.add(new JLabel("Transport To Sector:   ", SwingConstants.LEFT));
      pnlLeft.add(pnlTemp);

      pnlTemp = new JPanel();
      txtTo = new JTextField(5);
      pnlTemp.add(txtTo);
      txtTo.setText(theTWA.getCurrentSector().getNumber() + "");

      pnlRight.add(pnlTemp);

      pnlTemp = new JPanel();
      pnlTemp.add(new JLabel("Planet:", SwingConstants.LEFT));
      pnlLeft.add(pnlTemp);

      pnlTemp = new JPanel();
      txtPlanetTo = new JTextField(5);
      txtPlanetTo.setText(theTWA.getBBS().getGameData().getPlanetTo());
      pnlTemp.add(txtPlanetTo);
      pnlRight.add(pnlTemp);

      pnlTemp = new JPanel();
      pnlTemp.add(new JLabel("Planet for fuel:", SwingConstants.LEFT));
      pnlLeft.add(pnlTemp);

      pnlTemp = new JPanel();
      txtPlanetFuel = new JTextField(5);
      txtPlanetFuel.setText(theTWA.getBBS().getGameData().getPlanetFuel());
      pnlTemp.add(txtPlanetFuel);
      pnlRight.add(pnlTemp);


      pnlTemp = new JPanel();
      pnlTemp.add(new JLabel("Times:", SwingConstants.LEFT));
      pnlLeft.add(pnlTemp);

      pnlTemp = new JPanel();
      txtTimes = new JTextField(5);
      txtTimes.setText(theTWA.getBBS().getGameData().getPlanetTimesToLoop() + "");
      pnlTemp.add(txtTimes);
      pnlRight.add(pnlTemp);


      pnlMiddle.add(pnlLeft, BorderLayout.CENTER);
      pnlMiddle.add(pnlRight, BorderLayout.EAST);


    pnlResources = new JPanel(new GridLayout(5, 2));
      bgItem = new ButtonGroup();


        rbColonists = new JRadioButton("Colonists");
        rbColonists.setMnemonic('c');

        rbFuelOre = new JRadioButton("Fuel Ore");
        rbFuelOre.setMnemonic('f');

        rbOrganics = new JRadioButton("Organics");
        rbOrganics.setMnemonic('o');

        rbEquipment = new JRadioButton("Equipment");
        rbEquipment.setMnemonic('e');


        bgItem.add(rbColonists);
        bgItem.add(rbFuelOre);
        bgItem.add(rbOrganics);
        bgItem.add(rbEquipment);


        jcbTwarp = new JCheckBox("Use TransWarp");

      pnlResources.add(new JLabel("Resource"));
      pnlResources.add(new JLabel("Use Transwarp"));
      pnlResources.add(rbColonists);
      pnlResources.add(jcbTwarp);
      pnlResources.add(rbFuelOre);
      pnlResources.add(new JPanel());
      pnlResources.add(rbOrganics);
      pnlResources.add(new JPanel());
      pnlResources.add(rbEquipment);


    pnlJobs = new JPanel(new GridLayout(4, 1));
      bgJobs = new ButtonGroup();

        rbJobFuelOre = new JRadioButton(FUEL_ORE);
        rbJobFuelOre.setMnemonic('f');

        rbJobOrganics = new JRadioButton(ORGANICS);
        rbJobOrganics.setMnemonic('o');

        rbJobEquipment = new JRadioButton(EQUIPMENT);
        rbJobEquipment.setMnemonic('e');


        bgJobs.add(rbJobFuelOre);
        bgJobs.add(rbJobOrganics);
        bgJobs.add(rbJobEquipment);

      pnlJobs.add(new JLabel("Place Colonists in"));
      pnlJobs.add(rbJobFuelOre);
      pnlJobs.add(rbJobOrganics);
      pnlJobs.add(rbJobEquipment);

    pnlItems = new JPanel();
    pnlItems.add(pnlResources);
    pnlItems.add(pnlJobs);


    pnlButtons = new JPanel(new GridLayout(1, 2));

      pnlButtons.add(new JPanel());

      butOkay = new JButton(OKAY);
      butOkay.addActionListener(this);
      pnlButtons.add(butOkay);

      butCancel = new JButton(CANCEL);
      butCancel.addActionListener(this);
      pnlButtons.add(butCancel);





    this.getContentPane().add(pnlMiddle, BorderLayout.NORTH);
    this.getContentPane().add(pnlItems, BorderLayout.CENTER);
    this.getContentPane().add(pnlButtons, BorderLayout.SOUTH);
    this.setTitle("Colonizing");

    this.pack();
    setLocation(((TWA.SCREEN.width - getWidth()) / 2), ((TWA.SCREEN.height - getHeight()) / 2));
    this.show();

  }


  /**
    * parseString
    **/
  public void parseString(String theString) {
    String strTemp;

    if (iState == START_SCRIPT) {
        theTWA.getTelnet().getSender().sendData("cf" + strSectorFrom + "\r");
        theTWA.getTelnet().getSender().sendData(strSectorTo + "\r");
        iState = GET_HOPS_FROM;
    } else if (iState == GET_HOPS_FROM) {
      if (  (theString.indexOf("The shortest path") > -1) &&
            (theString.indexOf("from sector " + strSectorFrom + " to sector " + strSectorTo + " is:") > -1)  ) {
        iFuelFromToRequired = 3 * (Parser.toInteger(theString.substring(19, theString.indexOf(' ', 19)), 0) );
        iTotalFuelRequired = iFuelFromToRequired;
        theScriptWindow.addText("Fuel required for trip from " + strSectorFrom + " to " + strSectorTo + ": " + iFuelFromToRequired + "\n");
        iState = GET_HOPS_TO;

        theTWA.getTelnet().getSender().sendData("f" + strSectorTo + "\r");
        theTWA.getTelnet().getSender().sendData(strSectorFrom + "\r");


      }
    } else if (iState == GET_HOPS_TO) {
      if (  (theString.indexOf("The shortest path") > -1) &&
            (theString.indexOf("from sector " + strSectorTo + " to sector " + strSectorFrom + " is:") > -1)
         ) {
        iFuelToFromRequired = 3 * (Parser.toInteger(theString.substring(19, theString.indexOf(' ', 19)), 0 ));
        theScriptWindow.addText("Fuel required for trip from " + strSectorTo + " to " + strSectorFrom + ": " + iFuelToFromRequired + "\n");
        iTotalFuelRequired += iFuelToFromRequired;
        theScriptWindow.addText("Total fuel required for trip: " + iTotalFuelRequired + "\n");

        theTWA.getTelnet().getSender().sendData("q");

        iState = UNLOAD_ALL;
      }
    } else if (iState == UNLOAD_ALL) {
      if (  (theString.indexOf("Command") > -1) &&
            (theString.indexOf("[" + strSectorTo + "]") > -1)
         ) {
        theTWA.getTelnet().getSender().sendData("l");
      } else if (  (theString.indexOf("Land on which planet <Q to abort> ?") > -1) ) {
        theTWA.getTelnet().getSender().sendData(strPlanetFuel + "\r");
      } else if (  (theString.indexOf("Planet command (?=help) [D]") > -1) ) {

        if (theTWA.getBBS().getGameData().getCurrentShip().getOrganics() > 0) {
          theTWA.getTelnet().getSender().sendData("tnl2\r");
          theTWA.getBBS().getGameData().getCurrentShip().setOrganics(0);
        } else if (theTWA.getBBS().getGameData().getCurrentShip().getEquipment() > 0) {
          theTWA.getTelnet().getSender().sendData("tnl3\r");
          theTWA.getBBS().getGameData().getCurrentShip().setEquipment(0);
        } else if (theTWA.getBBS().getGameData().getCurrentShip().getColonists() > 0) {
          theTWA.getTelnet().getSender().sendData("snl" + strColoJob + "\r");
          theTWA.getBBS().getGameData().getCurrentShip().setColonists(0);
        } else if (theTWA.getBBS().getGameData().getCurrentShip().getFuelOre() > iTotalFuelRequired) {
          theTWA.getTelnet().getSender().sendData("tnl1" + (theTWA.getBBS().getGameData().getCurrentShip().getFuelOre() - iTotalFuelRequired) + "\rq");
          theTWA.getBBS().getGameData().getCurrentShip().setFuelOre(iTotalFuelRequired);
          iState = WARP_TO_COLOS;
        } else {
          iState = GET_FUEL;
          theTWA.getTelnet().getSender().sendData("\r");
        }

      }
    } else if (iState == GET_FUEL) {

      if (  (theString.indexOf("Planet command (?=help) [D]") > -1) ) {
        theTWA.getTelnet().getSender().sendData("tnt");
      } else if (  (theString.indexOf("Which product are you taking?") > -1)) {
        theTWA.getTelnet().getSender().sendData("1");
      } else if (  (theString.indexOf("How many holds of Fuel Ore do you want to take") > -1)) {
        if (iTotalFuelRequired > theTWA.getBBS().getGameData().getCurrentShip().getFuelOre()) {
          theTWA.getTelnet().getSender().sendData((iTotalFuelRequired - theTWA.getBBS().getGameData().getCurrentShip().getFuelOre()) + "\rq");
          theTWA.getBBS().getGameData().getCurrentShip().setFuelOre(iTotalFuelRequired);
        } else {
          theTWA.getTelnet().getSender().sendData("0\rq");  //needs to be fixed.. can warp with too much fuel
        }
        iState = WARP_TO_COLOS;
      }
    } else if (iState == WARP_TO_COLOS) {
      if (  (theString.indexOf("Command") > -1) &&
            (theString.indexOf("[" + strSectorTo + "]") > -1)
         ) {
        theTWA.getTelnet().getSender().sendData(strSectorFrom + "\r");
      } else if (  (theString.indexOf("Do you want to engage the TransWarp drive?") > -1)) {
        theTWA.getBBS().getGameData().getCurrentShip().burnFuel(iFuelToFromRequired);
        theScriptWindow.addText("Used " + iFuelToFromRequired + " fuel. " + theTWA.getBBS().getGameData().getCurrentShip().getFuelOre() + " remain.\n");
        theTWA.getTelnet().getSender().sendData("y");
      } else if (  (theString.indexOf("Command") > -1) &&
            (theString.indexOf("[" + strSectorFrom + "]") > -1)
         ) {
        theTWA.getTelnet().getSender().sendData("l");
      } else if (  (theString.indexOf("Do you wish to (L)eave or (T)ake Colonists? [T]") > -1)) {
        theTWA.getTelnet().getSender().sendData("t");
      } else if (  (theString.indexOf("How many groups of Colonists do you want to take") > -1)) {
        theTWA.getTelnet().getSender().sendData(theString.substring(50, theString.indexOf(']', 50)) + "\r");
        theTWA.getBBS().getGameData().getCurrentShip().setColonists(Parser.toInteger(theString.substring(50, theString.indexOf(']', 50)), 0));

        iState = WARP_HOME;
      }
    } else if (iState == WARP_HOME) {
      if (  (theString.indexOf("Command") > -1) &&
            (theString.indexOf("[" + strSectorFrom + "]") > -1)
         ) {
        theTWA.getTelnet().getSender().sendData(strSectorTo + "\r");
      } else if (  (theString.indexOf("Do you want to engage the TransWarp drive?") > -1)) {
        theTWA.getBBS().getGameData().getCurrentShip().burnFuel(iFuelFromToRequired);
        theScriptWindow.addText("Used " + iFuelFromToRequired + " fuel. " + theTWA.getBBS().getGameData().getCurrentShip().getFuelOre() + " remain.\n");
        theTWA.getTelnet().getSender().sendData("y");
      } else if (  (theString.indexOf("Command") > -1) &&
            (theString.indexOf("[" + strSectorTo + "]") > -1)
         ) {
        theTWA.getTelnet().getSender().sendData("l");
      } else if (  (theString.indexOf("Land on which planet <Q to abort> ?") > -1)) {
        theTWA.getTelnet().getSender().sendData(strPlanetTo + "\r");
      } else if (  (theString.indexOf("Planet command (?=help) [D]") > -1) ) {

        if (theTWA.getBBS().getGameData().getCurrentShip().getColonists() > 0) {
          theTWA.getTelnet().getSender().sendData("snl" + strColoJob + "\r");
          theScriptWindow.addText(theTWA.getBBS().getGameData().getCurrentShip().getColonists() + " colonists unloaded\n");
          iTotalColosMoved += theTWA.getBBS().getGameData().getCurrentShip().getColonists();
          theTWA.getBBS().getGameData().getCurrentShip().setColonists(0);

          if (moveForFuel) {
            theTWA.getTelnet().getSender().sendData("q");
          } else {
            iTimes--;
            if (iTimes > 0) {
              iState = GET_FUEL;
            } else {
              quitParsing();
            }
          }
        }

      } else if (  (theString.indexOf("Blasting off from") > -1) ) {

        iTimes--;
        if (iTimes > 0) {
          iState = UNLOAD_ALL;
        } else {
          quitParsing();
        }

      }


    } else if (iState == DUMP_COLOS) {
    }


    if (  (theString.indexOf("TransWarp Drive shutting down.") > -1)) {
      JOptionPane.showMessageDialog(this, "Unable to transwarp", "Error", JOptionPane.WARNING_MESSAGE);
      quitParsing();
    } else if (  (theString.indexOf("Invalid registry number, landing aborted.") > -1)) {
      JOptionPane.showMessageDialog(this, "Planet number not found", "Error", JOptionPane.WARNING_MESSAGE);
      quitParsing();
    }
  }



  /**
    * quitParsing
    **/
  public void quitParsing() {
    theTWA.getParser().getParsers().removeElement(this); //start getting lines from the parser
    theScriptWindow.addText("Total Colonists Moved: " + iTotalColosMoved + "\n");
    theTWA.getTelnet().getSender().sendData("i");
    theTWA.getTelnet().getSender().setUserInputEnabled(true);
    theTWA.getScreen().repaint();
  }




  /**
    * actionPerformed
    **/
  public void actionPerformed(ActionEvent e) {
    String strCommand;

    strCommand = e.getActionCommand();

    if (strCommand.equals(OKAY)) {
      strSectorFrom = txtFrom.getText();
      strSectorTo = txtTo.getText();
      strPlanetFrom = txtPlanetFrom.getText();
      strPlanetTo = txtPlanetTo.getText();
      strPlanetFuel = txtPlanetFuel.getText();

      if (rbJobFuelOre.isSelected()) {
        strColoJob = NUM_FUEL_ORE + "";
//        System.out.println("FO Selected");
      } else if (rbJobOrganics.isSelected()) {
        strColoJob = NUM_ORGANICS + "";
//        System.out.println("O Selected");

      } else if (rbJobEquipment.isSelected()) {
        strColoJob = NUM_EQUIPMENT + "";
//        System.out.println("E Selected");
      } else {
        strColoJob = NUM_FUEL_ORE + "";
//        System.out.println("Nothing Selected");
      }

      iFuelToFromRequired = 0;
      iFuelFromToRequired = 0;
      iTotalFuelRequired = 0;
      isFinished = false;

      if (strPlanetTo.equals(strPlanetFuel)) {
        moveForFuel = false;
      } else {
        moveForFuel = true;
      }

      try {
        iTimes = Parser.toInteger(txtTimes.getText(), 0);
        iTotalTimes = iTimes;

        iState = START_SCRIPT;
        theTWA.getParser().getParsers().addElement(this); //start getting lines from the parser
        setVisible(false);
        dispose();

        theScriptWindow = new ScriptWindow(theTWA.getFrame());
        theScriptWindow.addText("Getting colonists from sector " + strSectorFrom + " planet " + strPlanetFrom + "\n");
        theScriptWindow.addText("Moving colonists to sector " + strSectorTo + " planet " + strPlanetTo + "\n");
        if (moveForFuel) {
          theScriptWindow.addText("Using planet " + strSectorTo + " for fuel\n");
        } else {
          theScriptWindow.addText("Also using planet " + strPlanetFuel + " for fuel\n");
        }

        if (strColoJob.equals(NUM_FUEL_ORE + "")) {
          theScriptWindow.addText("Placing colonists in " + FUEL_ORE + " production\n");
        } else if (strColoJob.equals(NUM_ORGANICS + "")) {
          theScriptWindow.addText("Placing colonists in " + ORGANICS + " production\n");
        } else if (strColoJob.equals(NUM_EQUIPMENT + "")) {
          theScriptWindow.addText("Placing colonists in " + EQUIPMENT + " production\n");
        } else {
          theScriptWindow.addText("Error: colonist job not specified\n");
        }


        theTWA.getBBS().getGameData().setPlanetTimesToLoop(iTotalTimes);
        theTWA.getBBS().getGameData().setSectorFrom(strSectorFrom);
        theTWA.getBBS().getGameData().setPlanetFrom(strPlanetFrom);
        theTWA.getBBS().getGameData().setPlanetTo(strPlanetTo);
        theTWA.getBBS().getGameData().setPlanetFuel(strPlanetFuel);

//        theTWA.getTelnet().getSender().setUserInputEnabled(false);
        theTWA.getTelnet().getSender().sendData("i");

      } catch (NumberFormatException e2) {
        JOptionPane.showMessageDialog(this, "Number of times must be an integer value", "Error", JOptionPane.WARNING_MESSAGE);
        isFinished = true;
      }

    } else if (strCommand.equals(CANCEL)) {
      setVisible(false);
      dispose();
    }

  }

  /**
    * main
    **/
  public static void main(String argv[]) {
    JFrame myFrame = new JFrame();
    myFrame.setBounds(50, 50, 50, 50);
    myFrame.setVisible(true);
    new ColonizeDialog(myFrame, null);

  }

    


}
