import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.Random;
/**
 *  This class is the main class of the "World of Zuul: The Lesser Evil" application. 
 *  "World of Zuul" is a very simple, text based adventure game.
 * 
 *  To play this game, call the "main" method of the Game class, or
 *  create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms,the charachters, the items, the quests, the gamePlay instances,
 *  the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes and Luca-Dorin Anton
 * @version 2017.12.08
 */

public class Game 
{
    private Player player;
    private Parser parser;
    private ArrayList<Room> rooms;
    private ArrayList<Entity> characters;
    private ArrayList<Quest> quests;
    private MiniMap miniMap;
    private int miniMapSize;
    private int difficulty;
    private boolean gameFinished;
    private Random random;
    /**
     * Create the game and initialise its internal map.
     * Prepare the MiniMap.
     */
    public Game() 
    {
        difficulty = 0;
        rooms = new ArrayList<>();
        characters = new ArrayList<>();
        quests = new ArrayList<>();
        createRooms();
        player = new Player(80, 100);
        player.addToPath(rooms.get(0));
        rooms.get(0).setVisited();
        parser = new Parser();
        miniMap = new MiniMap(this);
        gameFinished = false;
        random = new Random();
    }
    
    /**
     * Main method in order to be able
     * to run the game
     * @param args Arguments (Array of String)
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }

    /**
     * Create all the rooms and link their exits together.
     * Create the items, charachters and quests.
     * Store them in their respective places.
     */
    private void createRooms()
    {
        Room aircraft, village, plains, marshlands, worldTop, dragonsLair, imperialBase, outback, scytheCamp, hospital, passage, pits, outpost; 

        // create the rooms
        aircraft = new Room("Crashed Aircraft", "A wreck of contorted metals which once soared high in the sky.\n" + "How did you manage to survive the crash?");

        village = new Room("Deserted Village","What was once a hub of the local community, now just a shadow of its former self.\n" +
            "All homes are deserted. Windows broken, roads overgrown.");

        plains = new Room("Plains of Silence" , "Here you find dry plains streching in all directions for what seems to be forever. \n " +
            "Sprinkled across the huge expanse of nothingness, you see an occasional oak tree\n reaching helplessly towards the sky.\n " + 
            "Right before you, you see the huge gate blocking your way\n to what the locals refer to as \"The Grand Passage\". Maybe salvation lies beyond?");

        marshlands = new Room("Marshlands" , "You find yourself in the middle of a thick marsh. This place seems to be a sanctuary of nature.\n " +
            "An ancient ecosystem beyond the reach of nefarious humans.");

        worldTop = new Room("Top of the World", "True to it's name, this mountaintop oversees the entire land.\n" + 
            "In the distance, you make out the remains of your crashed craft. Still smouldering.");

        dragonsLair = new Room("Dragon's Lair" , "This menacing place seems to be a hive of the creatures \n" + 
            "which have been obstucting your path since your unexpected arrival in theese lands. ");

        imperialBase = new Room("Forward Operations Base of the 5th Imperial Brigade" , "An impressive sight: in the middle of the wilderness a huge military establishment.\n" +
            "Soldiers patrolling, weapon stashes, all-terain vehicles.\nAfter a quick check, the guards let you in.");

        outback = new Room("Outback" , "Here you find yourself in the middle of a desert environment: sand dunes cover the horizont.\n" +
            "The normal flow of Time seems to not exist in this forgotten corner of this wretched world...");

        scytheCamp = new Room("\"The Scythe\" Encampment" , "Hidden beneathe the thick foliage of a thick, overgrown jungle, you spot signs of activity. \n" + 
            "You decide to investigate further.");

        hospital = new Room("Field Hospital" , "In a badly light cave, you spot what seems to be one of the last bastions of Humanity\n in this lands: a medical establishment.\n" + 
            "A few nurses try to alleviate the pain of the pacients who are suffering the most,\nallthough you can clearly read their facial expressions and understand\n" + 
            "what they are thinking: Most of them are not going to make it.");

        passage = new Room("The Grand Passage" , "Walls of Bedrock streching up to Infinity on either side,\nyou find yourself in the famous Grand Passage. /n" +
            "Ahead of you lie the Pits of Despair. Only the thought of this makes you shiver. You prepare yourself for what is to come.");

        pits = new Room("The Pits of Despair" , "Here you are, in the pits of despair, the heart of the epidemic which grips the area. \n" + 
            "You feel like you're being watched, although you killed all monsters you could see.\n" + " You'd better get out from here as soon as you can.");
        outpost = new Room("The Federation Outpost" , "Here it is! The Federation Outpost!\n" + "Here you will surely find the end of your nightmare!");
        
        
        //Set Exits
        aircraft.setExit("north", village, null);
        village.setExit("north" , plains, null);
        village.setExit("south" , aircraft, null);

        plains.setExit("north" , passage, "Grand Pass");
        plains.setExit("south" , village, null);
        plains.setExit("west", marshlands, null);
        plains.setExit("east", outback, null);

        marshlands.setExit("east", plains, null);
        marshlands.setExit("north", imperialBase, null);
        marshlands.setExit("south", worldTop, null);

        worldTop.setExit("north", marshlands, null);
        worldTop.setExit("west", dragonsLair, "Emperor's Seal");

        dragonsLair.setExit("east", worldTop, "Emperor's Seal");

        imperialBase.setExit("south", marshlands, null);

        outback.setExit("west", plains, null);
        outback.setExit("east", hospital, null);
        outback.setExit("south", scytheCamp, null);

        hospital.setExit("west", outback, null);

        scytheCamp.setExit("north", outback, null);

        passage.setExit("south", plains, "Grand Pass");
        passage.setExit("north", pits, null);

        pits.setExit("south", passage, null);
        pits.setExit("east", outpost, null);

        outpost.setExit("west", pits, null);
        
        //Set items in rooms
        aircraft.addToInventory(new Item("Mission Briefing" , "This document has been scorched by the crash... \n" + 
                "You can still make out some fragments: \n" + 
                "... preserve the Federation interests... \n" + 
                "... use upmost precaution ...\n" +
                "... Federation outpost at coordinates ..." , true, 2));
        aircraft.addToInventory(new HpBoostItem("Pack of dried meat" ,"This item restores 5 HP when used" , true, 3, 5));
        aircraft.addToInventory(new Item("Shell" , "Your standard 12-gauge Shotgun Shell", true, 1)); 

        village.addToInventory(new Item("Shell" , "Your standard 12-gauge Shotgun Shell", true, 1));
        village.addToInventory(new Item("Shell" , "Your standard 12-gauge Shotgun Shell", true, 1));
        village.addToInventory(new HpBoostItem("Bandage" , "A basic piece of medical equipment.\n Restores 8 HP when used." ,true, 4, 8));
        village.addToInventory(new Item("Shotgun" , "A standard 12-gauge Browning pump-action shotgun. \n" + 
                "Should be helpful for clearing out theese creatures\nwhen push comes to shove.", true, 10));
        Teleporter waygate = new Teleporter("Waygate" , "This Alien device seems to be,\n" + 
                "pulsating with energy. It looks like\n" +
                "it is inviting you." , false);

        waygate.addToRooms(aircraft);
        waygate.addToRooms(worldTop);
        waygate.addToRooms(hospital);

        plains.addToInventory(waygate);
        plains.addToInventory(new HpBoostItem("Bread" , "A great source of nutrition\nRestores 6HP when used", true, 3, 6));

        marshlands.addToInventory(new HpBoostItem("Bandage" , "A basic piece of medical equipment.\n Restores 8 HP when used." ,true, 4, 8));

        worldTop.addToInventory(new Item("Shell" , "Your standard 12-gauge Shotgun Shell", true, 1));

        dragonsLair.addToInventory(new Item("Shell" , "Your standard 12-gauge Shotgun Shell", true, 1));
        dragonsLair.addToInventory(new Item("Shell" , "Your standard 12-gauge Shotgun Shell", true, 1));
        dragonsLair.addToInventory(new Item("Shell" , "Your standard 12-gauge Shotgun Shell", true, 1));
        dragonsLair.addToInventory(new HpBoostItem("Bandage" , "A basic piece of medical equipment.\n Restores 8 HP when used." ,true, 4, 8));
        dragonsLair.addToInventory(new HpBoostItem("Bandage" , "A basic piece of medical equipment.\n Restores 8 HP when used." ,true, 4, 8));        
        dragonsLair.addToInventory(new HpBoostItem("Bread" , "A great source of nutrition\nRestores 6HP when used", true, 3, 6));
        dragonsLair.addToInventory(new HpBoostItem("Bread" , "A great source of nutrition\nRestores 6HP when used", true, 3, 6));

        imperialBase.addToInventory(new HpBoostItem("Bandage" , "A basic piece of medical equipment.\n Restores 8 HP when used." ,true, 4, 8));
        imperialBase.addToInventory(new HpBoostItem("Bandage" , "A basic piece of medical equipment.\n Restores 8 HP when used." ,true, 4, 8));
        imperialBase.addToInventory(new HpBoostItem("Bread" , "A great source of nutrition\nRestores 6HP when used", true, 3, 6));

        outback.addToInventory(new HpBoostItem("Bread" , "A great source of nutrition\nRestores 6HP when used", true, 3, 6));

        hospital.addToInventory(new HpBoostItem("Bread" , "A great source of nutrition\nRestores 6HP when used", true, 3, 6));
        hospital.addToInventory(new HpBoostItem("Bread" , "A great source of nutrition\nRestores 6HP when used", true, 3, 6));

        scytheCamp.addToInventory(new HpBoostItem("Bread" , "A great source of nutrition\nRestores 6HP when used", true, 3, 6));
        scytheCamp.addToInventory(new HpBoostItem("Bandage" , "A basic piece of medical equipment.\n Restores 8 HP when used." ,true, 4, 8));

        passage.addToInventory(new Item("Shell" , "Your standard 12-gauge Shotgun Shell", true, 1));
        passage.addToInventory(new Item("Shell" , "Your standard 12-gauge Shotgun Shell", true, 1));
        passage.addToInventory(new Item("Shell" , "Your standard 12-gauge Shotgun Shell", true, 1));
        passage.addToInventory(new HpBoostItem("Bread" , "A great source of nutrition\nRestores 6HP when used", true, 3, 6));
        passage.addToInventory(new HpBoostItem("Bread" , "A great source of nutrition\nRestores 6HP when used", true, 3, 6));
        passage.addToInventory(new HpBoostItem("Bandage" , "A basic piece of medical equipment.\n Restores 8 HP when used." ,true, 4, 8));
        passage.addToInventory(new HpBoostItem("Bandage" , "A basic piece of medical equipment.\n Restores 8 HP when used." ,true, 4, 8));
        passage.addToInventory(new HpBoostItem("Bandage" , "A basic piece of medical equipment.\n Restores 8 HP when used." ,true, 4, 8));
        
        //Create charachters
        Entity siegfried, shak, gramps, lena, wolfson, jason, agent;
        siegfried = new Entity(false, imperialBase,"Siegfried");
        shak = new Entity(false, scytheCamp, "Shak Raz");
        gramps = new Entity(true, aircraft, "Gramps");
        lena = new Entity(false, hospital, "Lena");
        wolfson = new Entity(false, dragonsLair, "von Wolfson");
        jason = new Entity(true, village, "Jason Raz");
        agent = new Entity(false, outpost, "Federation Agent");

        gramps.addRoom(aircraft);
        gramps.addRoom(village);
        gramps.addRoom(plains);
        gramps.addRoom(marshlands);
        gramps.addRoom(outback);

        jason.addRoom(village);
        jason.addRoom(plains);
        jason.addRoom(outback);
        
        
        //Create quests
        quests.add(
            new Quest("Get Out", "Find a way out of this place!\nIf you can remember correctly,\nthere was a federation base up north...", true, null, null, null, 0, null, true)
        );

        quests.add(
            new Quest("A Long Lost Love" , "Help Siegfried out by getting him a message\nfrom his lover, Lena.\nShe has been seen at the field hospital...", true, null, 
                new MaxHpBoostItem("Imperial Leather Tunic" , "This rugged piece of millitary equipment\nshould provide you with some much needed protection...", true, 10, 12),
                new Item("Lena's Letter to Siegfried" , "It reads:\nBeloved S.,\nI hope that this worn hasn't torn appart\ntour heart like it has mine...", true, 1), 1, siegfried, false
            )
        );

        quests.add(
            new Quest("Son of the Empire" , "Siegfried has tasked you with an important mission:\nYou have to go to the \"Dragon's Lair\" and find out\n" +
                "if his right hand, Knight von Wolfson, is still alive...", true,
                new Item("Emperor's Seal", "This sigil bears the mark of the Emperor,\n the (unchallenged?) ruler of these lands.\n" +
                    "It grants passage to the most remote of areas.", true, 5), 
                new MaxHpBoostItem("Shield of Zuul", "An ancient artifact witch boosts the users defense.",true, 15, 20 ), 
                new Item("von Wolfson's Pendant" , "A precious stone enclosed in silver, engraved with the mark of the Emperor.\nGive this item to Siegfried to show him\nthat" +
                    "von Wolfson is still alive",true, 7),1, siegfried, false)
        );

        quests.add(
            new Quest("For the Glory of the Emperor!", "This might be it!\nHelp Siegfried by assassinating Shak Raz, the leader of \"The Scythe\",\nand you might finally be able to get through" +
                "\nto the Grand Pass!", true,
                new Item("Degenerate Beacon" ,"A contraption designed to attract\nmassive amounts of Degenerates to an area." , true, 10),
                new Item("Grand Pass", "A sophisticated device used for opening\nthe way to the Grand Passage", true, 4),
                null, 0, siegfried, true
            )
        );

        quests.add(
            new Quest("How's Jason?", "Shak Raz has asked you to track down his brother, Jason\nand find out if he has completed his mission...", true, 
                null,
                new MaxHpBoostItem("Camo-Trousers", "A pair of sturdy camo-trousers", true, 8, 11),
                new Item("Jason's report", "The information has been scrambled\nusing some kind of encryption.\nPossibly SHA-256?", true, 3),
                1, shak, false
            )
        );

        quests.add(
            new Quest ("Intelligence Service" , "This time, Shak Raz has asked you\nto extort some information from Siegfried\nabout his forces...", true,
                null,
                new MaxHpBoostItem("Bulletproof Vest" , "An extroardinarly light self-defense item" , true, 3, 12 ),
                new Item("Intelligence Report" , "You quickly drafted this from Siegfrieds speech.\n...few thousand men...\n...a couple hundred armoured vehicles...", true, 2),
                1, shak, false
            )
        );

        quests.add(
            new Quest("The Price of Freedom" ,"This may be it!\nHelp Shak take down Siegfried and his Imperials\nby planting an explosive device in their base,\nand you might finally be able to get through" +
                "\nthe Grand Pass!", true,
                new Item("Explosive Charge" ,"Equivalent to about 2 megatons of TNT.\nIt will definetly pack a punch...", true, 15),
                new Item("Grand Pass", "A sophisticated device used for opening\nthe way to the Grand Passage", true, 4),
                null, 0 ,shak, true
            )
        );

        quests.add(
            new Quest("Get me my Bread!", "Get Gramps 5 loaves of Bread\nand he might give you a nice reward...", false,
                null,
                new HpBoostItem("Gramp's Pickles", "Gramp's deliciously nutritious pickles.\nWhat else is there to say?", true, 5, 200),
                new HpBoostItem("Bread" , "A great source of nutrition\nRestores 6HP when used", true, 3, 6),
                5, gramps, true
            )
        );

        quests.add(
            new Quest("Medical Supplies" , "Help Lena out by providing her with 5 bandages.\nShe might reward you for your feat!", false,
                null,
                new HpBoostItem("Lena's AntiBiotics Concoction" , "Lena proved to be a skilled biologist.\nThis concoction will surley make you feel better!", true , 5, 200),
                new HpBoostItem("Bandage" , "A basic piece of medical equipment.\n Restores 8 HP when used." ,true, 4, 8),
                5, lena, true
            )
        );
        //Start the main story-wrapper Quest
        quests.get(0).startQuest();
        
        //Store charachters
        characters.add(siegfried);
        characters.add(shak);
        characters.add(gramps);
        characters.add(lena);
        characters.add(wolfson);
        characters.add(jason);
        characters.add(agent);
        
        //Store rooms
        rooms.add(aircraft);
        rooms.add(village);
        rooms.add(plains);
        rooms.add(marshlands);
        rooms.add(worldTop);
        rooms.add(dragonsLair);
        rooms.add(imperialBase);
        rooms.add(outback);
        rooms.add(hospital);
        rooms.add(scytheCamp);
        rooms.add(passage);
        rooms.add(pits);
        rooms.add(outpost);

    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {         
        //Prepare all reuirements for game start
        displayWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing \"World of Zuul: The Lesser Evil\".\nGood bye.");
    }

    /**
     * Print out the opening message for the player.
     * Get the difficulty from the player.
     * Initialize the gamePlay objects accordingly
     * Introduce the player to the story
     */
    private void displayWelcome()
    {
        System.out.print('\u000C');
        System.out.println("##################################################");

        System.out.println("      _____       _    _            _     _          __   ______            _       _____     ");                    
        System.out.println("     / / \\ \\     | |  | |          | |   | |        / _| |___  /           | |     / / \\ \\    ");            
        System.out.println("    / /| |\\ \\   | |  | | ___  _ __| | __| |   ___ | |_     / / _   _ _   _| |    / /| |\\ \\  ");
        System.out.println("   / / | | \\ \\   | |/\\| |/ _ \\| '__| |/ _` |  / _ \\|  _|   / / | | | | | | | |   / / | | \\ \\  ");
        System.out.println("  / /  | |  \\ \\  \\  /\\  / (_) | |  | | (_| | | (_) | |   ./ /__| |_| | |_| | |  / /  | |  \\ \\ ");
        System.out.println(" /_/   | |   \\_\\  \\/  \\/ \\___/|_|  |_|\\__,_|  \\___/|_|   \\_____/\\__,_|\\__,_|_| /_/   | |   \\_\\");
        System.out.println("       | |                                                                           | |      ");
        System.out.println("       |_|                                                                           |_|      ");
        System.out.println("               _____ _            _                               _____      _ _              ");
        System.out.println("              |_   _| |          | |                             |  ___|    (_) |             ");
        System.out.println("                | | | |__   ___  | |     ___  ___ ___  ___ _ __  | |____   ___| |             ");
        System.out.println("                | | | '_ \\ / _ \\ | |    / _ \\/ __/ __|/ _ \\ '__| |  __\\ \\ / / | |             ");
        System.out.println("                | | | | | |  __/ | |___|  __/\\__ \\__ \\  __/ |    | |___\\ V /| | |             ");
        System.out.println("                \\_/ |_| |_|\\___| \\_____/\\___||___/___/\\___|_|    \\____/ \\_/ |_|_|             ");
        System.out.println("                                                                                              ");
        System.out.println("                                                                                              ");
        parser.getRawInputString();
        System.out.print('\u000C');

        System.out.println("# Welcome to \"World of Zuul: The lesser Evil\" !");
        System.out.println("#");
        System.out.println("# Before we start, please select a difficulty: ");
        System.out.println("# Your options are: ");
        System.out.println("# (1) I'm too afraid to die");
        System.out.println("#     - Indicated for beginners. A reduced number of enemies will spawn.");
        System.out.println("#");
        System.out.println("# (2) Tough as nails");
        System.out.println("#     -This mode is perfect for those who want a challenge, whitout getting frustrated. ");
        System.out.println("#     -A decent amount of enemies will spawn.");
        System.out.println("#");
        System.out.println("# (3) Ultra-Violence");
        System.out.println("#     -Good Luck.");
        System.out.println("#");
        System.out.println("# Input the number for the difficulty level you want: ");
        
        //Make sure the difficulty has been set up correctly
        setUpDifficulty();

        String difficultyName = null;
        if(difficulty == 1) {
            difficultyName = "\"I'm too afraid to die.\""; 
        } else if(difficulty == 2){
            difficultyName = "\"Though as nails.\"";
        } else {
            difficultyName = "\"Ultra-Violence\"";
        }
        System.out.println("# You have selected difficulty " + difficulty + ": " + difficultyName);
        System.out.println("#");
        // Setup gamePlay Scenes with apropiate difficulty
        rooms.get(1).setGamePlay(new GamePlay(25, difficulty*3, difficulty*3, random.nextInt(5)));
        rooms.get(3).setGamePlay(new GamePlay(25, difficulty*4, difficulty*4, random.nextInt(5)));
        rooms.get(7).setGamePlay(new GamePlay(25, difficulty*4, difficulty*4, random.nextInt(5)));
        rooms.get(6).setGamePlay(new GamePlay(25, difficulty*5, difficulty*4, random.nextInt(5)));
        rooms.get(9).setGamePlay(new GamePlay(25, difficulty*5, difficulty*4, random.nextInt(5)));
        rooms.get(5).setGamePlay(new GamePlay(25, difficulty*8, difficulty*7, random.nextInt(5)));
        rooms.get(11).setGamePlay(new GamePlay(25, difficulty*12, difficulty*10, random.nextInt(5)));
        
        //Confirm game start
        System.out.println("# Do you want to start \"World of Zuul: The Lesser Evil\" ? (Y/N)");
        startTheGame();
        
        //Introduce the player to the story
        System.out.print('\u000C');
        System.out.println();
        System.out.println("*White Tower to Foxtrot-22, clearance for RTB. \nExecute procedure 42...*\n[Press ENTER to continue.]");
        parser.getRawInputString();
        System.out.println("*White Tower to Foxtrott-2, come in Foxtrott...*");
        parser.getRawInputString();
        System.out.println("> This is Foxtrott-2 to White Tower. We have lost power to main engines.\n" + 
            ">Control surfaces are compromised. We're going down...");
        parser.getRawInputString();
        System.out.println("*Roger that Foxtrot. Mission Control has been informed. Best of luck...*");
        parser.getRawInputString();
        System.out.println("> Thank you White Tower. Altitude: 20 feet. Impact in 3...");
        parser.getRawInputString();
        System.out.println("> 2...");
        parser.getRawInputString();
        System.out.println(">1...");
        parser.getRawInputString();
        System.out.println("...");
        parser.getRawInputString();
        System.out.println("*This is White Tower to Mission Control. Foxtrot is down...*");
        parser.getRawInputString();
        System.out.println("You wake up after the crash. First things first: check if you're in one piece:");
        parser.getRawInputString();
        System.out.println("Besides a few bruises, all seems fine. Not the case for the rest of the crew though.\n" +
            "They are all dead. You are alone. ");
        parser.getRawInputString();      
        System.out.println("Your head hurts. You can't remember much. What were you doing?\n" +
            "Let's start whit the basics. What was your name? [Input your name] ");

        setPlayerName();

        System.out.println(player.getName() + ", of course. And, what were you doing ?");
        parser.getRawInputString();
        System.out.println("Right, you were on a reconnaissance mission.\n");
        parser.getRawInputString();                    
        System.out.println("You need to get back to the federation Outpost. ");
        parser.getRawInputString();
        System.out.println("It's your only chance of surviving");
        System.out.println();
        parser.getRawInputString();
        System.out.print('\u000C');
        System.out.println("########################################################");
        printHelp();
        System.out.println();
        System.out.println(player.getCurrentRoom().getLongDescription());

    }

    /**
     * Ask the player to select a difficulty level 
     * out of three options.
     * Loop until he has chosen a valid difficulty
     */
    private void setUpDifficulty() {

        while(true) {
            String input = parser.getRawInputString();
            if(input.equals("1")) {
                difficulty = 1;//Easy
                break;
            } else if(input.equals("2")) {
                difficulty = 2;//Medium
                break;
            } else if(input.equals("3")) {
                difficulty = 3;//Hard
                break;
            } else {
                //Input not valid
                System.out.println("# Please input a valid difficulty level:");
            }
        }
    }
    
    
    /**
     * Ask the player to confirm game start
     * loop until he provides valid input
     */
    private void startTheGame() {
        while(true) {
            String input = parser.getRawInputString();
            if(input.equals("Y") || input.equals("y")) {
                break;
            } else if(input.equals("N") || input.equals("n")) {
                System.exit(1);
                break;
            } else {
                System.out.println("# Please input a valid value;");
            }
        }
    }
    
    /**
     * Get the player's name
     * if his input is too short, try again
     */
    private void setPlayerName() {
        while(true) {
            String input = parser.getRawInputString();
            if(input.length() < 4) {
                System.out.println("No, that couldn't be you name. What was it?");
            } else {
                player.setName(input);
                break;
            }
        }
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("Command not recognized.");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.toLowerCase().equals("help")) {
            printHelp();
        }
        else if (commandWord.toLowerCase().equals("go")) {
            goRoom(command);
        }
        else if (commandWord.toLowerCase().equals("quit")) {
            wantToQuit = quit(command);
        }
        else if(commandWord.toLowerCase().equals("map")) {
            processMiniMap(command);
        } 
        else if(commandWord.toLowerCase().equals("back")) {
            goBack();
        } 
        else if(commandWord.toLowerCase().equals("inspect")) {
            inspect(command);
        }
        else if(commandWord.toLowerCase().equals("take")) {
            take(command);
        }
        else if(commandWord.toLowerCase().equals("use")) {
            use(command);
        } 
        else if(commandWord.toLowerCase().equals("inventory")) {
            inventory(command);
        }
        else if (commandWord.toLowerCase().equals("interact")) {
            interact(command);
        } 
        else if(commandWord.toLowerCase().equals("quest")) {
            showQuestLog(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * A list of the player's command words gets printed
     */
    private void printHelp() 
    {
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to move in to a room. If there is an exit, enter the new
     * room, otherwise print an error message. If the room requires
     * a key, check for the key. After the move attempt,
     * update the miniMap
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = player.getCurrentRoom().getExit(direction);
        String key = player.getCurrentRoom().getKey(direction);

        if (nextRoom == null) {
            System.out.println("There is no path!");
        }
        else {
            if(key == null) {
                //No key is required
                movePlayer(nextRoom);
                //move the characters of the game
                moveCharacters();
            } else {
                //Key IS required
                if(player.checkForItemInInventory(key)) {
                    //Player has key, so move
                    movePlayer(nextRoom);
                    //move the characters of the game
                    moveCharacters();
                } else {
                    //Player dosen't have key
                    System.out.println("You don't have the required key! The required key is: " + key);
                }
            }
        }
        //Update the miniMap.
        doMiniMap();
    }

    /**
     * Move all the game characters
     * which can move
     */
    private void moveCharacters() {
        for(Entity entity: characters) {
            entity.move();
        }
    }
    
    /**
     * Move the player to a room.
     * If the room has a gamePlay object,
     * check if the level is finished.
     * If it isn't, do the level. 
     * @param room the room to which the player moves
     */
    private void movePlayer(Room room) {
        room.setVisited();
        player.addToPath(room);
        //Check if the room has Gameplay
        if(player.getCurrentRoom().hasGamePlay()) {
            //check if the level is done
            if(!player.getCurrentRoom().getGamePlayState()) {
                //If we got here, it isn't
                
                //Prepare the requirements for the level(shells, HP, etc.)
                int shellCount = 0;
                boolean hasShotgun = false;
                ArrayList<Item> items= player.getInventory();
                for(Item item : items) {
                    if(item.getName().equals("Shell")) {
                        shellCount++;
                    }
                    if(item.getName().equals("Shotgun")) {
                        hasShotgun = true;
                    }
                }
                player.getCurrentRoom().setDetails(player.getCurrentHealth(), player.getMaxHealth(), shellCount, hasShotgun, player.getName());
                //Start the game
                player.getCurrentRoom().startGame();
                //Wait for the game to finish
                while(true) {

                    if(player.getCurrentRoom().getGamePlayState()) {
                        break;
                    }
                }
                //Game is done. Stop it, then update player status
                player.getCurrentRoom().stopGame();
                player.setCurrentHealth(player.getCurrentRoom().getHealth());
                shellCount = shellCount - player.getCurrentRoom().getShells();
                int i = 0;
                Iterator<Item> it = items.iterator();
                while(it.hasNext()) {
                    if(i == shellCount) {
                        break;
                    }
                    Item  currentItem = it.next();
                    if(currentItem.getName().equals("Shell")) {
                        it.remove();
                        i++;
                    }
                }
            }
        }
        //print location info
        System.out.println("Location: " + player.getCurrentRoom().getName());
        System.out.println(player.getCurrentRoom().getLongDescription());
    }
    
    
    /**
     * "map" command has been issued
     * open/close the miniMap
     * @param command the issuing command
     */
    private void processMiniMap(Command command) {
        if(!command.hasSecondWord()) {
            //print command help
            System.out.println("#############################");
            System.out.println("Map command - show/hide your trusty map.");
            System.out.println("'map show' -- show your map");
            System.out.println("'map hide' -- hide your map");
            System.out.println("#############################");
        } else {
            if(command.getSecondWord().equals("show")) {
                if(miniMap.mapIsVisible()) {
                    System.out.println("You already have your map out!");
                    System.out.println();
                } else {
                    //Map is not visible, so start it
                    miniMap.startMiniMap();
                }
            } else if(command.getSecondWord().equals("hide")) {
                if(!miniMap.mapIsVisible()) {
                    System.out.println("You don't have your map out!");
                    System.out.println();
                } else {
                    //Map is visible, so hide it
                    miniMap.hideMap();
                }
            } else {
                //Print command help
                System.out.println("#############################");
                System.out.println("Map command - show/hide your trusty map.");
                System.out.println("'map show' -- show your map");
                System.out.println("'map hide' -- hide your map");
                System.out.println("#############################");
            }
        }
    }
    
    /**
     * Update the minimap
     */
    private void doMiniMap() {
        if(miniMap.mapIsVisible()) {
            miniMap.startMiniMap();
        } 
    }
    
    
    /**
     * create an ArrayList of all the characters
     * in a room
     * @param room the room we're looking within
     * @return an ArrayList containing all characters in that room
     */
    private ArrayList<Entity> getCharactersInRoom(Room room) {
        ArrayList<Entity> result = new ArrayList<>();
        for(Entity entity : characters) {
            if(entity.getCurrentRoom().equals(room)) {
                result.add(entity);
            }
        }
        return result;
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            miniMap.hideMap();
            return true;  // signal that we want to quit
        }
    }
    
    
    /**
     * Use the player's path stack
     * to retrace his steps to the 
     * last room. If the stack is empty
     * send an error message
     */
    private void goBack() {
        if(player.pathSize()>1) {
            player.removeFromPath();
            doMiniMap();
            System.out.println(player.getCurrentRoom().getLongDescription());
        } else {
            System.out.println("There is nowhere to go!");
        }
    }
    
    /**
     * "insect" was issued if it didn't have
     * a second word, inspect the player's surroundings
     * (items, characters, exits) and return the gathered info
     * If there is a second word, gather info about that specific item
     * @param command the issuing command
     */
    private void inspect(Command command) {
        if(command.hasSecondWord()) {
            int index = -1;
            //See if the second word is a valid index
            try {
                index = Integer.parseInt(command.getSecondWord());   
            } catch (NumberFormatException e){
                System.out.println("What did you want to inspect?");
            }

            if(index-1 >=0 && index-1 < player.getCurrentRoom().getInventory().size()) {
                //get info about a specific item
                Item item = player.getCurrentRoom().getItemAtIndex(index-1);
                System.out.println("Item name: " + item.getName());
                System.out.println(item.getDesc());
                System.out.println("Item can be picked up: " + item.getMovable());
                System.out.println("Item weight: " + item.getWeight());
                System.out.println();
            } else {
                //print command help
                System.out.println("#############################");
                System.out.println("Inspect command - discover items in your proximity.\nAnalyze those items.");
                System.out.println("'inspect' -- display a list of all the items you can find");
                System.out.println("'inspect # -- display information about the item at index #");
                System.out.println("#############################");
            }
        } else {
            //gather and print info about the player's location
            ArrayList<Item> roomInventory = player.getCurrentRoom().getInventory();
            System.out.println("Location: " + player.getCurrentRoom().getName());
            System.out.println(player.getCurrentRoom().getLongDescription());
            System.out.println();
            System.out.println("#############################");
            System.out.println("You have discovered: ");
            for(int i =0; i<roomInventory.size();i++) {
                System.out.println((i+1)+". " + roomInventory.get(i).getName()) ;  
            }
            System.out.println();
            System.out.println();
            System.out.println("Following charachters are here: ");
            ArrayList<Entity> charactersInRoom = getCharactersInRoom(player.getCurrentRoom());
            for(int i =0; i < charactersInRoom .size(); i++) {
                System.out.println((i+1) + ". " + charactersInRoom.get(i).getName());
            }
            System.out.println();
            System.out.println("Use \"inspect + <item number>\" to get a closer look at the specific item");
            System.out.println("Use \"interact + <character number>\" to interact with that caracter");
            System.out.println("#############################");
        }
    }
    
    
    /**
     * "take" was issued
     * try to put an item from the location
     * to the player's inevntory
     * @param command the issuing command
     */
    private void take(Command command) {
        if(command.hasSecondWord()) {
            int index = -1;
            //check if the second Workd is a valid index
            try {
                index = Integer.parseInt(command.getSecondWord());   
            } catch (NumberFormatException e){
            }

            if(index-1 >=0 && index-1 < player.getCurrentRoom().getInventory().size()) {
                //try to add the item to the player's inevntory
                Item item = player.getCurrentRoom().getItemAtIndex(index-1);
                if(item.getMovable()) {
                    if(player.addToInventory(item)) {
                        player.getCurrentRoom().removeItemFromInventory(index-1);
                        System.out.println("Item " + item.getName() + " successfully added to your inventory!");
                    } else {
                        System.out.println("You don't have enough space!");
                    }
                } else {
                    System.out.println("You can't move that item!"); 
                }
            } else {
                System.out.println("Please select a valid item!"); 
            }
        } else {
            //print command help
            System.out.println("#############################");
            System.out.println("Take command - take an item from the environment\n and put it in your inventory.");
            System.out.println("'take #' -- take the item at index #");
            System.out.println("#############################");
        }
    }
    
    
    /**
     * "inventory" was issued
     * manage the player's inventory:
     * list all items
     * show info about a specific item
     * use an item
     * drop an item
     * @param command the issuing command
     */
    private void inventory(Command command) {
        if(command.hasSecondWord()) {
            //gather info about the player's inventory
            ArrayList<Item> playerInventory= player.getInventory();
            int inventorySize = playerInventory.size();
            int maxCarryWeight = player.getMaxCarryWeight();
            int currentCarryWeight = player.getCurrentCarryWeight();
            if(command.getSecondWord().toLowerCase().equals("list")) {
                System.out.println("#############################");
                System.out.println("Items in inventory: " + inventorySize);
                System.out.println();
                System.out.println("Weight: " + currentCarryWeight + "/" + maxCarryWeight + " lbs.");
                System.out.println();
                System.out.println("Inventory: ");
                System.out.println("#############################");
                int scrollCount = 0;
                for(int i = 0; i < inventorySize; i++) {
                    System.out.println((i+1) + ". " + playerInventory.get(i).getName());

                    if(scrollCount == 3 && i< inventorySize-1) {
                        System.out.println("[Press ENTER to countinue displaying your items, type 'stop' to stop.]");
                        if(parser.getRawInputString().toLowerCase().equals("stop")) {
                            break;
                        }
                        scrollCount = -1;
                    }
                    scrollCount++;
                }

            } else if(command.getSecondWord().toLowerCase().equals("show")) {
                if(command.hasWordAtIndex(2)) {
                    int index = -1;
                    try {
                        index = Integer.parseInt(command.getCommandWordAtIndex(2));   
                    } catch (NumberFormatException e){
                    }

                    if(index-1 >=0 && index-1 < playerInventory.size()) {
                        Item item = playerInventory.get(index-1);
                        System.out.println("#############################");
                        System.out.println("Item name: " + item.getName());
                        System.out.println(item.getDesc());
                        System.out.println("Item can be picked up: " + item.getMovable());
                        System.out.println("Item weight: " + item.getWeight());
                        System.out.println("#############################");
                        System.out.println();
                    } else {
                        System.out.println("Please select a valid item!");
                    }
                } else {
                    System.out.println("'inventory show #' --Displays details about the item at index #");
                }
            } else if(command.getSecondWord().toLowerCase().equals("use")) {
                if(command.hasWordAtIndex(2)) {
                    int index = -1;
                    try {
                        index = Integer.parseInt(command.getCommandWordAtIndex(2));   
                    } catch (NumberFormatException e){
                    }
                    //based on the subclass of the item, decide how to "use" it
                    if(index-1 >=0 && index-1 < playerInventory.size()) {
                        Item item = playerInventory.get(index-1);
                        if(item instanceof HpBoostItem) { // restore HP
                            player.removeFromInventoryAtIndex(index-1);
                            player.addToHealth(item.getHpBoost());
                            System.out.println("You have just used a " + item.getName() + "\nImproving your current Hit Points!");
                            System.out.println("You have gained : " + item.getHpBoost() + " HP!"); 
                        } else if(item instanceof MaxHpBoostItem) { // boost max HP
                            player.removeFromInventoryAtIndex(index-1);
                            player.addToMaxHealth(item.getMaxHpBoost());
                            System.out.println("You just put on " + item.getName() + "\nImproving your maximum Hit Points!");
                            System.out.println("Your maximum hitpoints have improved by: " + item.getMaxHpBoost() + " HP!");
                        } else {
                            System.out.println(item.getDesc()); // just print a description
                        }
                    } else {
                        System.out.println("Please select a valid item!");
                    }
                } else {
                    System.out.println("'inventory use #' --Uses the item at index #");
                }
            } else if(command.getSecondWord().toLowerCase().equals("drop")) {
                if(command.hasWordAtIndex(2)) {
                    int index = -1;
                    try {
                        index = Integer.parseInt(command.getCommandWordAtIndex(2));   
                    } catch (NumberFormatException e){
                    }

                    if(index-1 >=0 && index-1 < playerInventory.size()) {
                        Item item = playerInventory.get(index-1);
                        player.removeFromInventoryAtIndex(index-1);
                        player.getCurrentRoom().addToInventory(item);
                        System.out.println("Item " + item.getName() + " successfully dropped!");
                    } else {
                        System.out.println("Please select a valid item!");
                    }
                } else {
                    System.out.println("'inventory drop #' --Drops the item at index # in your current environment");
                }
            } else {
                System.out.println("#############################");
                System.out.println("Inventory command - manage your current inventory");
                System.out.println("'inventory list' --Lists all your current items");
                System.out.println("'inventory show #' --Displays details about the item at index #");
                System.out.println("'inventory use #' --Uses the item at index #");
                System.out.println("'inventory drop #' --Drops the item at index # in your current environment");
                System.out.println("#############################");
            }
        } else {
            System.out.println("#############################");
            System.out.println("Inventory command - manage your current inventory");
            System.out.println("'inventory list' --Lists all your current items");
            System.out.println("'inventory show #' --Displays details about the item at index #");
            System.out.println("'inventory use #' --Uses the item at index #");
            System.out.println("'inventory drop #' --Drops the item at index # in your current environment");
            System.out.println("#############################"); 
        }
    }
    
    /**
     * "use" was issued
     * try to use an item from the players location
     * @param command the issuing command
     */
    private void use(Command command) {
        if(command.hasSecondWord()) {
            int index = -1;
            try {
                index = Integer.parseInt(command.getCommandWordAtIndex(1));   
            } catch (NumberFormatException e){
            }
            //decide how to "use" the item based on it's subclass
            if(index-1 >=0 && index-1 < player.getCurrentRoom().getInventory().size()) {
                Item item = player.getCurrentRoom().getInventory().get(index-1);
                if(item instanceof HpBoostItem) { // restore HP
                    player.getCurrentRoom().removeItemFromInventory(index -1);
                    player.addToHealth(item.getHpBoost());
                    System.out.println("You have just used a " + item.getName() + "\nImproving your current Hit Points!");
                } else if(item instanceof MaxHpBoostItem) { // boost max HP
                    player.getCurrentRoom().removeItemFromInventory(index -1);
                    player.addToMaxHealth(item.getMaxHpBoost());
                    System.out.println("You just put on " + item.getName() + "\nImproving your maximum Hit Points!");
                } else if(item instanceof Teleporter) { // teleport the player
                    Room room = item.getARoom();
                    room.setVisited();
                    player.teleport(room);
                    System.out.println(player.getCurrentRoom().getLongDescription());
                    doMiniMap();
                } else {
                    System.out.println(item.getDesc()); // just print a description
                }
            } else {
                System.out.println("Please select a valid item!");
            }
        } else {
            System.out.println("#############################");
            System.out.println("Use command - use items from your surroundings");
            System.out.println("'use #' --Use item at index #");
            System.out.println("#############################");  
        }
    }
    
    /**
     * "interact" was issued
     * Interact with a character
     * 
     * This means:
     * Decide on a dialog set to display to the player
     * Execute the necessary actions based on the 
     * player's choice:
     *  -start quests
     *  -end quests
     *  -receive information
     *  - give/receive items
     * !DISCLAIMER! This method is very, very long.
     * The reason is: there are a LOT of String literals
     * and decision trees. Storing them in a data structure
     * would have been inefficient, as each charachter has it's own
     * unique decision tree.
     * 
     * @param command the issuing command
     */
    private void interact(Command command) {
        if(command.hasSecondWord()) {
            ArrayList<Entity> characters = getCharactersInRoom(player.getCurrentRoom());       
            int index = -1;
            try {
                index = Integer.parseInt(command.getCommandWordAtIndex(1));   
            } catch (NumberFormatException e){
            }
            //Decide if the index provided is correct
            if(index-1 >=0 && index-1 < characters.size()) {
                //This series of if/else decide which character decision
                //tree to pursue
                Entity interlocutor = characters.get(index-1);
                if(interlocutor.getName().equals("Siegfried")) {

                    System.out.print('\u000C');
                    System.out.println("---<Siegfried>---");
                    System.out.println("Greetings,stranger. I am Siegfried,\nleader ofthe 5th Mechanized Divison\nof our mighty Emperor's Imperial Guard.\nHow may I be of service?\n");
                    while(true) {
                        //based on past actions(completed/ongoing quests)
                        //decide what dialougue options the player has
                        //then respond to his choice
                        int listIndex = 1;
                        HashMap<Integer, String> choices = new HashMap<>();
                        choices.put(listIndex,"What are you doing here?" );
                        listIndex++;
                        choices.put(listIndex, "What are the creatures I keep seeing and fighting?");
                        listIndex++;
                        choices.put(listIndex, "Who is the \"Emperor\"?");
                        if(!quests.get(1).getCompleted()) {
                            listIndex++;
                            if(!quests.get(1).getStarted()) {
                                choices.put(listIndex ,"Can I help you out in any way?");
                            } else {
                                choices.put(listIndex, "So, I have Lena's Letter...");
                            }
                        }

                        if(!quests.get(2).getCompleted() && quests.get(1).getCompleted()) {
                            listIndex++;
                            if(!quests.get(2).getStarted()) {
                                choices.put(listIndex ,"Can I do something for you Siegfried?");
                            } else {
                                choices.put(listIndex ,"Siegfried, I found von Wolfson...");
                            }
                        }

                        if(!quests.get(3).getCompleted() && quests.get(2).getCompleted() && quests.get(1).getCompleted() && !quests.get(6).getStarted()) {
                            listIndex++;
                            if(!quests.get(3).getStarted()) {
                                choices.put(listIndex ,"Siegfried, I need help in order to get to The Great Passage.");
                            } else {
                                choices.put(listIndex ,"It is done.");
                            }
                        }

                        if(quests.get(5).getStarted() && !quests.get(5).getCompleted() && !quests.get(5).getRequirementGiven() ) {
                            listIndex++;
                            choices.put(listIndex, "So, Siegfried, just how vast is the Imperial Guard's 5'th Mechanized Division?");
                        }
                        listIndex++;    
                        choices.put(listIndex, "Have a good day, Siegfried.");

                        for(int i=1; i<=listIndex; i++) {
                            System.out.println(i + ". " + choices.get(i));
                        }
                        int choice = -1;
                        String choiceString = parser.getRawInputString();
                        try {
                            choice = Integer.parseInt(choiceString);
                        } catch (NumberFormatException e){

                        }

                        if(choice >=1 && choice <=listIndex) {
                            choiceString = choices.get(choice);
                            if(choiceString.equals("What are you doing here?")) {
                                System.out.println("---<Siegfried>---");
                                System.out.println("We are here on the orders of the Emperor\nto cleanse these lands of the\nfoul beasts known as \"The Scythe\". ");
                                System.out.println("These rebel scoundrels\n are taking advantage of the Emperors\n good will and harmony and disrupting ours,\n ahm, his affairs.");
                            } else if(choiceString.equals("What are the creatures I keep seeing and fighting?")) {
                                System.out.println("---<Siegfried>---");
                                System.out.println("One might wrongly think that they were once human,\nbut don't be fooled, my friend.");
                                System.out.println("They are not. Once degenerates of societey,\ncriminals, felons, drunkards, and so on,");
                                System.out.println("our benevolent Emperor decided\nto rid us of them\nby turning them into True Degenerates");
                                System.out.println("and letting them loose on our Enemies.");
                                System.out.println("Now they are mindless beasts,\nwandering around aimlessly");
                                System.out.println("and attacking anything that they see.");
                            } else if(choiceString.equals("Who is the \"Emperor\"?")) {
                                System.out.println("---<Siegfried>---");
                                System.out.println("Our might Emperor, The Blue Light Shimmering in the Night Sky,");
                                System.out.println("is the leader of these otherwise helpless lands.");
                                System.out.println("Through his Benevolence and Grace, we serve a greater purpose:");
                                System.out.println("The quest for a better and purer world, without these infidels.");
                            } else if(choiceString.equals("Can I help you out in any way?")) {
                                System.out.println("---<Siegfried>---");
                                System.out.println("My dear friend " + player.getName()+", although I have only met you,");
                                System.out.println("I have an important task for you to complete:");
                                System.out.println("It is of upmost importance that you maintain complete secrecy about the matter.");
                                System.out.println("The one and only women in my life, <Lena>, has been recently seen");
                                System.out.println("volunteering at a local field hospital.");
                                System.out.println("Could you find her and mention to her\nthat I would like to meet her?");
                                System.out.println("I would reward you generously for this kind act of frindship.");
                                quests.get(1).startQuest();
                                System.out.println();
                                System.out.println("<Quest: \"A Long Lost Love\" has started!>");
                            } else if(choiceString.equals("So, I have Lena's Letter...")) {
                                if(player.checkForItemInInventory("Lena's Letter to Siegfried")) {
                                    player.removeItemFromInventory("Lena's Letter to Siegfried");
                                    quests.get(1).finishQuest();
                                    System.out.println("---<Siegfried>---");
                                    System.out.println("Thank you so much for this service!");
                                    System.out.println("I will not let this act of kindness be forgotten.");
                                    if(player.getCurrentCarryWeight() <= 90) {
                                        player.addToInventory(quests.get(1).getReward());
                                        System.out.println("<Reward successfully added to your inventory!>");
                                    } else {
                                        player.getCurrentRoom().addToInventory(quests.get(1).getReward());
                                        System.out.println("<Reward successfully added to your local environment!>");
                                    }
                                } else {
                                    System.out.println("---<Siegfried>---");
                                    System.out.println("So, where is it? Come back when you actually have it.");
                                }
                            } else if(choiceString.equals("Can I do something for you Siegfried?")) {
                                System.out.println("---<Siegfried>---");
                                System.out.println("I do indeed have a task for you, " + player.getName() + ".");
                                System.out.println("My trustee, Knight von Wolfson, has not reported from his expedition");
                                System.out.println("to the \"Dragon's Lair\", a gathering place for Degenerates, for over a week.");
                                System.out.println("I ask you to go and discover what has happend to my dear von Wolfson.");
                                System.out.println("In order to gain access to the  \"Dragon's Lair\", i bestow upon you ");
                                System.out.println("the Emperor's seal. Good Luck.");
                                quests.get(2).startQuest();
                                System.out.println("<Quest: \" Son of the Empire\" has started!>");
                                if(player.getCurrentCarryWeight() <= 95) {
                                    player.addToInventory(quests.get(2).getStartItem());
                                    System.out.println("<Item successfully added to your inventory>");
                                } else {
                                    player.getCurrentRoom().addToInventory(quests.get(2).getStartItem());
                                    System.out.println("<Item successfully added to your local environment!>");
                                }
                            } else if(choiceString.equals("Siegfried, I found von Wolfson...")) {
                                if(player.checkForItemInInventory("von Wolfson's Pendant")) {
                                    System.out.println("---<Siegfried>---");
                                    System.out.println("Thank you so much. You will be generously rewarded for your efforts.");
                                    player.removeItemFromInventory("von Wolfson's Pendant");
                                    quests.get(2).finishQuest();
                                    if(player.getCurrentCarryWeight() <= 85) {
                                        player.addToInventory(quests.get(2).getReward());
                                        System.out.println("<Reward successfully added to your inventory!>");
                                    }else {
                                        player.getCurrentRoom().addToInventory(quests.get(2).getReward());
                                        System.out.println("<Reward successfully added to your current environment!>");
                                    }
                                } else {
                                    System.out.println("---<Siegfried>---");
                                    System.out.println("Do you have proof? Come back when you have proof!");
                                }
                            } else if(choiceString.equals("Siegfried, I need help in order to get to The Great Passage.")) {
                                if(!quests.get(6).getStarted()) {
                                    System.out.println("---<Siegfried>---");
                                    System.out.println("I can get you a Great Pass but first");
                                    System.out.println("you will have to help me one last time.");
                                    System.out.println("I've heard you know where the \"Scythe\" Camp");
                                    System.out.println("is located. Take this beacon to the camp and");
                                    System.out.println("leave it there. It will attract Degenrates and");
                                    System.out.println("completley purge those infidels.");
                                    System.out.println("Do this and the emperor will be pleased.");
                                    quests.get(3).startQuest();
                                    System.out.println("<Quest: \"For the Glory of the Emperor!\" has started!>");
                                    if(player.getCurrentCarryWeight() <= 90) {
                                        player.addToInventory(quests.get(3).getStartItem());
                                        System.out.println("<Item successfully added to your inventory>");                                       
                                    } else {
                                        player.getCurrentRoom().addToInventory(quests.get(3).getStartItem());
                                        System.out.println("<Item successfully added to your local environment!>");
                                    }
                                } else {
                                    System.out.println("---<Siegfried>---");
                                    System.out.println("I'm sorry. That is impossible.");
                                }
                            } else if(choiceString.equals("It is done.")) {
                                if(rooms.get(9).checkForItemInInventory("Degenerate Beacon")) {
                                    System.out.println("---<Siegfried>---");
                                    System.out.println("With this final blow, the \"Scythe\"");
                                    System.out.println("will sureley fall into chaos and disarray.");
                                    System.out.println("Thank you for your contribution to the Emperors cause.");
                                    System.out.println("As a sign of gratitude, take this Great Pass.");
                                    quests.get(3).finishQuest();
                                    if(player.getCurrentCarryWeight() <= 96) {
                                        player.addToInventory(quests.get(3).getReward());
                                        System.out.println("<Reward successfully added to your inventory!>");
                                    } else {
                                        player.getCurrentRoom().addToInventory(quests.get(3).getReward());
                                        System.out.println("<Reward successfully added to your current environment!>");
                                    }
                                } else {
                                    System.out.println("---<Siegfried>---");
                                    System.out.println("My sensors tell me that the device is not active yet.");
                                    System.out.println("Are you sure you have placed it correctly?");
                                }
                            } else if(choiceString.equals("So, Siegfried, just how vast is the Imperial Guard's 5'th Mechanized Division?")) {
                                System.out.println("---<Siegfried>---");
                                System.out.println("You couldn't even measure how great our troops are...");
                                System.out.println("We have thousands of troops ... \nand hundreds of armoured vehicles...");
                                if(player.getCurrentCarryWeight() <= 98) {
                                    player.addToInventory(quests.get(5).getRequirement());
                                    quests.get(5).setRequirementGiven();
                                } else {
                                    System.out.println("(Maybe I should ask Siegfried when I have space in my inventory");
                                    System.out.println("for some notes.)");
                                }
                            }else if(choiceString.equals("Have a good day, Siegfried.")) {
                                break;
                            }
                        } else {
                            System.out.println("Please select a valid dialogue choice.");
                        }
                        parser.getRawInputString();
                    } 

                } else if(interlocutor.getName().equals("Shak Raz")) {
                    System.out.print('\u000C');
                    System.out.println("---<Shak Raz>---");
                    System.out.println("Hey there champ. I'm Shak Raz, leader of \"The Scythe\". What's up?\n");
                    while(true) {
                        //based on past actions(completed/ongoing quests)
                        //decide what dialougue options the player has
                        //then respond to his choice
                        int listIndex = 1;
                        HashMap<Integer, String> choices = new HashMap<>();
                        choices.put(listIndex,"Who are you guys?" );
                        listIndex++;
                        choices.put(listIndex, "Why are you at war with the Emporor?");
                        listIndex++;
                        choices.put(listIndex, "What are the monsters that keep attacking me?");
                        if(!quests.get(4).getCompleted()) {
                            listIndex++;
                            if(!quests.get(4).getStarted()) {
                                choices.put(listIndex ,"Could I help you guys out?");
                            } else {
                                choices.put(listIndex, "So, I got the report from your brother...");
                            }
                        }

                        if(!quests.get(5).getCompleted() && quests.get(4).getCompleted()) {
                            listIndex++;
                            if(!quests.get(5).getStarted()) {
                                choices.put(listIndex ,"Can I do something for you Shak?");
                            } else {  
                                choices.put(listIndex ,"Ok Shak, i got some intel...");
                            }
                        }

                        if(!quests.get(6).getCompleted() && quests.get(5).getCompleted() && quests.get(4).getCompleted() && !quests.get(3).getStarted()) {
                            listIndex++;
                            if(!quests.get(6).getStarted()) {
                                choices.put(listIndex ,"Shak, I need help in order to get to The Great Passage.");
                            } else {
                                choices.put(listIndex ,"It is done.");
                            }
                        }

                        listIndex++;
                        choices.put(listIndex, "Godspeed, Shak.");

                        for(int i=1; i<=listIndex; i++) {
                            System.out.println(i + ". " + choices.get(i));
                        }
                        int choice = -1;
                        String choiceString = parser.getRawInputString();
                        try {
                            choice = Integer.parseInt(choiceString);
                        } catch (NumberFormatException e){

                        }

                        if(choice >=1 && choice <=listIndex) {
                            choiceString = choices.get(choice);
                            if(choiceString.equals("Who are you guys?")) {
                                System.out.println("---<Shak Raz>---");
                                System.out.println("We are \"The Scythe\", \na guerrilla warfare group fighting\nagainst the tyranny of the so-called Emeror.");
                                System.out.println("Our goal is true equality and peace in this lands,\nand not just for the privileged, how it is now.");
                            } else if(choiceString.equals("Why are you at war with the Emporor?")) {
                                System.out.println("---<Shak Raz>---");
                                System.out.println("The Emperor is a fake institution created to opress the weak and poor");
                                System.out.println("and to further strenghten the elites.");
                                System.out.println("We're not fighting against the Emperor, we're fighting for peace.\nAnd prosperity. And equlity.");
                                System.out.println("Some say that the Emporor dosen't even exist.");
                            } else if(choiceString.equals("What are the monsters that keep attacking me?")) {
                                System.out.println("---<Shak Raz>---");
                                System.out.println("The were, once, human. The Emperor turned them into this monsters.");
                                System.out.println("They call them Degenarates. They say it's a \"humane\" way\nto get rid of the failures of society,");
                                System.out.println("claiming that they no longer think, and, thus, that they are actually dead.");
                                System.out.println("I don't buy it. I think that deep inside those purple blobs,");
                                System.out.println("there still lies a human being. Thinking. Suffering.");
                            } else if(choiceString.equals("Could I help you guys out?")) {
                                System.out.println("---<Shak Raz>---");
                                System.out.println("Sure thing, " + player.getName() + "!");
                                System.out.println("My brother, Jason, has been out on a mission for quite some time.");
                                System.out.println("Could you go and find him and get me his report? Thanks.");
                                quests.get(4).startQuest();
                                System.out.println("<Quest: " + quests.get(4).getName() + " has started!>");
                            } else if(choiceString.equals("So, I got the report from your brother...")) {
                                if(player.checkForItemInInventory(quests.get(4).getRequirement().getName())) {
                                    player.removeItemFromInventory(quests.get(4).getRequirement().getName());
                                    quests.get(4).finishQuest();
                                    System.out.println("---<Shak Raz>---");
                                    System.out.println("Thank you man! ");
                                    System.out.println("Don't you think your work will go unpaid! Here, have this!");
                                    if(player.getCurrentCarryWeight() <= 92) {
                                        player.addToInventory(quests.get(4).getReward());
                                        System.out.println("<Reward successfully added to your inventory!>");
                                    } else {
                                        player.getCurrentRoom().addToInventory(quests.get(4).getReward());
                                        System.out.println("<Reward successfully added to your local environment!>");
                                    }
                                } else {
                                    System.out.println("---<Shak Raz>---");
                                    System.out.println("So, where is it? Come back when you actually have it.");
                                }
                            } else if(choiceString.equals("Can I do something for you Shak?")) {
                                System.out.println("---<Shak Raz>---");
                                System.out.println("Well, i guess there is something you could work on,  " + player.getName() + ".");
                                System.out.println("I heard that you might be allowed into the imperial base.");
                                System.out.println("Could you try and find out about how big their force is? Thanks.");
                                quests.get(5).startQuest();
                                System.out.println("<Quest: " + quests.get(5).getName() + " has started!>");
                            } else if(choiceString.equals("Ok Shak, i got some intel...")) {
                                if(player.checkForItemInInventory(quests.get(5).getRequirement().getName())) {
                                    System.out.println("---<Shak Raz>---");
                                    System.out.println("Woah man! That is some good stuff!");
                                    System.out.println("I'll have you rewarded right away!");
                                    player.removeItemFromInventory(quests.get(5).getRequirement().getName());
                                    quests.get(5).finishQuest();
                                    if(player.getCurrentCarryWeight() <= 97) {
                                        player.addToInventory(quests.get(5).getReward());
                                        System.out.println("<Reward successfully added to your inventory!>");
                                    }else {
                                        player.getCurrentRoom().addToInventory(quests.get(5).getReward());
                                        System.out.println("<Reward successfully added to your current environment!>");
                                    }
                                } else {
                                    System.out.println("---<Shak Raz>---");
                                    System.out.println("So, where is the stuff? Come back when you have something!");
                                }
                            } else if(choiceString.equals("Shak, I need help in order to get to The Great Passage.")) {
                                if(!quests.get(6).getStarted()) {
                                    System.out.println("---<Shak Raz>---");
                                    System.out.println("Man, guess what, I could see you situated, but first,");
                                    System.out.println("There's a little something I would like you to take care of.");
                                    System.out.println("Take this bomb, and place it in the imperial Base.");
                                    System.out.println("And then, we'll talk.");
                                    quests.get(6).startQuest();
                                    System.out.println("<Quest: " + quests.get(6).getName() + " has started!>");
                                    if(player.getCurrentCarryWeight() <= 85) {
                                        player.addToInventory(quests.get(6).getStartItem());
                                        System.out.println("<Item successfully added to your inventory>");                                       
                                    } else {
                                        player.getCurrentRoom().addToInventory(quests.get(6).getStartItem());
                                        System.out.println("<Item successfully added to your local environment!>");
                                    }
                                } else {
                                    System.out.println("---<Shak Raz>---");
                                    System.out.println("I'm sorry. That is impossible.");
                                }
                            } else if(choiceString.equals("It is done.")) {
                                if(rooms.get(6).checkForItemInInventory(quests.get(6).getStartItem().getName())) {
                                    System.out.println("---<Shak Raz>---");
                                    System.out.println("Good Job Man! That should surely ");
                                    System.out.println("wreak havok among the Emperor's troops.");
                                    System.out.println("Thank you for playing such a vital role");
                                    System.out.println("in our cause.");
                                    System.out.println("As a sign of gratitude, take this Great Pass.");
                                    quests.get(6).finishQuest();
                                    if(player.getCurrentCarryWeight() <= 96) {
                                        player.addToInventory(quests.get(6).getReward());
                                        System.out.println("<Reward successfully added to your inventory!>");
                                    } else {
                                        player.getCurrentRoom().addToInventory(quests.get(6).getReward());
                                        System.out.println("<Reward successfully added to your current environment!>");
                                    }
                                } else {
                                    System.out.println("---<Shak Raz>---");
                                    System.out.println("Hmm, the bomb dosen't seem to have activated.");
                                    System.out.println("Are you sure you have placed it correctly?");
                                }
                            } else if(choiceString.equals("Godspeed, Shak.")) {
                                break;
                            }
                        } else {
                            System.out.println("Please select a valid dialogue choice.");
                        }
                        parser.getRawInputString();
                    }

                } else if (interlocutor.getName().equals("Gramps")) {
                    System.out.print('\u000C');
                    System.out.println("---<Gramps>---");
                    System.out.println("Hello, young man. They call me Gramps.\nHow can i help you?\n");
                    while(true) {
                        //based on past actions(completed/ongoing quests)
                        //decide what dialougue options the player has
                        //then respond to his choice
                        int listIndex = 1;
                        HashMap<Integer, String> choices = new HashMap<>();
                        choices.put(listIndex,"What are these lands?" );
                        listIndex++;
                        choices.put(listIndex, "Have you seen my plane crash?");

                        if(!quests.get(7).getCompleted()) {
                            listIndex++;
                            if(!quests.get(7).getStarted()) {
                                choices.put(listIndex ,"Do you need anything, Gramps?");
                            } else {
                                choices.put(listIndex, "So, i gathered some bread...");
                            }
                        }

                        listIndex++;
                        choices.put(listIndex, "See you, Gramps");

                        for(int i=1; i<=listIndex; i++) {
                            System.out.println(i + ". " + choices.get(i));
                        }
                        int choice = -1;
                        String choiceString = parser.getRawInputString();
                        try {
                            choice = Integer.parseInt(choiceString);
                        } catch (NumberFormatException e){

                        }

                        if(choice >=1 && choice <=listIndex) {
                            choiceString = choices.get(choice);
                            if(choiceString.equals("What are these lands?")) {
                                System.out.println("---<Gramps>---");
                                System.out.println("Oh, these lands have many, many names....");
                                System.out.println("Most commonly they are known as The Eastern Realms...");
                                System.out.println("The seat of the mighty Emperor...");
                            } else if(choiceString.equals("Have you seen my plane crash?")) {
                                System.out.println("---<Gramps>---");
                                System.out.println("Oh, you were in that thing? ");
                                System.out.println("I didn't think anyone could survive such  a crash...");
                                System.out.println("I just came to see if I could scavange anything.");
                            } else if(choiceString.equals("Do you need anything, Gramps?")) {
                                System.out.println("---<Gramps>---");
                                System.out.println("Oh, sure thing, young man.");
                                System.out.println("Gramps needs his fibers, and there's no better");
                                System.out.println("fiber source like bread!");
                                System.out.println("Get me five loaves and I might reward you!");
                                System.out.println();
                                quests.get(7).startQuest();
                                System.out.println("<Side-Quest " + quests.get(7).getName() + " has started!>");
                            }else if(choiceString.equals("So, i gathered some bread...")) {
                                ArrayList<Item> playerInventory = player.getInventory();
                                int count = 0;
                                for(Item item : playerInventory) {
                                    if(item.getName().equals(quests.get(7).getRequirement().getName())) {
                                        count++;
                                    }
                                }

                                if(count >= quests.get(7).getCount()) {
                                    int removed = 0;
                                    quests.get(7).finishQuest();
                                    Iterator<Item> it = playerInventory.iterator();
                                    while(it.hasNext()) {
                                        Item item = it.next();
                                        if(item.getName().equals(quests.get(7).getRequirement().getName())) {
                                            player.setCurrentCarryWeight(player.getCurrentCarryWeight()-item.getWeight());
                                            it.remove();
                                            removed++;
                                        }

                                        if(removed == quests.get(7).getCount()) {
                                            break;
                                        }
                                    }
                                    System.out.println("---<Gramps>---");
                                    System.out.println("Thank you so much young man! Here is your reward!");
                                    if(player.getCurrentCarryWeight() <= 95) {
                                        player.addToInventory(quests.get(7).getReward());
                                        System.out.println("<Reward successfully added to your inventory!>");
                                    } else {
                                        player.getCurrentRoom().addToInventory(quests.get(7).getReward());
                                        System.out.println("<Reward successfully added to your local environment!>"); 
                                    }

                                } else {
                                    System.out.println("---<Gramps>---");
                                    System.out.println("Oh, young man. That's not enough bread.");
                                    System.out.println("Come back with more.");
                                }
                            }else if(choiceString.equals("See you, Gramps")) {
                                break;
                            }

                        } else {
                            System.out.println("Please select a valid dialogue choice.");
                        }
                        parser.getRawInputString();
                    }
                } else if(interlocutor.getName().equals("Lena")) {

                    System.out.print('\u000C');
                    System.out.println("---<Lena>---");
                    System.out.println("Welcome to our humble establishment. I am Lena.\nHow may I help you?\n");
                    while(true) {
                        //based on past actions(completed/ongoing quests)
                        //decide what dialougue options the player has
                        //then respond to his choice
                        int listIndex = 1;
                        HashMap<Integer, String> choices = new HashMap<>();
                        choices.put(listIndex,"What do you think about the War?" );
                        listIndex++;
                        choices.put(listIndex, "What do you think about the Degenerates?");

                        if(!quests.get(8).getCompleted()) {
                            listIndex++;
                            if(!quests.get(8).getStarted()) {
                                choices.put(listIndex ,"Could I help you in any way?");
                            } else {
                                choices.put(listIndex, "So, i recovered some bandages...");
                            }
                        }

                        if(!quests.get(1).getCompleted() && !quests.get(1).getRequirementGiven() && quests.get(1).getStarted()) {
                            listIndex++;
                            choices.put(listIndex ,"Lena, Siegfried spoke of you...");
                        }

                        listIndex++;
                        choices.put(listIndex, "See you soon, Lena");

                        for(int i=1; i<=listIndex; i++) {
                            System.out.println(i + ". " + choices.get(i));
                        }
                        int choice = -1;
                        String choiceString = parser.getRawInputString();
                        try {
                            choice = Integer.parseInt(choiceString);
                        } catch (NumberFormatException e){

                        }

                        if(choice >=1 && choice <=listIndex) {
                            choiceString = choices.get(choice);
                            if(choiceString.equals("What do you think about the War?")) {
                                System.out.println("---<Lena>---");
                                System.out.println("One of the most vile things Man has been able");
                                System.out.println("to conjure. The suffering is immense.");
                                System.out.println("We don't have enough nurses to tend to the wounded.");
                                System.out.println("None of us can sleep properly.");
                                System.out.println("Just recently one of us died from exhaustion.");
                            } else if(choiceString.equals("What do you think about the Degenerates?")) {
                                System.out.println("---<Lena>---");
                                System.out.println("What the Emperor is doing to those");
                                System.out.println("poor human beings is horrendous. I can't");
                                System.out.println("understand how he can deem such methods acceptable.");
                            } else if(choiceString.equals("Could I help you in any way?")) {
                                System.out.println("---<Lena>---");
                                System.out.println("We could always use some help");
                                System.out.println("in order to ease the pain of the ones who suffer.");
                                System.out.println("We are running low on medical supplies.");
                                System.out.println("Bring us 5 clean Bandages, and I might have something for you...");
                                System.out.println();
                                quests.get(8).startQuest();
                                System.out.println("<Side-Quest " + quests.get(8).getName() + " has started!>");
                            }else if(choiceString.equals("So, i recovered some bandages...")) {
                                ArrayList<Item> playerInventory = player.getInventory();
                                int count = 0;
                                for(Item item : playerInventory) {
                                    if(item.getName().equals(quests.get(8).getRequirement().getName())) {
                                        count++;
                                    }
                                }

                                if(count >= quests.get(8).getCount()) {
                                    int removed = 0;
                                    quests.get(8).finishQuest();
                                    Iterator<Item> it = playerInventory.iterator();
                                    while(it.hasNext()) {
                                        Item item = it.next();
                                        if(item.getName().equals(quests.get(8).getRequirement().getName())) {
                                            player.setCurrentCarryWeight(player.getCurrentCarryWeight()-item.getWeight());
                                            it.remove();
                                            removed++;
                                        }

                                        if(removed == quests.get(8).getCount()) {
                                            break;
                                        }
                                    }
                                    System.out.println("---<Lena>---");
                                    System.out.println("Thank you so much " + player.getName()+ " !\nHave this as a sign of gratefulness.");
                                    if(player.getCurrentCarryWeight() <= 95) {
                                        player.addToInventory(quests.get(8).getReward());
                                        System.out.println("<Reward successfully added to your inventory!>");
                                    } else {
                                        player.getCurrentRoom().addToInventory(quests.get(8).getReward());
                                        System.out.println("<Reward successfully added to your local environment!>"); 
                                    }

                                } else {
                                    System.out.println("---<Lena>---");
                                    System.out.println("I'm afraid that this won't suffice");
                                    System.out.println("Please come back with more.");
                                }
                            }else if(choiceString.equals("Lena, Siegfried spoke of you...")) {
                                System.out.println("---<Lena>---");
                                System.out.println("Oh, did he? I can't wait to meet him again...");
                                System.out.println("It's just that I have so much work here, with the ill...");
                                System.out.println("Please give him this letter...");
                                if(player.getCurrentCarryWeight() <= 99) {
                                    player.addToInventory(quests.get(1).getRequirement());
                                    quests.get(1).setRequirementGiven();
                                } else {
                                    System.out.println("Oh, I see you don't have space in your bag for it.");
                                    System.out.println("Come to pick it up later.");
                                }
                            }else if(choiceString.equals("See you soon, Lena")) {
                                break;
                            }

                        } else {
                            System.out.println("Please select a valid dialogue choice.");
                        }
                        parser.getRawInputString();
                    }
                } else if(interlocutor.getName().equals("von Wolfson")) {

                    System.out.print('\u000C');
                    System.out.println("---<Knight von Wolfson>---");
                    System.out.println("Thank you for helping me out, starnger.\nI am Knight von Wolfson\n");
                    while(true) {
                        //based on past actions(completed/ongoing quests)
                        //decide what dialougue options the player has
                        //then respond to his choice
                        int listIndex = 1;
                        HashMap<Integer, String> choices = new HashMap<>();
                        choices.put(listIndex,"What do you hold of \"The Scythe\"?" );
                        listIndex++;
                        choices.put(listIndex, "Why is the Empire producing Degenerates ?");

                        if(!quests.get(2).getCompleted() && !quests.get(2).getRequirementGiven() && quests.get(2).getStarted()) {
                            listIndex++;
                            choices.put(listIndex, "Siegfried asked me to look for you.");
                        }

                        listIndex++;
                        choices.put(listIndex, "Look after yourself, von Wolfson");

                        for(int i=1; i<=listIndex; i++) {
                            System.out.println(i + ". " + choices.get(i));
                        }
                        int choice = -1;
                        String choiceString = parser.getRawInputString();
                        try {
                            choice = Integer.parseInt(choiceString);
                        } catch (NumberFormatException e){

                        }

                        if(choice >=1 && choice <=listIndex) {
                            choiceString = choices.get(choice);
                            if(choiceString.equals("What do you hold of \"The Scythe\"?" )) {
                                System.out.println("---<Knight von Wolfson>---");
                                System.out.println("This rebellious organization must be ");
                                System.out.println("temrinated immediatly. They are seeking false hopes, ");
                                System.out.println("false dreams and false freedoms and in their ");
                                System.out.println("selfish quests they are destroying the lives");
                                System.out.println("of many, many innocents.");

                            } else if(choiceString.equals("Why is the Empire producing Degenerates ?")) {
                                System.out.println("---<Knight von Wolfson>---");
                                System.out.println("My friend, the Empire is not producing Degenarates.");
                                System.out.println("It is simply taking off their masks and");
                                System.out.println("showing their true face.");
                                System.out.println("The Empire's subjects take great pride in doing so.");

                            } else if(choiceString.equals("Siegfried asked me to look for you.")) {
                                System.out.println("---<Knight von Wolfson>---");
                                System.out.println("Give this token to Siegfried ");
                                System.out.println("to show him that I am alive.");

                                if(player.getCurrentCarryWeight() <= 95) {
                                    player.addToInventory(quests.get(2).getRequirement());
                                    quests.get(2).setRequirementGiven();
                                } else {
                                    System.out.println("Oh, it seems that your pouch is full.");
                                    System.out.println("Come to pick it up later.");
                                }
                            } else if(choiceString.equals("Look after yourself, von Wolfson")) {
                                break;
                            }

                        } else {
                            System.out.println("Please select a valid dialogue choice.");    
                        }
                        parser.getRawInputString();
                    }
                } else if(interlocutor.getName().equals("Jason Raz")) {

                    System.out.print('\u000C');
                    System.out.println("---<Jason Raz>---");
                    System.out.println("Hello and welcome\nI am Jason Raz.\n");
                    while(true) {
                        //based on past actions(completed/ongoing quests)
                        //decide what dialougue options the player has
                        //then respond to his choice
                        int listIndex = 1;
                        HashMap<Integer, String> choices = new HashMap<>();
                        choices.put(listIndex,"How is the war going?" );
                        listIndex++;
                        choices.put(listIndex, "Do you know Siegfried?");

                        if(!quests.get(4).getCompleted() && !quests.get(4).getRequirementGiven() && quests.get(4).getStarted()) {
                            listIndex++;
                            choices.put(listIndex, "Shak asked me to look for you.");
                        }

                        listIndex++;
                        choices.put(listIndex, "So long, Jason.");

                        for(int i=1; i<=listIndex; i++) {
                            System.out.println(i + ". " + choices.get(i));
                        }
                        int choice = -1;
                        String choiceString = parser.getRawInputString();
                        try {
                            choice = Integer.parseInt(choiceString);
                        } catch (NumberFormatException e){

                        }

                        if(choice >=1 && choice <=listIndex) {
                            choiceString = choices.get(choice);
                            if(choiceString.equals("How is the war going?")) {
                                System.out.println("---<Jason Raz>---");
                                System.out.println("Atrociously. Heavy losses on both sides.");
                                System.out.println("Kind of pointless suffering if you ask me.");
                                System.out.println("Nevertheless, my brother is leading the whole damn thing,");
                                System.out.println("So I have to be a part of it too.");

                            } else if(choiceString.equals("Do you know Siegfried?")) {
                                System.out.println("---<Jason Raz>---");
                                System.out.println("I have heard horrible stories about him.");
                                System.out.println("Some say he is the Emperors right hand.");
                                System.out.println("Some say the Emperor is dead, but ");
                                System.out.println("Siegfried keeps his Image alive...");

                            } else if(choiceString.equals("Shak asked me to look for you.")) {
                                System.out.println("---<Jason Raz>---");
                                System.out.println("Yes. I was supposed to return a report.");
                                System.out.println("Could you bring it to him, please?");
                                System.out.println("I fear I'm being followed.");
                                if(player.getCurrentCarryWeight() <= 97) {
                                    player.addToInventory(quests.get(4).getRequirement());
                                    quests.get(4).setRequirementGiven();
                                } else {
                                    System.out.println("You seem not to have any space for it.");
                                    System.out.println("Come back for it later.");
                                }
                            } else if(choiceString.equals("So long, Jason.")) {
                                break;
                            }

                        } else {
                            System.out.println("Please select a valid dialogue choice.");    
                        }
                        parser.getRawInputString();
                    }
                } else if(interlocutor.getName().equals("Federation Agent")) {

                    System.out.print('\u000C');
                    System.out.println("---<Federation Agent>---");
                    System.out.println("This is the Federation outpost of the Eastern Realms..");
                    System.out.println("How may I help you?\n");
                    while(true) {
                        //based on past actions(completed/ongoing quests)
                        //decide what dialougue options the player has
                        //then respond to his choice
                        int listIndex = 0;
                        HashMap<Integer, String> choices = new HashMap<>();

                        if((quests.get(3).getCompleted() || quests.get(6).getCompleted()) && !gameFinished) {
                            listIndex++;
                            choices.put(listIndex, "Please get me out of here.");
                        }

                        listIndex++;
                        choices.put(listIndex, "See you later.");

                        for(int i=1; i<=listIndex; i++) {
                            System.out.println(i + ". " + choices.get(i));
                        }
                        int choice = -1;
                        String choiceString = parser.getRawInputString();
                        try {
                            choice = Integer.parseInt(choiceString);
                        } catch (NumberFormatException e){

                        }

                        if(choice >=1 && choice <=listIndex) {
                            choiceString = choices.get(choice);
                            if(choiceString.equals("Please get me out of here.")) {

                                gameEnd();

                                break;
                            } else if(choiceString.equals("See you later.")){
                                break;
                            }
                        } else {
                            System.out.println("Please select a valid dialogue choice.");    
                        }
                        parser.getRawInputString();
                    }  
                }
            } else {
                System.out.println("Please select a valid character!");
            }
        } else {
            System.out.println("#############################");
            System.out.println("Interact command - interact with neighbouring characters.");
            System.out.println("'interact #' --interact with character at index #");
            System.out.println("#############################"); 
        }
    }
    
    
    /**
     * The player has won the game
     * Based on his choice, decide which ending
     * to display. Then ask the player
     * if he wants to continue playing
     */
    private void gameEnd() {
        quests.get(0).finishQuest();
        gameFinished = true;
        if(quests.get(3).getCompleted()) {
            System.out.println();
            System.out.println("##########################################");
            System.out.println("You have successfully escaped your nightmare.");
            System.out.println("In doing so, you decided to aid \"The Empire\".");
            System.out.println("By planting the beacon in the rebel base, you have ");
            System.out.println("assured their demise. They will have to survive waves");
            System.out.println("after waves of Degenerates. Those who will survive");
            System.out.println("will be captured and executed by the Imperial Guard");
            System.out.println("The Empire's reign remains supreme.");
            System.out.println("##########################################");

        } else if(quests.get(6).getCompleted()) {
            System.out.println();
            System.out.println("##########################################");
            System.out.println("You have successfully escaped your nightmare.");
            System.out.println("In doing so, you decided to aid \"The Scythe\".");
            System.out.println("By planting the explosive in the imperial base, you have" );
            System.out.println("assured their demise. The remaining rebel forces will be no match");
            System.out.println("for the rebels. The rebels, enraged by their hate for the");
            System.out.println("establishment, will murder almost all loyal imperialists.");
            System.out.println("It will take decades for the land and for it's people to heal.");
            System.out.println("##########################################");
        }

        System.out.println();
        System.out.println("Do you want to continue playing?(Y/N)");
        String input = parser.getRawInputString();
        while(true) {
            if(input.toLowerCase().equals("n")) {
                System.out.println("Thank you for playing \"World of Zuul: The Lesser Evil\"");
                System.exit(1);
                break;
            } else if(input.toLowerCase().equals("y")) {
                System.out.println("Thank you for playing \"World of Zuul: The Lesser Evil\"");
                System.out.println("Have fun!");
                break;
            } else {
                System.out.println("Plese enter a valid value.");
                input = parser.getRawInputString();
            }
        }
    }
    
    /**
     * "quest" was issued
     * show the player a Log of his quests
     * or display information about a specific quest
     * The quest Log only displays info about 
     * the quests which have been started.
     * @param command the issuing command
     */
    public void showQuestLog(Command command) {
        if(command.hasSecondWord()) {
            ArrayList<Quest> startedQuests = new ArrayList<>();

            for(Quest quest : quests) {
                if(quest.getStarted()) {
                    startedQuests.add(quest);
                }
            }

            if(command.getSecondWord().toLowerCase().equals("list")) {
                System.out.println("################################");
                System.out.println("Index -/- Quest Name -/- Main Quest -/- Quest Finished");
                for(int i=0;i<startedQuests.size();i++) {
                    System.out.println((i+1) +". " + startedQuests.get(i).getName() +" "+ startedQuests.get(i).isMainQuest() + " "+ startedQuests.get(i).getCompleted());  
                }
                System.out.println("################################");
                System.out.println();
            } else if(command.getSecondWord().toLowerCase().equals("show")) {
                if(command.hasWordAtIndex(2)) {
                    int index = -1;
                    String input = command.getCommandWordAtIndex(2);
                    try {
                        index= Integer.parseInt(input);
                    } catch (NumberFormatException e){

                    }

                    if(index-1 >=0 && index-1 <startedQuests.size()) {
                        Quest selectedQuest = startedQuests.get(index-1);
                        System.out.println();
                        System.out.println("Quest name: " + selectedQuest.getName());
                        System.out.println();
                        System.out.println("Quest description: " + selectedQuest.getDesc());
                        System.out.println();
                        try{
                            System.out.println("Quest contractor: " + selectedQuest.getContractor().getName());
                        } catch(NullPointerException e) {
                            System.out.println("Quest contractor: no contractor");
                        }
                        System.out.println();
                        try{
                            System.out.println("Quest Item requirement: " + selectedQuest.getRequirement().getName() );
                        }  catch(NullPointerException e) {
                            System.out.println("Quest Item rquirement: no item requirement.");
                        }
                        System.out.println();
                        System.out.println("Quest is main quest: " + selectedQuest.isMainQuest());
                        System.out.println();  
                        System.out.println("Quest is finished " + selectedQuest.getCompleted());
                        System.out.println("################################");
                        System.out.println();
                    } else {
                        System.out.println("Please select a valid index!");
                    }

                } else {
                    System.out.println("'quest show #' -- display information about the quest at index #.");
                }
            } else {
                //print command help
                System.out.println("#############################");
                System.out.println("Quest command - display information about your quests.");
                System.out.println("'quest list' --list all quests");
                System.out.println("'quest show #' -- display information about the quest at index #.");
                System.out.println("#############################"); 
            }

        } else {
            //print command help
            System.out.println("#############################");
            System.out.println("Quest command - display information about your quests.");
            System.out.println("'quest list' --list all quests");
            System.out.println("'quest show #' -- display information about the quest at index #.");
            System.out.println("#############################"); 
        }
    }
    
    /**
     * Get a room at a specific index
     * from the Game ArrayList collection
     * 
     * @param index the index for the room
     * @return room the returned room
     */
    public Room getRoomAtIndex(int index) {
        return rooms.get(index);   
    }
    
    /**
     * Get the total number of rooms
     * in the game
     * @return number of total Rooms
     */
    public int getNumberOfRooms() {
        return rooms.size();
    }

    /**
     * Get the room in which the player is 
     * currently in.
     * @return the room in which
     * the player is currently in.
     */
    public Room getCurrentRoom() {
        return player.getCurrentRoom();
    }
}