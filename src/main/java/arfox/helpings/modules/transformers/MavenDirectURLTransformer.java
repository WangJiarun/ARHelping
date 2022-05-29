package arfox.helpings.modules.transformers;

import arfox.helpings.modules.Module;
import arfox.helpings.lambda.ThrowingPass;
import com.google.common.collect.Lists;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URL;
import java.util.List;

public class MavenDirectURLTransformer extends URLTransformer {

    @Override
    public List<Module> transform(JavaPlugin plugin, String url) {
        MavenURLTransformer.MavenInfo info = MavenURLTransformer.toMavenUrl(url);
        return Lists.newArrayList(new Module(ThrowingPass.of(() -> new URL(info.url() + info.name()).openStream()), info.name()));
    }

    @Override
    public String getIdentifier() {
        return "maven.direct";
    }
}
