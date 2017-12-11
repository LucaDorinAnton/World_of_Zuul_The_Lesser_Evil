import java.util.ArrayList;
/**
 * This class is part of the "World of Zuul:The Lesser Evil" game.
 *
 * This class holds information about a command that was issued by the user.
 * A command can consist of as many words as needed (for example: "inspect" or
 * "inventory use 3")
 * 
 * The way this is used is: Commands are already checked for being valid
 * command words. If the user entered an invalid command (a word that is not
 * known) then the command word is <"NO_COMMAND_WORD">.
 *
 * If the command had only one word, then the second word is <"NO_COMMAND_WORD">.
 * 
 * @author  Michael Kölling and David J. Barnes and Anton Luca-Dorin
 * @version 2017.12.08
 */

public class Command
{
    private ArrayList<String> command;

    /**
     * Create a command object.
     * @param command the ArrayList containing the individual commands
     */
    public Command(ArrayList<String> command)
    {
        this.command = command;
    }

    /**
     * Return the command word (the first word) of this command. If the
     * command was not understood, the result is "NO_COMMAND_WORD".
     * @return The command word.
     */
    public String getCommandWord() {
            return command.get(0);
    }
    
    /**
     * @param index the index at which we want to acces the command word
     * @return the command word at the specified index
     */
    public String getCommandWordAtIndex(int index) {
        if(command.size() >= index + 1) {
            return command.get(index);
        } else {
            return "NO_COMMAND_WORD";
        }
    }

    /**
     * @return The second word of this command. Returns "NO_COMMAND_WORD" if there was no
     * second word.
     */
    public String getSecondWord()
    {
        if(command.size() > 1) {
            return command.get(1);
        } else {
            return "NO_COMMAND_WORD";   
        }
    }

    /**
     * @return true if this command was not understood.
     */
    public boolean isUnknown()
    {
        if(command.size() != 0 && command.get(0) != "NO_COMMAND_WORD") {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @return true if the command has a second word.
     */
    public boolean hasSecondWord()
    {
        if(command.size() <= 1) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * @param index the index we want to search for a word at
     * @return true if the command has a word at the specified index
     */
    public boolean hasWordAtIndex(int index) {
        if(command.size() <= index) {
            return false;
        } else {
            return true;
        }
    }
}

