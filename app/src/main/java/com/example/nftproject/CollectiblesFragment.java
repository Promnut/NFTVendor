package com.example.nftproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import org.bouncycastle.math.raw.Mod;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollectiblesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectiblesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CollectiblesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CollectiblesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CollectiblesFragment newInstance(String param1, String param2) {
        CollectiblesFragment fragment = new CollectiblesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    Abi abi;
    RecyclerView recyclerView ;
    LinearLayoutManager layoutManager ;
    List<ModelClass> userModelClass ;
    Adapter adapter ;
    View view ;
    Integer id;
    String uri, owner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_collectibles, container, false);

        // Inflate the layout for this fragment
        initData();

        return view;
    }

    private void initData() {
        Web3j web3 = Web3j.build(new HttpService("https://goerli.infura.io/v3/1feae2c5863e407e8cf52cb9680344c2"));
        Credentials credentials = Credentials.create("f510442dfed47b4ea8a4b1b34d931ffa6d1ab4263e177ddf67f96123f664d6e2");
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        abi = Abi.load("0x25761218c0153f060eC2959167C0029E611a73Fd", web3, credentials, contractGasProvider);
        userModelClass = new ArrayList<>();

        abi.count().flowable().subscribeOn(Schedulers.io()).subscribe(new Consumer<BigInteger>() {
            @Override
            public void accept(BigInteger bigInteger) throws Exception {
                id = bigInteger.intValue() ;
            }
        }, error -> {
            Log.i("vac", "Error");
        });

        try {
            TimeUnit.MILLISECONDS.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < id ; i++ ) {
            try {
                abi.tokenURI(BigInteger.valueOf(i)).flowable().subscribeOn(Schedulers.io()).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        uri = s;
                    }}, error -> {
                    Log.i("vac", "Error");
                });

                abi.ownerOf(BigInteger.valueOf(i)).flowable().subscribeOn(Schedulers.io()).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        owner = s;
                    }}, error -> {
                    Log.i("vac", "Error");
                });
            } catch (Exception e) {};

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            userModelClass.add(new ModelClass("TokenID: " + i, "TokenURI: " + uri , "Owner: " + owner));
        }

        initRecycleView(userModelClass);
    }

    private void initRecycleView(List<ModelClass> userModelClass) {
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(userModelClass) ;
        recyclerView.setAdapter(adapter);
    }
}