package insta.soul.keycloak.actiffinances.coingecko.beans;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@Setter
public class Feed {
    @SerializedName("title")
    private  String title;
    @SerializedName("description")
    private  String description;
    @SerializedName("author")
    private  String author;
    @SerializedName("url")
    private  String url;
    @SerializedName( "updated_at")
    private  long updatedAt;
    @SerializedName(  "news_site")
    private  String newsSite;
    @SerializedName("thumb_2x")
    private  String thumbX;

}
