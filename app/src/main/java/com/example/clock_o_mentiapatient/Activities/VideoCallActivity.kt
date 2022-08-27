package com.example.clock_o_mentiapatient.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.clock_o_mentiapatient.R
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.log.JitsiMeetLogger
import java.net.MalformedURLException
import java.net.URL

class VideoCallActivity : JitsiMeetActivity() {

    override fun onConferenceTerminated(extraData: HashMap<String?, Any?>) {
//        super.onConferenceTerminated(extraData);
        JitsiMeetLogger.i("Conference terminated: $extraData", *arrayOfNulls(0))
        Log.e("workingFine", "conference terminated")
        startActivity(Intent(this, NearbyDoctorsActivity::class.java))

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_call)

        val intent = intent
        val meetingId = intent.getStringExtra(getString(R.string.meetingId))

        try {
            val options = JitsiMeetConferenceOptions.Builder()
                .setServerURL(URL("https://meet.jit.si"))
                .setRoom(meetingId)
                .setAudioMuted(false)
                .setVideoMuted(false)
                .setAudioOnly(false)
                .setConfigOverride("requireDisplayName", true)
                .build()
            launch(this, options)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

    }
}