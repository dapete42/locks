package net.dapete.locks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SemaphoresTest {

    @Test
    void instance() throws InterruptedException {
        final var semaphores = Semaphores.instance(2, Integer.class);

        final var semaphore = semaphores.get(1);

        assertEquals(2, semaphore.availablePermits());
        semaphore.acquire();
        try {
            assertEquals(1, semaphore.availablePermits());
        } finally {
            semaphore.release();
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void instance(boolean fair) throws InterruptedException {
        final var semaphores = Semaphores.instance(fair, 2, Integer.class);

        assertEquals(fair, semaphores.isFair());

        final var semaphore = semaphores.get(1);
        assertEquals(2, semaphore.availablePermits());
        semaphore.acquire();
        try {
            assertEquals(fair, semaphore.isFair());
            assertEquals(1, semaphore.availablePermits());
        } finally {
            semaphore.release();
        }
    }

    @Test
    void size() {
        final var semaphores = Semaphores.instance(2, Integer.class);

        assertEquals(0, semaphores.size(), "Initial size should be 0");
    }

}
