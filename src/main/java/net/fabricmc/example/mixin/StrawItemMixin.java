package net.fabricmc.example.mixin;

import btw.item.items.StrawItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StrawItem.class)
public class StrawItemMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void onConstruct(int iItemID, CallbackInfo ci) {
        // Cast to StrawItem and call the setAsBasicPigFood() method
        ((StrawItem) (Object) this).setAsBasicPigFood();
    }
}
