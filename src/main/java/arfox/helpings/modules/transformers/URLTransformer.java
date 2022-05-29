package arfox.helpings.modules.transformers;

import arfox.helpings.modules.Module;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public abstract class URLTransformer {

    public abstract List<Module> transform(JavaPlugin plugin, String url);

    public abstract String getIdentifier();
}
