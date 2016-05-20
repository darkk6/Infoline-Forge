package tw.darkk6.mcmod.infoline.util;
import net.minecraft.client.resources.I18n;

public class Lang {
	public static String get(String key){
		return I18n.format(key);
	}
}
