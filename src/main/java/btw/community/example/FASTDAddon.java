package btw.community.example;

import btw.AddonHandler;
import btw.BTWAddon;

public class FASTDAddon extends BTWAddon {
    private static FASTDAddon instance;

    private FASTDAddon() {
        super("BTW FASTD", "1.0.2", "FASTD");
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
    }

    public static FASTDAddon getInstance() {
        if (instance == null)
            instance = new FASTDAddon();
        return instance;
    }
}
