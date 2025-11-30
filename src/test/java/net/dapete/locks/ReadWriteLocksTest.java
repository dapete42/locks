package net.dapete.locks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class ReadWriteLocksTest {

    @Test
    void withSupplier() {
        final Supplier<ReentrantReadWriteLock> lockSupplier = mock();
        when(lockSupplier.get()).thenAnswer(invocation -> new ReentrantReadWriteLock());

        final var locks = ReadWriteLocks.withSupplier(lockSupplier);

        verifyNoInteractions(lockSupplier);

        locks.readLock(1).readLock().unlock();

        verify(lockSupplier).get();
    }

    @Test
    void reentrant() {
        final var locks = ReadWriteLocks.reentrant(Integer.class);

        final var lock = locks.readLock(1);
        try {
            assertFalse(lock.isFair());
        } finally {
            lock.readLock().unlock();
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void reentrant(boolean fair) {
        final var locks = ReadWriteLocks.reentrant(fair, Integer.class);

        final var lock = locks.readLock(1);
        try {
            assertEquals(fair, lock.isFair());
        } finally {
            lock.readLock().unlock();
        }
    }

}
