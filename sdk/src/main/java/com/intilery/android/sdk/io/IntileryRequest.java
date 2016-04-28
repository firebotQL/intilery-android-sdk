package com.intilery.android.sdk.io;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.intilery.android.sdk.Intilery;
import com.intilery.android.sdk.obj.RequestResult;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.ToString;

//When a request is added it is immediately tried to be sent, if that is not possible it is put on a queue and is then tried to be sent every 20 minutes.
// N.B it will be tried AFTER 20 minutes. Thread Pool will not wake android to perform tasks and we don't want it to.
@ToString
class IntileryRequest {

    private static final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(2, new ThreadFactory() {
        private AtomicInteger integer = new AtomicInteger(1);
        private ThreadGroup threadGroup = new ThreadGroup("IntileryIO");
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(threadGroup, r, "IntileryHTTP-"+integer.getAndIncrement());
        }
    });

    private static final ConcurrentLinkedQueue<IntileryRequest> pendingRequests = new ConcurrentLinkedQueue<>();
    private final IntileryIO.RequestResultReceiver receiver;
    private int attempt = 0;
    private int lastStatusCode = 0;
    private String internalResponse = "";
    private String serverResponses = "";
    private String lastSuccessfulServerResponse = "";
    private final String url;
    private final JSONObject object;
    private final String method;

    protected IntileryRequest(String url, JSONObject object, final IntileryIO.RequestResultReceiver receiver) {
        this("POST", url, object, receiver);
    }

    protected IntileryRequest(String method, String url, JSONObject object, final IntileryIO.RequestResultReceiver receiver) {
        final IntileryRequest me = this;
        this.url = url;
        this.object = object;
        this.receiver = receiver;
        this.method = method;
        scheduledExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                ConnectivityManager connMgr = (ConnectivityManager) Intilery.i().getConfig().getRootContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    if(me.send()) return;
                }
                pendingRequests.add(me);
            }
        });
    }

    private synchronized boolean send() {
        attempt++;
        if (attempt >= 3) {
            receiver.receive(RequestResult.builder().success(false).code(lastStatusCode).response(serverResponses).internalResponse("Three attempts passed, no success - giving up!").build());
        }

        try {


            HttpURLConnection con = (HttpURLConnection) (new URL(url)).openConnection();
            if(!method.equals("GET")) con.setDoOutput(true);
            con.setDoInput(true);
            if(!method.equals("GET")) con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic " + Intilery.i().getConfig().getIntileryToken());
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod(method);

            if(object != null) {
                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(object.toString());
                wr.flush();
            }
            //display what returns the POST request

            StringBuilder sb = new StringBuilder();
            lastStatusCode = con.getResponseCode();
            if (200 <= lastStatusCode && lastStatusCode <= 299) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                lastSuccessfulServerResponse = sb.toString();
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }

            }
            serverResponses += "Attempt "+attempt+"\n"+sb.toString();
            if (200 <= lastStatusCode && lastStatusCode <= 299 ) {
                receiver.receive(RequestResult.builder().success(true).internalResponse(internalResponse).lastSuccessfulResponse(lastSuccessfulServerResponse).response(serverResponses).code(lastStatusCode).build());
                return true;
            } if (400 <= lastStatusCode && lastStatusCode <= 499 ) {
                receiver.receive(RequestResult.builder().success(false).internalResponse(internalResponse).response(serverResponses).code(lastStatusCode).build());
                return true; //Retrying won't fix this - fail
            } else {
                return false;
            }

        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            internalResponse += "\nEncountered an exception whilst running HTTP!" + sw.toString(); // stack trace as a string
            e.printStackTrace();
            return false;
        }

    }

    static {
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                ConnectivityManager connMgr = (ConnectivityManager) Intilery.i().getConfig().getRootContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    Set<IntileryRequest> fails = new HashSet<>();
                    while(true) {
                        IntileryRequest request = pendingRequests.poll();
                        if(request == null) break;
                        if(!request.send()) fails.add(request);
                    }
                    pendingRequests.addAll(fails);
                }
            }
        }, 20*60, 20*60, TimeUnit.SECONDS);
    }
}
