package palm.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import static palm.util.Dictionary.Entry;

class DictionaryTest {

    @Nested
    class FactoryTest {
        @Test
        void dict_of_nothing() {
            assertEquals(0, Dictionary.of().size());
        }
    
        @Test
        void dict_of_items() {
            assertEquals(2, Dictionary.of(
                Entry.of("Foo", "Bar"),
                Entry.of("Hello", "World")
            ).size());
        }
    }

    @Nested
    class GetSetTest {
        Dictionary<Integer, String> dict;

        @BeforeEach
        void initDict() {
            dict = Dictionary.of(
                Entry.of(4, "Foo"),
                Entry.of(13, "Hello")
            );
        }

        @Test
        void get_existing_key() {
            assertEquals("Hello", dict.get(13));
            assertEquals("Foo", dict.get(4));
        }

        @Test
        void get_non_existing_key() {
            assertNull(dict.get(100));
        }

        @Test
        void add_new_key() {
            dict.add(Entry.of(5, "Bar"));
            assertEquals(3, dict.size());
            assertEquals("Bar", dict.get(5));
        }

        @Test
        void add_same_key() {
            dict.add(Entry.of(4, "Bar"));
            assertEquals(2, dict.size());
            assertEquals("Bar", dict.get(4));
        }

        @Test
        void add_same_bucket() {
            dict.add(Entry.of(20, "Bar"));
            assertEquals(3, dict.size());
            assertEquals("Foo", dict.get(4));
            assertEquals("Bar", dict.get(20));
        }

        @Test
        void set_new_key() {
            dict.set(5, "Bar");
            assertEquals(3, dict.size());
            assertEquals("Bar", dict.get(5));
        }

        @Test
        void set_same_key() {
            dict.set(4, "Bar");
            assertEquals(2, dict.size());
            assertEquals("Bar", dict.get(4));
        }

        @Test
        void set_same_bucket() {
            dict.set(20, "Bar");
            assertEquals(3, dict.size());
            assertEquals("Foo", dict.get(4));
            assertEquals("Bar", dict.get(20));
        }
    }

    @Nested
    class ContainsTest {
        Dictionary<String, String> dict;

        @BeforeEach
        void initDict() {
            dict = Dictionary.of(
                Entry.of("Foo", "Bar"),
                Entry.of("Hello", "World")
            );
        }

        @Test
        void containsKey_non_existing() {
            assertFalse(dict.containsKey("Something"));
        }

        @Test
        void containsKey_existing() {
            assertTrue(dict.containsKey("Hello"));
        }

        @Test
        void containsKey_after_set() {
            dict.set("Foo", "Something");
            assertTrue(dict.containsKey("Foo"));
        }
    }

    @Nested
    class ClearTest {
        Dictionary<String, String> dict = Dictionary.of(
            Entry.of("Foo", "Bar"),
            Entry.of("Hello", "World")
        );

        @Test
        void clear_test() {
            dict.clear();
            assertEquals(0, dict.size());
            assertFalse(dict.containsKey("Hello"));
            assertNull(dict.get("Foo"));
        }
    }

    @Nested
    class RemoveTest {
        Dictionary<String, String> dict;

        @BeforeEach
        void initDict() {
            dict = Dictionary.of(
                Entry.of("Foo", "Bar"),
                Entry.of("Hello", "World")
            );
        }

        @Test
        void remove_non_existing_key() {
            assertNull(dict.remove("Something"));
            assertEquals(2, dict.size());
        }

        @Test
        void remove_existing_key() {
            String removed = dict.remove("Foo");
            assertEquals("Bar", removed);
            assertEquals(1, dict.size());
            assertNull(dict.get("Foo"));
        }
    }
}