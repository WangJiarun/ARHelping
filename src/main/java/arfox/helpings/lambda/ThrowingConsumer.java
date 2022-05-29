package arfox.helpings.lambda;

import java.util.function.Consumer;

@FunctionalInterface
public interface ThrowingConsumer<T> {
    void accept(T in) throws Throwable;

    static <T> Consumer<T> of(ThrowingConsumer<T> throwing) {
        return (T) -> {
            try {
                throwing.accept(T);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        };
    }
}
