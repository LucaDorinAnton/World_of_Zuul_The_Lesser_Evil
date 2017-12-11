
/**
 * "HpBoostItem" is a subclass of the "Item" class and
 * a part of the "World of Zuul: The Lesser Evil" game
 * 
 * It models an Item like food or first aid
 * which can replenish your HP
 * 
 * @author Anton Luca-Dorin
 * @version 2017.12.09
 */
public class HpBoostItem extends Item
{
    private int hpBoost;
    /**
     * Create a HpBoostItem
     * @param name the name of the HpBoostItem
     * @param desc the description of the HpBoostItem
     * @param movable if the HpBoostItem can be moved or not
     * @param weight the weight of the HpBoostItem
     * @param value how many Hit Points the HpBoostItem restores
     */
    public HpBoostItem(String name, String desc, boolean movable, int weight, int value) {
        super(name, desc, movable, weight);
        this.hpBoost = value;
    }
    
    /**
     * Override the stub method of the parent class.
     * @return hpBoost the Hit Points this HpBoostItem restores
     */
    @Override
    public int getHpBoost() {
        return hpBoost;
    }

}
