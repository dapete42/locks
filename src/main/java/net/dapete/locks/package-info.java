/**
 * <p>
 * This package contains classes for key-based locking, as in a way to obtain {@link java.util.concurrent.locks.Lock Lock}s
 * or {@link java.util.concurrent.locks.ReadWriteLock ReadWriteLock}s which are identified by a key. These locks are guaranteed to by different for each key and
 * will be kept as long as they are referenced.
 * </p>
 * <p>
 * The possible ways to obtain these are:
 * </p>
 * <table>
 *     <thead>
 *         <tr>
 *             <th>Type of Lock</th>
 *             <th>Method(s)</th>
 *             <th>Type returned</th>
 *         </tr>
 *     </thead>
 *     <tbody>
 *         <tr>
 *             <td>{@link java.util.concurrent.locks.ReentrantLock RentrantLock}</td>
 *             <td>{@link net.dapete.locks.Locks#reentrant() Locks.reentrant()}<br>{@link net.dapete.locks.Locks#reentrant(Class)}</td>
 *             <td>{@link net.dapete.locks.ReentrantLocks}</td>
 *         </tr>
 *         <tr>
 *             <td>any implementation of {@link java.util.concurrent.locks.Lock Lock}</td>
 *             <td>{@link net.dapete.locks.Locks#withSupplier(java.util.function.Supplier) Locks.withSupplier(Supplier)}</td>
 *             <td>{@link net.dapete.locks.Locks}</td>
 *         </tr>
 *         <tr>
 *             <td>{@link java.util.concurrent.locks.ReentrantReadWriteLock ReentrantReadWriteLock}</td>
 *             <td>{@link net.dapete.locks.ReadWriteLocks#reentrant()}<br>{@link net.dapete.locks.ReadWriteLocks#reentrant(Class)}</td>
 *             <td>{@link net.dapete.locks.ReentrantReadWriteLocks}</td>
 *         </tr>
 *         <tr>
 *             <td>any implementation of {@link java.util.concurrent.locks.ReadWriteLock ReadWriteLock}</td>
 *             <td>{@link net.dapete.locks.ReadWriteLocks#withSupplier(java.util.function.Supplier) ReadWriteLocks.withSupplier(Supplier)}</td>
 *             <td>{@link net.dapete.locks.ReadWriteLocks}</td>
 *         </tr>
 *     </tbody>
 * </table>
 * <p>
 *     All implementations use generics for the type of the key as well as the type of lock, if this is not fixed (as with
 *     {@link net.dapete.locks.ReentrantLocks} and {@link net.dapete.locks.ReentrantReadWriteLocks}).
 * </p>
 * <p>
 *     The parameter on the {@code reentrant(Class)} methods is the {@link java.lang.Class} of the key type. This is a shortcut for readability if the compiler
 *     does not automatically detect it, for example:
 * </p>
 * <pre>
 *     {@code var x = Locks.reentrant(String.class)}
 * </pre>
 * <p>
 *     The {@code withSupplier(Supplier)} methods allow for any implementation of {@link java.util.concurrent.locks.Lock Lock} or
 *     {@link java.util.concurrent.locks.ReadWriteLock ReadWriteLock} to be used. Just use the constructor as the {@code Supplier}, for example:
 * </p>
 * <pre>
 *     {@code Locks.withSupplier(SuperEpicLock::new)}
 * </pre>
 * <p>
 *     This can also be used if 'fair' versions of {@code ReentrantLock} or {@code ReentrantReadWriteLock} are needed, for example:
 * </p>
 * <pre>
 *     {@code Locks.withSupplier(() -> new ReentrantLock(true))}.
 * </pre>
 */
package net.dapete.locks;
