package arfox.helpings.core;

import arfox.helpings.lambda.ThrowingRunnable;
import arfox.helpings.modules.ModuleResolver;
import org.bukkit.craftbukkit.Main;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ARHelpings extends JavaPlugin {

    @Override
    public void onEnable() {
        StringBuilder builder = new StringBuilder();
        new ModuleResolver(this, getResource("modules.json")).installModule().forEach(url -> builder.append(url.getFile()).append(';'));

        String filename = getFile().getAbsolutePath();
        String bootPaths = "%s;%s;%s;%s".formatted(filename, "./bundler/versions/*", "./bundler/libraries/*", builder.toString());
        String agentName = "-javaagent:%s".formatted(filename);

        String mainClassName = Main.class.getName();

        if (System.getProperty("HELPING_LOADED") == null) {
            new ModuleResolver(this, getResource("modules.json")).installModule().forEach(System.out::println);

            Runtime.getRuntime().addShutdownHook(new Thread(ThrowingRunnable.of(() -> {
                while (!((CraftServer) getServer()).getHandle().b().hasStopped());

                new ProcessBuilder()
                        .directory(new File("./"))
                        .command("java", agentName, "-Dmixin.hotSwap=true", "-DHELPING_LOADED=true", "-cp", bootPaths, mainClassName)
                        .redirectError(ProcessBuilder.Redirect.INHERIT)
                        .redirectInput(ProcessBuilder.Redirect.INHERIT)
                        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                        .start();
            })));

            getServer().shutdown();
        }
    }
}
