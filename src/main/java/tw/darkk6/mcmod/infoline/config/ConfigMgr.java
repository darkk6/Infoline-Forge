package tw.darkk6.mcmod.infoline.config;

import java.io.File;
import java.util.ArrayList;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import tw.darkk6.mcmod.infoline.InfolineMod;
import tw.darkk6.mcmod.infoline.util.Lang;

public class ConfigMgr {
	public static ConfigMgr instance;
	
	public static ConfigMgr getInstance(File file){
		if(instance==null){
			instance=new ConfigMgr(file);
		}
		return instance;
	}
	
	private static final int DEFAULT_POS=0;
	private static final int DEFAULT_XPOS=5,DEFAULT_YPOS=5,DEFAULT_GAP=0;
	private static final boolean DEFAULT_ENABLED=true;
	private static final String DEFAULT_DISPLAY="{clock} {coordinate} {light}{N}{biome}{N}{health}";
	
	public static Property position,xOffset,yOffset,lineGap;
	public static Property isEnabled;
	public static Property displayString;
	
	public Configuration cfg;
	
	private ArrayList<String> propOrder;
	private File file;
	
	private ConfigMgr(File file){
		this.file=file;
		this.cfg=new Configuration(file);
		reload();
	}
	
	public Configuration getConfiguration(){
		return cfg;
	}
	
	public void update(){
		if(cfg.hasChanged()) cfg.save();
		reload();
	}
	
	private void reload(){
		this.cfg.load();
		boolean setupOrder = (propOrder==null);
		
		cfg.setCategoryComment("general",Lang.get("infoline.setting.catalog.general.comment"));
		
		isEnabled = this.cfg.get("general", 
						Lang.get("infoline.setting.enable"), 
						DEFAULT_ENABLED,
						Lang.get("infoline.setting.enable.comment")
					);
		
		// PS : 這邊設定的範圍 [不會] 在 get 的時候幫忙檢查
		lineGap = this.cfg.get("general", 
				Lang.get("infoline.setting.linegap"), 
				DEFAULT_GAP,
				Lang.get("infoline.setting.linegap.comment"),
				0,5
			);
		
		if(lineGap.getInt()>5 || lineGap.getInt()<0) lineGap.set(DEFAULT_GAP);
		
		position = this.cfg.get("general", 
					Lang.get("infoline.setting.position"), 
					DEFAULT_POS, 
					Lang.get("infoline.setting.position.comment"),
					0,3
				);
		
		if(position.getInt()>3 || position.getInt()<0) lineGap.set(DEFAULT_POS);
		
		xOffset = this.cfg.get("general", 
				Lang.get("infoline.setting.xoffset"), 
				DEFAULT_XPOS, 
				Lang.get("infoline.setting.xoffset.comment")
			);
		yOffset = this.cfg.get("general", 
				Lang.get("infoline.setting.yoffset"), 
				DEFAULT_YPOS, 
				Lang.get("infoline.setting.yoffset.comment")
			);
		
		displayString = this.cfg.get("general", 
				Lang.get("infoline.setting.displaystring"), 
				DEFAULT_DISPLAY, 
				Lang.get("infoline.setting.displaystring.comment")
			);
		
		if(setupOrder){
			propOrder=new ArrayList<String>();
			propOrder.add(Lang.get("infoline.setting.enable"));
			propOrder.add(Lang.get("infoline.setting.displaystring"));
			propOrder.add(Lang.get("infoline.setting.linegap"));
			propOrder.add(Lang.get("infoline.setting.position"));
			propOrder.add(Lang.get("infoline.setting.xoffset"));
			propOrder.add(Lang.get("infoline.setting.yoffset"));
			cfg.setCategoryPropertyOrder("general", propOrder);
		}
		
		InfolineMod.loadModsWithConfig(cfg);
		
		if(cfg.hasChanged()) cfg.save();
	}
}
