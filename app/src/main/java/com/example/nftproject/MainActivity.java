package com.example.nftproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Abi abi;
    String v1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView linkTextView = findViewById(R.id.getCIDTextView);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
        linkTextView.setLinkTextColor(Color.BLUE);

        Web3j web3 = Web3j.build(new HttpService("https://goerli.infura.io/v3/1feae2c5863e407e8cf52cb9680344c2"));
        Credentials credentials = Credentials.create("f510442dfed47b4ea8a4b1b34d931ffa6d1ab4263e177ddf67f96123f664d6e2");
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        abi = Abi.load("0xb5F5395bBADcC75347a0D5AcC65339c65B117BDF", web3, credentials, contractGasProvider);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            try {
                abi.name().flowable().subscribeOn(Schedulers.io()).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i("vac", "NAME");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

