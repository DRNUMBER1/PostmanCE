package me.srgantmoomoo.postman.client;

import java.awt.Font;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import me.srgantmoomoo.postman.api.event.EventProcessor;
import me.srgantmoomoo.postman.api.proxy.CommonProxy;
import me.srgantmoomoo.postman.api.save.SaveLoad;
import me.srgantmoomoo.postman.api.util.Reference;
import me.srgantmoomoo.postman.api.util.font.CustomFontRenderer;
import me.srgantmoomoo.postman.client.module.Module;
import me.srgantmoomoo.postman.client.module.ModuleManager;
import me.srgantmoomoo.postman.client.notification.Notification;
import me.srgantmoomoo.postman.client.setting.SettingsManager;
import me.srgantmoomoo.postman.client.ui.TabGui;
import me.srgantmoomoo.postman.client.ui.clickgui.ClickGui;
import me.srgantmoomoo.postman.client.ui.clickgui.ClickGuiConfig;
import me.zero.alpine.EventBus;
import me.zero.alpine.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

/*
 * Written by @SrgantMooMoo on 11/17/20.
 */

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
public class Main {
	public static ArrayList<Module> modules;
	
	int strong;
	int postman = strong;
	
	public static ModuleManager moduleManager;
	public static SettingsManager settingsManager;
	public static SaveLoad saveLoad;
	public ClickGuiConfig clickGuiConfig;
	public ClickGui clickGui;
	public static TabGui tabGui;
	public EventProcessor eventProcessor;
	public static Notification notification;
	public CustomFontRenderer customFontRenderer;
	
	public static final Logger log = LogManager.getLogger("postman");
	
	public static final EventBus EVENT_BUS = new EventManager();
	
	@Instance
	public static Main instance;
	
	public Main() {
		instance = this;
	}
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public void PreInit (FMLPreInitializationEvent event) {
		
	}
	
	@EventHandler
	public void init (FMLInitializationEvent event) {
		eventProcessor = new EventProcessor();
		eventProcessor.init();
		log.info("event system initialized.");
		
		customFontRenderer = new CustomFontRenderer(new Font("Verdana", Font.PLAIN, 18), true,true);
		log.info("Custom font initialized!");
		
		MinecraftForge.EVENT_BUS.register(this);
		log.info("minecraft forge events initialized.");
		
		notification = new Notification(null, null, null, 0);
		log.info("notification system initialized.");
		
		settingsManager = new SettingsManager();
		log.info("settings system initialized.");
		
		moduleManager = new ModuleManager();
		log.info("module system initialized.");
		
		MinecraftForge.EVENT_BUS.register(new TabGui());
		tabGui = new TabGui();
		log.info("user interface initialized.");
		
		clickGui = new ClickGui();
		log.info("clickGui initialized!");
		
		saveLoad = new SaveLoad();
		log.info("configs initialized.");
		
		log.info("postman initialization finished");
	
	} //pp
	
	@EventHandler
	public void PostInit (FMLPostInitializationEvent event) {
		
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	@SubscribeEvent
	public void key(KeyInputEvent e) {
		if(Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().player == null)
			return;
		try {
			if(Keyboard.isCreated()) {
				if(Keyboard.getEventKeyState()) {
					int keyCode = Keyboard.getEventKey();
					if(keyCode <= 0)
						return;
					for(Module m : ModuleManager.modules) {
						if(m.getKey() == keyCode && keyCode > 0) {
							m.toggle();
						}
					}
				}
			}
		} catch (Exception q) { q.printStackTrace(); }
	}
}
