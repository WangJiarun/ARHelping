package arfox.helpings.lambda;

@FunctionalInterface
public interface ThrowingIgnore {
    void accept() throws Throwable;

    static void of(ThrowingIgnore throwing) {
        try {
            throwing.accept();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    static boolean ifThrow(ThrowingIgnore throwing, boolean ifThrow) {
        try {
            throwing.accept();
            return !ifThrow;
        } catch (Throwable ignored) {}

        return ifThrow;
    }
}
