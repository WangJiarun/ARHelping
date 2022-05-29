package arfox.helpings.mixin.mixins;

import joptsimple.OptionSet;
import net.minecraft.server.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class MainMixin {
    @Inject(at = @At("HEAD"), method = "main")
    public static void main(OptionSet optionset, CallbackInfo info) {
        System.out.println("Hello Mixin");
    }
}
