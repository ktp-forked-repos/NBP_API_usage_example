import com.google.gson.Gson;
import com.sun.tools.corba.se.idl.constExpr.Positive;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.*;

public class Main {
    static final String FORMAT = "/?format=json";
    static String compareBidAsk;

    public static void main(String[] args) throws IOException {
        String urlTableA = "http://api.nbp.pl/api/exchangerates/rates/a/";
        String urlTableC = "http://api.nbp.pl/api/exchangerates/rates/c/";
        LocalDate lastMonth = LocalDate.now().minusMonths(1);

        List<CurrencyCode> currencyCodes = Arrays.asList(CurrencyCode.values());
        for (CurrencyCode currencyCode : currencyCodes) {
            String urlStringA = String.valueOf(new StringBuilder(urlTableA).append(currencyCode).append(FORMAT));
            String urlStringC = String.valueOf(new StringBuilder(urlTableC).append(currencyCode).append(FORMAT));
            String urlLastMonth = String.valueOf(new StringBuilder(urlTableC).append(currencyCode).append("/").append(lastMonth).append(FORMAT));

            URL urlA = new URL(urlStringA);
            URL urlC = new URL(urlStringC);
            URL urlClm = new URL(urlLastMonth);

            String jsonCurrencyRateA = getCurrencyFromUrl(urlA);
            String jsonCurrencyRateC = getCurrencyFromUrl(urlC);
            String jsonCurrencyRateClm = getCurrencyFromUrl(urlClm);

            Gson gson = new Gson();
            Currency currencyA = gson.fromJson(jsonCurrencyRateA, Currency.class);
            Currency currencyC = gson.fromJson(jsonCurrencyRateC, Currency.class);
            Currency currencyClm = gson.fromJson(jsonCurrencyRateClm, Currency.class);


            double midToday = currencyA.rates.get(0).mid;
            double bidToday = currencyC.rates.get(0).bid;
            double askLM = currencyClm.rates.get(0).ask;
            double calculate100PLN = (1/midToday)*100;
            double calculate100PLNbuyLastMonth = (1/askLM)*100;
            double calculate100PLNsellToday = (1/bidToday)*100;
            String currencyName = currencyA.getCurrency();
            String effectiveDate = currencyA.rates.get(0).effectiveDate;
            String code = currencyCode.toString().toUpperCase();

            double difference = calculate100PLNsellToday - calculate100PLNbuyLastMonth;
            if (difference >= 0 ) {
                compareBidAsk = "z zyskiem";
            } else {
                compareBidAsk = "ze stratą";
            }

            System.out.printf("Waluta: %-18s | %s | %.4f PLN | 100 PLN = %.2f %s\n", currencyName ,effectiveDate, midToday, calculate100PLN, code);
            System.out.printf("%-27s| %s za 100 PLN można było kupić %.2f %s , dzisiaj można je sprzedać %s %.2f PLN\n", "", lastMonth, calculate100PLNbuyLastMonth, code, compareBidAsk, difference);
            System.out.println();
        }
    }

    private static String getCurrencyFromUrl(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("user-agent", "Chrome");
        try(InputStream inputStream = connection.getInputStream()) {
            Scanner scanner = new Scanner(inputStream);
            return scanner.nextLine();
        }
    }

}

enum CurrencyCode {
    usd, eur, gbp, chf
}
