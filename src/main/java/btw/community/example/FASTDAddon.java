package btw.community.example;

import btw.AddonHandler;
import btw.BTWAddon;

import java.util.Map;

public class FASTDAddon extends BTWAddon {
    private static FASTDAddon instance;
    private static int CowDungTime;
    private static int PigDungTime;
    private static int SheepDungTime;
    private static int WolfDungTime;
    private static int FishTimer;
    private static int HempSeedChance;
    private static int WheatSeedChance;

    private FASTDAddon() {
        super("BTW FASTD Conf", "2.0.0", "FASTDconf");
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
        this.registerProperty("CowDungTime", "13200", "Average amount of ticks to produce dung");
        this.registerProperty("PigDungTime", "14400", "Average amount of ticks to produce dung");
        this.registerProperty("SheepDungTime", "14400", "Average amount of ticks to produce dung");
        this.registerProperty("WolfDungTime", "10800", "Average amount of ticks to produce dung");
        this.registerProperty("FishTimer", "350", "Average amount of ticks to fish");
        this.registerProperty("HempSeedChance", "16", "Chance to get hemp seeds");
        this.registerProperty("WheatSeedChance", "60", "Chance to get wheat seeds");
        Map<String, String> config = this.loadConfigProperties();
        if (config != null) {
            this.handleConfigProperties(config);
        } else {
            AddonHandler.logWarning("Failed to load config properties!");
        }

        if (config != null) {
            this.repopulateConfigFile(config);
        } else {
            AddonHandler.logWarning("Failed to repopulate config file!");
        }

    }
    public void handleConfigProperties(Map<String, String> propertyValues) {
        CowDungTime = Integer.parseInt(propertyValues.get("CowDungTime"));
        PigDungTime = Integer.parseInt(propertyValues.get("PigDungTime"));
        SheepDungTime = Integer.parseInt(propertyValues.get("SheepDungTime"));
        WolfDungTime = Integer.parseInt(propertyValues.get("WolfDungTime"));
        FishTimer = Integer.parseInt(propertyValues.get("FishTimer"));
        HempSeedChance = Integer.parseInt(propertyValues.get("HempSeedChance"));
        WheatSeedChance = Integer.parseInt(propertyValues.get("WheatSeedChance"));
    }
    public static int getCowDungTime() { return CowDungTime; }
    public static int getPigDungTime() { return PigDungTime; }
    public static int getSheepDungTime() { return SheepDungTime; }
    public static int getWolfDungTime() { return WolfDungTime; }
    public static int getFishTimer() { return FishTimer; }
    public static int getHempSeedChance() { return HempSeedChance; }
    public static int getWheatSeedChance() { return WheatSeedChance; }

    public static FASTDAddon getInstance() {
        if (instance == null)
            instance = new FASTDAddon();
        return instance;
    }
}
