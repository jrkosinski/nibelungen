package horsa.nibelungen.ui.camera;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.google.android.gms.common.images.Size;
import com.google.android.gms.vision.CameraSource;

import java.io.IOException;

public class CameraSourcePreview extends ViewGroup {
    private static final String TAG = "CameraSourcePreview";

    private Context _context;
    private SurfaceView _surfaceView;
    private boolean _startRequested;
    private boolean _surfaceAvailable;
    private CameraSource _cameraSource;
    private GraphicOverlay _overlay;

    public CameraSourcePreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        _context = context;
        _startRequested = false;
        _surfaceAvailable = false;

        _surfaceView = new SurfaceView(context);
        _surfaceView.getHolder().addCallback(new SurfaceCallback());
        addView(_surfaceView);
    }

    public SurfaceView getSurface(){
        return _surfaceView;
    }

    public void start(CameraSource cameraSource) throws IOException {
        if (cameraSource == null) {
            stop();
        }

        _cameraSource = cameraSource;

        if (_cameraSource != null) {
            _startRequested = true;
            startIfReady();
        }
    }

    public void start(CameraSource cameraSource, GraphicOverlay overlay) throws IOException {
        _overlay = overlay;
        start(cameraSource);
    }

    public void stop() {
        if (_cameraSource != null) {
            _cameraSource.stop();
        }
    }

    public void release() {
        if (_cameraSource != null) {
            _cameraSource.release();
            _cameraSource = null;
        }
    }

    private void startIfReady() throws IOException {
        if (_startRequested && _surfaceAvailable) {
            _cameraSource.start(_surfaceView.getHolder());
            if (_overlay != null) {
                Size size = _cameraSource.getPreviewSize();
                int min = Math.min(size.getWidth(), size.getHeight());
                int max = Math.max(size.getWidth(), size.getHeight());
                if (isPortraitMode()) {
                    // Swap width and height sizes when in portrait, since it will be rotated by
                    // 90 degrees
                    _overlay.setCameraInfo(min, max, _cameraSource.getCameraFacing());
                } else {
                    _overlay.setCameraInfo(max, min, _cameraSource.getCameraFacing());
                }
                _overlay.clear();
            }
            _startRequested = false;
        }
    }

    private class SurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder surface) {
            _surfaceAvailable = true;
            try {
                startIfReady();
            } catch (IOException e) {
                Log.e(TAG, "Could not start camera source.", e);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surface) {
            _surfaceAvailable = false;
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.e(TAG, "surface changed");
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = 320;
        int height = 240;
        if (_cameraSource != null) {
            Size size = _cameraSource.getPreviewSize();
            if (size != null) {
                width = size.getWidth();
                height = size.getHeight();
            }
        }

        // Swap width and height sizes when in portrait, since it will be rotated 90 degrees
        if (isPortraitMode()) {
            int tmp = width;
            width = height;
            height = tmp;
        }

        final int layoutWidth = right - left;
        final int layoutHeight = bottom - top;

        // Computes height and width for potentially doing fit width.
        int childWidth = layoutWidth;
        int childHeight = (int)(((float) layoutWidth / (float) width) * height);

        // If height is too tall using fit width, does fit height instead.
        if (childHeight > layoutHeight) {
            childHeight = layoutHeight;
            childWidth = (int)(((float) layoutHeight / (float) height) * width);
        }

        for (int i = 0; i < getChildCount(); ++i) {
            getChildAt(i).layout(0, 0, childWidth, childHeight);
        }

        try {
            startIfReady();
        } catch (IOException e) {
            Log.e(TAG, "Could not start camera source.", e);
        }
    }

    private boolean isPortraitMode() {
        int orientation = _context.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return false;
        }
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return true;
        }

        Log.d(TAG, "isPortraitMode returning false by default");
        return false;
    }
}
