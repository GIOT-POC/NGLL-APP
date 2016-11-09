package com.ngll_prototype.model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.ngll_prototype.R;
import com.ngll_prototype.object.TrackerInfoClass;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

public class NodeDataFetchImpl implements NodeDataFetch{
    private static final String TAG = "NodeDataFetchImpl";

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
                    searchSourceBuilderTry.size(1);
                    searchSourceBuilderTry.query(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("data.macAddr", mac)).mustNot(QueryBuilders.termQuery("data.GPS_N", "0")));

                    Search search = new Search.Builder(searchSourceBuilderTry.toString()).addIndex("client_report").addType("asset_tracker").build();
                    SearchResult searchResult = client.execute(search);
                    Log.d(TAG, "searchResult: \n" + searchResult.getJsonString());
                    TrackerInfoClass result = gson.fromJson(
                            searchResult.getJsonObject()
                                    .get("hits").getAsJsonObject()
                                    .get("hits").getAsJsonArray()
                                    .get(0).getAsJsonObject()
                                    .get("_source").toString(), TrackerInfoClass.class);

                    if(result == null){
                        listener.onError();
                    }
                    listener.onSucess(result);

//                    Log.d(TAG, "searchSourceBuilder:\t" + searchSourceBuilder.toString());
//                    Log.d(TAG, "Total:\t" + searchResult.getTotal());
//                    Log.d(TAG, "searchResult:\t" + searchResult.getJsonString());

                    //get specific document
//                    Get get = new Get.Builder("client_report", "AVe2_o5uJozORed8MBm_").type("asset_tracker").build();
//                    JestResult result = client.execute(get);
//                    Log.d(TAG, "Getting Document:\t" + result.getJsonString());


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
