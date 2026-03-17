class MainActivity : AppCompatActivity() {

    private lateinit var knob: CircularKnobView
    private lateinit var visualizer: Visualizer
    private lateinit var recyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        knob = findViewById(R.id.play_dial)
        recyclerView = findViewById(R.id.track_list)

        trackAdapter = TrackAdapter(getTracks())
        recyclerView.adapter = trackAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        initVisualizer()
        initKnob()
    }

    private fun initKnob() {
        knob.setOnRotationChangedListener { angle ->
            // map angle to volume or playback
        }
        knob.setOnTouchListener { v, event ->
            v.scaleX = 1.1f
            v.scaleY = 1.1f
            v.invalidate()
            false
        }
    }

    private fun initVisualizer() {
        val audioSessionId = 0 // replace with actual MediaPlayer session
        visualizer = Visualizer(audioSessionId).apply {
            captureSize = Visualizer.getCaptureSizeRange()[1]
            setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
                override fun onWaveFormDataCapture(
                    visualizer: Visualizer?, waveform: ByteArray?, samplingRate: Int
                ) { /* update bars */ }

                override fun onFftDataCapture(
                    visualizer: Visualizer?, fft: ByteArray?, samplingRate: Int
                ) { /* update bars */ }
            }, Visualizer.getMaxCaptureRate() / 2, true, true)
            enabled = true
        }
    }
}
