
/**
 * "MaxHpBoostItem" is a subclass of "Item" and a part
 * of the "World of Zuul: A lesser Evil" game
 * 
 * It models an itam which can increase the player's
 * maxiumum hit points, like a clothing item.
 * 
 * @author Anton Luca-Dorin
 * @version 2017.12.09
 */
public class MaxHpBoostItem extends Item
{
    private int maxHpBoost;
    
    /**
     * Create a MaxHpBoostItem
     * @param name the name of the MaxHpBoostItem
     * @param desc the description of the MaxHpBoostItem
     * @param movable if the MaxHpBoostItem can be moved or not
     * @param weight the weight of the MaxHpBoostItem
     * @param value how many Hit Points the MaxHpBoostItem adds to the maxiumum amount of HP of the player
     */
    public MaxHpBoostItem(String name, String desc, boolean movable, int weight, int value) {
        super(name, desc, movable, weight);
        
        this.maxHpBoost = value;
    }
    
    /**
     * Override the stub method of the parent class.
     * @return maxHpBoost the Hit Points this MaxHpBoostItem adds to the maxiumum amount of HP of the player
     */
    @Override
    public int getMaxHpBoost() {
        return maxHpBoost;
    }

}
