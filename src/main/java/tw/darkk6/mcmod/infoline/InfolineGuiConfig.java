package tw.darkk6.mcmod.infoline;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import tw.darkk6.mcmod.infoline.config.ConfigMgr;
import tw.darkk6.mcmod.infoline.config.Reference;
import tw.darkk6.mcmod.infoline.util.Lang;

// http://forum.feed-the-beast.com/threads/code-snippets-classes.51404/page-4#post-876553

public class InfolineGuiConfig extends GuiConfig {
	public InfolineGuiConfig(GuiScreen parent) {
		super(parent,
				getConfigElements(),
				Reference.MOD_ID,
				false,//需要重新進入世界 ?
				false,//需要重新啟動 MC ?
				Lang.get("infoline.setting.gui.title")//標題
			);
		// 設定標題 2 顯示文字
		// GuiConfig.getAbridgedConfigPath() 可以把檔案路徑改成  .minecraft/ 底下的對應路徑呈現
		this.titleLine2 = GuiConfig.getAbridgedConfigPath(ConfigMgr.instance.getConfiguration().toString());
	}
	
	 private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
      
        //Add categories to config GUI
        String tmp="infoline.setting.catalog.general.comment";
        list.add(categoryElement("general", Lang.get(tmp), tmp));
        
        tmp="infoline.setting.catalog.clock.comment";
        list.add(categoryElement("clock", Lang.get(tmp), tmp));
        
        tmp="infoline.setting.catalog.light.comment";
        list.add(categoryElement("light", Lang.get(tmp), tmp));
        
        tmp="infoline.setting.catalog.coordinate.comment";
        list.add(categoryElement("coordinate", Lang.get(tmp), tmp));
        
        tmp="infoline.setting.catalog.biome.comment";
        list.add(categoryElement("biome", Lang.get(tmp), tmp));
        
        tmp="infoline.setting.catalog.health.comment";
        list.add(categoryElement("health", Lang.get(tmp), tmp));
      
        return list;
    }
	 
	/** Creates a button linking to another screen where all options of the category are available */
	private static IConfigElement categoryElement(String category, String name, String tooltip_key) {
	    return new DummyConfigElement.DummyCategoryElement(name, tooltip_key,
	            new ConfigElement(ConfigMgr.instance.getConfiguration().getCategory(category)).getChildElements());
	}
}
