
package io.cdap.plugin;

import com.facebook.ads.sdk.*;
import java.util.Collections;
import java.util.Random;

public class FacebookAds {
    public static void main(String[] args) throws APIException, InterruptedException {
        // Needs Page access token here with listed permissions.
        //pages_read_engagement, pages_manage_metadata, pages_read_user_content, pages_manage_ads, pages_show_list, pages_messaging
        //business_management.

        APIContext context = new APIContext("EAAESZCur0MDYBOyfEc1sMZBm2MMvi0V2UtcWvZBUd3oWaMZClwBC33miN6D1I8ZBMZAC710Lpww1flRN53eZBwUd9SIVDiub1ZCAQVGow822DxovqGEcXx5igbMi5JAQuCqOMV81zuOXzvkSYvjtA28QEaaiZBj3cjzIHzItPEmdWOYPMEyBZCsCYAZAiiFht554S7o",
                "7e7e5cff30c7bfd1dbaa713cf950123d");

        String accountId = "6895315967212184";
        String pageId = "329599513559420"; // 245142132021997
        String campaignId = "120208736715850110";
        //camp 1 - 120208736715850110
        // camp 2 - 120208736726850110

        while(true) {
            for (int i = 0; i < 299; i++) {
                createAdSet(accountId, context, campaignId, pageId);
                Thread.sleep(2000);
            }
            Thread.sleep(3600000);
        }
    }

    public static String createCampaign(String accountId, APIContext context) throws APIException {
        Campaign campaign = new AdAccount(accountId, context).createCampaign()
                .setObjective(Campaign.EnumObjective.VALUE_OUTCOME_TRAFFIC)
                .setStatus(Campaign.EnumStatus.VALUE_PAUSED)
                .setSpecialAdCategories("Housing")
                .setName("Campaign-2")
                .execute();


        return campaign.getId();
    }

    public static String createAdSet(String accountId, APIContext context, String campaignId, String pageId) throws APIException {
        Random random = new Random();
        AdSet adSet = new AdAccount(accountId, context).createAdSet()
                .setStatus(AdSet.EnumStatus.VALUE_PAUSED)
                .setTargeting(
                        new Targeting()
                                .setFieldGeoLocations(
                                        new TargetingGeoLocation()
                                                .setFieldCountries(Collections.singletonList("US"))
                                )
                )
                .setDailyBudget(10000L)
                .setBillingEvent(AdSet.EnumBillingEvent.VALUE_IMPRESSIONS)
                .setBidAmount(100L)
                .setCampaignId(campaignId)
                .setOptimizationGoal(AdSet.EnumOptimizationGoal.VALUE_REACH)
                .setPromotedObject("{\"page_id\": \"" + pageId + "\"}")
                .setName("AdSet-number-" + random.nextInt(100000))
                .execute();

        return adSet.getId();
    }

    public static String createAdCreative(String accountId, APIContext context, String pageId) throws APIException {
        AdCreative creative = new AdAccount(accountId, context).createAdCreative()
                .setBody("Like My Page")
                .setImageUrl("https://www.internetmatters.org/wp-content/uploads/2021/03/facebook-logo-new.png")
                .setName("Test Creative")
                .setTitle("My Page Like Ad")
                .setObjectId(pageId)
                .execute();

        return creative.getId();
    }
    public static String createAd(String accountId, APIContext context, String adSetId, String creativeId) throws APIException {
        Ad ad = new AdAccount(accountId, context).createAd()
                .setStatus(Ad.EnumStatus.VALUE_PAUSED)
                .setAdsetId(adSetId)
                .setName("Test Ad")
                .setCreative(
                        new AdCreative()
                                .setFieldId(creativeId)
                )
                .setParam("ad_format", "DESKTOP_FEED_STANDARD")
                .execute();

        return ad.getId();
    }
    public static void fetchInsights() throws APIException {
        String access_token = "EAAKGoUlLALwBOyAzZBfSOwq8NSLzwpKPZA1rpiJqnRFJlubHpEwYpLBPURHaavKA0SxkyVFvIEckku6Rs1PurcNOu2xUQ5IPYZBNpO1rR0Yxh3GZAb5UB31t6Bm1zmBACaIgZAkQSgmgZC9IHZBxrqFrZB8gEctx2SHcg1ZB56MdIQeX6GLbFFkraPiiTm2LkfgSNPD2gRPO8xuvTaSqWByHRFiJ1sO1SwDlSpwA4";

//        String app_secret = "c35498962aa30b1fef88a9acb29331c9";
//        String app_id = "710977230930108";

        String id = "120207020343010396";

        APIContext context = new APIContext(access_token).enableDebug(true);

        AdsInsights  adsInsights = new Ad(id, context).getInsights()
                .setParam("breakdown", "publisher_platform")
                .requestField("impressions")
                .execute().head();

        System.out.println(adsInsights);

    }

}

