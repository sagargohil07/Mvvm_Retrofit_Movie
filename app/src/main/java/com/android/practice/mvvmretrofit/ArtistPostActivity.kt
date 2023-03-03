package com.android.practice.mvvmretrofit

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings.Global
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.practice.mvvmretrofit.artistpost.adapter.SongAdapter
import com.android.practice.mvvmretrofit.artistpost.api.OnItemSelectListener
import com.android.practice.mvvmretrofit.artistpost.model.ArtistPost
import com.android.practice.mvvmretrofit.artistpost.model.Songs
import com.android.practice.mvvmretrofit.databinding.ActivityArtistPostBinding
import com.android.practice.mvvmretrofit.databinding.DialogDownloadProgressBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class ArtistPostActivity : AppCompatActivity() , OnItemSelectListener {

    private lateinit var binding : ActivityArtistPostBinding
    private lateinit var resourceReader : InputStream
    private var json : String = ""
    private lateinit var jsonObj : JSONObject
    private lateinit var artistPosJsontObj : JSONObject
    private lateinit var songsJsonArray : JSONArray
    private var songArrayList: ArrayList<Songs> = ArrayList<Songs>()
    private lateinit var artistPostModel : ArtistPost
    private var isShuffle : Boolean = false
    private var isRepeat : Boolean = false

    private var curr_index : Int = 0

    var medialPlayer : MediaPlayer = MediaPlayer()
    private  var musicUrl: String = ""

    private lateinit var dialog : Dialog
    private lateinit var dialogBinding : DialogDownloadProgressBinding
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtistPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        jsonObj = JSONObject(getJsonFromResource())
        artistPosJsontObj = jsonObj.get("artist_post") as JSONObject
        songsJsonArray = artistPosJsontObj.get("songs") as JSONArray

        artistPostModel = ArtistPost(
            artistPosJsontObj.getString("artist_thumb"),
            artistPosJsontObj.getString("artist_name"),
            artistPosJsontObj.getString("post_date"),
            artistPosJsontObj.getString("post_thumb"),
            songArrayList
        )

        Log.d("json", "onCreate:jsonObj $jsonObj")
        Log.d("json", "onCreate:artistPosJsontObj $artistPosJsontObj")
        Log.d("json", "onCreate:songsJsonArray $songsJsonArray")

        for ( i in 0 until songsJsonArray.length() ){
            val temp : JSONObject = songsJsonArray[i] as JSONObject
            songArrayList.add(Songs(
                temp.getString("song_thumb"),
                temp.getString("song_name"),
                temp.getString("song_likes"),
                temp.getString("song_comments"),
                temp.getString("song_url"),
                temp.getString("song_track_timeline")
            ))
        }

        Log.d("array", "onCreate: $songArrayList")

        initView()
        onItemSelected(curr_index)

    }

    private fun initView(){
        Glide.with(this).load(R.drawable.image).apply(RequestOptions.circleCropTransform()).into(binding.ivArtistThumb)
        binding.tvArtistName.text = artistPostModel.artist_name
        binding.tvPostDate.text = artistPostModel.post_date

        binding.rvSongs.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.rvSongs.adapter = SongAdapter(this,songArrayList,this)

        //setting media player type streaming music with help of audio-manger class
        medialPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

        dialog = Dialog(this)
        dialogBinding = DialogDownloadProgressBinding.inflate(layoutInflater)

        initListener()

    }

    @SuppressLint("Range")
    private fun initListener(){

        medialPlayer.setOnCompletionListener {
            binding.btnPlay.setImageResource(R.drawable.play)
            if (isShuffle){
                playnext()
            }else if (isRepeat){
                medialPlayer.start()
            }

        }

        binding.btnShuffle.setOnClickListener {
            if (isShuffle){
                binding.btnShuffle.setColorFilter(Color.rgb(0,0,0),PorterDuff.Mode.SRC_ATOP)//black
                isShuffle = false
            }
            else {
                isShuffle = true
                binding.btnShuffle.setColorFilter(Color.rgb(255,87,34),PorterDuff.Mode.SRC_ATOP)//orange
            }
        }

        binding.btnPrevious.setOnClickListener {
            playPrevious()
        }

        binding.btnPlay.setOnClickListener {
            if (medialPlayer.isPlaying){
                binding.btnPlay.setImageResource(R.drawable.play)
                medialPlayer.pause()
                medialPlayer.stop()
                medialPlayer.prepare()
            }
            else if (!musicUrl.isEmpty()) {
                //start playing
                binding.btnPlay.setImageResource(R.drawable.pause)
                medialPlayer.start()
            }
        }

        binding.btnNext.setOnClickListener {
            playnext()
        }

        binding.btnRepeat.setOnClickListener {
            if (isRepeat){
                binding.btnRepeat.setColorFilter(Color.rgb(0,0,0),PorterDuff.Mode.SRC_ATOP)//black
                isRepeat = false
            }
            else {
                isRepeat = true
                binding.btnRepeat.setColorFilter(Color.rgb(255,87,34),PorterDuff.Mode.SRC_ATOP)//orange
            }
        }

        binding.ivDownload.setOnClickListener {

            val uri : Uri = Uri.parse(musicUrl)
            
            val urlList = musicUrl.split("/")

            // setting download manager variable
            val downloadmanger  = applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            //download manager configurations for downloading file
            val request  = DownloadManager.Request(uri)

            // setting path for external write in public directory with file name
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,urlList[urlList.size-1])

            //setting download complete notification
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setTitle("${urlList[urlList.size - 1]} Download Completed")

            showDownloaDialog(urlList[urlList.size-1])

            //starting download
            val downloadID =  downloadmanger.enqueue(request)

            var isDownloadFinished = false
            var progress : Int = 0

            while (!isDownloadFinished){

                val cursor : Cursor  = downloadmanger.query(DownloadManager.Query().setFilterById(downloadID))
                if (cursor.moveToFirst()){
                    val status : Int =  cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

                    when(status){

                        DownloadManager.STATUS_FAILED -> isDownloadFinished=true

                        DownloadManager.STATUS_RUNNING -> {
                            val total : Long = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                            if (total>=0){
                                val downloaded : Long  =cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                                progress = ((downloaded/100L)/total).toInt()
                            }
                        }

                        DownloadManager.STATUS_SUCCESSFUL->{
                            progress = 100
                            isDownloadFinished = true

                        }
                    }
                }
                dialogBinding.pbDownload.progress = progress
                Log.d("download", "initListener: Progress : $progress")

            }

        }

    }

    private fun showDownloaDialog(fileName : String){
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(1000,500)

        dialog.setContentView(dialogBinding.root)


        //progressBar = dialogBinding.pbDownload
        dialog.show()

        dialogBinding.tvFileName.text = fileName
        dialogBinding.btnOk.setOnClickListener {
            dialog.dismiss()
        }

    }

    private fun playnext(){
        Log.d("next", "btnNext: songArrayList.size ${songArrayList.size}")
        if (curr_index<songArrayList.size-1){
            curr_index++
            Log.d("next", "btnNext: curr_index1 $curr_index")
            Log.d("next", "btnNext: isShuffle $isShuffle")
            Log.d("next", "==================================")
            onItemSelected(curr_index)
            binding.btnPlay.setImageResource(R.drawable.pause)
            medialPlayer.start()
        }else if(isShuffle){
            curr_index=-1
            playnext()
        }
    }

    private fun playPrevious(){
        if (curr_index!=0){
            curr_index--
            onItemSelected(curr_index)
            binding.btnPlay.setImageResource(R.drawable.pause)
            medialPlayer.prepare()
            medialPlayer.start()

        }
    }

    private fun getJsonFromResource() : String{
        try {
            resourceReader = applicationContext.resources.openRawResource(R.raw.song)
            val reader = BufferedReader(InputStreamReader(resourceReader, "UTF-8"))

            reader.forEachLine {
                Log.d("json", "getJsonFromResource: $it")
                json+=it
            }

        }catch (e : java.lang.Exception){
            Log.d("json", "getJsonFromResource: ${e.message}")
        }finally {
            resourceReader.close()
            Log.d("json", "getJsonFromResource:String :  $json")
        }
        return json
    }

    override fun onItemSelected(index: Int) {
        Glide.with(this).load(songArrayList[index].song_thumb).apply(RequestOptions.circleCropTransform()).into(binding.ivPostThumb)
        binding.tvLikes.text = songArrayList[index].song_likes
        binding.tvComments.text = songArrayList[index].song_comments
        binding.tvTrackTimeLine.text = songArrayList[index].song_track_timeline
        //Toast.makeText(this, "media player Url : ${songArrayList[index].song_url}", Toast.LENGTH_SHORT).show()
        musicUrl = songArrayList[index].song_url
        curr_index=index

        Log.d("next", "onItemSelected: curr_index $index")
        Log.d("next", "onItemSelected: songArrayList.size ${songArrayList.size}")

        if(medialPlayer.isPlaying){
            medialPlayer.stop()
        }
        medialPlayer.reset()

        //setting url data to media player
        medialPlayer.setDataSource(musicUrl)
        //ready for playback
        medialPlayer.prepare()
    }

    override fun onDestroy() {
        super.onDestroy()
        medialPlayer.release()
    }

}
