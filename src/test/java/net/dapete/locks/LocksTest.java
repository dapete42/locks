package net.dapete.locks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class LocksTest {

    @Test
    void withSupplier() {
        final Supplier<ReentrantLock> lockSupplier = mock();
        when(lockSupplier.get()).thenAnswer(invocation -> new ReentrantLock());

        final var locks = Locks.withSupplier(lockSupplier);
        verifyNoInteractions(lockSupplier);

        locks.lock(1).unlock();

        verify(lockSupplier).get();
    }

    @Test
    void reentrant() {
        final var locks = Locks.reentrant(Integer.class);

        final var lock = locks.lock(1);
        try {
            assertFalse(lock.isFair());
        } finally {
            lock.unlock();
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void reentrant(boolean fair) {
        final var locks = Locks.reentrant(fair, Integer.class);

        final var lock = locks.lock(1);
        try {
            assertEquals(fair, lock.isFair());
        } finally {
            lock.unlock();
        }
    }

}
