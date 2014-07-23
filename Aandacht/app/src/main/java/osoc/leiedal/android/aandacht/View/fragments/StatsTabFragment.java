package osoc.leiedal.android.aandacht.View.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import osoc.leiedal.android.aandacht.R;


public class StatsTabFragment extends Fragment{

    private final static String ARG_POSITION = "arg_position";

    // ------------------------------

    private OnFragmentInteractionListener mListener;

    public static final StatsTabFragment instance (int page ){
        final StatsTabFragment frag = new StatsTabFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_POSITION, page);
        frag.setArguments(args);
        return frag;
    }

    public StatsTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_stats_tab, container, false);

        String[] text = {"5 minutes", "3 hours 2 minutes"}; //TODO get stats from backend

        //((ImageView) v.findViewById(R.id.avg_image)).setImageDrawable(getResources().getDrawable(R.drawable.icon_average_response_time));
        ((TextView) v.findViewById(R.id.avg_text)).setText(text[0]);

        //((ImageView) v.findViewById(R.id.total_image)).setImageDrawable(getResources().getDrawable(R.drawable.icon_total_time_active));
        ((TextView) v.findViewById(R.id.total_text)).setText(text[1]);

        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // ------------------------------

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
        public void onFragmentInteraction(Uri uri);
    }

}
