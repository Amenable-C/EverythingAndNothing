package org.techtown.testrecyclerview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import org.techtown.testrecyclerview.result.CameraResult
import org.techtown.testrecyclerview.tutorial.CurrentWeight
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var dbHelper : DBHelper
    lateinit var database : SQLiteDatabase

    val REQUEST_IMAGE_CAPTURE = 1 //카메라 사진촬영 요청코드
    lateinit var curPhotoPath: String //문자열 형태의 사진 경로 값(초기값을 null로 시작하고 싶을 때)

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
        ///////////// FoodCalculator - START
        dbHelper = DBHelper(this, "food_nutri.db", null, 1)
        database = dbHelper.readableDatabase

        println("현재 몸무게, 목표 몸무게, 키 : ${dbHelper.getColValue(0, "user_info").toInt()}, ${dbHelper.getColValue(1, "user_info").toInt()}, ${dbHelper.getColValue(4, "user_info").toInt()}")
        // 1. 하루 권장 칼로리 계산
        var recommendedKcal : Int = recommendedKcal(dbHelper.getColValue(0, "user_info").toInt(), dbHelper.getColValue(1, "user_info").toInt(), dbHelper.getColValue(4, "user_info").toInt())
        println("하루 권장 칼로리 : ${recommendedKcal}")
        // 2. "하루 권장 칼로리"를 이용하여 "하루 권장 탄수화물, 단백질, 지방" 계산
        var nutrientRate : Triple<Int, Int, Int> = nutrientRate(dbHelper.getColValue(0, "user_info").toInt(), dbHelper.getColValue(1, "user_info").toInt(), recommendedKcal)
        println("하루 권장 탄수화물, 단백질, 지방: ${nutrientRate}")
        // 3. |'하루 권장 탄, 단, 지' - '이미 섭취한 탄, 단, 지'|를 계산 하여 '부족한 탄, 단, 지(%)' 계산 // 임의의 값 넣어놓은 것
        var scarceNutrientList:List<Double> = listOf(rateOfScarceNutrient(100.0, 500.0, 200.0).component1(), rateOfScarceNutrient(100.0, 500.0, 200.0).component2(), rateOfScarceNutrient(100.0, 500.0, 200.0).component3())
        println("비교를 위한 '부족한 영양소 비율(탄, 단, 지)(%)' $scarceNutrientList")

        // *차이값 = '우리가 생각하는 이상적인 음식'과 '현재 존재하는 음식'의 비교값(작을수록 좋음)
        // 4. 'dictionaryDifferenceName' = 차이값과 음식이름 매칭, 'differences' 차이값이 작은 순으로 정렬
//        var dictionaryDifferenceName = addTheDataToList(scarceNutrientList).first // MutableMap<Int, String>
//        var differences = addTheDataToList(scarceNutrientList).second // MutableList<Int>
//        println("차이값 : 음식이름 => ${dictionaryDifferenceName}")
//        println("차이값 오름차순 정렬: ${differences}")
        // differences의 값을 이용해서 '음식 이름'을 가져올 것이기 때문에, differences는 20개면 충분
//        for (i in 0..4) {
//            println("${i+1}순위 : ${dictionaryDifferenceName.get(differences[i])}")
//        }
        // more test
        var name : String
        var cab : Double
        var pro : Double
        var fat : Double
        var differenceAndName = mutableMapOf<Double, String>()
        var differenceList = mutableListOf<Double>()
        // 4251
        for (i in 0..1000) {
//            println(dbHelper.getColValueTest(i, 1, "real_nutri").toString())
//            println(dbHelper.getColValueTest(i, 3, "real_nutri").toDouble())
//            println(dbHelper.getColValueTest(i, 4, "real_nutri").toDouble())
//            println(dbHelper.getColValueTest(i, 5, "real_nutri").toDouble())
            name = dbHelper.getColValueTest(i, 1, "real_nutri").toString()
            cab = dbHelper.getColValueTest(i, 3, "real_nutri").toDouble()
            pro = dbHelper.getColValueTest(i, 4, "real_nutri").toDouble()
            fat = dbHelper.getColValueTest(i, 5, "real_nutri").toDouble()
            var foodNutrientList = listOf(cab, pro, fat, name)
//            println(foodNutrientList)


            differenceAndName.put(comparingForTheBest (scarceNutrientList, foodNutrientList.filterIsInstance<Double>()), foodNutrientList[3].toString())
            differenceList.add(comparingForTheBest (scarceNutrientList, foodNutrientList.filterIsInstance<Double>()))
        }
//        println(differenceAndName)
//        println(differenceList)

                for (i in 0..4) {
            println("${i+1}순위 : ${differenceAndName.get(differenceList[i])}")
        }

//        differenceList.sort() // 차이값 오름차순 정렬
//        var dictionaryDifferenceName = differenceAndName // MutableMap<Int, String>
//        var differences = differenceList // MutableList<Int>
//        println("차이값 : 음식이름 => ${differenceAndName}")
//        println("차이값 오름차순 정렬: ${differenceList[0]}")


        /////////////  FoodCalculator - FINISH

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setPermission()// 권한을 체크하는 메소드 수행
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
//        FileUploadUtils().receiveFromServer() // 받는 부분은 일단 구현 안함
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


    fun takeCapture() { //카메라 촬영
        // 기본 카메라 앱 실행
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
                        "org.techtown.testrecyclerview.fileprovider", //보안 서명
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    public fun createImageFile(): File { // 이미지파일 생성
        val timeStamp: String = SimpleDateFormat("yyyy-MM-dd-HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_",".jpg",storageDir)
            .apply { curPhotoPath = absolutePath }

    }

    /*
    테드 퍼미션 설정
     */
    public fun setPermission() {
        val permission = object : PermissionListener {
            override fun onPermissionGranted() { // 설정해놓은 권한 들이 허용 되었을 경우
                Toast.makeText(this@MainActivity, "권한이 허용 되었습니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) { // 설정해 놓은 권한들을 거부한 경우
                Toast.makeText(this@MainActivity, "권한이 거부 되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        TedPermission.with(this)
            .setPermissionListener(permission)
            .setRationaleMessage("영양피디아를 사용하시려면 권한을 허용해주세요.")
            .setDeniedMessage("권한을 거부하셨습니다. [앱 설정] -> [권한] 항목에서 허용해주세요.")
            .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA)
            .check()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { //startActivityForResult를 통해서 기본 카메라 앱으로 부터 받아온 사진 결과값
        super.onActivityResult(requestCode, resultCode, data)
        //사진을 성공적으로 가져 온 경우
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            //val ivPhoto : ImageView = findViewById(R.id.ivPhoto)
            val bitmap: Bitmap
            val file = File(curPhotoPath) // 절대 경로인 사진이 저장된 값
            if (Build.VERSION.SDK_INT < 28) { // 안드로이드9.0(PIE) 버전보다 낮을 경우
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))
                // 끌어온 비트맵을 넣음
            } else { //PIE버전 이상인 경우
                val decode = ImageDecoder.createSource( //변환을 해서 가져옴
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
        val folderPath = Environment.getExternalStorageDirectory().absolutePath + "/Pictures/" //사진 폴더에 저장 경로 선언
        val timeStamp: String = SimpleDateFormat("yyyy-MM-dd-HHmmss").format(Date())
        val fileName = "${timeStamp}.jpeg"
        val folder = File(folderPath)
        if (!folder.isDirectory) { // 현재 해당 경로에 폴더가 존재하지 않는다면
            folder.mkdirs()
        }
        //실제적인 저장처리
        val out = FileOutputStream(folderPath + fileName)
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,out)
        Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show()
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