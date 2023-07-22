package de.p2l.ui.ingame.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.p2l.R;

import static android.content.Context.MODE_PRIVATE;
import static de.p2l.ui.menu.mainmenu.MainActivity.SHARED_PREFS;

/*
AnnotationFragment describes the right component of the swipe screen.
Depending on the current level, the annotations are loaded in as well as performance stars are loaded in.
 */

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AnnotationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AnnotationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnnotationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int level;

    private SharedPreferences sharedPreferences;

    private OnFragmentInteractionListener mListener;

    public AnnotationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnnotationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnnotationsFragment newInstance(String param1, String param2) {
        AnnotationsFragment fragment = new AnnotationsFragment();
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

        View view = inflater.inflate(R.layout.fragment_annotations,container,false);
        TextView textView = view.findViewById(R.id.textViewAnnotations);
        int annotations;
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        int leistung = sharedPreferences.getInt(getLevelName(level),0);
        if(leistung>=1){
            ImageView star1 = (ImageView) view.findViewById(R.id.starImage1);
            star1.setImageResource(android.R.drawable.btn_star_big_on);
        }
        if(leistung>=2){
            ImageView star2 = (ImageView) view.findViewById(R.id.starImage2);
            star2.setImageResource(android.R.drawable.btn_star_big_on);
        }
        if(leistung==3){
            ImageView star3 = (ImageView) view.findViewById(R.id.starImage3);
            star3.setImageResource(android.R.drawable.btn_star_big_on);
        }
        switch(level){
            case 11:
                annotations = R.string.annt_level_11;
                break;
            case 12:
                annotations = R.string.annt_level_12;
                break;
            case 13:
                annotations = R.string.annt_level_13;
                break;
            case 14:
                annotations = R.string.annt_level_14;
                break;
            case 15:
                annotations = R.string.annt_level_15;
                break;

            case 21:
                annotations = R.string.annt_level_21;
                break;
            case 22:
                annotations = R.string.annt_level_22;
                break;
            case 23:
                annotations = R.string.annt_level_23;
                break;
            case 24:
                annotations = R.string.annt_level_24;
                break;

            case 31:
                annotations = R.string.annt_level_31;
                break;
            case 32:
                annotations = R.string.annt_level_32;
                break;
            case 33:
                annotations = R.string.annt_level_33;
                break;
            case 34:
                annotations = R.string.annt_level_34;
                break;

            case 41:
                annotations = R.string.annt_level_41;
                break;
            case 42:
                annotations = R.string.annt_level_42;
                break;
            case 43:
                annotations = R.string.annt_level_43;
                break;
            case 44:
                annotations = R.string.annt_level_44;
                break;

            default: annotations = R.string.annt_invalid_level;
                break;
        }
        textView.setText(annotations);
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

    public String getLevelName(int i ){
        String levelName = "";
            if (i == 11) levelName = "tut1";
            if (i == 12) levelName = "tut2";
            if (i == 13) levelName = "tut3";
            if (i == 14) levelName = "tut4";
            if (i == 15) levelName = "tut5";

            if (i == 21) levelName = "easy1";
            if (i == 22) levelName = "easy2";
            if (i == 23) levelName = "easy3";
            if (i == 24) levelName = "easy4";
            if (i == 25) levelName = "easy5";

            if (i == 31) levelName = "midd1";
            if (i == 32) levelName = "midd2";
            if (i == 33) levelName = "midd3";
            if (i == 34) levelName = "midd4";
            if (i == 35) levelName = "midd5";

            if (i == 41) levelName = "diff1";
            if (i == 42) levelName = "diff2";
            if (i == 43) levelName = "diff3";
            if (i == 44) levelName = "diff4";
            if (i == 45) levelName = "diff5";

            return levelName;
            }

}
