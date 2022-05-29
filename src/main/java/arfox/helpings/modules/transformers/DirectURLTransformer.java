package arfox.helpings.modules.transformers;

import arfox.helpings.modules.Module;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class DirectURLTransformer extends URLTransformer {

    @Override
    public List<Module> transform(JavaPlugin plugin, String url) {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "direct";
    }
}
