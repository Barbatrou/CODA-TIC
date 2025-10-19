package coda.tic;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.DayOfWeek;
import java.time.LocalDate;
import org.json.JSONObject;

public class Cobaye {

    private final HttpClient httpClient;

    // Production constructor
    public Cobaye() {
        this.httpClient = HttpClient.newHttpClient();
    }

    // Package-private constructor for testing (allows injection of mock client)
    Cobaye(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    static int add(int a, int b) {
        return a + b;
    }

    static int multiply(int a, int b) {
        return a * b;
    }

    static int divide(int a, int b) {
        return a / b;
    }

    static int[] insertionSort(int[] arr) {
        int[] result = arr.clone();
        int n = result.length;

        for (int i = 1; i < n; i++) {
            int key = result[i];
            int j = i - 1;

            while (j >= 0 && result[j] > key) {
                result[j + 1] = result[j];
                j--;
            }
            result[j + 1] = key;
        }

        return result;
    }

    static int[] bubbleSort(int[] arr) {
        if (alreadySorted(arr)) {
            return arr;
        }
        int[] result = arr.clone();
        int n = result.length;
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (result[j] > result[j + 1]) {
                    int temp = result[j];
                    result[j] = result[j + 1];
                    result[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }

        return result;
    }

    static boolean alreadySorted(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return true;
        }
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] < arr[i + 1]) {
                return false;
            }
        }
        return true;
    }

    static boolean isWeekend() {
        DayOfWeek day = LocalDate.now().getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    String getJoke() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://official-joke-api.appspot.com/random_joke")) // return a random joke each call
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());
        String setup = json.getString("setup");
        String punchline = json.getString("punchline");

        return setup + "\n" + punchline;
    }

}
