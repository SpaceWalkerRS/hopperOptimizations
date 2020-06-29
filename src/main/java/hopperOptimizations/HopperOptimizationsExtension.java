package hopperOptimizations;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.settings.SettingsManager;
import hopperOptimizations.settings.Settings;

/**
 * Hopper Optimization Mod
 *
 * @author 2No2Name
 * adapted from the fabric carpet extension example mod.
 * bloom filter contribution originally by skyrising
 */
public class HopperOptimizationsExtension implements CarpetExtension {
    private static final SettingsManager mySettingManager;

    static {
        String myVersion = "0.1.18";
        mySettingManager = new SettingsManager(myVersion, "hopperoptimizations", "Hopper Optimizations Mod");
        CarpetServer.manageExtension(new HopperOptimizationsExtension());
        System.out.println("Hopper optimizations registered to the carpet mod options system!");
    }

    public static void noop() {
    }

    @Override
    public void onGameStarted() {
        // let's /carpet handle our few simple settings
        CarpetServer.settingsManager.parseSettingsClass(Settings.class);
    }

    @Override
    public SettingsManager customSettingsManager() {
        // this will ensure that our settings are loaded properly when world loads
        return mySettingManager;
    }
}
