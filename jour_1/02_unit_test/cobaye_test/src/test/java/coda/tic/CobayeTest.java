package coda.tic;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

class CobayeTest {

    @Test
    @DisplayName("Add two numbers")
    void testAdd() {
        assertEquals(4, Cobaye.add(2, 2));
        assertEquals(0, Cobaye.add(-1, 1));
        assertEquals(-4, Cobaye.add(-2, -2));
    }

    @Test
    @DisplayName("Multiply two numbers")
    void testMultiply() {
        assertEquals(4, Cobaye.multiply(2, 2));
        assertEquals(-4, Cobaye.multiply(2, -2));
        assertEquals(4, Cobaye.multiply(-2, -2));
        assertEquals(0, Cobaye.multiply(1, 0));
    }

    @Test
    @DisplayName("Divide two numbers")
    void testDivide() {
        // your implementation here
    }

    @Test
    @DisplayName("Divide by zero throws exception")
    void testDivideByZero() {
        // your implementation here
    }

    @Test
    @DisplayName("Is weekend returns true for Saturday or Sunday")
    void testIsWeekendWeekend()  {
        // your implementation here
        // use a mock to simulate the date
    }

    @Test
    @DisplayName("Is weekend returns false for weekday")
    void testIsWeekendWeekday()   {
        // your implementation here
        // use a mock to simulate the date
    }

    @Test
    @DisplayName("GetJoke format the joke with newline")
    void testGetJoke() throws Exception {
        String setup = "Pete et Repete sont sur un bateau, Pete tombe a l'eau qui reste-il ?";
        String punchline = "Repete";
        String mockJson = String.format("{\"type\":\"general\",\"setup\": \"%s\", \"punchline\":\"%s\",\"id\":1}", setup, punchline);

        // use a mock to avoid calling the external api in the tests

        Cobaye cobaye = new Cobaye();

        String result = cobaye.getJoke();

        assertEquals(result, setup + "\n" + punchline);
    }

    // Add you own test scenarios for the insertion sort function tests

    // Add your own test scenarios for the bubble sort function tests
}
