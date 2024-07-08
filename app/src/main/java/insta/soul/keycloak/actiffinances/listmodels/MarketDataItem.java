package insta.soul.keycloak.actiffinances.listmodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class MarketDataItem {
    private String bcName;
    private String bcSymbol;
    private String bcIconPath;
    private double bcPriceChange;
    private double bcPrice;


}
