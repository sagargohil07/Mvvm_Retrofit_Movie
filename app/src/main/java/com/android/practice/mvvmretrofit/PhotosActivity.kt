package com.android.practice.mvvmretrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.practice.mvvmretrofit.databinding.ActivityArtistPostBinding
import com.android.practice.mvvmretrofit.databinding.ActivityPhotosBinding
import com.android.practice.mvvmretrofit.photo.adapter.PhotosAdapter
import com.android.practice.mvvmretrofit.photo.model.PhotoModel


class PhotosActivity : AppCompatActivity() {

    private var photoslist : ArrayList<PhotoModel> = ArrayList<PhotoModel>()
    private lateinit var binding : ActivityPhotosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        photoslist.add(PhotoModel("https://i.pinimg.com/originals/54/27/47/542747347e08aadb04e03e666d984f2c.png"))
        photoslist.add(PhotoModel("https://wallpapercave.com/wp/wp9713789.jpg"))
        photoslist.add(PhotoModel("https://wallpaperaccess.com/full/288476.jpg"))
        photoslist.add(PhotoModel("https://e0.pxfuel.com/wallpapers/944/150/desktop-wallpaper-avengers-mobile-avengers-phone.jpg"))
        photoslist.add(PhotoModel("https://wallpapers.com/images/featured/mghdp4gaqzu4q4us.jpg"))
        photoslist.add(PhotoModel("https://cdn.wallpapersafari.com/56/34/NG1oO0.jpg"))
        photoslist.add(PhotoModel("https://cdn.wallpapersafari.com/5/86/X9UB7n.jpg"))
        photoslist.add(PhotoModel("https://wallpapercave.com/wp/wp6385339.jpg"))
        photoslist.add(PhotoModel("https://i.pinimg.com/736x/ab/26/2f/ab262f326686e240a18a66efc665e6e8.jpg"))
        photoslist.add(PhotoModel("https://e0.pxfuel.com/wallpapers/817/859/desktop-wallpaper-iron-man-iphone-best-iron-man-iphone-and-on-chat-11-iron-man.jpg"))
        photoslist.add(PhotoModel("https://images5.alphacoders.com/838/838300.jpg"))
        photoslist.add(PhotoModel("https://i.pinimg.com/originals/d6/cc/ac/d6ccac78f4e85206ff2bc3ca782ed512.jpg"))
        photoslist.add(PhotoModel("https://4kwallpapers.com/images/wallpapers/iron-man-avengers-infinity-war-black-background-5k-8k-1440x2560-463.jpg"))
        photoslist.add(PhotoModel("https://images.hdqwalls.com/download/iron-man-hd-2018-bn-1366x768.jpg"))


        val staggeredGridLayoutManager  = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
        binding.rvPhotos.layoutManager = staggeredGridLayoutManager
        binding.rvPhotos.adapter = PhotosAdapter(this ,photoslist)

    }


}

