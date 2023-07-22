package de.p2l.ui.ingame.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.p2l.R;
import de.p2l.service.sharedDataManager.SharedPrefManager;
import de.p2l.service.parser.commands.Command;
import de.p2l.service.parser.parser.Input;
import de.p2l.service.parser.parser.InputToImg;
import de.p2l.service.parser.parser.Parser;
import de.p2l.ui.ingame.camera.CameraActivity;

/*
CommandFragment describes the middle component of the swipe screen.
Depending on the pictures the user takes, different images are loaded in.
The class ColorCoder takes care of control structures being dyed correctly.
Clicking the Start button here starts the AndroidLauncher class and subsequently the LibGDX action
 */

public class CommandsFragment extends Fragment {

    private GridView gridView;
    private ImageAdapter adapter;

    private Parser parser;

    private static final String TAG = "CommandsFragment";

    private ArrayList<Input> inputList;
    private ArrayList<Integer> imageList;
    private ColorCoder colorCoder;

    private String level;
    private OnFragmentInteractionListener mListener;

    public CommandsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_commands, container, false);

        inputList = new ArrayList<>();
        imageList = new ArrayList<>();
        colorCoder = new ColorCoder();
        SharedPrefManager.write(getActivity(), inputList, getString(R.string.inputList_key));
        SharedPrefManager.write(getActivity(), 0, getString(R.string.selected_position_key));
        parser = new Parser();
        parser.setInput(inputList);

        gridView = (GridView) view.findViewById(R.id.imageGridview);
        adapter = new ImageAdapter(this.getActivity(), imageList, colorCoder);
        gridView.setAdapter(adapter);

        startCommands(view);

        deleteLastCommand(view);

        toCamera(view);

        selectPosition();

        return view;
    }

    private void toCamera(View view) {
        FloatingActionButton cameraBtn = (FloatingActionButton) view.findViewById(R.id.cameraFABtn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                startActivity(intent);
            }
        });
    }

    /*
    selects Position of Item after which new Items are to be inserted
    returns position for insertion
     */
    private void selectPosition(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // if (selectedPosition==position+1) inputList.size();
                colorCoder.setSelectedPosition(position+1);
                updateGridView();
            }
        });
    }

    private void deleteLastCommand(View view) {
        Button wrongBtn = (Button) view.findViewById(R.id.wrongBtn);
        wrongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputList.size() <= 0) {
                    return;
                }

                if(colorCoder.getSelectedPosition()>0){
                    colorCoder.decreaseSelectedPosition();
                }
                inputList.remove(colorCoder.getSelectedPosition());

                updateParser();
                updateGridView();
            }
        });
    }

    private void updateParser(){
        parser.setInput(inputList);
        String result = parser.checkParser();
        TextView textView = getView().findViewById(R.id.parserTextView);
        textView.setText(result);
    }

    private void updateGridView() {
        imageList.clear();
        imageList.addAll(InputToImg.parseList(inputList));
        colorCoder.generateColorCode(parser.getIndexPairs());
        adapter.notifyDataSetChanged();
        gridView.setAdapter(adapter);
    }



    private void startCommands(View view) {
        Button libgdxBtn = (Button) view.findViewById(R.id.libgdxBtn);
        libgdxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AndroidLauncher.class);
                parser.setInput(inputList);
                String pa = parser.parse();
                if(pa.equals("Die Eingabe ist ausführbar")) {
                    ArrayList<Command> commands = parser.getCommands();
                    intent.putExtra("commands", commands);
                    intent.putExtra("level", level);
                    //why do we need to save numberOfCommands (implicitly known bc of commands length): imputList.size() >= commands.size (controll structures!)
                    intent.putExtra("numberOfCommands", inputList.size());
                    startActivityForResult(intent, 1);
                } else {
                    TextView textView = getView().findViewById(R.id.parserTextView);
                    textView.setText("Das Programm ist so nicht ausführbar! "+pa);
                }
            }
        });
    }

    public void setLevel(int i) {
        if (i == 11) this.level = "tut1";
        if (i == 12) this.level = "tut2";
        if (i == 13) this.level = "tut3";
        if (i == 14) this.level = "tut4";
        if (i == 15) this.level = "tut5";

        if (i == 21) this.level = "easy1";
        if (i == 22) this.level = "easy2";
        if (i == 23) this.level = "easy3";
        if (i == 24) this.level = "easy4";

        if (i == 31) this.level = "midd1";
        if (i == 32) this.level = "midd2";
        if (i == 33) this.level = "midd3";
        if (i == 34) this.level = "midd4";

        if (i == 41) this.level = "diff1";
        if (i == 42) this.level = "diff2";
        if (i == 43) this.level = "diff3";
        if (i == 44) this.level = "diff4";
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

    @Override
    public void onResume() {
        super.onResume();
        inputList = SharedPrefManager.read(getActivity(), getString(R.string.inputList_key));
        int selectedPosition = SharedPrefManager.readInt(getActivity(), getString(R.string.selected_position_key));
        colorCoder.setSelectedPosition(selectedPosition);
        if (inputList == null) inputList = new ArrayList<>();
        updateParser();
        updateGridView();
    }

    @Override
    public void onPause() {
        SharedPrefManager.write(getActivity(), inputList, getString(R.string.inputList_key));
        SharedPrefManager.write(getActivity(), colorCoder.getSelectedPosition(), getString(R.string.selected_position_key));
        super.onPause();
    }

}


