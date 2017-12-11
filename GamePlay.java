import java.util.Random;
import java.util.ArrayList;
import java.util.Iterator;
/**
 * "GamePlay" is a part of the "World of Zull: The Lesser Evil" game.
 * 
 * It models the logic behind the levels of the game, which can take place in some rooms.
 * GamePlay Objects gather Information about the player and construct a level, through which
 * the player must navigate and kill enemies. Tha player can move (using WASD),
 * attack/dash (using Shift + WASD) and shoot a Shotgun Shell (using Alt + WASD).
 * The enemies move randomly around the level as long as they are far away from the player.
 * If they get closer to the player, they will start to pursue him. If they get near him, they
 * will damage him. If the player runs out of Hit Points, he will lose the game. When all enemies
 * have been killed, the level ends.
 * 
 * A GamePlayGUI object in order to display the level to the user and capture his input.
 *
 * @author Anton Luca-Dorin
 * @version 2017.12.08
 */
public class GamePlay
{
    private int[][] display; //main matrix of the level
    private boolean finished; // true if the level is done
    private int playerXPos; 
    private int playerYPos;
    private int maxEnemiesOnScreen;
    private int totalEnemies;
    private int totalEnemiesSpawned; // how many enemies have been spwaned in the game up to a point in time
    private int layout; // 0-4, specifies which level layout to use
    private int displaySize;
    private int playerHealth;
    private int maxPlayerHealth;
    private int shells;
    private int dashRecharge; // 0-3, the player can only use Dash if it is 3
    private boolean hasShotgun;
    private String playerName;
    private Random random;
    private ArrayList<Enemy> enemies;
    private GamePlayGUI displayFrame;

    /**
     * Create a GamePlay object
     * @param displaySize the size of display
     * @param totalEnemies how many enemies the level will have
     * @param maxEnemiesOnScreen the maximum amount of enemies which can exist at some point
     * @param layout the level layout to be used
     */
    public GamePlay(int displaySize, int totalEnemies, int maxEnemiesOnScreen, int layout) {
        display = new int[displaySize][displaySize];
        this.displaySize = displaySize;
        this.totalEnemies = totalEnemies;
        this.maxEnemiesOnScreen = maxEnemiesOnScreen;
        this.layout = layout;
        this.playerName = playerName;
        playerXPos = 22;//temp values
        playerYPos = 22; //temp values
        totalEnemiesSpawned = 0;
        dashRecharge = 3;
        enemies = new ArrayList<>();
        random = new Random();
        finished = false;
        initializeDisplay();
        displayFrame = new GamePlayGUI(displaySize, this);

    }

    /**
     * Get all other required information in order to successfully run the level
     * This is not included in the constructer because some of the information might not be available
     * at the time of construction.
     * @param playerHealth the health of the player
     * @param maxPlayerHealth the maximum health of the player
     * @param shells the number of Shotgun shells the player has
     * @param hasShotgun if the player has aquired the Shotgun item
     * @param playerName the name of the player
     */
    public void setDetails(int playerHealth, int maxPlayerHealth, int shells, boolean hasShotgun, String playerName) {
        this.playerHealth = playerHealth;
        this.maxPlayerHealth = maxPlayerHealth;
        this.shells = shells;
        this.hasShotgun = hasShotgun;
        updateDisplay();
    }

    /**
     * Get the level layout from the WallLoader
     * Initialize the player
     * load in the Enemies
     */
    private void initializeDisplay() {
        display = WallLoader.getWalls(displaySize, layout);
        setPlayer(23,23);
        loadEnemies();
    }

    /**
     * Set the location of the Player 
     * @param x the new x Position of the player
     * @param y the new y position of the Player
     */
    private void setPlayer(int x, int y) {
        display[x][y] = 3;
        display[playerXPos][playerYPos] = 0;
        playerXPos = x;
        playerYPos = y;

    }

    /**
     * Load in all the Enemies
     */
    private void loadEnemies() {
        for(int i=0;i<totalEnemies;i++) {
            spawnAnEnemy();  
            if (i == maxEnemiesOnScreen) {
                break;
            }
        }

    }

    /**
     * randomly select point from the display matrix
     * if they are a valid spawning point, spawn an enemy there
     * do this until enough enemies have spawned
     */
    private void spawnAnEnemy() {
        while (true) {
            int x = random.nextInt(23) + 1;
            int y = random.nextInt(23) + 1;
            if(display[x][y] == 0 && (x<playerXPos-3 || x>playerXPos+3) || (y<playerYPos-3 || y> playerYPos+3)) {
                enemies.add(new Enemy(x, y));
                display[x][y] = 2;
                totalEnemiesSpawned++;
                break;
            }
        }
    }

    /**
     * if more enemies need to spawn,
     * spawn them in
     */
    private void addEnemies() {
        int dif = maxEnemiesOnScreen - enemies.size();
        if(dif > 0 && totalEnemiesSpawned < totalEnemies) {
            int amount = random.nextInt(dif) +1;
            for(int i=0;i<amount;i++) {
                spawnAnEnemy();
            }
        }
    }

    /**
     * update the GUI
     */
    private void updateDisplay() {
        displayFrame.update();
    }

    /**
     * main Play routine
     * gets called automatically by the KeyListener from the GUI
     * @param dir the direction in which the command goes ("UP", "DOWN", "LEFT", RIGHT")
     * @param shift if shift was held down when the key was released
     * @param alt if alt was held down when the key was released
     */
    public void play(String dir, boolean shift, boolean alt) {
        if(!shift && !alt) {
            // move the Player normally
            movePlayer(dir);
            addEnemies();
            moveEnemies();
            if(dashRecharge < 3) {
                dashRecharge++;
            }
            displayFrame.setExtraText("");
            updateDisplay();
        } else if(shift && !alt) {
            //try to perfom a dash
            dash(dir);
            addEnemies();
            moveEnemies();
            updateDisplay();
        } else if(!shift && alt) {
            //try to shoot a shotgun shell
            shoot(dir);
            addEnemies();
            moveEnemies();
            if(dashRecharge < 3) {
                dashRecharge++;
            }
            updateDisplay();
        }
        // if the user held down both shift and alt, we assume it was a mispress and ignore it
    }

    /**
     * change the location of the player
     * @param dir the diraction in which the player moves
     */
    private void movePlayer(String dir) {
        if(dir.equals("UP")) {
            if(display[playerXPos-1][playerYPos] == 0) {
                setPlayer(playerXPos-1, playerYPos);
            }
        } else if(dir.equals("DOWN")) {
            if(display[playerXPos+1][playerYPos] == 0) {
                setPlayer(playerXPos+1, playerYPos);
            }
        } else if(dir.equals("LEFT")) {
            if(display[playerXPos][playerYPos-1] == 0) {
                setPlayer(playerXPos, playerYPos-1);
            }
        }else {
            if(display[playerXPos][playerYPos+1] == 0) {
                setPlayer(playerXPos, playerYPos+1);
            }
        }
    }

    /**
     * perfom a dash attack
     * a dash is a jump of (maximum) 3 points in a direction
     * if the player jumps over any enemies, the enemies get killed
     * @param dir the direction in which the dash is performed
     */
    private void dash(String dir) {
        if(dashRecharge == 3) {
            //the dash is ready to go
            int dist = 0;
            int x = playerXPos;
            int y = playerYPos;
            if(dir.equals("UP")) {
                //see how far the player can go
                if(display[x-1][y] != 1) {
                    dist++;
                    if(display[x-2][y] != 1) {
                        dist++;
                        if(display[x-3][y] != 1) {
                            dist++;
                        }

                    }

                }

                if(dist == 0) {
                    displayFrame.setExtraText("You can't dash through walls!");
                } else {
                    //remove any enemies player will jump over
                    Iterator<Enemy> it = enemies.iterator();
                    while(it.hasNext()) {
                        Enemy enemy = it.next();
                        if(enemy.getY() == y && (enemy.getX() < x && enemy.getX() >= x-dist) ) {
                            display[enemy.getX()][enemy.getY()] =0;
                            it.remove();
                        }
                    }
                    //perform the jump
                    setPlayer(x-dist, y);
                    //the dash needs to recharge now
                    dashRecharge = 0;
                    displayFrame.setExtraText("");
                }
            } else if(dir.equals("DOWN")) {
                //see how far the player can go
                if(display[x+1][y] != 1) {
                    dist++;
                    if(display[x+2][y] != 1) {
                        dist++;
                        if(display[x+3][y] != 1) {
                            dist++;
                        }

                    }

                }

                if(dist == 0) {
                    displayFrame.setExtraText("You can't dash through walls!");
                } else {
                    //remove any enemies player will jump over
                    Iterator<Enemy> it = enemies.iterator();
                    while(it.hasNext()) {
                        Enemy enemy = it.next();
                        if(enemy.getY() == y && (enemy.getX() > x && enemy.getX() <= x+dist) ) {
                            display[enemy.getX()][enemy.getY()] =0;
                            it.remove();
                        }
                    }
                    //perform the jump
                    setPlayer(x+dist, y);
                    //the dash needs to recharge now
                    dashRecharge = 0;
                    displayFrame.setExtraText("");
                }
            } else if(dir.equals("LEFT")) {
                //see how far the player can go
                if(display[x][y-1] != 1) {
                    dist++;
                    if(display[x][y-2] != 1) {
                        dist++;
                        if(display[x][y-3] != 1) {
                            dist++;
                        }

                    }

                }

                if(dist == 0) {
                    displayFrame.setExtraText("You can't dash through walls!");
                } else {
                    //remove any enemies player will jump over
                    Iterator<Enemy> it = enemies.iterator();
                    while(it.hasNext()) {
                        Enemy enemy = it.next();
                        if(enemy.getX() == x && (enemy.getY() < y && enemy.getY() >= y-dist) ) {
                            display[enemy.getX()][enemy.getY()] =0;
                            it.remove();
                        }
                    }
                    //perform the jump
                    setPlayer(x, y-dist);
                    //the dash needs to recharge now
                    dashRecharge = 0;
                    displayFrame.setExtraText("");
                }
            } else if(dir.equals("RIGHT")) {
                //see how far the player can go
                if(display[x][y+1] != 1) {
                    dist++;
                    if(display[x][y+2] != 1) {
                        dist++;
                        if(display[x][y+3] != 1) {
                            dist++;
                        }

                    }

                }

                if(dist == 0) {
                    displayFrame.setExtraText("You can't dash through walls!");
                } else {
                    //remove any enemies player will jump over
                    Iterator<Enemy> it = enemies.iterator();
                    while(it.hasNext()) {
                        Enemy enemy = it.next();
                        if(enemy.getX() == x && (enemy.getY() > y && enemy.getY() <= y+dist) ) {
                            display[enemy.getX()][enemy.getY()] =0;
                            it.remove();
                        }
                    }
                    //perform the jump
                    setPlayer(x, y+dist);
                    //the dash needs to recharge now
                    dashRecharge = 0;
                    displayFrame.setExtraText("");
                }
            }
        } else {
            //dash not ready
            dashRecharge++;
            displayFrame.setExtraText("Dash not yet charged!");
        }
    }

    /**
     * try to shoot a shotgun shell in a direction
     * the success of this action depends on if the player has a shotgun and 
     * at least one shell.
     * If he can shoot, a shotgun shell will travel along a straight line
     * and kill any enemies it meets in it's way 
     * until it hits a wall.
     * @param dir the direction in which the shotgun shell will be fired
     */
    private void shoot(String dir) {
        if(!hasShotgun) {
            displayFrame.setExtraText("You don't have a weapon!");
        } else  {
            if(shells == 0) {
                displayFrame.setExtraText("You don't have any shells!");
            } else {
                displayFrame.setExtraText("");
                shells--;
                if(dir.equals("UP")) {
                    int x = playerXPos;
                    int y = playerYPos;
                    while(true) {
                        if(display[x-1][y] == 1) {
                            //The shell hit a wall
                            break;
                        } else {
                            if(display[x-1][y] == 2) {
                                //the shell encountered an enemy, so it will kill it
                                Iterator<Enemy> it = enemies.iterator();
                                while(it.hasNext()) {
                                    Enemy enemy = it.next();
                                    if(enemy.getX() == x-1 && enemy.getY() == y) {
                                        display[enemy.getX()][enemy.getY()] = 0;
                                        it.remove();
                                        break;
                                    }
                                }
                            }
                            //update the location of the shell
                            x = x-1;
                        }
                    }
                }else if(dir.equals("DOWN")) {
                    int x = playerXPos;
                    int y = playerYPos;
                    while(true) {
                        if(display[x+1][y] == 1) {
                            //The shell hit a wall
                            break;
                        } else {
                            if(display[x+1][y] == 2) {
                                //the shell encountered an enemy, so it will kill it
                                Iterator<Enemy> it = enemies.iterator();
                                while(it.hasNext()) {
                                    Enemy enemy = it.next();
                                    if(enemy.getX() == x+1 && enemy.getY() == y) {
                                        display[enemy.getX()][enemy.getY()] = 0;
                                        it.remove();
                                        break;
                                    }
                                }
                            }
                            //update the location of the shell
                            x = x+1;
                        }
                    }
                } else if(dir.equals("LEFT")) {
                    int x = playerXPos;
                    int y = playerYPos;
                    while(true) {
                        if(display[x][y-1] == 1) {
                            //The shell hit a wall
                            break;
                        } else {
                            if(display[x][y-1] == 2) {
                                //the shell encountered an enemy, so it will kill it
                                Iterator<Enemy> it = enemies.iterator();
                                while(it.hasNext()) {
                                    Enemy enemy = it.next();
                                    if(enemy.getX() == x && enemy.getY() == y-1 ){
                                        display[enemy.getX()][enemy.getY()] = 0;
                                        it.remove();
                                        break;
                                    }
                                }
                            }
                            //update the location of the shell
                            y = y-1;
                        }
                    }
                }else if(dir.equals("RIGHT")) {
                    int x = playerXPos;
                    int y = playerYPos;
                    while(true) {
                        if(display[x][y + 1] == 1) {
                            //The shell hit a wall
                            break;
                        } else {
                            if(display[x][y + 1] == 2) {
                                //the shell encountered an enemy, so it will kill it
                                Iterator<Enemy> it = enemies.iterator();
                                while(it.hasNext()) {
                                    Enemy enemy = it.next();
                                    if(enemy.getX() == x && enemy.getY() == y + 1) {
                                        display[enemy.getX()][enemy.getY()] = 0;
                                        it.remove();
                                        break;
                                    }
                                }
                            }
                            //update the location of the shell
                            y = y+1;
                        }
                    }
                }
            }
        }
    }

    /**
     * Change the location of all the Enemies which can move
     * this method gets called whenever the player executes an action
     */
    private void moveEnemies() {
        for(Enemy enemy : enemies) {
            int x = enemy.getX();
            int y = enemy.getY();

            if((x<playerXPos-6 || x>playerXPos+6) || (y<playerYPos-6 || y> playerYPos+6)) {
                //This enemy is far from the player
                moveRandom(enemy);
            } else {
                //This enemy is close to the player
                moveToPlayer(enemy);
            }
        }
    }
    
    /**
     * If the enemy can move, it will move in a random direction
     * else it will stay put
     * @param enemy the enemy which is going to mover andomly
     */
    private void moveRandom(Enemy enemy) {
        int x = enemy.getX();
        int y = enemy.getY();
        if(display[x-1][y] ==0 || display[x+1][y] == 0 || display[x][y-1] == 0 || display[x][y+1] == 0) {
            while(true) {
                // each value of dir is a direction
                int dir = random.nextInt(4);
                if(dir == 0) {
                    if(display[x-1][y] == 0) {
                        display[x-1][y] = 2;
                        display[x][y] = 0;
                        enemy.setXY(x-1, y);
                        break;
                    }
                } else if(dir == 1) {
                    if(display[x+1][y] == 0) {
                        display[x+1][y] = 2;
                        display[x][y] = 0;
                        enemy.setXY(x+1, y);
                        break;
                    }
                } else if(dir == 2) {
                    if(display[x][y-1] == 0) {
                        display[x][y-1] = 2;
                        display[x][y] = 0;
                        enemy.setXY(x, y-1);
                        break;
                    }
                } else {
                    if(display[x][y+1] == 0) {
                        display[x][y+1] = 2;
                        display[x][y] = 0;
                        enemy.setXY(x, y+1);
                        break;
                    }
                }
            }
        }
    }

    /**
     * The enemy will try to get closer to the player
     * @param enemy the enemy which will try to get closer to the player
     */
    private void moveToPlayer(Enemy enemy){
        //find next move in order to get closer to the player
        String nextMove = getNextMove(enemy);
        int x = enemy.getX();
        int y = enemy.getY();
        if(nextMove.equals("NO_WAY")) {
            //the path to the player is blocked, so move randomly
            moveRandom(enemy);
        } else if(nextMove.equals("UP")) {
            display[x+1][y] = 2;
            display[x][y] = 0;
            enemy.setXY(x+1, y);
        } else if(nextMove.equals("DOWN")) {
            display[x-1][y] = 2;
            display[x][y] = 0;
            enemy.setXY(x-1, y);
        } else if(nextMove.equals("LEFT")) {
            display[x][y+1] = 2;
            display[x][y] = 0;
            enemy.setXY(x, y+1);
        } else if(nextMove.equals("RIGHT")) {
            display[x][y-1] = 2;
            display[x][y] = 0;
            enemy.setXY(x, y-1);
        } else if(nextMove.equals("ATTACK")) {
            //the enemy is close enough to the player to attack
            attackPlayer(enemy);
        }

    }

    /**
     * This method uses a variation of Lee's Algorithm 
     * in order to determine the next move towards the player.
     * It uses two helper bi-dimensional Arrays: one for storing distance,
     * and one for storing if the point has been visited.
     * Each point ads it's surrounding not visited points to a queue with 
     * a distance equal to it's own distance plus one from the epicenter( the
     * current enemy location). When it reaches the player, it finds it's way
     * back from the player to the epicenter. The last move (reversed) is 
     * the desired result. If the queue becomes empty, it means that the player 
     * is unreachable. Then the enemy will have to move randomly.
     * 
     * @param enemy the enemy which will try to calculate it's next move towards the player
     * @return dir the direction in which the enemy will need to move in order to get closer to the player(reversed)
     */
    private String getNextMove(Enemy enemy) {
        int[][] paths = new int[displaySize][displaySize];
        boolean[][] visited = new boolean[displaySize][displaySize];
        visited[enemy.getX()][enemy.getY()] =true;
        ArrayList<Node> queue = new ArrayList<>();

        queue.add(new Node(enemy.getX(), enemy.getY(), 0));
        int x = 0;
        int y = 0;
        int dist = 0;
        while(true) {

            if(queue.size() == 0) {
                return "NO_WAY";
            }

            Node currentNode = queue.get(0);
            queue.remove(0);
            x = currentNode.getX();
            y = currentNode.getY();
            dist = currentNode.getDist();
            //Add neighboring nodes to the queue
            //If they are the player, break out from the loop
            if(display[x-1][y] == 3 ) {
                break;
            } else {
                if(display[x-1][y] == 0 && visited[x-1][y] == false ) {
                    visited[x-1][y] = true;
                    paths[x-1][y] = dist+1;
                    queue.add(new Node(x-1, y, dist+1));
                }
            }
            if(display[x+1][y] == 3) { 
                break;
            } else {
                if(display[x+1][y] == 0 && visited[x+1][y] == false) {
                    visited[x+1][y] = true;
                    paths[x+1][y] = dist+1;
                    queue.add(new Node(x+1, y, dist+1));
                }
            }
            if(display[x][y-1] == 3) {

                break;
            } else {
                if(display[x][y-1] == 0 && visited[x][y-1] == false) {
                    visited[x][y-1] = true;
                    paths[x][y-1] = dist+1;
                    queue.add(new Node(x, y-1, dist+1));
                }
            }
            if(display[x][y+1] == 3) {
                break;
            } else {
                if(display[x][y+1] == 0 & visited[x][y+1] == false) {
                    visited[x][y+1] = true;
                    paths[x][y+1] = dist +1;
                    queue.add(new Node(x, y+1, dist+1));
                }
            }
        }
        //if the enemy is next to the player, the while loop will iterate 0 times
        String dir = "ATTACK";
        while(x != enemy.getX() || y != enemy.getY()) {
            //try to find the next step backwards
            if(paths[x-1][y]+1 == paths[x][y] && visited[x-1][y]) {
                x = x-1;
                dir = "UP";
            }else if(paths[x+1][y]+1 == paths[x][y] && visited[x+1][y]) {
                x = x+1;
                dir = "DOWN";
            }else if(paths[x][y-1]+1 == paths[x][y] && visited[x][y-1]) {
                y = y-1;
                dir = "LEFT";
            } else if(paths[x][y+1] +1 == paths[x][y] && visited[x][y+1]){
                y = y+1;
                dir = "RIGHT";
            }
        }

        return dir;

    }

    /**
     * try to damage the player if he is next to an enemy.
     * the damage value received will be somewhat random.
     * 
     * @param enemy the enemy who will try to damage the player
     */
    public void attackPlayer(Enemy enemy) {
        int x = enemy.getX();
        int y = enemy.getY();

        if(display[x+1][y] == 3 || display[x-1][y] == 3 || display[x][y+1] == 3 || display[x][y+1] ==3 ) {
            playerHealth = playerHealth - 5 - random.nextInt(6);
            if(playerHealth <= 0) {
                displayFrame.makeNotVisible();
                gameLost();
            }
        }
    }

    /**
     * @return the player's health
     */
    public int getPlayerHealth() {
        return playerHealth;
    }

    /**
     * @return the player's max health
     */
    public int getMaxPlayerHealth() {
        return maxPlayerHealth;
    }

    /**
     * @return the player's shells
     */
    public int getShells() {
        return shells;
    }

    /**
     * @return the number of enemies currently
     */
    public int getEnemiesCount() {
        return enemies.size();
    }

    /**
     * @return the player's name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * @return the display matrix
     */
    public int[][] getDisplay() {
        return display;
    }

    /**
     * show the GUI
     */
    public void show() {
        displayFrame.makeVisible();
    }

    /**
     * hide the GUI
     */
    public void hide() {
        displayFrame.makeNotVisible();
    }

    /**
     * @return the current dash recharge level
     */
    public int getDashRecharge() {
        return dashRecharge;
    }

    /**
     * auxilliary methode for debugging
     * print's out a bi-dimensional integer array
     * @param mat the matrix to be printed
     */
    private void printMat(int[][] mat) {
        for(int i=0;i<mat.length;i++) {
            for(int j=0;j<mat.length;j++) {
                System.out.print(mat[i][j] + " ");
            }
            System.out.print("\n");
        }
        System.out.println("############################");
    }

    /**
     * auxilliary methode for debugging
     * print's out a bi-dimensional boolean array
     * @param mat the matrix to be printed
     */
    private void printMatBool(boolean[][] mat) {
        for(int i=0;i<mat.length;i++) {
            for(int j=0;j<mat.length;j++) {
                System.out.print(mat[i][j] + " ");
            }
            System.out.print("\n");
        }
        System.out.println("############################");
    }

    /**
     * @return true if the level is finished (a.k.a when there are no more enemies)
     */
    public boolean isDone() {
        if(enemies.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * If the player's health drops below zero,
     * end the game in a Loss scenario
     */
    public void gameLost() {
        System.out.println("The creatures of this land proved to be fiercer than you expected.");
        System.out.println("You died a quick and somewhat painless death.");
        System.exit(1);
    }
}
