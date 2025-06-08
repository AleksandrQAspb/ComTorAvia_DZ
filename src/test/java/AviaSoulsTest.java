import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class AviaSoulsTest {

    // 1. findAll и add
    @Test
    public void test_addAndFindAll_oneTicket() {
        AviaSouls manager = new AviaSouls();
        Ticket t1 = new Ticket("A", "B", 100, 7, 9);
        manager.add(t1);
        assertArrayEquals(new Ticket[]{t1}, manager.findAll());
    }

    @Test
    public void test_findAll_whenNoTickets() {
        AviaSouls manager = new AviaSouls();
        assertArrayEquals(new Ticket[0], manager.findAll());
    }

    // 2. Поиск по маршруту по цене (search)
    @Test
    public void search_noTickets_returnsEmpty() {
        AviaSouls manager = new AviaSouls();
        assertArrayEquals(new Ticket[0], manager.search("A", "B"));
    }

    @Test
    public void search_noMatch_returnsEmpty() {
        AviaSouls manager = new AviaSouls();
        manager.add(new Ticket("X", "Y", 100, 4, 10));
        assertArrayEquals(new Ticket[0], manager.search("A", "B"));
    }

    @Test
    public void search_oneMatch_returnsSortedOne() {
        AviaSouls manager = new AviaSouls();
        Ticket t = new Ticket("A", "B", 350, 12, 18);
        manager.add(new Ticket("C", "C", 200, 1, 2));
        manager.add(t);
        Ticket[] expected = {t};
        assertArrayEquals(expected, manager.search("A", "B"));
    }

    @Test
    public void search_severalMatches_returnsSortedByPrice() {
        AviaSouls manager = new AviaSouls();
        Ticket t1 = new Ticket("A", "B", 500, 7, 9);
        Ticket t2 = new Ticket("A", "B", 200, 8, 10);
        Ticket t3 = new Ticket("A", "B", 300, 11, 12);
        Ticket t4 = new Ticket("X", "B", 199, 11, 12); // не включится в ответ

        manager.add(t1);
        manager.add(t2);
        manager.add(t3);
        manager.add(t4);

        Ticket[] expected = {t2, t3, t1}; // сортировка по цене возрастанию
        assertArrayEquals(expected, manager.search("A", "B"));
    }

    @Test
    public void search_matchesWithEqualPrices_sortedByOriginalOrder() {
        AviaSouls manager = new AviaSouls();
        Ticket t1 = new Ticket("A", "B", 300, 1, 2);
        Ticket t2 = new Ticket("A", "B", 300, 3, 4);
        manager.add(t2);
        manager.add(t1);

        Ticket[] expected = {t2, t1}; // стабильная сортировка: как в массиве (оба по цене 300)
        assertArrayEquals(expected, manager.search("A", "B"));
    }

    // 3. searchAndSortBy

    @Test
    public void searchAndSortBy_noTickets_returnsEmpty() {
        AviaSouls manager = new AviaSouls();
        Comparator<Ticket> cmp = new TicketTimeComparator();
        assertArrayEquals(new Ticket[0], manager.searchAndSortBy("A", "B", cmp));
    }

    @Test
    public void searchAndSortBy_noMatch_returnsEmpty() {
        AviaSouls manager = new AviaSouls();
        manager.add(new Ticket("X", "Y", 100, 6, 7));
        Comparator<Ticket> cmp = new TicketTimeComparator();
        assertArrayEquals(new Ticket[0], manager.searchAndSortBy("A", "B", cmp));
    }

    @Test
    public void searchAndSortBy_oneMatch_returnsOne() {
        AviaSouls manager = new AviaSouls();
        Ticket t = new Ticket("A", "B", 150, 12, 13);
        manager.add(new Ticket("H", "I", 123, 1, 2));
        manager.add(t);
        Comparator<Ticket> cmp = new TicketTimeComparator();
        assertArrayEquals(new Ticket[]{t}, manager.searchAndSortBy("A", "B", cmp));
    }

    @Test
    public void searchAndSortBy_severalMatches_sortByFlightTime() {
        AviaSouls manager = new AviaSouls();
        Ticket t1 = new Ticket("A", "B", 400, 0, 4); // 4h
        Ticket t2 = new Ticket("A", "B", 500, 6, 7); // 1h
        Ticket t3 = new Ticket("A", "B", 300, 2, 9); // 7h
        Ticket t4 = new Ticket("Z", "Y", 110, 1, 2);

        manager.add(t1);
        manager.add(t2);
        manager.add(t3);
        manager.add(t4);

        Comparator<Ticket> cmp = new TicketTimeComparator();
        Ticket[] expected = {t2, t1, t3}; // 1h, 4h, 7h
        assertArrayEquals(expected, manager.searchAndSortBy("A", "B", cmp));
    }

    @Test
    public void searchAndSortBy_equalFlightTimes_originalOrder() {
        AviaSouls manager = new AviaSouls();
        Ticket t1 = new Ticket("A", "B", 100, 1, 4); // 3h
        Ticket t2 = new Ticket("A", "B", 200, 2, 5); // 3h
        manager.add(t2);
        manager.add(t1);

        Comparator<Ticket> cmp = new TicketTimeComparator();
        Ticket[] expected = {t2, t1};
        assertArrayEquals(expected, manager.searchAndSortBy("A", "B", cmp));
    }

    //  ДОБАВЛЯЕМ основные тесты для Ticket
    @Test
    public void testEquals_SameObject() {
        Ticket ticket = new Ticket("A", "B", 100, 10, 12);
        assertTrue(ticket.equals(ticket));
    }

    @Test
    public void testEquals_Null() {
        Ticket ticket = new Ticket("A", "B", 100, 10, 12);
        assertFalse(ticket.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        Ticket ticket = new Ticket("A", "B", 100, 10, 12);
        assertFalse(ticket.equals("not a ticket"));
    }

    @Test
    public void testEquals_SameFields() {
        Ticket t1 = new Ticket("A", "B", 100, 10, 12);
        Ticket t2 = new Ticket("A", "B", 100, 10, 12);
        assertTrue(t1.equals(t2));
        assertTrue(t2.equals(t1));
    }

    @Test
    public void testNotEquals_DifferentFrom() {
        Ticket t1 = new Ticket("A", "B", 100, 10, 12);
        Ticket t2 = new Ticket("X", "B", 100, 10, 12);
        assertFalse(t1.equals(t2));
    }

    @Test
    public void testNotEquals_DifferentTo() {
        Ticket t1 = new Ticket("A", "B", 100, 10, 12);
        Ticket t2 = new Ticket("A", "X", 100, 10, 12);
        assertFalse(t1.equals(t2));
    }

    @Test
    public void testNotEquals_DifferentPrice() {
        Ticket t1 = new Ticket("A", "B", 100, 10, 12);
        Ticket t2 = new Ticket("A", "B", 99, 10, 12);
        assertFalse(t1.equals(t2));
    }

    @Test
    public void testNotEquals_DifferentTimeFrom() {
        Ticket t1 = new Ticket("A", "B", 100, 10, 12);
        Ticket t2 = new Ticket("A", "B", 100, 9, 12);
        assertFalse(t1.equals(t2));
    }

    @Test
    public void testNotEquals_DifferentTimeTo() {
        Ticket t1 = new Ticket("A", "B", 100, 10, 12);
        Ticket t2 = new Ticket("A", "B", 100, 10, 13);
        assertFalse(t1.equals(t2));
    }

    @Test
    public void testHashCode_EqualsObjects() {
        Ticket t1 = new Ticket("A", "B", 100, 10, 12);
        Ticket t2 = new Ticket("A", "B", 100, 10, 12);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    public void testHashCode_NotEqualsObjects() {
        Ticket t1 = new Ticket("A", "B", 100, 10, 12);
        Ticket t2 = new Ticket("A", "B", 99, 10, 12);
        assertNotEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    public void testGetters() {
        Ticket t = new Ticket("FROM", "TO", 555, 21, 22);
        assertEquals("FROM", t.getFrom());
        assertEquals("TO", t.getTo());
        assertEquals(555, t.getPrice());
        assertEquals(21, t.getTimeFrom());
        assertEquals(22, t.getTimeTo());
    }

    @Test
    public void testCompareTo_LessEqualGreater() {
        Ticket t1 = new Ticket("A", "B", 100, 10, 12);
        Ticket t2 = new Ticket("A", "B", 200, 10, 12);
        Ticket t3 = new Ticket("A", "B", 100, 10, 12);
        assertTrue(t1.compareTo(t2) < 0);
        assertTrue(t2.compareTo(t1) > 0);
        assertEquals(0, t1.compareTo(t3));
    }

    // Полный тест поиска с проверкой сортировки — пример:
    @Test
    public void testManagerSearch_SortedByPrice() {
        AviaSouls manager = new AviaSouls();
        Ticket t1 = new Ticket("MOW", "LED", 3000, 10, 12);
        Ticket t2 = new Ticket("MOW", "LED", 1000, 10, 12);
        Ticket t3 = new Ticket("MOW", "LED", 2000, 10, 12);
        manager.add(t1);
        manager.add(t2);
        manager.add(t3);

        Ticket[] expected = {t2, t3, t1};
        assertArrayEquals(expected, manager.search("MOW", "LED"));
    }

    @Test
    public void testManagerSearchAndSortByTime() {
        AviaSouls manager = new AviaSouls();
        Ticket t1 = new Ticket("MOW", "LED", 3000, 10, 14); // 4 часа
        Ticket t2 = new Ticket("MOW", "LED", 1000, 10, 11); // 1 час
        Ticket t3 = new Ticket("MOW", "LED", 2000, 10, 13); // 3 часа
        manager.add(t1);
        manager.add(t2);
        manager.add(t3);

        TicketTimeComparator comp = new TicketTimeComparator();
        Ticket[] expected = {t2, t3, t1};
        assertArrayEquals(expected, manager.searchAndSortBy("MOW", "LED", comp));
    }

    @Test
    public void testSearch_FromMatches_ToNotMatches() {
        // Покрываем ветку, когда from правильное, а to НЕ подходит (should not be found)
        AviaSouls souls = new AviaSouls();
        Ticket t1 = new Ticket("MOW", "LED", 1000, 10, 12);
        souls.add(t1);

        Ticket[] expected = {};
        assertArrayEquals(expected, souls.search("MOW", "EKT")); // from совпал, to не совпал
    }

    @Test
    public void testSearch_ToMatches_FromNotMatches() {
        // Покрываем ветку, когда to правильное, а from НЕ подходит (should not be found)
        AviaSouls souls = new AviaSouls();
        Ticket t1 = new Ticket("MOW", "LED", 1000, 10, 12);
        souls.add(t1);

        Ticket[] expected = {};
        assertArrayEquals(expected, souls.search("SPB", "LED")); // to совпал, from не совпал
    }

    @Test
    public void testSearchAndSortBy_FromMatches_ToNotMatches() {
        // Аналогичный тест для searchAndSortBy — логика абсолютно та же
        AviaSouls souls = new AviaSouls();
        Ticket t1 = new Ticket("MSK", "KZN", 1200, 13, 16);
        souls.add(t1);

        Ticket[] expected = {};
        TicketTimeComparator comparator = new TicketTimeComparator();
        assertArrayEquals(expected, souls.searchAndSortBy("MSK", "EKT", comparator));
    }

    @Test
    public void testSearchAndSortBy_ToMatches_FromNotMatches() {
        // Аналогичный тест для searchAndSortBy — логика абсолютно та же
        AviaSouls souls = new AviaSouls();
        Ticket t1 = new Ticket("MSK", "KZN", 1200, 13, 16);
        souls.add(t1);

        Ticket[] expected = {};
        TicketTimeComparator comparator = new TicketTimeComparator();
        assertArrayEquals(expected, souls.searchAndSortBy("EKT", "KZN", comparator));
    }
}
