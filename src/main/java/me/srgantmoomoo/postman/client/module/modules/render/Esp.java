package me.srgantmoomoo.postman.client.module.modules.render;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.srgantmoomoo.postman.api.event.events.RenderEvent;
import me.srgantmoomoo.postman.api.util.Wrapper;
import me.srgantmoomoo.postman.api.util.render.JColor;
import me.srgantmoomoo.postman.api.util.render.JTessellator;
import me.srgantmoomoo.postman.api.util.world.GeometryMasks;
import me.srgantmoomoo.postman.client.module.Category;
import me.srgantmoomoo.postman.client.module.Module;
import me.srgantmoomoo.postman.client.module.modules.pvp.Surround;
import me.srgantmoomoo.postman.client.setting.settings.BooleanSetting;
import me.srgantmoomoo.postman.client.setting.settings.ModeSetting;
import me.srgantmoomoo.postman.client.setting.settings.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/*
 * Written by @SrgantMooMoo on 11/17/20.
 */

public class Esp extends Module {
	public BooleanSetting chams = new BooleanSetting("chams", this, false);
	public ModeSetting entityMode = new ModeSetting("entity", this, "box", "box", "outline", "2dEsp", "off");
	public ModeSetting storage = new ModeSetting("storage", this, "fill", "fill", "outline", "off");
	public BooleanSetting mob = new BooleanSetting("mob", this, false);
	public BooleanSetting item = new BooleanSetting("item", this, true);
	public NumberSetting range = new NumberSetting("range", this, 100, 10, 260, 10);
	public NumberSetting lineWidth = new NumberSetting("lineWidth", this, 3, 0, 10, 1);
	public NumberSetting pRed = new NumberSetting("plyrRed", this, 0, 0, 250, 10);
	public NumberSetting pGreen = new NumberSetting("plyrGreen", this, 121, 0, 250, 10);
	public NumberSetting pBlue = new NumberSetting("plyrBlue", this, 194, 0, 250, 10);
	
	public Esp() {
		super ("esp's", "draws esp around storage blocks", Keyboard.KEY_NONE, Category.RENDER);
		this.addSettings(entityMode, storage, mob, item, chams, range, lineWidth, pRed, pGreen, pBlue);
	}
	private static final Minecraft mc = Wrapper.getMinecraft();

    JColor playerColor;
    JColor mobColor;
    JColor mainIntColor;
    JColor containerColor;
    JColor containerBox;
    int opacityGradient;

    public void onWorldRender(RenderEvent event) {
    	
        mc.world.loadedEntityList.stream().filter(entity -> entity != mc.player).filter(entity -> rangeEntityCheck(entity)).forEach(entity -> {
            defineEntityColors(entity);
            if (entityMode.getMode().equals("box") && entity instanceof EntityPlayer) {
            	JTessellator.playerEsp(entity.getEntityBoundingBox(), (float) lineWidth.getValue(), playerColor);
            }
            if (mob.isEnabled() && !entityMode.getMode().equals("outline") && !entityMode.getMode().equals("off")){
                if (entity instanceof EntityCreature || entity instanceof EntitySlime) {
                    JTessellator.drawBoundingBox(entity.getEntityBoundingBox(), 2, mobColor);
                }
            }
            if (item.isEnabled() && !entityMode.getMode().equals("off") && entity instanceof EntityItem){
            	JTessellator.drawBoundingBox(entity.getEntityBoundingBox(), 2, mainIntColor);
            }
        });
        
        if (storage.getMode().equals("outline")) {
            mc.world.loadedTileEntityList.stream().filter(tileEntity -> rangeTileCheck(tileEntity)).forEach(tileEntity -> {
                if (tileEntity instanceof TileEntityChest){
                    containerColor = new JColor(255, 255, 0, opacityGradient);
                    JTessellator.drawBoundingBox(mc.world.getBlockState(tileEntity.getPos()).getSelectedBoundingBox(mc.world, tileEntity.getPos()), 2, containerColor);
                }
                if (tileEntity instanceof TileEntityEnderChest){
                    containerColor = new JColor(180, 70, 200, opacityGradient);
                    JTessellator.drawBoundingBox(mc.world.getBlockState(tileEntity.getPos()).getSelectedBoundingBox(mc.world, tileEntity.getPos()), 2, containerColor);
                }
                if (tileEntity instanceof TileEntityShulkerBox){
                    containerColor = new JColor(255, 182, 193, opacityGradient);
                    JTessellator.drawBoundingBox(mc.world.getBlockState(tileEntity.getPos()).getSelectedBoundingBox(mc.world, tileEntity.getPos()), 2, containerColor);
                }
                if(tileEntity instanceof TileEntityDispenser || tileEntity instanceof TileEntityFurnace || tileEntity instanceof TileEntityHopper || tileEntity instanceof TileEntityDropper){
                    containerColor = new JColor(150, 150, 150, opacityGradient);
                    JTessellator.drawBoundingBox(mc.world.getBlockState(tileEntity.getPos()).getSelectedBoundingBox(mc.world, tileEntity.getPos()), 2, containerColor);
                }
            });
        }
        
        if (storage.getMode().equals("fill")) {
            mc.world.loadedTileEntityList.stream().filter(tileEntity -> rangeTileCheck(tileEntity)).forEach(tileEntity -> {
                if (tileEntity instanceof TileEntityChest){
                    containerColor = new JColor(255, 255, 0, opacityGradient);
                    containerBox = new JColor(255, 255, 0, 50);
                    JTessellator.drawBoundingBox(mc.world.getBlockState(tileEntity.getPos()).getSelectedBoundingBox(mc.world, tileEntity.getPos()), 2, containerColor);
                    drawStorageBox(tileEntity.getPos(),1, containerBox);
                }
                if (tileEntity instanceof TileEntityEnderChest){
                    containerColor = new JColor(180, 70, 200, opacityGradient);
                    containerBox = new JColor(255, 70, 200, 50);
                    JTessellator.drawBoundingBox(mc.world.getBlockState(tileEntity.getPos()).getSelectedBoundingBox(mc.world, tileEntity.getPos()), 2, containerColor);
                    drawStorageBox(tileEntity.getPos(),1, containerBox);
                }
                if (tileEntity instanceof TileEntityShulkerBox){
                    containerColor = new JColor(255, 182, 193, opacityGradient);
                    containerBox = new JColor(255, 182, 193, 50);
                    JTessellator.drawBoundingBox(mc.world.getBlockState(tileEntity.getPos()).getSelectedBoundingBox(mc.world, tileEntity.getPos()), 2, containerColor);
                    drawBox(tileEntity.getPos(),1, containerBox);
                }
                if(tileEntity instanceof TileEntityDispenser || tileEntity instanceof TileEntityFurnace || tileEntity instanceof TileEntityHopper || tileEntity instanceof TileEntityDropper){
                    containerColor = new JColor(150, 150, 150, opacityGradient);
                    containerBox = new JColor(150, 150, 150, 50);
                    JTessellator.drawBoundingBox(mc.world.getBlockState(tileEntity.getPos()).getSelectedBoundingBox(mc.world, tileEntity.getPos()), 2, containerColor);
                    drawBox(tileEntity.getPos(),1, containerBox);
                }
            });
        }
    }
    
    private void drawStorageBox(BlockPos blockPos, int width, JColor color) {
		JTessellator.drawStorageBox(blockPos, 0.88, color, GeometryMasks.Quad.ALL);
    }
    
    private void drawBox(BlockPos blockPos, int width, JColor color) {
		JTessellator.drawBox(blockPos, 1, color, GeometryMasks.Quad.ALL);
   }

    public void onDisable(){
    }

    private void defineEntityColors(Entity entity) {
        //should have everything covered here, mob categorizing is weird
        if (entity instanceof EntityPlayer){
                playerColor = new JColor((int) pRed.getValue(), (int) pGreen.getValue(), (int) pBlue.getValue(), opacityGradient);
            }

        if (entity instanceof EntityMob){
            mobColor = new JColor(255, 0, 0, opacityGradient);
        }
        else if (entity instanceof EntityAnimal){
            mobColor = new JColor(0, 255, 0, opacityGradient);
        }
        else {
            mobColor = new JColor(255, 165, 0, opacityGradient);
        }

        if (entity instanceof EntitySlime){
            mobColor = new JColor(255, 0, 0, opacityGradient);
        }

        if (entity != null) {
            mainIntColor = new JColor(0, 121, 194, opacityGradient);
        }
        }
    //boolean range check and opacity gradient

    private boolean rangeEntityCheck(Entity entity) {
        if (entity.getDistance(mc.player) > range.getValue()){
            return false;
        }

        if (entity.getDistance(mc.player) >= 180){
            opacityGradient = 50;
        }
        else if (entity.getDistance(mc.player) >= 130 && entity.getDistance(mc.player) < 180){
            opacityGradient = 100;
        }
        else if (entity.getDistance(mc.player) >= 80 && entity.getDistance(mc.player) < 130){
            opacityGradient = 150;
        }
        else if (entity.getDistance(mc.player) >= 30 && entity.getDistance(mc.player) < 80){
            opacityGradient = 200;
        }
        else {
            opacityGradient = 255;
        }

        return true;
    }

    private boolean rangeTileCheck(TileEntity tileEntity) {
        //the range value has to be squared for this
        if (tileEntity.getDistanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) > range.getValue() * range.getValue()){
            return false;
        }

        if (tileEntity.getDistanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) >= 32400){
            opacityGradient = 50;
        }
        else if (tileEntity.getDistanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) >= 16900 && tileEntity.getDistanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) < 32400){
            opacityGradient = 100;
        }
        else if (tileEntity.getDistanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) >= 6400 && tileEntity.getDistanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) < 16900){
            opacityGradient = 150;
        }
        else if (tileEntity.getDistanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) >= 900 && tileEntity.getDistanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) < 6400){
            opacityGradient = 200;
        }
        else {
            opacityGradient = 255;
        }

        return true;
    }
}

