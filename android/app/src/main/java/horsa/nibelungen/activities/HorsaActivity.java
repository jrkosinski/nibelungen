package horsa.nibelungen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import horsa.nibelungen.R;

public abstract class HorsaActivity extends AppCompatActivity
{
    private ProgressBar progressSpinner;

    protected abstract int getLayoutResId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(getLayoutResId());
        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        this.progressSpinner = (ProgressBar)findViewById(R.id.progressSpinner);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }


    protected void directToMain()
    {
        // finish();
    }

    protected void showSpinner()
    {
        try {
            this.progressSpinner.setVisibility(View.VISIBLE);
        }
        catch (Exception e)
        {
            //TODO: log error
        }
    }

    protected void dismissSpinner()
    {
        try {
            this.progressSpinner.setVisibility(View.GONE);
        }
        catch (Exception e) {
            //TODO: log error
        }
    }
}
