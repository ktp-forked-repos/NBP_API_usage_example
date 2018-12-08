import com.sun.xml.internal.xsom.impl.scd.Iterators;

import java.util.ArrayList;

public class Currency {
    public String getCurrency() {
        return currency;
    }

    public String currency;
    public String code;
    public ArrayList<CurrencyRates> rates;

}

class CurrencyRates {
    public String effectiveDate;
    public double mid;
    public double ask;
    public double bid;
}


//"table":"A",
//"currency":"frank szwajcarski",
//"code":"CHF",
//"rates":[{"no":"238/A/NBP/2018","effectiveDate":"2018-12-07","mid":3.7924}]}
