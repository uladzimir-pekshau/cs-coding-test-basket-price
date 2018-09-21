package com.pricecalculator;

import strategy.PriceStrategy;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A class containing psvm and doing all the logic. As the task was to write a SIMPLE program - i decided to
 * put everything into one class and add comments on what can be improved/extended for future needs.
 */
public class PriceCalculatorMain
{
    /**
     * This map contains the core logic of application - price strategies for types of items.
     * Each key is a name of item, value - price strategy.
     * For better readability and easier scalability we could create enum of items. That would do:
     * - less bug-prone code because we use enum instead of manually written string values
     * - we can encapsulate strategy logic into enum without need of creating extra map
     */
    private static Map<String, PriceStrategy<Long>> priceStrategyMap = createPriceStrategyMap();

    /**
     * I do not do any safety-checks in my app, e.g. what to do if have item type without price strategy?
     * Switching to enum would partially solve this issue.
     * Or we could add some data validation on input, or we could agree on some extra logic if unknown item is added.
     * Initialization of this map can be extracted to separate interface-based class where we could configure
     * different ways of strategies initialization, e.g. read configuration from file/db.
     *
     * @return map of price strategies
     */
    private static Map<String, PriceStrategy<Long>> createPriceStrategyMap()
    {
        Map<String, PriceStrategy<Long>> priceStrategyMap = new HashMap<>();
        priceStrategyMap.put("Apple", (amount) -> amount * 35);
        priceStrategyMap.put("Banana", (amount) -> amount * 20);
        priceStrategyMap.put("Melon", (amount) -> (amount - Math.floorDiv(amount, 2)) * 50);
        priceStrategyMap.put("Lime", (amount) -> (amount - Math.floorDiv(amount, 3)) * 15);
        return priceStrategyMap;
    }

    /**
     * Method calculates the total price of items in the list.
     * First line of the method converts list of items to basket - hashmap, where
     * - key - item name/type
     * - value - amount of items in basket
     * Second line create a list of total prices for each item type and output the sum of all prices
     * This method could've been extracted to some helper or entity class, e.g. Basket.class which would contain
     * list of items as a member and been able to do number of logics about basket, e.g. calculate total price,
     * add some related merchandise, analyze the list and add some missing items to it etc.
     *
     * @param list initial list of items to buy
     * @return total price of all items in list
     */
    private static long calculatePrice(List<String> list)
    {
        Map<String, Long> basket = list.stream().collect(Collectors.groupingBy((i) -> i, Collectors.mapping((i) -> i, Collectors.counting())));
        return basket.entrySet().stream().mapToLong((e) -> priceStrategyMap.get(e.getKey()).calculatePrice(e.getValue())).sum();
    }

    /**
     * main methods contains tests of calculatePrice method. If we would do real application and not psvm,
     * all those tests could be moved to unit test classes. We would do separate tests for basket creation,
     * price calculation and maybe some checks price strategies (if we would have predefined ones).
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        List<String> list1 = Arrays.asList("Apple", "Apple", "Banana");
        List<String> list2 = Arrays.asList("Apple", "Banana", "Banana", "Melon", "Melon");
        List<String> list3 = Arrays.asList("Apple", "Banana", "Banana", "Melon", "Melon", "Melon", "Lime", "Lime", "Lime", "Lime");
        List<String> list4 = Arrays.asList("Banana", "Melon", "Banana", "Lime", "Apple", "Apple", "Melon", "Melon", "Melon", "Banana", "Lime", "Lime");
        List<String> list5 = Arrays.asList("Melon", "Lime", "Melon", "Lime", "Melon", "Lime", "Melon", "Lime");
        List<String> list6 = Collections.emptyList();
        List<String> list7 = Collections.singletonList("Lime");

        System.out.println("Test with list1 " + (calculatePrice(list1) == 90 ? "PASSED" : "NOT PASSED"));
        System.out.println("Test with list2 " + (calculatePrice(list2) == 125 ? "PASSED" : "NOT PASSED"));
        System.out.println("Test with list3 " + (calculatePrice(list3) == 220 ? "PASSED" : "NOT PASSED"));
        System.out.println("Test with list4 " + (calculatePrice(list4) == 260 ? "PASSED" : "NOT PASSED"));
        System.out.println("Test with list5 " + (calculatePrice(list5) == 145 ? "PASSED" : "NOT PASSED"));
        System.out.println("Test with list6 " + (calculatePrice(list6) == 0 ? "PASSED" : "NOT PASSED"));
        System.out.println("Test with list7 " + (calculatePrice(list7) == 15 ? "PASSED" : "NOT PASSED"));
    }
}