
/**
 * "Quest" is a part of the "World of Zuul: The Lesser Evil" game.
 * 
 * It stores information about a quest the player has to complete.
 * For example: a quest can have :
 *  a starting item(an item which the player receives when the quest starts)
 *  a requirement(an item which the player has to aquire to finish the quest)
 *  a reward( an item which the player receives when the player finishes the quest)
 *
 * @author Anton Luca-Dorin
 * @version 2017.12.08
 */
public class Quest
{
    private String name;
    private String desc;
    private boolean mainQuest; // if the quest is a main Quest or just a Side Quest
    private Item startItem;
    private Item reward;
    private Item requirement;
    private int count; // how many copies of the requirement are required
    private boolean completed; // quest finished
    private boolean started; //quest started
    private boolean requirementGiven; // true if the player has received the quest requirement
    private Entity contractor; // Who has given the quest
    
    
    /**
     * Create a Quest
     * @param name the name of the quest
     * @param mainQuest if the quest is a Main Quest or just a Side Quest
     * @param startItem the item which the player receives upon starting the quest
     * @param reward the item the player receives when finishing the quest
     * @param requirement the item the player needs to finish the quest
     * @param count how many copies of the requirement are required in order to finish the quest
     * @param contractor the character who gives the quest
     * @param requirementGiven if the player has received the item required to finish the Quest
     */
    public Quest(String name,String desc, boolean mainQuest, Item startItem, Item reward, Item requirement, int count,  Entity contractor, boolean  requirementGiven) {
        this.name = name;
        this.desc = desc;
        this.mainQuest = mainQuest;
        this.startItem = startItem;
        this.reward = reward;
        this.requirement = requirement;
        this.count = count;
        this.contractor = contractor;
        this.completed = false;
        this.started = false;
        this.requirementGiven = requirementGiven;
    }
    
    /**
     * begin the quest
     */
    public void startQuest() {
        started = true;
    }
    
    /**
     * end the quest
     */
    public void finishQuest() {
     this.completed = true;   
    }
    
    /**
     * @return the name of this quest
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return true if the quest hasstarted
     */
    public boolean getStarted() {
        return started;
    }
    
    /**
     * @return contractor the character who has given this quest
     */
    public Entity getContractor() {
        return contractor;
    }
    
    /**
     * @return item the reward for the player for finishing the quest
     */
    public Item getReward() {
        return reward;
    }
    
    /**
     * @return item the item the player receives upon starting the quest
     */
    public Item getStartItem() {
        return startItem;
    }
    
    /**
     * @return true if the quest has been completed
     */
    public boolean getCompleted() {
        return completed;
    }
    
    /**
     * @return item the item required in order to finish this quest
     */
    public Item getRequirement() {
        return requirement;
    }
    
    /**
     * @return how many copies of the requirement are required in order to finish the quest
     */
    public int getCount() {
        return count;
    }
    
    /**
     * @return true if the player has received the requirement item for this quest
     */
    public boolean getRequirementGiven() {
        return requirementGiven;
    }
    
    /**
     * Denote that the player has received the requirement for this quest
     */
    public void setRequirementGiven() {
     requirementGiven = true;   
    }
    
    /**
     * return true if this quest is a main Quest
     */
    public boolean isMainQuest() {
        return mainQuest;
    }
    
    /**
     * @return desc the decription of this Quest
     */
    public String getDesc() {
        return desc;   
    }
}
