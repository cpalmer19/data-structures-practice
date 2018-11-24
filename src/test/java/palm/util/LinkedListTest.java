package palm.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class LinkedListTest {

    @Nested
    class FactoryTest {
        @Test
        void list_of_nothing() {
            assertEquals(0, LinkedList.of().size());
        }
    
        @Test
        void list_of_items() {
            assertEquals(2, LinkedList.of("Foo", "Bar").size());
        }
    }

    @Nested
    class GetTest {
        LinkedList<String> list;

        @BeforeEach
        void initList() {
            list = LinkedList.of("Foo", "Bar");
        }

        @Test
        void get_under() {
            assertThrows(IndexOutOfBoundsException.class, () -> {
                list.get(-5);
            });
        }
    
        @Test
        void get_over() {
            assertThrows(IndexOutOfBoundsException.class, () -> {
                list.get(5);
            });
        }
    
        @Test
        void get_size() {
            assertThrows(IndexOutOfBoundsException.class, () -> {
                list.get(list.size());
            });
        }
    
        @Test
        void get_last() {
            assertEquals("Bar", list.get(list.size()-1), "Item should be at end");
        }
    
        @Test
        void get_last_negative() {
            assertEquals("Bar", list.get(-1), "-1 should return last item");
        }
    
        @Test
        void get_negative() {
            assertEquals("Foo", list.get(-2));
        }
    
        @Test
        void get_first() {
            assertEquals("Foo", list.get(0));
        }
    }

    @Nested
    class InsertTest {
        LinkedList<String> list;

        @BeforeEach
        void initList() {
            list = LinkedList.of("Foo", "Bar");
        }
        
        @Test
        void insert_index_under() {
            assertThrows(IndexOutOfBoundsException.class, () -> {
                list.insert("Hi", -5);
            });
        }
        
        @Test
        void insert_index_over() {
            assertThrows(IndexOutOfBoundsException.class, () -> {
                list.insert("Hi", 5);
            });
        }
    
        @Test
        void insert_index_positive_mid() {
            list.insert("Hi", 1);
    
            assertEquals(3, list.size());
            assertEquals("Foo", list.get(0), "First item should be unaffected");
            assertEquals("Hi", list.get(1), "Item should be at inserted index");
            assertEquals("Bar", list.get(2), "Items should shift up");
        }
    
        @Test
        void insert_index_negative_mid() {
            list.insert("Hi", -2);
    
            assertEquals(3, list.size());
            assertEquals("Foo", list.get(0), "First item should be unaffected");
            assertEquals("Hi", list.get(1), "Item should be at inserted index");
            assertEquals("Bar", list.get(2), "Items should shift up");
        }

        @Test
        void insert_index_negative_end() {
            list.insert("Hi", -1);
            assertEquals(3, list.size());
            assertEquals("Hi", list.get(-1));
        }
    
        @Test
        void insert_index_is_size() {
            list.insert("Hi", 2);
            assertEquals(3, list.size());
            assertEquals("Hi", list.get(list.size()-1));
        }
    }

    @Nested
    class IndexOfTest {
        LinkedList<String> list;

        @BeforeEach
        void indexOf_initList() {
            list = LinkedList.of("Foo", "Bar", "Hi");
        }

        @Test
        void indexOf_item_at_start() {
            assertEquals(0, list.indexOf("Foo"));
        }

        @Test
        void indexOf_item_in_mid() {
            assertEquals(1, list.indexOf("Bar"));
        }
    
        @Test
        void indexOf_item_at_end() {
            assertEquals(list.size()-1, list.indexOf("Hi"), "Item should be at end");
        }
    
        @Test
        void indexOf_item_not_found() {
            assertEquals(-1, list.indexOf("Something"));
        }
    }

    @Nested
    class RemoveTest {
        LinkedList<String> list;

        @BeforeEach
        void initList() {
            list = LinkedList.of("Foo", "Bar", "Hi");
        }

        @Test
        void remove_index_invalid() {
            assertThrows(IndexOutOfBoundsException.class, () -> {
                list.remove(5);
            });
            assertThrows(IndexOutOfBoundsException.class, () -> {
                list.remove(-5);
            });
        }

        @Test
        void remove_index_positive_valid() {
            String removed = list.remove(1);
            assertEquals(2, list.size(), "Size should decrease");
            assertEquals(removed, "Bar", "Item should be returned");
            assertEquals("Hi", list.get(1), "Items should be shifted down");
        }

        @Test
        void remove_index_negative_valid() {
            String removed = list.remove(-2);
            assertEquals(2, list.size(), "Size should decrease");
            assertEquals(removed, "Bar", "Item should be returned");
            assertEquals("Hi", list.get(1), "Items should be shifted down");
        }

        @Test
        void remove_item_present() {
            String removed = list.remove("Bar");
            assertEquals(2, list.size(), "Size should decrease");
            assertEquals(removed, "Bar", "Item should be returned");
            assertEquals("Hi", list.get(1), "Items should be shifted down");
        }
    
        @Test
        void remove_item_absent() {
            String removed = list.remove("Something");
            assertNull(removed, "Returned item should be null");
            assertEquals(3, list.size(), "Size should be unchanged");
            assertEquals("Bar", list.get(1), "Items should be unchanged");
        }

        @Test
        void remove_item_last() {
            String value = "Hi";
            String removed = list.remove(value);
            assertEquals(value, removed);
            assertEquals(2, list.size());
            assertFalse(list.contains(value));
        }

        @Test
        void remove_item_single() {
            String value = "Foo";
            list = LinkedList.of(value);
            String removed = list.remove(value);
            assertEquals(value, removed);
            assertEquals(0, list.size());
            assertFalse(list.contains(value));
        }

        @Test
        void remove_no_items() {
            list = LinkedList.of();
            assertNull(list.remove("Foo"));
        }
    }
}