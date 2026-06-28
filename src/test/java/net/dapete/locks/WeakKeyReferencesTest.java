package net.dapete.locks;

import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

class WeakKeyReferencesTest {

    private static class TestWeakKeyReferences extends WeakKeyReferences<@Nullable Integer, Object> {

        private TestWeakKeyReferences() {
            super(Object::new);
        }

    }

    private static MethodHandle getReferenceMethodHandle;
    private static VarHandle referenceQueueVarHandle;
    private static MethodHandle processQueueMethodHandle;

    @BeforeAll
    static void beforeAll() throws IllegalAccessException, NoSuchMethodException, NoSuchFieldException {
        final var lookup = MethodHandles.lookup();
        final var privateLookup = MethodHandles.privateLookupIn(WeakKeyReferences.class, lookup);

        final var getReferenceMethod = WeakKeyReferences.class.getDeclaredMethod("getReference", Object.class);
        getReferenceMethod.setAccessible(true);
        getReferenceMethodHandle = lookup.unreflect(getReferenceMethod);

        referenceQueueVarHandle = privateLookup.findVarHandle(WeakKeyReferences.class, "referenceQueue", ReferenceQueue.class);

        final var processQueueMethod = WeakKeyReferences.class.getDeclaredMethod("processQueue");
        processQueueMethod.setAccessible(true);
        processQueueMethodHandle = lookup.unreflect(processQueueMethod);
    }

    private void clearKeyReference(WeakKeyReferences<?, ?> objects, Integer key) throws Throwable {
        final var objectReference = getReferenceMethodHandle.invoke(objects, key);
        ((WeakKeyReference<?, ?>) objectReference).clear();
    }

    private static ReferenceQueue<Object> getReferenceQueue(TestWeakKeyReferences objects) {
        return (ReferenceQueue<Object>) referenceQueueVarHandle.get(objects);
    }

    @Test
    void testObjectsAreReleasedWhenUnused() {
        final var objects = new TestWeakKeyReferences();

        objects.get(1);

        assertEquals(1, objects.size());

        /*
         * Wait up to 30 seconds for the size to change after dereferencing the object. There is no way to force the garbage collector to run, System.gc() is just
         * a suggestion, but this seems to work.
         */
        System.gc();
        await().atMost(30, TimeUnit.SECONDS).until(() -> objects.size() == 0);
    }

    @Test
    void get_null() {
        final var objects = new TestWeakKeyReferences();

        final var object = objects.get(null);

        assertNotNull(object);
    }

    @Test
    void get_createNewObjectIfKeyReferenceIsNull() throws Throwable {
        final var objects = new TestWeakKeyReferences();

        // get one object and then clear the reference to it
        final var object1 = objects.get(1);
        clearKeyReference(objects, 1);

        // this should not be null, but a different object
        final var object2 = objects.get(1);

        assertNotNull(object2);
        assertNotSame(object1, object2);
    }

    @Test
    void get_differentForDifferentKeys() {
        final var objects = new TestWeakKeyReferences();

        assertNotSame(objects.get(1), objects.get(2));
    }

    @Test
    void get_identicalForIdenticalKey() {
        final var objects = new TestWeakKeyReferences();

        assertSame(objects.get(1), objects.get(1));
    }

    @Test
    void processQueue_ignoresNonKeyReference() throws Throwable {
        final var objects = new TestWeakKeyReferences();
        final var queue = getReferenceQueue(objects);
        final var nonKeyReference = new WeakReference<>(new Object(), queue);
        nonKeyReference.enqueue();

        processQueueMethodHandle.invoke(objects);

        assertNull(queue.poll());
    }

    @Test
    void size() {
        final var objects = new TestWeakKeyReferences();

        // Initially, the size should be 0
        assertEquals(0, objects.size(), "Initial size should be 0");

        // Add some objects
        final var objectList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            objectList.add(objects.get(i));
        }

        // Size should reflect the number of objects
        assertEquals(5, objects.size(), "Size should match the number of objects");

        // Clear some references and force GC
        objectList.subList(0, 3).clear();
        System.gc();

        // Wait for size to decrease
        await().atMost(30, TimeUnit.SECONDS).until(() -> objects.size() == 2);

        // Clear remaining references
        objectList.clear();
        System.gc();

        // Wait for size to reach 0
        await().atMost(30, TimeUnit.SECONDS).until(() -> objects.size() == 0);
    }

}
