package org.salespointframework.core.accountancy;

import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.salespointframework.core.money.Money;

/**
 * This interface provides access to an accountancy. An accountancy consists of
 * <code>AccountancyEntries</code>.
 * 
 * @author Hannes Weisbach
 * 
 */
public interface Accountancy {

    /**
     * Adds a new <code>AccountancyEntry</code> to this <code>Accountancy</code>
     * The new entry is persisted transparently into the underlying database.
     * Once an <code>AccountancyEntry</code> has been written to the database,
     * it cannot be added a second time.
     * 
     * @param accountancyEntry
     *            <code>AccountancyEntry</code> which should be added to the
     *            <code>Accountancy</code>
     */
    void addEntry(AbstractAccountancyEntry accountancyEntry);

    /**
     * Adds multiple <code>AccountancyEntry</code>s to this
     * <code>Accountancy</code> and persists them to underlying database. Once
     * an <code>AccountancyEntry</code> has been added to the persistence layer,
     * it cannot be modified again.
     * 
     * @param accountancyEntries
     *            an {@link Iterable} of all <code>AccountancyEntry</code>s
     *            which should be added to the <code>Accountancy</code>
     */
    void addEntries(Iterable<AbstractAccountancyEntry> accountancyEntries);

    /**
     * Returns all <code>AccountancyEntry</code>s in between the dates
     * <code>from</code> and <code>to</code>, including from and to. So every
     * entry with an time stamp <= to and >= from is returned. If no entries
     * within the specified time span exist, an empty Iterable is returned.
     * 
     * @param from
     *            time stamp denoting the start of the requested time period
     * @param to
     *            time stamp denoting the end of the requested time period
     * @return an unmodifiable Iterable containing all entries between from and
     *         to
     */
    Iterable<AbstractAccountancyEntry> getEntries(DateTime from, DateTime to);

    // TODO häßlich mit Class<T>, gez. Paul
    /**
     * Returns all <code>AccountancyEntry</code>s of the specified type
     * <code>class</code>. If no entries of the specified type exist, an empty
     * Iterable is returned.
     * 
     * @param <T>
     *            Type of the specific entries that are stored in the
     *            <code>Accountancy</code>
     * 
     * @param clazz
     *            The type of the entries.
     * @return an unmodifiable Iterable containing all entries of type clazz
     */
    <T extends AbstractAccountancyEntry> Iterable<T> getEntries(Class<T> clazz);

    /**
     * Returns all <code>AccountancyEntry</code>s in between the dates
     * <code>from</code> and <code>to</code> of the specified class type
     * <code>clazz</code>, including from and to. So every entry with an time
     * stamp <= to and >= from is returned. If no entries within the specified
     * time span exist, or no entries of the specified class type exist, an
     * empty Iterable is returned.
     * 
     * @param <T>
     *            type of the requested entries
     * 
     * @param from
     *            time stamp denoting the start of the requested time period
     * @param to
     *            time stamp denoting the end of the requested time period
     * @param clazz
     *            class type of the requested entries
     * @return an unmodifiable Iterable containing all entries between from and
     *         to of type T
     */
    <T extends AbstractAccountancyEntry> Iterable<T> getEntries(Class<T> clazz, DateTime from, DateTime to);

    /**
     * Returns all <code>AccountancyEntry</code>s of type <code>clazz</code>,
     * which have their <code>timeStamp</code> within (including)
     * <code>from</code> and <code>to</code>. <br>
     * The time between <code>from</code> and <code>to</code> is divided into
     * parts of <code>period</code> length. According to their time stamp,
     * entries are sorted in exactly one of the time intervals. The last time
     * interval may be shorter than <code>period</code>.<br>
     * Returned is a map, having a <code>Interval</code> objects as its key, and
     * an <code>Iterable&lt;T&gt;</code> as value. The <code>Iterable</code>
     * contains all entries of the specific type with its time stamp in the
     * interval specified by the key.
     * 
     * 
     * @param <T>
     *            type of the requested entries
     * 
     * @param clazz
     *            class type of the requested entries, has to be a subclass of
     *            AbstractAccountancyEntry
     * @param from
     *            all returned entries will have a time stamp after
     *            <code>from</code>
     * @param to
     *            all returned entries will have a time stamp before
     *            <code>to</code>
     * @param period
     *            length of the time intervals, the period between
     *            <code>from</code> and <code>to</code> is divided
     * @return a map, with intervals of <code>period</code> length between
     *         <code>from</code> and <code>to</code> as keys, and as value an
     *         <code>Iterable</code> containing all entries within the key-
     *         <code>Interval</code>
     */
    <T extends AbstractAccountancyEntry> Map<Interval, Iterable<T>> getEntries(Class<T> clazz, DateTime from, DateTime to, Period period);

    /**
     * Returns the sum of the field <code>amount</code> of all
     * <code>AccountancyEntry</code>s of type <code>clazz</code>, which have
     * their <code>timeStamp</code> within (including) <code>from</code> and
     * <code>to</code>. <br>
     * The time between <code>from</code> and <code>to</code> is divided into
     * parts of <code>period</code> length. According to their time stamp,
     * entries are sorted in exactly one of the time intervals. The last time
     * interval may be shorter than <code>period</code>.<br>
     * Returned is a map, having a <code>Interval</code> objects as its key, and
     * an <code>Money</code> as value. The <code>Money</code> object's value is
     * equal to the sum of all entries' <code>amount</code>-field, with a time
     * stamp within the key-<code>Interval</code>.
     * 
     * @param <T>
     *            type of the requested entries
     * 
     * @param clazz
     *            class type of the requested entries, has to be a subclass of
     *            AbstractAccountancyEntry
     * @param from
     *            all returned entries will have a time stamp after
     *            <code>from</code>
     * @param to
     *            all returned entries will have a time stamp before
     *            <code>to</code>
     * @param period
     *            length of the time intervals, the period between
     *            <code>from</code> and <code>to</code> is divided
     * @return a map, with intervals of <code>period</code> length between
     *         <code>from</code> and <code>to</code> as keys, and as value a
     *         <code>Money</code> object, equal to the sum of the amount fields
     *         of all entries within the key-<code>Interval</code>
     */
    <T extends AbstractAccountancyEntry> Map<Interval, Money> getSalesVolume(Class<T> clazz, DateTime from, DateTime to, Period period);
}