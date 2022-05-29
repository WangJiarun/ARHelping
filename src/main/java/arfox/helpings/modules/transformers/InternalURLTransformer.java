package arfox.helpings.modules.transformers;

import arfox.helpings.modules.Module;
import arfox.helpings.helpings.FileHelpings;
import arfox.helpings.helpings.ReflectHelpings;
import arfox.helpings.lambda.ThrowingIgnore;
import arfox.helpings.modules.exceptions.MalformedInternalURLException;
import arfox.helpings.modules.exceptions.PluginNotFoundException;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class InternalURLTransformer extends URLTransformer {

    @Override
    public List<Module> transform(JavaPlugin plugin, String url) {
        String[] parts = url.split(":");
        if (parts.length > 2) throw new MalformedInternalURLException("The URL \"%s\" is not recognized as a legal maven artifact.".formatted(url));

        Plugin resourcePlugin = parts.length == 1 ? plugin : Bukkit.getPluginManager().getPlugin(parts[0]);
        if (resourcePlugin == null) throw new PluginNotFoundException("Cannot found Plugin \"%s\". Probably it is not loaded. Try add it to plugin dependencies.");

        File file = ReflectHelpings.getObjectField(resourcePlugin.getClass().getClassLoader(), "file", File.class);
        ArrayList<JarEntry> entries = Lists.newArrayList();

        ThrowingIgnore.of(() -> {
            JarFile jarFile = new JarFile(file);
            entries.addAll(jarFile.stream().filter(entry -> Path.of(entry.getName()).startsWith(parts.length == 1 ? parts[0] : parts[1])).toList());
            jarFile.close();
        });

        return entries.stream().map(entry -> new Module(plugin.getResource(entry.getName()), FileHelpings.getFilename(entry.getName()))).toList();
    }

    @Override
    public String getIdentifier() {
        return "internal";
    }
}
