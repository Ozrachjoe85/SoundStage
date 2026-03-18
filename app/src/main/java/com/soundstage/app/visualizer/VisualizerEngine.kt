import android.media.audiofx.Visualizer
fun startWaveform(onData: (FloatArray) -> Unit) {
    visualizer.setDataCaptureListener(
        object : Visualizer.OnDataCaptureListener {
            override fun onWaveFormDataCapture(v: Visualizer?, data: ByteArray?, rate: Int) {
                data?.let {
                    val floatData = it.map { b -> b / 128f }.toFloatArray()
                    onData(floatData)
                }
            }

            override fun onFftDataCapture(v: Visualizer?, data: ByteArray?, rate: Int) {}
        },
        Visualizer.getMaxCaptureRate() / 2,
        true,
        false
    )
    visualizer.enabled = true
}
