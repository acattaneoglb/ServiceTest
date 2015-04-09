package com.globant.cattaneo.ariel.servicetest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements DataServiceAvailableListener {

/*
    private LocalService mDataService;
*/

    public MainFragment() {
    }

/*
    protected LocalService getDataService() {
        if (mDataService == null) {
            mDataService = MainApplication.getInstance().getServiceManager().getDataService();
        }

        return mDataService;
    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final DataServiceAvailableListener listener = this;

        rootView.findViewById(R.id.button_get_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainApplication.getInstance().getServiceManager().requestDataService(listener);
            }
        });

        return rootView;
    }

    @Override
    public void serviceAvailable(LocalService boundService) {
        Event event = boundService.getEvent();
        Toast.makeText(getActivity(), event.getTitle(), Toast.LENGTH_LONG).show();
    }
}
