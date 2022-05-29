package arfox.helpings.core;

import arfox.helpings.mixin.HelpingsMixinService;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.tools.agent.MixinAgent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class HelpingsAgent {

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        MixinBootstrap.init();
        MixinBootstrap.getPlatform().inject();
        MixinAgent.premain(agentArgs, instrumentation);
        Mixins.addConfiguration("mixins.json");

        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                if (className.contains("MainMixin")) return classfileBuffer;
                return HelpingsMixinService.transformer == null ? classfileBuffer : HelpingsMixinService.transformer.transformClass(MixinEnvironment.getDefaultEnvironment(), className.replace('/', '.'), classfileBuffer);
            }
        });
    }
}
