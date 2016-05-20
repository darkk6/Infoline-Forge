package tw.darkk6.mcmod.infoline;

import java.util.ArrayList;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import tw.darkk6.mcmod.infoline.config.ConfigMgr;
import tw.darkk6.mcmod.infoline.config.Reference;
import tw.darkk6.mcmod.infoline.mod.Biome;
import tw.darkk6.mcmod.infoline.mod.Clock;
import tw.darkk6.mcmod.infoline.mod.Coordinate;
import tw.darkk6.mcmod.infoline.mod.Health;
import tw.darkk6.mcmod.infoline.mod.IModBase;
import tw.darkk6.mcmod.infoline.mod.Light;

@Mod(modid=Reference.MOD_ID , version=Reference.MOD_VER , clientSideOnly=true , guiFactory = Reference.GUI_FACTORY)
public class InfolineMod {
	
	@Instance(Reference.MOD_ID)
	public static InfolineMod mod;
	public static ConfigMgr config;
	
	public static ArrayList<IModBase> modList;;
	public static void loadModsWithConfig(Configuration cfg){
		if(modList==null){
			//還沒載入 mod , 初始化與載入設定檔案
			modList=new ArrayList<IModBase>();
			modList.add(new Clock(cfg));
			modList.add(new Light(cfg));
			modList.add(new Coordinate(cfg));
			modList.add(new Biome(cfg));
			modList.add(new Health(cfg));
		}else{
			//已經載入 Mod , 只要更新就好
			for(IModBase mod:InfolineMod.modList)
				mod.update(cfg);
		}
	}
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		config = ConfigMgr.getInstance(evt.getSuggestedConfigurationFile());
	}

	@Mod.EventHandler
	public void load(FMLInitializationEvent evt) {
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}
}
