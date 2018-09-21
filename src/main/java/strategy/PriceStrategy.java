package strategy;

/**
 * This interface is used to create a price strategy map.
 *
 * @param <T> type of total price. In my app i used type long to able to use .sum() method of stream API.
 *           In practice it can be any type like float, BigInteger etc.
 */
@FunctionalInterface
public interface PriceStrategy<T>
{
    /**
     * Method accepts the amount of items been bought and do the calculation specific to merchandise type.
     *
     * @param amount number of items been bought
     * @return total price for one type of merchandise
     */
    public T calculatePrice(long amount);
}
