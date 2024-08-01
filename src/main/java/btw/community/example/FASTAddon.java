package btw.community.example;

import btw.AddonHandler;
import btw.BTWAddon;

public class FASTAddon extends BTWAddon {
    private static FASTAddon instance;

    private FASTAddon() {
        super("BTW FAST", "1.0.1", "FAST");
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
    }

    public static FASTAddon getInstance() {
        if (instance == null)
            instance = new FASTAddon();
        return instance;
    }
}
