package tw.darkk6.mcmod.infoline.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import tw.darkk6.mcmod.infoline.util.Lang;
import tw.darkk6.mcmod.infoline.util.Util;

public class Coordinate extends IModBase {
	
	public static final String MOD_CATALOG="coordinate";
	public static Coordinate instance;
	
	private CoordCfg config; 
	public Coordinate(Configuration cfg) {
		super(cfg);
		config=new CoordCfg();
		config.update(cfg);
		instance=this;
	}

	@Override
	public String parseResult(String str) {
		if(str.indexOf("{coordinate}")==-1) return str;
		return str.replaceAll("\\{coordinate\\}",getCoordString());
	}

	@Override
	protected IConfig getConfig() {
		return config;
	}

	public String getCoordInChatStr(){
		String result=config.chatFormat.getString();
		//格式中沒出現 {X}{Y}{Z}就直接結束
		if(!result.matches(".*\\{[XYZ]\\}.*")) return null;
		BlockPos pos = Util.getPlayerPos();
		if(pos==null) return null;
		result=result.replaceAll("\\{X\\}",String.valueOf(pos.getX()))
				.replaceAll("\\{Y\\}",String.valueOf(pos.getY()))
				.replaceAll("\\{Z\\}",String.valueOf(pos.getZ()));
		return result;
	}
	
	private String getCoordString(){
		if(!config.isEnabled()) return "";
		String result=config.displayFormat.getString();
		//格式中沒出現 {X}{Y}{Z}就直接結束
		if(!result.matches(".*\\{[XYZF]\\}.*")) return "";
		BlockPos pos = Util.getPlayerPos();
		if(pos==null) return "Get playe coordinate error.";
		String facing="";
		Entity player=Minecraft.getMinecraft().thePlayer;
		facing=getFacing(player);
		String posY="";
		if(config.useColor.getBoolean()){
			int y=pos.getY();
			if( 4<y && y<12 ) posY = TextFormatting.AQUA.toString();
			else if( 11<y && y<23 ) posY = TextFormatting.BLUE.toString();
			else if( 22<y && y<29 ) posY = TextFormatting.YELLOW.toString();
			posY=posY+y+TextFormatting.RESET;
		}else
			posY=String.valueOf(pos.getY());
		
		result=result.replaceAll("\\{X\\}",String.valueOf(pos.getX()))
				.replaceAll("\\{Y\\}",posY)
				.replaceAll("\\{Z\\}",String.valueOf(pos.getZ()))
				.replaceAll("\\{F\\}",facing);
		return result;
	}
	
	private String getFacing(Entity entity){
		if(entity==null) return "?";
        double rotation = (entity.rotationYaw - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
         if (0 <= rotation && rotation < 22.5) {
            return "W";
        } else if (22.5 <= rotation && rotation < 67.5) {
            return "NW";
        } else if (67.5 <= rotation && rotation < 112.5) {
            return "N";
        } else if (112.5 <= rotation && rotation < 157.5) {
            return "NE";
        } else if (157.5 <= rotation && rotation < 202.5) {
            return "E";
        } else if (202.5 <= rotation && rotation < 247.5) {
            return "SE";
        } else if (247.5 <= rotation && rotation < 292.5) {
            return "S";
        } else if (292.5 <= rotation && rotation < 337.5) {
            return "SW";
        } else if (337.5 <= rotation && rotation < 360.0) {
            return "W";
        } else {
            return "??";
        }
	}
	
	public class CoordCfg extends IConfig{

		private static final boolean DEFAULT_ENABLED=true,DEFAULT_USECOLOR=true;
		private static final String DEFAULT_DISPLAY="[{X}, {Y}, {Z}] §7[§c{F}§7]§r";
		private static final String DEFAULT_DISPLAYCHAT="{X}, {Y}, {Z}";
		
		private Property isEnabled,useColor,displayFormat,chatFormat;
		
		@Override
		public boolean isEnabled() {
			return isEnabled.getBoolean();
		}

		@Override
		public void update(Configuration cfg) {
			cfg.setCategoryComment(MOD_CATALOG,Lang.get("infoline.setting.catalog.coordinate.comment"));
			
			isEnabled=cfg.get(MOD_CATALOG, 
					Lang.get("infoline.coordinate.enable"), 
					DEFAULT_ENABLED,
					Lang.get("infoline.coordinate.enable.comment")
				);
			
			useColor=cfg.get(MOD_CATALOG, 
					Lang.get("infoline.coordinate.color"), 
					DEFAULT_USECOLOR,
					Lang.get("infoline.coordinate.color.comment")
				);
			
			displayFormat=cfg.get(MOD_CATALOG, 
					Lang.get("infoline.coordinate.display"), 
					DEFAULT_DISPLAY,
					Lang.get("infoline.coordinate.display.comment")
				);
			
			chatFormat=cfg.get(MOD_CATALOG, 
					Lang.get("infoline.coordinate.chatformat"), 
					DEFAULT_DISPLAYCHAT,
					Lang.get("infoline.coordinate.chatformat.comment")
				);
			
			this.sortOrder(cfg, MOD_CATALOG,
					"infoline.coordinate.enable",
					"infoline.coordinate.color",
					"infoline.coordinate.display",
					"infoline.coordinate.chatformat"
				);
		}
		
	}
}
