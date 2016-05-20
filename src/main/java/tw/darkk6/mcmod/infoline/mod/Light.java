package tw.darkk6.mcmod.infoline.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import tw.darkk6.mcmod.infoline.util.Lang;
import tw.darkk6.mcmod.infoline.util.Util;

public class Light extends IModBase {

public static final String MOD_CATALOG="light";
	
	private LightCfg config; 
	public Light(Configuration cfg) {
		super(cfg);
		config=new LightCfg();
		config.update(cfg);
	}

	@Override
	public String parseResult(String str) {
		if(str.indexOf("{light}")==-1) return str;
		return str.replaceAll("\\{light\\}",getLightString());
	}
	
	private String getLightString(){
		if(!config.isEnabled()) return "";
		String fmt = config.lightFormat.getString();
		if(!fmt.matches(".*\\{[BS]\\}.*")) return "";
		int[] lights = getLightLevel(Util.getPlayerPos());
		String[] lightStr=new String[2];
		lightStr[0] = String.valueOf(lights[0]);
		lightStr[1] = String.valueOf(lights[1]);
		if(config.useColor.getBoolean()){
			for(int i=0;i<lights.length;i++){
				if(lights[i]<8)
					lightStr[i] = TextFormatting.RED+lightStr[i]+TextFormatting.RESET;
			}
		}
		return fmt.replaceAll("\\{B\\}", lightStr[0])
				.replaceAll("\\{S\\}", lightStr[1]);
	}
	
	private int[] getLightLevel(BlockPos pos){
		int[] res=new int[]{0,0};
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.thePlayer == null) return res;
		WorldClient world = mc.theWorld;
		int lSky = world.getLightFor(EnumSkyBlock.SKY, pos);
		int lBlock = world.getLightFor(EnumSkyBlock.BLOCK, pos);
		lSky = lSky - world.calculateSkylightSubtracted(1.0F);
		lSky = Math.max(lSky, lBlock);
		res[0] = lBlock;
		res[1] = lSky;
		return res;
	}
	
	@Override
	protected IConfig getConfig() {
		return config;
	}
	
	public class LightCfg extends IConfig{
		private static final boolean DEFAULT_ENABLED=true,DEFAULT_USECOLOR=true;
		private static final String DEFAULT_DISPLAY="{B} [{S}]";
		
		private Property isEnabled,useColor,lightFormat;
		
		@Override
		public boolean isEnabled() {
			return isEnabled.getBoolean();
		}

		@Override
		public void update(Configuration cfg) {
			cfg.setCategoryComment(MOD_CATALOG,Lang.get("infoline.setting.catalog.light.comment"));
			
			isEnabled=cfg.get(MOD_CATALOG, 
					Lang.get("infoline.light.enable"), 
					DEFAULT_ENABLED,
					Lang.get("infoline.light.enable.comment")
				);
			
			useColor=cfg.get(MOD_CATALOG, 
					Lang.get("infoline.light.color"), 
					DEFAULT_USECOLOR,
					Lang.get("infoline.light.color.comment")
				);
			
			lightFormat=cfg.get(MOD_CATALOG, 
					Lang.get("infoline.light.display"), 
					DEFAULT_DISPLAY,
					Lang.get("infoline.light.display.comment")
				);
			
			this.sortOrder(cfg, MOD_CATALOG,
						"infoline.light.enable",
						"infoline.light.color",
						"infoline.light.display"
					);
		}
	}
}
