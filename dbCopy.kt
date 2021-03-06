package org.techtown.testrecyclerview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.card_layout.*
import kotlinx.android.synthetic.main.search_bar.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.techtown.testrecyclerview.result.CameraResult
import org.techtown.testrecyclerview.tutorial.CurrentWeight
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private val DB_PATH = "/data/data/org.techtown.testrecyclerview/databases/"
    private val DB_NAME = "food_nutri.db"

    private fun copyDataBaseFromAssets(context: Context) {

        var myInput: InputStream? = null
        var myOutput: OutputStream? = null
        try {

            val folder = context.getDatabasePath("databases")

            if (!folder.exists()) {
                if (folder.mkdirs()) folder.delete()
            }

            myInput = context.assets.open("$DB_NAME")
            val outFileName = DB_PATH + DB_NAME
            Log.e("Log1", outFileName)
            val f = File(outFileName)
            if (f.exists()){
                Log.e("Log1", "Log ----- ?????? COPY ??????")
                return
            }
            myOutput = FileOutputStream(outFileName)

            //transfer bytes from the inputfile to the outputfile
            val buffer = ByteArray(1024)
            var length: Int = myInput.read(buffer)

            while (length > 0) {
                myOutput!!.write(buffer, 0, length)
                length = myInput.read(buffer)
            }
            //Close the streams
            myOutput!!.flush()
            myOutput.close()
            myInput.close()


        } catch (e: IOException) {
            e.printStackTrace()
        }
        Log.e("Log1", "Log ----- ?????? DB COPY ??????")
    }

    val REQUEST_IMAGE_CAPTURE = 1 //????????? ???????????? ????????????
    lateinit var curPhotoPath: String //????????? ????????? ?????? ?????? ???(???????????? null??? ???????????? ?????? ???)

    init {
        instance = this
    }
    companion object {
        var instance: MainActivity? = null

        fun gContext() : Context {
            return instance!!.applicationContext
        }
    }
    private val fl: FrameLayout by lazy {
        findViewById(R.id.main_frame)
    }
    private val bn: BottomNavigationView by lazy{
        findViewById(R.id.bottomNavigationView)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        copyDataBaseFromAssets(this)


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setPermission()// ????????? ???????????? ????????? ??????
        supportFragmentManager.beginTransaction().add(fl.id,FragmentOne()).commit()
        supportActionBar!!.hide()

        bn.setOnNavigationItemSelectedListener {
            replaceFragment(
                when (it.itemId) {
                    R.id.home -> FragmentOne()
                    else -> FragmentTwo()

                }
            )
            true
        }
//
//        FileUploadUtils().receiveFromServer() // ?????? ????????? ?????? ?????? ??????
        val preferences = getSharedPreferences("a", MODE_PRIVATE)
        var editor = preferences.edit()
        var firstViewShow : Boolean = preferences.getBoolean("First", false)

        if (!firstViewShow) {
            editor.putBoolean("First",true).apply()
            var firstIntent = Intent(applicationContext,CurrentWeight::class.java)
            startActivity(firstIntent)
        }

        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(1).isEnabled = false

        fab.setOnClickListener {
            takeCapture()
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(fl.id, fragment).commit()
    }


    fun takeCapture() { //????????? ??????
        // ?????? ????????? ??? ??????
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photofile: File? =try {
                    createImageFile()
                } catch(ex: IOException) {
                    null
                }
                photofile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "org.techtown.testrecyclerview.fileprovider", //?????? ??????
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    public fun createImageFile(): File { // ??????????????? ??????
        val timeStamp: String = SimpleDateFormat("yyyy-MM-dd-HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_",".jpg",storageDir)
            .apply { curPhotoPath = absolutePath }

    }

    /*
    ?????? ????????? ??????
     */
    public fun setPermission() {
        val permission = object : PermissionListener {
            override fun onPermissionGranted() { // ??????????????? ?????? ?????? ?????? ????????? ??????
                Toast.makeText(this@MainActivity, "????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) { // ????????? ?????? ???????????? ????????? ??????
                Toast.makeText(this@MainActivity, "????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show()
            }
        }
        TedPermission.with(this)
            .setPermissionListener(permission)
            .setRationaleMessage("?????????????????? ?????????????????? ????????? ??????????????????.")
            .setDeniedMessage("????????? ?????????????????????. [??? ??????] -> [??????] ???????????? ??????????????????.")
            .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA)
            .check()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { //startActivityForResult??? ????????? ?????? ????????? ????????? ?????? ????????? ?????? ?????????
        super.onActivityResult(requestCode, resultCode, data)
        //????????? ??????????????? ?????? ??? ??????
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            //val ivPhoto : ImageView = findViewById(R.id.ivPhoto)
            val bitmap: Bitmap
            val file = File(curPhotoPath) // ?????? ????????? ????????? ????????? ???
            if (Build.VERSION.SDK_INT < 28) { // ???????????????9.0(PIE) ???????????? ?????? ??????
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))
                // ????????? ???????????? ??????
            } else { //PIE?????? ????????? ??????
                val decode = ImageDecoder.createSource( //????????? ?????? ?????????
                    this.contentResolver,
                    Uri.fromFile(file)
                )
                bitmap = ImageDecoder.decodeBitmap(decode)
                //ivPhoto.setImageBitmap(bitmap)
            }
            savePhoto(bitmap)
        }

    }

//    @Multipart
//    @POST("/orders{order_idx}/file")
//        fun sendFIle(
//        @Path("order_idx") orderIdx: Int,
//        @Part file: MultipartBody.Part
//        ):Call<FileResponse>{
//        }

    fun savePhoto(bitmap: Bitmap) {
        val folderPath = Environment.getExternalStorageDirectory().absolutePath + "/Pictures/" //?????? ????????? ?????? ?????? ??????
        val timeStamp: String = SimpleDateFormat("yyyy-MM-dd-HHmmss").format(Date())
        val fileName = "${timeStamp}.jpeg"
        val folder = File(folderPath)
        if (!folder.isDirectory) { // ?????? ?????? ????????? ????????? ???????????? ????????????
            folder.mkdirs()
        }
        //???????????? ????????????
        val out = FileOutputStream(folderPath + fileName)
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,out)
        Toast.makeText(this, "????????? ????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
        Toast.makeText(this, folderPath, Toast.LENGTH_SHORT).show()
        val file = File("/storage/emulated/0/Pictures/${fileName}")
        FileUploadUtils().send2Server(file)
        var cameraIntent = Intent(applicationContext, CameraResult::class.java)
        startActivity(cameraIntent)
    }


    class MyAdapter(context: Context): RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

        var titles = arrayOf("one", "two", "three", "four", "five")
        var details = arrayOf("Item one", "Item two", "Item three", "Item four", "Itme five")
        var images = intArrayOf(R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground)

        class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
            var itemimage: ImageView = itemview.findViewById(R.id.item_image)
            var itemtitle: TextView = itemview.findViewById(R.id.item_title)
            var itemdetail: TextView = itemview.findViewById(R.id.item_detail)
            var cameraIb: ImageButton = itemview.findViewById(R.id.cameraIb)
            var cardTanTv: TextView = itemview.findViewById(R.id.cardTanTv)
            var cardDanTv: TextView = itemview.findViewById(R.id.cardDanTv)
            var cardJiTv: TextView = itemview.findViewById(R.id.cardJiTv)
        }

        override fun onCreateViewHolder(viewgroup: ViewGroup, position: Int): MyViewHolder {
            var v: View = LayoutInflater.from(viewgroup.context).inflate(R.layout.card_layout, viewgroup, false)

            return MyViewHolder(v)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.itemtitle.setText(titles.get(position))
            holder.itemimage.setImageResource(images.get(position))
            holder.itemdetail.setText(details.get(position))
            holder.itemView.setOnClickListener {
                itemClickListner.onClick(it,position)
            }
            holder.cameraIb.setOnClickListener {
                var activity : MainActivity = instance!!
                activity.takeCapture()
            }
//        holder.cardTanTv.setText()
//        holder.cardDanTv.setText()
//        holder.cardJiTv.setText()

        }

        override fun getItemCount(): Int {
            return titles.size
        }
        interface OnItemClickListner {
            fun onClick(v:View, position: Int)
        }
        private lateinit var itemClickListner: OnItemClickListner

        fun setItemClickListner(itemClickListner: OnItemClickListner) {
            this.itemClickListner = itemClickListner
        }
    }
}