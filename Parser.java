import java.util.Scanner;
import java.util.ArrayList;
/**
 * This class is part of the "World of Zuul: The Lesser Evil" game.
 * 
 * This parser reads user input and tries to interpret it as an "Adventure"
 * command. Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a two word command. It returns the command
 * as an object of class Command.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 * 
 * @author  Michael Kölling and David J. Barnes and Anton Luca-Dorin
 * @version 2017.12.08
 */
public class Parser 
{
    private CommandWords commands;  // holds all valid command words
    private Scanner reader;         // source of command input

    /**
     * Create a parser to read from the terminal window.
     */
    public Parser() 
    {
        commands = new CommandWords();
        reader = new Scanner(System.in);
    }

    /**
     * @return The next command from the user.
     */
    public Command getCommand() 
    {
        String inputLine;   // will hold the full input line
        ArrayList<String> command = new ArrayList<>();

        System.out.print("> ");     // print prompt

        inputLine = reader.nextLine();
        
        Scanner tokenizer = new Scanner(inputLine);
        while(tokenizer.hasNext()) {
            String word = tokenizer.next();
            command.add(word);
        }


        if(command.size() != 0 && commands.isCommand(command.get(0))) {
            return new Command(command);
       }
       else{
            command.add(0, "NO_COMMAND_WORD");
            return new Command(command);
        }

    }
    
    /**
     * @return input the unprocessed user input
     */
    public String getRawInputString() {
        return reader.nextLine();
    }
    
    /**
     * Print out a list of valid command words.
     */
    public void showCommands()
    {
        commands.showAll();
    }
}
