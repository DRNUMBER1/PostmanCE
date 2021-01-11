package me.srgantmoomoo.postman.client.module.modules.client;

import java.awt.Color;
import java.awt.Point;

import com.lukflug.panelstudio.hud.HUDList;
import com.lukflug.panelstudio.hud.ListComponent;
import com.lukflug.panelstudio.theme.Theme;
import com.mojang.realmsclient.gui.ChatFormatting;

import me.srgantmoomoo.postman.api.util.render.JColor;
import me.srgantmoomoo.postman.client.module.HudModule;
import me.srgantmoomoo.postman.client.setting.settings.ColorSetting;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;


public class Totems extends HudModule {
	private TotemList list=new TotemList();
	public ColorSetting color = new ColorSetting("color", this, new JColor(103, 167, 221, 255)); 

	public Totems() {
		super("totems", "thatweehoo", new Point(-3,11));
		this.addSettings(color);
	}
	
	   public void onRender() {
	    	list.totems = mc.player.inventory.mainInventory.stream()
	    			.filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING)
	    			.mapToInt(ItemStack::getCount).sum();
	    	if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING)
	    		list.totems++;
	    }
	
	@Override
	public void populate (Theme theme) {
		component = new ListComponent(getName(), theme.getPanelRenderer(), position, list);
	}
	
	private class TotemList implements HUDList {

		public int totems=0;
		
		@Override
		public int getSize() {
			return 1;
		}

		@Override
		public String getItem(int index) {
			return "" + ChatFormatting.GOLD + totems;
		}

		@Override
		public Color getItemColor(int index) {
			return color.getValue();
		}

		@Override
		public boolean sortUp() {
			return false;
		}

		@Override
		public boolean sortRight() {
			return false;
		}
	}
}