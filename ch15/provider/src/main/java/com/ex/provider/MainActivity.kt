package com.ex.provider

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.ex.provider.databinding.ActivityMainBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //gallery request launcher..................
        var requestGallaryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult())
//      메소드가 들어가면 메소드(ActivityResultContracts.StartActivityForResult())의 결과값이
//      다시 requestGallaryLauncher 에 들어간다고 생각.
//      갤러리를 사용할거니까 사용에 대한 요청
        {
            try {
                val calRatio = calculateInSampleSize(
//              calculateInSampleSize() = 사이즈를 계산하는 함수.
                it.data!!.data!!,
//              이미지를 불러왔는데, 선택된 것이 여러 형식(jpg, png 등)이 있으니
//              Bitmap형태로 디코딩해서 이미지뷰로 보여지게 하는 작업
//              코틀린에서는, null값의 안정성을 보장하기 위해서, !!를 붙인다.(null값이 들어올 가능성 제외)
//              it.data!!.data!!, 매개변수형태. registerForActivityResult의 return 값(intent)
//              it.OOO = intent 변수가 될 수 있다.
//              it.data.data
                    resources.getDimensionPixelSize(R.dimen.imgSize),
                    resources.getDimensionPixelSize(R.dimen.imgSize)
//                     resources.getDimensionPixelSize(R.dimen.imgSize), 매개변수(SampleSize 가로길이)
//                     resources.getDimensionPixelSize(R.dimen.imgSize) 매개변수(SampleSize 세로길이)
                )
                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio
//              sampleSize 이미지 축소 길이 조절
                var inputStream = contentResolver.openInputStream(it.data!!.data!!)
//              openInputStream()의 매개변수는 절대 null값 가지지 않는다.
                val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
//              inputStream, null, option를 이용해서(매개변수로) bitmap 이미지 형태로 만들어준다.
                inputStream!!.close()
                inputStream = null
//              딱히 의미 X(inputStream에 null값 못 들어감)
                bitmap?.let{
                    binding.userImageView.setImageBitmap(bitmap)
//              bitmap이 null값이 아니면 가지고 온 이미지를 userImageView에 보여라.
                } ?: let{
                    Log.d("kk", "bitmap null")
//              bitmap이 null값이면 걍 로그만 남겨라.
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.galleryButton.setOnClickListener {
            //gallery app........................
            val intent = Intent(Intent. ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//          외부(app) -> 내부(gallery) 접근.
            intent.type = "image/*"
            requestGallaryLauncher.launch(intent)
        }

        //camera request launcher.................
        val requestCameraLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            val calRatio = calculateInSampleSize(Uri.fromFile(File(filePath)),
//          경로를 가지고 와서
            resources.getDimensionPixelSize(R.dimen.imgSize), resources.getDimensionPixelSize(R.dimen.imgSize))
//          샘플사이즈 가로세로
            val option = BitmapFactory.Options()
            option.inSampleSize = calRatio
            val bitmap = BitmapFactory.decodeFile(filePath, option)
//          사진을 찍고 불러오려면 사진이 어느 경로에 저장되어야 한다.에서 어느 경로를 지정해서 사진 불러오기 위함.
            bitmap?.let{
            binding.userImageView.setImageBitmap(bitmap)
        }
    }

        binding.cameraButton.setOnClickListener {
            //camera app......................
            //파일 준비...............
            val timeStamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//          날자를 사진데이터에 포함시키기 위함. + 휴대폰 시간을 그대로 끌고 오기 때문에 format(Date()) 이용.
            val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//          저장 장소(위치) 체크.
            val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
//          파일 양식 지정.(파일이름, 양식, 어느위치에 저장)
            filePath = file.absolutePath
//          저장하는 위치에 대한 절대장소, 절대경로를 지정.
//          상대경로 - 내 위치에 따라 바뀌는 경로.
            val photoURI : Uri = FileProvider.getUriForFile(this, "com.ex.provider", file)
//          파일에 대한 Uri(주소)를 다른 앱과 공유할 수 있게 해주는 클래스.
//          getUriForFile() 파일 공유가 가능하게 하는 Uri를 생성.
//          this, "com.ex.provider", file - 파일(이미지)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//          이미지 캡쳐할 수 있도록.
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//          사진이 저장된 위치를 지정하는 것. ????
            requestCameraLauncher.launch(intent)
        }
    }

    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
//      가로, 세로 정의.
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try {
            var inputStream = contentResolver.openInputStream(fileUri)

            //inJustDecodeBounds 값을 true 로 설정한 상태에서 decodeXXX() 를 호출.
            //로딩 하고자 하는 이미지의 각종 정보가 options 에 설정 된다.
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream!!.close()
            inputStream = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //비율 계산........................
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1
        //inSampleSize 비율 계산
        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

}