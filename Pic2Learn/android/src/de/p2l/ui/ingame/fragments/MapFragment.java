package de.p2l.ui.ingame.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import de.p2l.R;

import static android.content.Context.MODE_PRIVATE;
import static de.p2l.ui.menu.mainmenu.MainActivity.SHARED_PREFS;

/*
MapFragment describes the left component of the swipe screen.
Depending on the current level and hero, the map imageview is loaded in.
 */

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int level;
    private int skin;
    SharedPreferences sharedPreferences;
    private Dialog myDialog1;
    private Dialog myDialog2;
    private Dialog myDialog3;

    int i = 0;

    private OnFragmentInteractionListener mListener;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map,container,false);
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        skin = sharedPreferences.getInt("currentBtn",0);
        //pic 0, viking 1, lea 2, goblin 3

        ImageView imageView = view.findViewById(R.id.imageViewMap);

        switch(level){
            case 11:
                switch(skin){
                    case 0: imageView.setImageResource(R.drawable.newtut1);
                        break;
                    case 1: imageView.setImageResource(R.drawable.newtut1olaf);
                        break;
                    case 2: imageView.setImageResource(R.drawable.newtut1lea);
                        break;
                    case 3: imageView.setImageResource(R.drawable.newtut1knolch);
                        break;
                    default: System.out.println("invalid skin");
                        break;
                }
                break;
            case 12:
                switch(skin){
                    case 0: imageView.setImageResource(R.drawable.newtut2);
                        break;
                    case 1: imageView.setImageResource(R.drawable.newtut2olaf);
                        break;
                    case 2: imageView.setImageResource(R.drawable.newtut2lea);
                        break;
                    case 3: imageView.setImageResource(R.drawable.newtut2knolch);
                        break;
                    default: System.out.println("invalid skin");
                        break;
                }
                break;
            case 13:
                switch(skin){
                    case 0: imageView.setImageResource(R.drawable.newtut3);
                        break;
                    case 1: imageView.setImageResource(R.drawable.newtut3olaf);
                        break;
                    case 2: imageView.setImageResource(R.drawable.newtut3lea);
                        break;
                    case 3: imageView.setImageResource(R.drawable.newtut3knolch);
                        break;
                    default: System.out.println("invalid skin");
                        break;
                }
                break;
            case 14:
                switch(skin){
                    case 0: imageView.setImageResource(R.drawable.newtutschleifen);
                        break;
                    case 1: imageView.setImageResource(R.drawable.newtutschleifenolaf);
                        break;
                    case 2: imageView.setImageResource(R.drawable.newtutschleifenlea);
                        break;
                    case 3: imageView.setImageResource(R.drawable.newtutschleifenknolch);
                        break;
                    default: System.out.println("invalid skin");
                        break;
                }
                break;
            case 15:
                switch(skin){
                    case 0: imageView.setImageResource(R.drawable.newtut4);
                        break;
                    case 1: imageView.setImageResource(R.drawable.newtut4olaf);
                        break;
                    case 2: imageView.setImageResource(R.drawable.newtut4lea);
                        break;
                    case 3: imageView.setImageResource(R.drawable.newtut4knolch);
                        break;
                    default: System.out.println("invalid skin");
                        break;
                }
                break;

            case 21:
                switch(skin){
                    case 0: imageView.setImageResource(R.drawable.easy1);
                        break;
                    case 1: imageView.setImageResource(R.drawable.easy1olaf);
                        break;
                    case 2: imageView.setImageResource(R.drawable.easy1lea);
                        break;
                    case 3: imageView.setImageResource(R.drawable.easy1knolch);
                        break;
                    default: System.out.println("invalid skin");
                        break;
                }
                break;
            case 22:
                switch(skin){
                    case 0: imageView.setImageResource(R.drawable.easykreis);
                        break;
                    case 1: imageView.setImageResource(R.drawable.easykreisolaf);
                        break;
                    case 2: imageView.setImageResource(R.drawable.easykreislea);
                        break;
                    case 3: imageView.setImageResource(R.drawable.easykreisknolch);
                        break;
                    default: System.out.println("invalid skin");
                        break;
                }
                break;
            case 23:
                switch(skin){
                    case 0: imageView.setImageResource(R.drawable.rechtslinks);
                        break;
                    case 1: imageView.setImageResource(R.drawable.rechtslinksolaf);
                        break;
                    case 2: imageView.setImageResource(R.drawable.rechtslinkslea);
                        break;
                    case 3: imageView.setImageResource(R.drawable.rechtslinksknolch);
                        break;
                    default: System.out.println("invalid skin");
                        break;
                }
                break;
            case 24:
                switch(skin){
                    case 0: imageView.setImageResource(R.drawable.umgkreis);
                        break;
                    case 1: imageView.setImageResource(R.drawable.umgkreisolaf);
                        break;
                    case 2: imageView.setImageResource(R.drawable.umgkreislea);
                        break;
                    case 3: imageView.setImageResource(R.drawable.umgkreisknolch);
                        break;
                    default: System.out.println("invalid skin");
                        break;
                }
                break;

            case 31:
                switch(skin){
                    case 0: imageView.setImageResource(R.drawable.backkreismid);
                        break;
                    case 1: imageView.setImageResource(R.drawable.backkreismidolaf);
                        break;
                    case 2: imageView.setImageResource(R.drawable.backkreismidlea);
                        break;
                    case 3: imageView.setImageResource(R.drawable.backkreismidknolch);
                        break;
                    default: System.out.println("invalid skin");
                        break;
                }
                break;
            case 32:
                switch(skin){
                    case 0: imageView.setImageResource(R.drawable.rechtschecker);
                        break;
                    case 1: imageView.setImageResource(R.drawable.rechtscheckerolaf);
                        break;
                    case 2: imageView.setImageResource(R.drawable.rechtscheckerlea);
                        break;
                    case 3: imageView.setImageResource(R.drawable.rechtscheckerknolch);
                        break;
                    default: System.out.println("invalid skin");
                        break;
                }
                break;
            case 33:
                switch(skin){
                    case 0: imageView.setImageResource(R.drawable.vszs);
                        break;
                    case 1: imageView.setImageResource(R.drawable.vszsolaf);
                        break;
                    case 2: imageView.setImageResource(R.drawable.vszslea);
                        break;
                    case 3: imageView.setImageResource(R.drawable.vszsknolch);
                        break;
                    default: System.out.println("invalid skin");
                        break;
                }
                break;
            case 34:
                switch(skin){
                    case 0: imageView.setImageResource(R.drawable.onlyoneway);
                        break;
                    case 1: imageView.setImageResource(R.drawable.onlyonewayolaf);
                        break;
                    case 2: imageView.setImageResource(R.drawable.onlyonewaylea);
                        break;
                    case 3: imageView.setImageResource(R.drawable.onlyonewayknolch);
                        break;
                    default: System.out.println("invalid skin");
                        break;
                }
                break;

            case 41:
                switch(skin){
                    case 0: imageView.setImageResource(R.drawable.diffk);
                        break;
                    case 1: imageView.setImageResource(R.drawable.diffkreisolaf);
                        break;
                    case 2: imageView.setImageResource(R.drawable.diffkreislea);
                        break;
                    case 3: imageView.setImageResource(R.drawable.diffkreisknolch);
                        break;
                    default: System.out.println("invalid skin");
                        break;
                }
                break;
            case 42:
                switch(skin){
                    case 0: imageView.setImageResource(R.drawable.backkreis);
                        break;
                    case 1: imageView.setImageResource(R.drawable.backkreisolaf);
                        break;
                    case 2: imageView.setImageResource(R.drawable.backkreislea);
                        break;
                    case 3: imageView.setImageResource(R.drawable.backkreisknolch);
                        break;
                    default: System.out.println("invalid skin");
                        break;
                }
                break;
            case 43:
                switch(skin){
                    case 0: imageView.setImageResource(R.drawable.fogoftest);
                        break;
                    case 1: imageView.setImageResource(R.drawable.fogoftestolaf);
                        break;
                    case 2: imageView.setImageResource(R.drawable.fogoftestlea);
                        break;
                    case 3: imageView.setImageResource(R.drawable.fogoftestknolch);
                        break;
                    default: System.out.println("invalid skin");
                        break;
                }
                break;
            case 44:
                switch(skin){
                    case 0: imageView.setImageResource(R.drawable.wanderer3);
                        break;
                    case 1: imageView.setImageResource(R.drawable.wanderer3olaf);
                        break;
                    case 2: imageView.setImageResource(R.drawable.wanderer3lea);
                        break;
                    case 3: imageView.setImageResource(R.drawable.wanderer3knolch);
                        break;
                    default: System.out.println("invalid skin");
                        break;
                }
                break;

            default: System.out.println("invalid level");
                break;
        }

        if (i==0&&level==11){
            i=1;
            myDialog1 = new Dialog(getActivity());
            myDialog1.setContentView(R.layout.introduction);
            myDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button buttonBack = (Button) myDialog1.findViewById(R.id.backBtn);
            buttonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog1.hide();
                }
            });

            myDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog1.show();
        }

        return view;
    }

    public void setLevel(int i){
        this.level = i;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
