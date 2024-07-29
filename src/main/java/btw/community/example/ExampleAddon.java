package btw.community.example;

import btw.AddonHandler;
import btw.BTWAddon;

public class ExampleAddon extends BTWAddon {
    private static ExampleAddon instance;

    private ExampleAddon() {
        super("BTW FASTD", "1.0.2", "FAST");
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
    }

    public static ExampleAddon getInstance() {
        if (instance == null)
            instance = new ExampleAddon();
        return instance;
    }
}
