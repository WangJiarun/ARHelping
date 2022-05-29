package arfox.helpings.mixin;

import arfox.helpings.core.ARHelpings;
import com.google.common.collect.Lists;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.launch.platform.container.ContainerHandleVirtual;
import org.spongepowered.asm.launch.platform.container.IContainerHandle;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.mixin.transformer.IMixinTransformerFactory;
import org.spongepowered.asm.service.*;
import org.spongepowered.asm.transformers.MixinClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

public class HelpingsMixinService extends MixinServiceAbstract implements IClassProvider, IClassBytecodeProvider {
    public static IMixinTransformer transformer;

    @Override
    public ClassNode getClassNode(String s) throws ClassNotFoundException, IOException {
        return getClassNode(s, true);
    }

    @Override
    public ClassNode getClassNode(String s, boolean b) throws ClassNotFoundException, IOException {
        InputStream stream = getResourceAsStream("%s.class".formatted(s.replace('.', '/')));
        MixinClassReader reader = new MixinClassReader(stream.readAllBytes(), s);
        stream.close();

        ClassNode node = new ClassNode();
        reader.accept(node, ClassReader.EXPAND_FRAMES);

        return node;
    }

    @Override
    public URL[] getClassPath() {
        return new URL[0];
    }

    @Override
    public Class<?> findClass(String s) throws ClassNotFoundException {
        return Class.forName(s);
    }

    @Override
    public Class<?> findClass(String s, boolean b) throws ClassNotFoundException {
        return Class.forName(s, b, getClass().getClassLoader());
    }

    @Override
    public Class<?> findAgentClass(String s, boolean b) throws ClassNotFoundException {
        return findClass(s, b);
    }

    @Override
    public String getName() {
        return "ARHelpings";
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public IClassProvider getClassProvider() {
        return this;
    }

    @Override
    public IClassBytecodeProvider getBytecodeProvider() {
        return this;
    }

    @Override
    public ITransformerProvider getTransformerProvider() {
        return null;
    }

    @Override
    public IClassTracker getClassTracker() {
        return null;
    }

    @Override
    public IMixinAuditTrail getAuditTrail() {
        return null;
    }

    @Override
    public Collection<String> getPlatformAgents() {
        return Lists.newArrayList(HelpingsMixinAgent.class.getName());
    }

    @Override
    public IContainerHandle getPrimaryContainer() {
        return new ContainerHandleVirtual(getName());
    }

    @Override
    public InputStream getResourceAsStream(String s) {
        return ARHelpings.class.getClassLoader().getResourceAsStream(s);
    }

    @Override
    public void offer(IMixinInternal internal) {
        if (internal instanceof IMixinTransformerFactory factory && HelpingsMixinService.transformer == null) HelpingsMixinService.transformer = factory.createTransformer();
        super.offer(internal);
    }
}
