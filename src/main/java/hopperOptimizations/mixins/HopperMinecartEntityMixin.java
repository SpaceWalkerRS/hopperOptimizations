package hopperOptimizations.mixins;


import hopperOptimizations.utils.HopperHelper;
import hopperOptimizations.utils.IHopper;
import hopperOptimizations.utils.inventoryOptimizer.InventoryOptimizer;
import net.minecraft.entity.vehicle.HopperMinecartEntity;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(HopperMinecartEntity.class)
public class HopperMinecartEntityMixin implements IHopper {
    private int this_lastChangeCount_Extract;
    private InventoryOptimizer previousExtract;
    private int previousExtract_lastChangeCount;
    private boolean previousExtract_causeMarkDirty;

    /**
     * Checks whether the last item extract attempt was with the same inventory as the current one AND
     * since before the last item transfer attempt the hopper's inventory and the other inventory did not change.
     * Requires optimizedInventories.
     *
     * @param thisOpt  InventoryOptimizer of this hopper
     * @param other    Inventory interacted with
     * @param otherOpt InventoryOptimizer of other
     *                 <p>
     *                 Side effect: Sends comparator updates that would be sent on normal failed transfers.
     * @return Whether the current item transfer attempt is known to fail.
     */
    @Override
    public boolean tryShortcutFailedExtract(InventoryOptimizer thisOpt, Inventory other, InventoryOptimizer otherOpt) {
        int thisChangeCount = thisOpt.getInventoryChangeCount();
        int otherChangeCount = otherOpt.getInventoryChangeCount();
        if (this_lastChangeCount_Extract != thisChangeCount || otherOpt != previousExtract || previousExtract_lastChangeCount != otherChangeCount) {
            this_lastChangeCount_Extract = thisChangeCount;
            previousExtract = otherOpt;
            previousExtract_lastChangeCount = otherChangeCount;
            previousExtract_causeMarkDirty = false;
            return false;
        }
        if (previousExtract_causeMarkDirty)
            HopperHelper.markDirtyLikeHopperWould(other, otherOpt, null); //failed transfers sometimes cause comparator updates

        return true;
    }

    @Override
    public void setMarkOtherDirty() {
        this.previousExtract_causeMarkDirty = true;
    }
}
