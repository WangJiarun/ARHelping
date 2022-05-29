package arfox.helpings.lambda;

@FunctionalInterface
public interface ThrowingRunnable {
    void accept() throws Throwable;

    static Runnable of(ThrowingRunnable throwing) {
        return () -> {
            try {
                throwing.accept();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        };
    }
}
