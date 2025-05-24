

public class Player {
    private String name;
    private boolean isRed;

    public Player(String name, boolean isRed) {
        this.name = name;
        this.isRed = isRed;
    }

    public String getName() {
        return name;
    }

    public boolean isRed() {
        return isRed;
    }

    public String getColorName() {
        return isRed ? GameConstants.RED + "Red" + GameConstants.RESET : 
                      GameConstants.BLUE + "Blue" + GameConstants.RESET;
    }
}