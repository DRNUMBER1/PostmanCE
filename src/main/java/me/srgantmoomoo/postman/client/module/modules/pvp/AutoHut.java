package me.srgantmoomoo.postman.client.module.modules.pvp;

import org.lwjgl.input.Keyboard;

import me.srgantmoomoo.postman.client.module.Category;
import me.srgantmoomoo.postman.client.module.Module;

public class AutoHut extends Module {
	
	public AutoHut() {
		super ("autoHut", "draws esp around storage blocks", Keyboard.KEY_NONE, Category.PVP);
	}

}
