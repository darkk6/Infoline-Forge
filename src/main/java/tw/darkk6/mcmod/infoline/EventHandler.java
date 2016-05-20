package tw.darkk6.mcmod.infoline;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import jline.internal.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.input.Keyboard;

import tw.darkk6.mcmod.infoline.config.ConfigMgr;
import tw.darkk6.mcmod.infoline.config.Reference;
import tw.darkk6.mcmod.infoline.mod.Coordinate;
import tw.darkk6.mcmod.infoline.mod.IModBase;

public class EventHandler {
	//============== Mod 設定檔儲存事件 =================
		@SubscribeEvent
		public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent e){
			if (Reference.MOD_ID.equals(e.getModID())) {
				ConfigMgr.instance.update();
			}
		}
	//============== Render Tick 事件 =================
	/*
	 * 若要 Render Debug screen 的畫面 可以這樣判斷
	 * evt.getType()==RenderGameOverlayEvent.ElementType.DEBUG
	*/
	@SubscribeEvent
	public void onRenderTick(RenderGameOverlayEvent evt){
		if(evt.getType()==RenderGameOverlayEvent.ElementType.TEXT){
			// Render 文字時的事件
			if(!ConfigMgr.isEnabled.getBoolean()) return;
			if(Minecraft.getMinecraft()==null) return;
			if(Minecraft.getMinecraft().theWorld==null) return;
			GuiScreen gui=Minecraft.getMinecraft().currentScreen;
			if(gui instanceof GuiChat){
				checkAndInsertCoord(gui);
			}
			String textToDraw=ConfigMgr.displayString.getString();
			//跑遍每一個 mod 取代要顯示的文字
			for(IModBase mod:InfolineMod.modList){
				textToDraw=mod.parseResult(textToDraw);
			}
			String[] arr=textToDraw.split("\\{[Nn]\\}");
			drawText(Arrays.asList(arr));
		}
	}
	
	private boolean hasF1Released=true;
	private Field inputfield;
	private void checkAndInsertCoord(Object guiChatObj){
		//檢查 F1 是否按下，將座標插入對話區
		if(!hasF1Released){
			if(!Keyboard.isKeyDown(Keyboard.KEY_F1)) hasF1Released=true;
			return;
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_F1)) return;
		hasF1Released=false;
		String xyz=Coordinate.instance.getCoordInChatStr();
		if(xyz==null) return;
		try{
			if(inputfield==null) inputfield=GuiChat.class.getDeclaredField("inputField");
			inputfield.setAccessible(true);
			GuiTextField iptField = (GuiTextField)inputfield.get(guiChatObj);
			iptField.writeText(xyz);
		}catch(Exception e){
			Log.info(e.getMessage());
		}
	}
	
	private void drawText(List<String> strList){
		//計算出出現位置
		Minecraft mc = Minecraft.getMinecraft();
		FontRenderer render = mc.fontRendererObj;
		ScaledResolution sres=new ScaledResolution(mc);
		int width = sres.getScaledWidth();
		int height = sres.getScaledHeight();
		
		int lines=strList.size();
		int idx=0;
		for(String text:strList){
			int strWidth = render.getStringWidth(text);
			int strHeight = render.FONT_HEIGHT;
			int x = ConfigMgr.xOffset.getInt(), y = ConfigMgr.yOffset.getInt();
			
			switch(ConfigMgr.position.getInt()){
				case Reference.POS_TOP_LEFT:
					y = ( (strHeight + ConfigMgr.lineGap.getInt()) * idx) + y;
					break;
				case Reference.POS_TOP_RIGHT:
					x = width - strWidth - x;
					y = ( (strHeight + ConfigMgr.lineGap.getInt()) * idx) + y;
					break;
				case Reference.POS_BOTTOM_LEFT:
					y = height - (strHeight+ConfigMgr.lineGap.getInt())*(lines-idx) - y;
					break;
				case Reference.POS_BOTTOM_RIGHT:
					x = width - strWidth - x;
					y = height - (strHeight+ConfigMgr.lineGap.getInt())*(lines-idx) - y;
					break;
			}
			render.drawStringWithShadow(text, x, y, 0xFFFFFFFF);
			idx++;
		}
	}
}
