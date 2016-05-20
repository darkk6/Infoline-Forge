package tw.darkk6.mcmod.infoline.mod;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import tw.darkk6.mcmod.infoline.util.Lang;

public class Clock extends IModBase {

	private static final int MC_TIME=0;
	private static final int MC_TICK=1;
	private static final int REAL_TIME=2;
	
	public static final String MOD_CATALOG="clock";
	
	private ClockCfg config;
	
	public Clock(Configuration cfg) {
		super(cfg);
		config=new ClockCfg();
		config.update(cfg);
	}
	
	@Override
	public String parseResult(String str) {
		if(str.indexOf("{clock}")==-1) return str;
		String clock=getClockString();
		return str.replaceAll("\\{clock\\}", clock);
	}
	
	private String getClockString(){
		/*
		 * 	一天有 24*60 = 1440 分鐘
		 * 	==> 24000/1440 => 16.6_ 為 1 分鐘
		 * 	==> 24000/24 => 每 1000 為 1 小時
		*/
		if(!config.isEnabled()) return "";
		String clockString;
		if(config.clockMode.getInt()==Clock.REAL_TIME){
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			Date date = new Date();
			clockString=dateFormat.format(date);
		}else{
			long time=Minecraft.getMinecraft().theWorld.getWorldTime();
			clockString=getFormatedTimeString(time);
		}
		return clockString;
	}
	
	private String getFormatedTimeString(long time){
		// Config.REAL_TIME 已經判斷過了所以不需要
		time %= 24000;
		int hour = (int)time / 1000;
		int min = (int)Math.floor((time % 1000)/(50f/3f));
		hour=(hour+6) % 24;
		String formatedTime=String.format("%02d:%02d",hour,min);
		if(!config.useColor.getBoolean())
			return config.clockMode.getInt()==Clock.MC_TIME ? formatedTime : String.valueOf(time);
		
		StringBuilder show=new StringBuilder();
		//晚上:GRAY , 黃昏/清晨:GOLD , 白天 : YELLOW
		if(time>=13187L && time<23600L) show.append(TextFormatting.GRAY);
		else if( time<12540L ) show.append(TextFormatting.YELLOW);
		else show.append(TextFormatting.GOLD);
		show.append(config.clockMode.getInt()==Clock.MC_TIME ? formatedTime : String.valueOf(time));
		show.append(TextFormatting.RESET);
		return show.toString();
	}

	@Override
	public IConfig getConfig() {
		return config;
	}

	public class ClockCfg extends IConfig{
		
		private static final boolean DEFAULT_ENABLED=true,DEFAULT_USECOLOR=true;
		private static final int DEFAULT_CLOCKMODE=0;
		
		private Property isEnabled,useColor,clockMode;
		
		@Override
		public void update(Configuration cfg){
			cfg.setCategoryComment(MOD_CATALOG,Lang.get("infoline.setting.catalog.clock.comment"));
			
			isEnabled=cfg.get(MOD_CATALOG, 
					Lang.get("infoline.clock.enable"), 
					DEFAULT_ENABLED,
					Lang.get("infoline.clock.enable.comment")
				);
			
			useColor=cfg.get(MOD_CATALOG, 
					Lang.get("infoline.clock.color"), 
					DEFAULT_USECOLOR,
					Lang.get("infoline.clock.color.comment")
				);
			
			clockMode=cfg.get(MOD_CATALOG, 
					Lang.get("infoline.clock.mode"), 
					DEFAULT_CLOCKMODE,
					Lang.get("infoline.clock.mode.comment"),
					0,2
				);
			
			if(clockMode.getInt()<0 || clockMode.getInt()>2) clockMode.setValue(DEFAULT_CLOCKMODE);
			
			this.sortOrder(cfg, MOD_CATALOG,
					"infoline.clock.enable",
					"infoline.clock.color",
					"infoline.clock.mode"
				);
		}
		
		
		@Override
		public boolean isEnabled(){ return isEnabled.getBoolean(); }
	}
}
