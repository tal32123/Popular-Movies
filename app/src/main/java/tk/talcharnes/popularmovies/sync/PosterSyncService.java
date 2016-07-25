package tk.talcharnes.popularmovies.sync;

/**
 * Created by Tal on 7/24/2016.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Define a Service that returns an IBinder for the
 * sync adapter class, allowing the sync adapter framework to call
 * onPerformSync().
 */
public class PosterSyncService extends Service {
    // Storage for an instance of the sync adapter
    private static PosterSyncAdapter sPosterSyncAdapter = null;
    // Object to use as a thread-safe lock
    private static final Object sSyncAdapterLock = new Object();
    /*
     * Instantiate the sync adapter object.
     */
    @Override
    public void onCreate() {
        /*
         * Create the sync adapter as a singleton.
         * Set the sync adapter as syncable
         * Disallow parallel syncs
         */
        Log.d("PosterSyncService", "onCreate - PosterSyncService");

        synchronized (sSyncAdapterLock) {
            if (sPosterSyncAdapter == null) {
                sPosterSyncAdapter = new PosterSyncAdapter(getApplicationContext(), true);
            }
        }
    }
    /**
     * Return an object that allows the system to invoke
     * the sync adapter.
     *
     */
    @Override
    public IBinder onBind(Intent intent) {
        /*
         * Get the object that allows external processes
         * to call onPerformSync(). The object is created
         * in the base class code when the SyncAdapter
         * constructors call super()
         */
        return sPosterSyncAdapter.getSyncAdapterBinder();
    }
}
