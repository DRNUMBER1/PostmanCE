package me.srgantmoomoo.postman.client.module.modules.player;

import java.util.Arrays;

import org.lwjgl.input.Keyboard;

import me.srgantmoomoo.postman.client.module.Category;
import me.srgantmoomoo.postman.client.module.Module;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatWatermark extends Module {
	public boolean on;
	
	public ChatWatermark() {
		super ("chatSuffix", "pp", Keyboard.KEY_NONE, Category.PLAYER);
		this.addSettings();
	}

	@SubscribeEvent
	public void onChat(final ClientChatEvent event)
	{
		if(on) {
		for (final String s : Arrays.asList("/", ".", "-", ",", ":", ";", "'", "\"", "+", "\\", "@"))
		{
			if (event.getMessage().startsWith(s)) return;
		}
		event.setMessage(event.getMessage() + " " + "\\u007c" + " " + "postman strong"); 
	}
	}
	public void onEnable() {
		super.onEnable();
		on = true;
	}
	
	public void onDisbale() {
		super.onDisable();
		on = false;
	}
}
