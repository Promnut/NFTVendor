package com.example.nftproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MintFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MintFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MintFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MintFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MintFragment newInstance(String param1, String param2) {
        MintFragment fragment = new MintFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    View view;

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
        view = inflater.inflate(R.layout.fragment_mint, container, false);
        // Inflate the layout for this fragment

        Abi abi;
        EditText address = view.findViewById(R.id.addressEditText);
        EditText CID = view.findViewById(R.id.CIDEditText);
        TextView linkTextView = view.findViewById(R.id.getCIDTextView);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
        linkTextView.setLinkTextColor(Color.BLUE);

        final LoadingBar loadingBar = new LoadingBar(getActivity());


        Web3j web3 = Web3j.build(new HttpService("https://goerli.infura.io/v3/1feae2c5863e407e8cf52cb9680344c2"));
        Credentials credentials = Credentials.create("f510442dfed47b4ea8a4b1b34d931ffa6d1ab4263e177ddf67f96123f664d6e2");
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        abi = Abi.load("0x25761218c0153f060eC2959167C0029E611a73Fd", web3, credentials, contractGasProvider);

        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(v -> {
            if (!address.getText().toString().equals("") && !CID.getText().toString().equals("")
                    && address.getText().toString().substring(0,2).equals("0x") ) {
                abi.safeMint(address.getText().toString(), CID.getText().toString()).flowable().subscribeOn(Schedulers.io()).subscribe(new Consumer<TransactionReceipt>() {
                    @Override
                    public void accept(TransactionReceipt transactionReceipt) throws Exception {
                        Log.i("xxx", "Accept");
                    }
                }, error -> {
                    Log.i("vac", "Error");
                });

                loadingBar.showDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingBar.dismissBar();
                    }
                }, 18000);

            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("                  Invalid input");
                alertDialog.setMessage("                      Please try agent");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}