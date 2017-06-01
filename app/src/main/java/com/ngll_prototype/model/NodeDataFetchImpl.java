package com.ngll_prototype.model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ngll_prototype.R;
import com.ngll_prototype.object.SourceClass;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

public class NodeDataFetchImpl implements NodeDataFetch{
    private static final String TAG = "NodeDataFetchImpl";
    private String query = "{\n" +
            "    \"size\": 10,\n" +
            "    \"sort\": {\"@timestamp\":\"desc\"},\n" +
            "    \"query\": {\n" +
            "        \"bool\": {\n" +
            "            \"must\": {\n" +
            "                \"match\": {\n" +
            "                    \"data.macAddr\": {\n" +
            "                        \"query\": \"101a0a000025\",\n" +
            "                        \"type\": \"boolean\"\n" +
            "                    }\n" +
            "                }\n" +
            "            },\n" +
            "            \"must_not\": {\n" +
            "                \"term\": {\n" +
            "                    \"data.GPS_N\": \"0\"\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}";

    String dummy = "[{\"_index\":\"client_report\",\"_type\":\"asset_tracker\",\"_id\":\"AVj85TUTJozORed8M6tp\",\"_score\":null,\"_source\":{\"data\":{\"SIGS\":\"-128.2\",\"AP_TS\":\"1481711627000\",\"macAddr\":\"101a0a000025\",\"frameCnt\":\"391\",\"snr\":\"-8.2\",\"GWID\":\"00001c497b432116\",\"TEMP\":\"30.5\",\"BATL\":\"255\",\"GPS_N\":\"2499.684005\",\"GPS_E\":\"1214.881005\",\"GPS_STA\":\"1\",\"MOTION\":\"0\"},\"api_key\":\"TRACKER-gxcJqqvNOD\",\"Gateway\":[{\"rssi\":-128.2,\"snr\":-8.2,\"gatewayID\":\"00001c497b432116\"},{\"rssi\":-71,\"snr\":22.5,\"gatewayID\":\"00000049060f630a\"},{\"rssi\":-57,\"snr\":24.5,\"gatewayID\":\"00000049060f610a\"},{\"rssi\":-71,\"snr\":22,\"gatewayID\":\"00000049060f640a\"},{\"rssi\":-81,\"snr\":23.8,\"gatewayID\":\"00000049060f620a\"}],\"@timestamp\":\"2016-12-14T10:33:49.566Z\",\"raw\":\"415eff94fe26a548699ceb\",\"account\":{\"gid\":\"600020270\",\"mac\":\"101a0a000025\",\"pin\":\"07941989\"},\"ALRT\":\"0000000000000010\"},\"sort\":[\"1481711627000\"]},{\"_index\":\"client_report\",\"_type\":\"asset_tracker\",\"_id\":\"AVj86SzXJozORed8M6t2\",\"_score\":null,\"_source\":{\"data\":{\"SIGS\":\"-112.5\",\"AP_TS\":\"1481711627000\",\"macAddr\":\"101a0a000025\",\"frameCnt\":\"391\",\"snr\":\"-0.5\",\"GWID\":\"00001c497b3b80f0\",\"TEMP\":\"30.5\",\"BATL\":\"255\",\"GPS_N\":\"2499.684005\",\"GPS_E\":\"1214.881005\",\"GPS_STA\":\"1\",\"MOTION\":\"0\"},\"api_key\":\"TRACKER-gxcJqqvNOD\",\"Gateway\":[{\"rssi\":-112.5,\"snr\":-0.5,\"gatewayID\":\"00001c497b3b80f0\"}],\"@timestamp\":\"2016-12-14T10:38:09.601Z\",\"raw\":\"415eff94fe26a548699ceb\",\"account\":{\"gid\":\"600020270\",\"mac\":\"101a0a000025\",\"pin\":\"07941989\"},\"ALRT\":\"0000000000000010\"},\"sort\":[\"1481711627000\"]},{\"_index\":\"client_report\",\"_type\":\"asset_tracker\",\"_id\":\"AVj86SzXJozORed8M6t3\",\"_score\":null,\"_source\":{\"data\":{\"SIGS\":\"-106\",\"AP_TS\":\"1481711623000\",\"macAddr\":\"101a0a000025\",\"frameCnt\":\"390\",\"snr\":\"10.5\",\"GWID\":\"00001c497b3b80f0\",\"TEMP\":\"30.5\",\"BATL\":\"255\",\"GPS_N\":\"2499.684005\",\"GPS_E\":\"1214.881005\",\"GPS_STA\":\"1\",\"MOTION\":\"0\"},\"api_key\":\"TRACKER-gxcJqqvNOD\",\"Gateway\":[{\"rssi\":-106,\"snr\":10.5,\"gatewayID\":\"00001c497b3b80f0\"}],\"@timestamp\":\"2016-12-14T10:38:09.602Z\",\"raw\":\"415eff94fe26a548699ceb\",\"account\":{\"gid\":\"600020270\",\"mac\":\"101a0a000025\",\"pin\":\"07941989\"},\"ALRT\":\"0000000000000010\"},\"sort\":[\"1481711623000\"]},{\"_index\":\"client_report\",\"_type\":\"asset_tracker\",\"_id\":\"AVj85TUTJozORed8M6to\",\"_score\":null,\"_source\":{\"data\":{\"SIGS\":\"-123.8\",\"AP_TS\":\"1481711623000\",\"macAddr\":\"101a0a000025\",\"frameCnt\":\"390\",\"snr\":\"-3.8\",\"GWID\":\"00001c497b432116\",\"TEMP\":\"30.5\",\"BATL\":\"255\",\"GPS_N\":\"2499.684005\",\"GPS_E\":\"1214.881005\",\"GPS_STA\":\"1\",\"MOTION\":\"0\"},\"api_key\":\"TRACKER-gxcJqqvNOD\",\"Gateway\":[{\"rssi\":-123.8,\"snr\":-3.8,\"gatewayID\":\"00001c497b432116\"},{\"rssi\":-51,\"snr\":25,\"gatewayID\":\"00000049060f610a\"},{\"rssi\":-69,\"snr\":26.3,\"gatewayID\":\"00000049060f630a\"},{\"rssi\":-81,\"snr\":24.5,\"gatewayID\":\"00000049060f640a\"},{\"rssi\":-77,\"snr\":25.8,\"gatewayID\":\"00000049060f620a\"}],\"@timestamp\":\"2016-12-14T10:33:49.566Z\",\"raw\":\"415eff94fe26a548699ceb\",\"account\":{\"gid\":\"600020270\",\"mac\":\"101a0a000025\",\"pin\":\"07941989\"},\"ALRT\":\"0000000000000010\"},\"sort\":[\"1481711623000\"]},{\"_index\":\"client_report\",\"_type\":\"asset_tracker\",\"_id\":\"AVj85TUTJozORed8M6tr\",\"_score\":null,\"_source\":{\"data\":{\"SIGS\":\"-124.8\",\"AP_TS\":\"1481711619000\",\"macAddr\":\"101a0a000025\",\"frameCnt\":\"389\",\"snr\":\"-3.8\",\"GWID\":\"00001c497b432116\",\"TEMP\":\"30.5\",\"BATL\":\"255\",\"GPS_N\":\"2499.684005\",\"GPS_E\":\"1214.881003\",\"GPS_STA\":\"1\",\"MOTION\":\"0\"},\"api_key\":\"TRACKER-gxcJqqvNOD\",\"Gateway\":[{\"rssi\":-124.8,\"snr\":-3.8,\"gatewayID\":\"00001c497b432116\"},{\"rssi\":-52,\"snr\":22.5,\"gatewayID\":\"00000049060f610a\"},{\"rssi\":-73,\"snr\":24.5,\"gatewayID\":\"00000049060f640a\"},{\"rssi\":-73,\"snr\":23.8,\"gatewayID\":\"00000049060f620a\"},{\"rssi\":-69,\"snr\":23.8,\"gatewayID\":\"00000049060f630a\"}],\"@timestamp\":\"2016-12-14T10:33:49.566Z\",\"raw\":\"415eff94fe26a548699ceb\",\"account\":{\"gid\":\"600020270\",\"mac\":\"101a0a000025\",\"pin\":\"07941989\"},\"ALRT\":\"0000000000000010\"},\"sort\":[\"1481711619000\"]},{\"_index\":\"client_report\",\"_type\":\"asset_tracker\",\"_id\":\"AVj86SzbJozORed8M6t-\",\"_score\":null,\"_source\":{\"data\":{\"SIGS\":\"-108\",\"AP_TS\":\"1481711619000\",\"macAddr\":\"101a0a000025\",\"frameCnt\":\"389\",\"snr\":\"2\",\"GWID\":\"00001c497b3b80f0\",\"TEMP\":\"30.5\",\"BATL\":\"255\",\"GPS_N\":\"2499.684005\",\"GPS_E\":\"1214.881003\",\"GPS_STA\":\"1\",\"MOTION\":\"0\"},\"api_key\":\"TRACKER-gxcJqqvNOD\",\"Gateway\":[{\"rssi\":-108,\"snr\":2,\"gatewayID\":\"00001c497b3b80f0\"}],\"@timestamp\":\"2016-12-14T10:38:09.602Z\",\"raw\":\"415eff94fe26a548699ceb\",\"account\":{\"gid\":\"600020270\",\"mac\":\"101a0a000025\",\"pin\":\"07941989\"},\"ALRT\":\"0000000000000010\"},\"sort\":[\"1481711619000\"]},{\"_index\":\"client_report\",\"_type\":\"asset_tracker\",\"_id\":\"AVj85TUTJozORed8M6tq\",\"_score\":null,\"_source\":{\"data\":{\"SIGS\":\"-127.2\",\"AP_TS\":\"1481711615000\",\"macAddr\":\"101a0a000025\",\"frameCnt\":\"388\",\"snr\":\"-7.2\",\"GWID\":\"00001c497b432116\",\"TEMP\":\"30.5\",\"BATL\":\"255\",\"GPS_N\":\"2499.684005\",\"GPS_E\":\"1214.881011\",\"GPS_STA\":\"1\",\"MOTION\":\"0\"},\"api_key\":\"TRACKER-gxcJqqvNOD\",\"Gateway\":[{\"rssi\":-127.2,\"snr\":-7.2,\"gatewayID\":\"00001c497b432116\"},{\"rssi\":-49,\"snr\":25,\"gatewayID\":\"00000049060f610a\"},{\"rssi\":-67,\"snr\":26.3,\"gatewayID\":\"00000049060f630a\"},{\"rssi\":-70,\"snr\":23.8,\"gatewayID\":\"00000049060f640a\"},{\"rssi\":-70,\"snr\":22.5,\"gatewayID\":\"00000049060f620a\"}],\"@timestamp\":\"2016-12-14T10:33:49.566Z\",\"raw\":\"415eff94fe26a548699ceb\",\"account\":{\"gid\":\"600020270\",\"mac\":\"101a0a000025\",\"pin\":\"07941989\"},\"ALRT\":\"0000000000000010\"},\"sort\":[\"1481711615000\"]},{\"_index\":\"client_report\",\"_type\":\"asset_tracker\",\"_id\":\"AVj86SzXJozORed8M6t4\",\"_score\":null,\"_source\":{\"data\":{\"SIGS\":\"-108\",\"AP_TS\":\"1481711615000\",\"macAddr\":\"101a0a000025\",\"frameCnt\":\"388\",\"snr\":\"5\",\"GWID\":\"00001c497b3b80f0\",\"TEMP\":\"30.5\",\"BATL\":\"255\",\"GPS_N\":\"2499.684005\",\"GPS_E\":\"1214.881011\",\"GPS_STA\":\"1\",\"MOTION\":\"0\"},\"api_key\":\"TRACKER-gxcJqqvNOD\",\"Gateway\":[{\"rssi\":-108,\"snr\":5,\"gatewayID\":\"00001c497b3b80f0\"}],\"@timestamp\":\"2016-12-14T10:38:09.601Z\",\"raw\":\"415eff94fe26a548699ceb\",\"account\":{\"gid\":\"600020270\",\"mac\":\"101a0a000025\",\"pin\":\"07941989\"},\"ALRT\":\"0000000000000010\"},\"sort\":[\"1481711615000\"]},{\"_index\":\"client_report\",\"_type\":\"asset_tracker\",\"_id\":\"AVj85TUSJozORed8M6tn\",\"_score\":null,\"_source\":{\"data\":{\"SIGS\":\"-129.2\",\"AP_TS\":\"1481711611000\",\"macAddr\":\"101a0a000025\",\"frameCnt\":\"387\",\"snr\":\"-6.2\",\"GWID\":\"00001c497b432116\",\"TEMP\":\"30.5\",\"BATL\":\"255\",\"GPS_N\":\"2499.684005\",\"GPS_E\":\"1214.881011\",\"GPS_STA\":\"1\",\"MOTION\":\"0\"},\"api_key\":\"TRACKER-gxcJqqvNOD\",\"Gateway\":[{\"rssi\":-129.2,\"snr\":-6.2,\"gatewayID\":\"00001c497b432116\"},{\"rssi\":-75,\"snr\":21.3,\"gatewayID\":\"00000049060f620a\"},{\"rssi\":-52,\"snr\":22,\"gatewayID\":\"00000049060f610a\"},{\"rssi\":-71,\"snr\":23.3,\"gatewayID\":\"00000049060f630a\"},{\"rssi\":-73,\"snr\":21.3,\"gatewayID\":\"00000049060f640a\"}],\"@timestamp\":\"2016-12-14T10:33:49.566Z\",\"raw\":\"415eff94fe26a548699ceb\",\"account\":{\"gid\":\"600020270\",\"mac\":\"101a0a000025\",\"pin\":\"07941989\"},\"ALRT\":\"0000000000000010\"},\"sort\":[\"1481711611000\"]},{\"_index\":\"client_report\",\"_type\":\"asset_tracker\",\"_id\":\"AVj86SzXJozORed8M6t6\",\"_score\":null,\"_source\":{\"data\":{\"SIGS\":\"-112\",\"AP_TS\":\"1481711611000\",\"macAddr\":\"101a0a000025\",\"frameCnt\":\"387\",\"snr\":\"-1\",\"GWID\":\"00001c497b3b80f0\",\"TEMP\":\"30.5\",\"BATL\":\"255\",\"GPS_N\":\"2499.684005\",\"GPS_E\":\"1214.881009\",\"GPS_STA\":\"1\",\"MOTION\":\"0\"},\"api_key\":\"TRACKER-gxcJqqvNOD\",\"Gateway\":[{\"rssi\":-112,\"snr\":-1,\"gatewayID\":\"00001c497b3b80f0\"}],\"@timestamp\":\"2016-12-14T10:38:09.602Z\",\"raw\":\"415eff94fe26a548699ceb\",\"account\":{\"gid\":\"600020270\",\"mac\":\"101a0a000025\",\"pin\":\"07941989\"},\"ALRT\":\"0000000000000010\"},\"sort\":[\"1481711611000\"]}]";
    @Override
    public void getNodeData(final Context context, final String mac, final OnNodeDataFetchListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                try {
                    JestClientFactory factory = new JestClientFactory();
                    factory.setDroidClientConfig(new DroidClientConfig.Builder(context.getString(R.string.elasticsvr))
                            .multiThreaded(true)
                            .build());
                    JestClient client = factory.getObject();

                    // newer search rule, desc find latest data & GPS_N not 0
                    SearchSourceBuilder searchSourceBuilderTry = new SearchSourceBuilder();
                    searchSourceBuilderTry.sort("@timestamp", SortOrder.DESC);
//                    searchSourceBuilderTry.sort("data.AP_TS", SortOrder.DESC);
//                    searchSourceBuilderTry.size(10);
                    searchSourceBuilderTry.size(5);
                    searchSourceBuilderTry.query(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("data.macAddr", mac)).mustNot(QueryBuilders.termQuery("data.GPS_N", "0")));
//                    searchSourceBuilderTry.query(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("data.macAddr", mac)));

//                    Log.d(TAG, "searchSourceBuilder:\t" + searchSourceBuilderTry.toString()); // print query cmd
//                    Search search = new Search.Builder(searchSourceBuilderTry.toString()).addIndex("client_report").addType("asset_tracker").build();

                    //work around change to use JSON query body.
                    Search search = new Search.Builder(query).addIndex("client_report").addType("asset_tracker").build();
                    SearchResult searchResult = client.execute(search);
                    Log.d(TAG, "searchResult: \n" + searchResult.getJsonString());

                    //get single tracker data
//                    TrackerInfoClass result = gson.fromJson(
//                            searchResult.getJsonObject()
//                                    .get("hits").getAsJsonObject()
//                                    .get("hits").getAsJsonArray()
//                                    .get(0).getAsJsonObject()
//                                    .get("_source").toString(), TrackerInfoClass.class);

                    //trytry handle 10-piece of Tracker data
                    Type listType = new TypeToken<ArrayList<SourceClass>>() {}.getType();
                    ArrayList<SourceClass> jsonArr = gson.fromJson(searchResult.getJsonObject()
                            .get("hits").getAsJsonObject()
                            .get("hits").getAsJsonArray()
                            .toString(), listType);
//                    Log.d(TAG, "jsonArr.size"+ jsonArr.size());

                    //dummy data
//                    ArrayList<SourceClass> jsonArrDummy = gson.fromJson(dummy, listType);
//                    Log.d(TAG, "jsonArr.size"+ jsonArrDummy.size());

                    if(jsonArr.size() == 0){
                        listener.onError();
                    }
                    listener.onSucess(jsonArr);

//                    Log.d(TAG, "result.getGatewayList.size:\t" + result.getGatewayList().size());
//                    for (TrackerInfoClass.gatewatItem item: result.getGatewayList()) {
//                        Log.d(TAG, "" + item.id + "\trssi\t" + item.rssi + "\tsnr\t"+ item.snr);
//
//                    }
//                    Log.d(TAG, "Total:\t" + searchResult.getTotal());
//                    Log.d(TAG, "searchResult:\t" + searchResult.getJsonString());

                    //get specific document
//                    Get get = new Get.Builder("client_report", "AVe2_o5uJozORed8MBm_").type("asset_tracker").build();
//                    JestResult result = client.execute(get);
//                    Log.d(TAG, "Getting Document:\t" + result.getJsonString());
//                    gson.fromJson(br, new TypeToken<List<JsonLog>>(){}.getType());

                    client.shutdownClient();

                } catch (UnknownError e) {
                    e.printStackTrace();
                    listener.onError();
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError();
                }
            }
        }).start();
    }


}
