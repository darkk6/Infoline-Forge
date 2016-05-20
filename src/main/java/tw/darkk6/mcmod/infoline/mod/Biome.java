package tw.darkk6.mcmod.infoline.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import tw.darkk6.mcmod.infoline.util.Lang;
import tw.darkk6.mcmod.infoline.util.Util;

public class Biome extends IModBase {


	public static final String MOD_CATALOG="biome";
	
	private BiomeCfg config; 
	public Biome(Configuration cfg) {
		super(cfg);
		config=new BiomeCfg();
		config.update(cfg);
	}

	@Override
	public String parseResult(String str) {
		if(str.indexOf("{biome}")==-1) return str;
		return str.replaceAll("\\{biome\\}",getBiomeString());
	}

	private String getBiomeString(){
		if(!config.isEnabled()) return "";
		String fmt=config.displayFormat.getString();
		//格式中沒出現 {B}或{T}就直接結束
		if(!fmt.matches(".*\\{[BT]\\}.*")) return "";
		Minecraft mc=Minecraft.getMinecraft();
		BlockPos pos=Util.getPlayerPos();
		if(pos==null) return "{Error}";
		//== Check Can Snow ==
		// 要檢查的是腳底下那一格，所以要 .down()
		boolean canSnow=mc.theWorld.canSnowAtBody(pos.down(),false);
		StringBuilder name=new StringBuilder();
		float temp=getBiomeNameTempture(pos,name);
		if(canSnow){
			name.insert(0,TextFormatting.AQUA);
			name.append(TextFormatting.RESET);
		}
		StringBuilder tempStr=new StringBuilder();
		if(temp<=0.15f)// 下雪
			tempStr.append(TextFormatting.AQUA);
		else if( temp<=0.95f ){
			// 下雨 , DoNothing
		}else if(temp<=1.0f){// 0.95~1.0 似乎還會下雨
			tempStr.append(TextFormatting.YELLOW);
		}else//>0.95  熱帶 ， 不下雨
			tempStr.append(TextFormatting.GOLD);
		//如果高度在 64 以下，氣溫不會變動，用斜體表示 , 效果不好  算了
		//if(pos.getY()<64) tempStr.append(APILog.TextFormatting.ITALIC);
		tempStr.append(String.format("%.02f", temp)).append(TextFormatting.RESET);
		
		return fmt.replaceAll("\\{B\\}",name.toString())
				.replaceAll("\\{T\\}", tempStr.toString());
	}
	
	@Override
	protected IConfig getConfig() {
		return config;
	}

	private float getBiomeNameTempture(BlockPos playerPos,StringBuilder name){
		name.setLength(0);
		BlockPos pos64=new BlockPos(playerPos.getX(),64,playerPos.getZ());
		net.minecraft.world.biome.Biome biome=Minecraft.getMinecraft().theWorld.getBiomeGenForCoords(pos64);
		String biomeName=biome.getBiomeName();
		name.append(biomeName);
		//getFloatTemperature 是根據 pos 計算溫度， getTemperature 是取得基本溫度
		return biome.getFloatTemperature(playerPos);
	}
	
	public class BiomeCfg extends IConfig{

		private static final boolean DEFAULT_ENABLED=true;
		private static final String DEFAULT_DISPLAY="{B} - {T}";
		
		private Property isEnabled,displayFormat;
		
		@Override
		public boolean isEnabled() {
			return isEnabled.getBoolean();
		}

		@Override
		public void update(Configuration cfg) {
			cfg.setCategoryComment(MOD_CATALOG,Lang.get("infoline.setting.catalog.biome.comment"));
			
			isEnabled=cfg.get(MOD_CATALOG, 
					Lang.get("infoline.biome.enable"), 
					DEFAULT_ENABLED,
					Lang.get("infoline.biome.enable.comment")
				);
			
			displayFormat=cfg.get(MOD_CATALOG, 
					Lang.get("infoline.biome.display"), 
					DEFAULT_DISPLAY,
					Lang.get("infoline.biome.display.comment")
				);
			
			this.sortOrder(cfg, MOD_CATALOG,
					"infoline.biome.enable",
					"infoline.biome.display"
				);
			
		}
	}

}
