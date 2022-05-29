package arfox.helpings.lambda;

import java.util.function.Function;

@FunctionalInterface
public interface ThrowingPass<T> {
    T accept() throws Throwable;

    static <T> T of(ThrowingPass<T> throwing) {
        try {
            return throwing.accept();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }
}
