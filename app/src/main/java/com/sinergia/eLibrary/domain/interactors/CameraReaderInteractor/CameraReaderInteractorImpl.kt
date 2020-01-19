package com.sinergia.eLibrary.domain.interactors.CameraReaderInteractor

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions

class CameraReaderInteractorImpl: CameraReaderInteractor {

    val options = FirebaseVisionBarcodeDetectorOptions
                    .Builder()
                    .setBarcodeFormats(
                        FirebaseVisionBarcode.FORMAT_EAN_13,
                        FirebaseVisionBarcode.FORMAT_EAN_8,
                        FirebaseVisionBarcode.FORMAT_QR_CODE)
                    .build()



}