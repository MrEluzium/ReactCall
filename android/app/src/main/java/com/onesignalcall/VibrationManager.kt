import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

class VibrationManager(private val context: Context) {

    private var vibrator: Vibrator? = null
    private var isVibrating = false

    init {
        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
    }

    fun startVibration(pattern: LongArray, repeat: Int = -1) {
        if (vibrator != null && !isVibrating) {
            isVibrating = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val vibrationEffect = VibrationEffect.createWaveform(pattern, repeat)
                vibrator?.vibrate(vibrationEffect)
            } else {
                // Deprecated in API 26
                @Suppress("DEPRECATION")
                vibrator?.vibrate(pattern, repeat)
            }
        }
    }

    fun stopVibration() {
        if (vibrator != null && isVibrating) {
            isVibrating = false
            vibrator?.cancel()
        }
    }
}