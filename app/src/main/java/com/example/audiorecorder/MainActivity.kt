package com.example.audiorecorder

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileInputStream
import java.lang.Exception
import java.util.jar.Manifest


class MainActivity : AppCompatActivity()
{
    var total = 0
    val myAudioRecorder = MediaRecorder()
    val audioNames = ArrayList<String>()
    val x = MediaPlayer()


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==1)
        {
            for(i in grantResults.indices)
            {
                if(grantResults[i]==PackageManager.PERMISSION_DENIED)
                {
                    Toast.makeText(this,"Please provide all the permissions",Toast.LENGTH_LONG).show()
                    return
                }
            }

            /**
             * Since till here all the permissions are granted, therefore now we can start to record the audio
             */
            startRecordingAudio()
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * First set the total of all the previously audio recorded
         */
        val sharedPref = getSharedPreferences("myFile",Context.MODE_PRIVATE)
        total = sharedPref.getInt("number",0)
        for(t in 1..total)
        {
            audioNames.add("AUDIO ${t}")
        }
        Log.i("tag",audioNames.size.toString())
        var adapter = myAdapter(audioNames,this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter




        /**
         * Setting up the onClick function of recycler view
         */
        adapter.inter = object : myInterface
        {
            override fun itemClicked(position: Int)
            {
                val externalDir = Environment.getExternalStorageDirectory().absolutePath
                val presentFile = File("${externalDir}/${position}.3gp")
                if(!presentFile.exists())
                {
                    Log.i("tag","this file doesn't exist")
                }
                val temp = FileInputStream("${externalDir}/${position}.3gp")
                try {
                    x.setDataSource("${externalDir}/${position}.3gp")
                    x.prepare()
                }catch (e : Exception) {
                    Log.i("error1",e.message)
                }
                x.start()
            }
        }


        recordButton.setOnClickListener {

            /**
             * First check for all the permissions, then only proceed forward
             */
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.RECORD_AUDIO,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),1)

            }
            else
            {
                //Now we can start to record audio
                startRecordingAudio()
            }
        }


        stopRecordingAudio.setOnClickListener {
            myAudioRecorder.stop()
            myAudioRecorder.release()
            stopRecordingAudio.isEnabled = false
            recordButton.isEnabled = true

            total++
            sharedPref.edit {
                putInt("number",total)
            }
            audioNames.add("AUDIO ${total}")
            adapter.notifyDataSetChanged()
        }
    }

    fun startRecordingAudio()
    {

        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_WB)
        val externalDir = Environment.getExternalStorageDirectory().absolutePath
        val presentFile = File("${externalDir}/${total}.3gp")
        presentFile.createNewFile()
        myAudioRecorder.setOutputFile(presentFile)
        stopRecordingAudio.isEnabled = true
        recordButton.isEnabled = false
        try {
            myAudioRecorder.prepare()
            myAudioRecorder.start()
        }catch (e : Exception){
            Log.i("error",e.printStackTrace().toString())
            Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
            stopRecordingAudio.isEnabled = false
            recordButton.isEnabled = true
        }
    }
}
