package me.srgantmoomoo.postman.client.module.modules.pvp;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import me.srgantmoomoo.postman.client.module.Category;
import me.srgantmoomoo.postman.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoClicker extends Module {
	
	private Minecraft mc = Minecraft.getMinecraft();
	
	public boolean on;
	
	private long lastClick;
	private long hold;
	
	private double speed;
	private double holdLength;
	
	public AutoClicker() {
		super ("autoClicker", "clicks fast when holding down left click", Keyboard.KEY_NONE, Category.PVP);
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.RenderTickEvent e) {
		if(on) {
		if(Mouse.isButtonDown(0)) {
			if(System.currentTimeMillis() - lastClick > speed * 1000) {
				lastClick = System.currentTimeMillis();
				if(hold < lastClick) {
					hold = lastClick;
				}
						int key = mc.gameSettings.keyBindAttack.getKeyCode();
						KeyBinding.setKeyBindState(key, true);
						KeyBinding.onTick(key);
					} else if (System.currentTimeMillis() - hold > holdLength * 1000) {
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
					}
				}
			}
	}
	
	public void onEnable() {
		super.onEnable();
		on = true;
	}
	
	public void onDisable() {
		super.onDisable();
		on = false;
	}

}
