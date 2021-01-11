package me.srgantmoomoo.postman.api.mixin.mixins;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.srgantmoomoo.postman.api.event.events.PlayerMoveEvent;
import me.srgantmoomoo.postman.api.event.events.PlayerUpdateEvent;
import me.srgantmoomoo.postman.client.Main;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {

	public MixinEntityPlayerSP() {
		super(null, null);
	}
	
	 @Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true)
	    public void onUpdate(CallbackInfo p_Info)
	    {
	        PlayerUpdateEvent event = new PlayerUpdateEvent();
	        Main.EVENT_BUS.post(event);
	        if (event.isCancelled())
	            p_Info.cancel();
	    }

	@Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
	public void move(AbstractClientPlayer player, MoverType type, double x, double y, double z) {
		PlayerMoveEvent moveEvent = new PlayerMoveEvent(type, x, y, z);
		Main.EVENT_BUS.post(moveEvent);
		super.move(type, moveEvent.x, moveEvent.y, moveEvent.z);
	}
}