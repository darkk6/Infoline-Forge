package tw.darkk6.mcmod.infoline.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import tw.darkk6.mcmod.infoline.util.Lang;

public class Health extends IModBase {

	public static final String MOD_CATALOG="health";
	
	private HealthCfg config; 
	public Health(Configuration cfg) {
		super(cfg);
		config=new HealthCfg();
		config.update(cfg);
	}

	@Override
	public String parseResult(String str) {
		if(str.indexOf("{health}")==-1) return str;
		return str.replaceAll("\\{health\\}",getHealthString());
	}

	private String getHealthString(){
		if(!config.isEnabled()) return "";
		String fmt=config.displayFormat.getString();
		//格式中沒出現 {A},{M},{H}就直接結束
		if(!fmt.matches(".*\\{[AMH]\\}.*")) return "";
		Minecraft mc=Minecraft.getMinecraft();
		Entity e=mc.pointedEntity;
		if(e==null) return "";
		if(!(e instanceof EntityLivingBase)) return "";
		EntityLivingBase entity=(EntityLivingBase)e;
		String name = entity.getName();
		int now = (int)Math.ceil(entity.getHealth());
		int max = (int)Math.ceil(entity.getMaxHealth());
		return fmt.replaceAll("\\{A\\}",name.toString())
				.replaceAll("\\{H\\}", String.valueOf(now))
				.replaceAll("\\{M\\}", String.valueOf(max));
	}
	
	@Override
	protected IConfig getConfig() {
		return config;
	}
	
	public class HealthCfg extends IConfig{

		private static final boolean DEFAULT_ENABLED=true;
		private static final String DEFAULT_DISPLAY="{A}:{H}/{M}";
		
		private Property isEnabled,displayFormat;
		
		@Override
		public boolean isEnabled() {
			return isEnabled.getBoolean();
		}

		@Override
		public void update(Configuration cfg) {
			
			cfg.setCategoryComment(MOD_CATALOG,Lang.get("infoline.setting.catalog.health.comment"));
			
			isEnabled=cfg.get(MOD_CATALOG, 
					Lang.get("infoline.health.enable"), 
					DEFAULT_ENABLED,
					Lang.get("infoline.health.enable.comment")
				);
			
			displayFormat=cfg.get(MOD_CATALOG, 
					Lang.get("infoline.health.display"), 
					DEFAULT_DISPLAY,
					Lang.get("infoline.health.display.comment")
				);
			
			this.sortOrder(cfg, MOD_CATALOG,
					"infoline.health.enable",
					"infoline.health.display"
				);
			
		}
		
	}
}
