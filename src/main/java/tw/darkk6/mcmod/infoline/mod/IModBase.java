package tw.darkk6.mcmod.infoline.mod;

import java.util.ArrayList;

import tw.darkk6.mcmod.infoline.util.Lang;
import net.minecraftforge.common.config.Configuration;

public abstract class IModBase {
	
	public IModBase(Configuration cfg){ }
	
	public boolean isEnabled(){
		return getConfig().isEnabled();
	}
	
	public void update(Configuration cfg){
		this.getConfig().update(cfg);
	}
	
	/**  Abstract methods  **/
	public abstract String parseResult(String str);
	protected abstract IConfig getConfig();
	
	//==== Config Interface ====
	public abstract class IConfig{
		public abstract boolean isEnabled();
		public abstract void update(Configuration cfg);
		
		protected boolean setupOrder=true;
		protected void sortOrder(Configuration cfg,String catalog,String...order){
			if(setupOrder){
				setupOrder=false;
				ArrayList<String> propOrder=new ArrayList<String>();
				for(String str:order){
					propOrder.add(Lang.get(str));
				}
				cfg.setCategoryPropertyOrder(catalog, propOrder);
			}
		}
	}
}
