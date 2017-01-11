package cme.pointmila.com.election.Util;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cme.pointmila.com.election.models.DetailInfo;
import cme.pointmila.com.election.models.Election;
import cme.pointmila.com.election.models.ElectionCenterArrayModel;
import cme.pointmila.com.election.models.MMCList;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Path;


/**
 *
 */
public class RestClient {


    private static GitApiInterface gitApiInterface;

    public static String baseUrl = "http://103.242.119.63:8090/"; //"http://52.172.185.34/";//Local base link

    public static GitApiInterface getClient() {

        if (gitApiInterface == null) {

            OkHttpClient okClient = new OkHttpClient();
            okClient.setConnectTimeout(2, TimeUnit.MINUTES);
            okClient.setReadTimeout(2, TimeUnit.MINUTES);
            okClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {

                    return chain.proceed(chain.request());
                }
            });

            Retrofit client = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            gitApiInterface = client.create(GitApiInterface.class);
        }
        return gitApiInterface;
    }


    //Development API interface
    public interface GitApiInterface {
        @GET("/Election/GetCandidateList")
        Call<Election> callElection();

        @GET("/Election/GetCandidateInfo/{value}")
        Call<DetailInfo> callElectionDetail(@Path("value") String value);

        @GET("/Election/GetElectionCenter")
        Call<ElectionCenterArrayModel>getElectionCenter();

        @GET("/Election/GetMMCPanelList")
        Call<MMCList>getElectionMMCPannel();

//        @POST("/CommonAPI/ReqRes/CVR")
//        Call<CVR> CallCVR(@Body ReqCVR reqCVR);
//
//        @POST("/CommonAPI/Config/AckTags")
//        Call<ACKTAG> CallACK(@Body ACKTAG acktag);
//
//        @POST("/CommonAPI/Security/CHANGEPWD")
//        Call<ChangePassword> CallChangePassword(@Body ChangePassword changePassword);
//
//        @POST("/CommonAPI/Config/ReadScript")
//        Call<TablesConfig> CallTagDownload(@Body TAG tag);
//
//        @POST("/CommonAPI/Detailing/UDDET")
//        Call<List<OutputPOJO>> uploadContainerData(@Body List<ContainerPOJO> req);
//
//        @POST("/CommonAPI/ReqRes/IRCSF")
//        Call<List<IRCSFResponsePOJO>> downloadContentUrl(@Body IRCSFPOJO req);
//
//        @POST("/CommonAPI/Config/CONTENTACK")
//        Call<String> contentAcknowledge(@Body CNTACKPOJO req);
//
//        @POST("/CommonAPI/Security/FORGOTPWD")
//        Call<ChangePassword> CallForgotPassword(@Body ChangePassword changePassword);
//
//        @POST("/CommonAPI/Detailing/SYNCDETAILING")
//        Call<List<SyncDetailingAckPOJO>> CallSyncDetailingAcknowledge(@Body List<SyncDetailingAckPOJO> req);
    }


}
