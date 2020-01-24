package com.sinergia.eLibrary.presentation.CameraScan.View

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class CameraScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private var escanerZXing: ZXingScannerView? = null

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        escanerZXing = ZXingScannerView(this)
        setContentView(escanerZXing)
    }

    public override fun onResume() {
        super.onResume()
        escanerZXing!!.setResultHandler(this)
        escanerZXing!!.startCamera()
    }

    public override fun onPause() {
        super.onPause()
        escanerZXing!!.stopCamera() // Pausar en onPause
    }

    override fun handleResult(resultado: Result) {

        // For continue with scanning uncomment next line and comment finish()
        //        escanerZXing.resumeCameraPreview(this);

        val codigo = resultado.text
        val intentRegreso = Intent()
        intentRegreso.putExtra("codigo", codigo)
        setResult(Activity.RESULT_OK, intentRegreso)
        finish()

    }
}
