package com.android.practice.mvvmretrofit.workManger

import android.Manifest.permission.POST_NOTIFICATIONS
import android.R
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.practice.mvvmretrofit.databinding.ActivityWorkManagerBinding
import java.time.LocalTime
import java.util.*


private lateinit var binding: ActivityWorkManagerBinding

    private var mDay: String = ""
    private var mMonth: String = ""
    private var mYear: String = ""
    private var selectedDate: Int = 0
    private var currentDate: Int = 0

    private var selectedHour: Int = 0
    private var selectedMinute: Int = 0
    private var isDateValid = false
    private var isTimeValid = false
    private lateinit var alarmManager : AlarmManager

class WorkManagerActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        binding.etStart.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val datetimepickerListener =
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

                    Log.d(
                        "date",
                        "onCreate: DatePickerDialog.OnDateSetListener " + "\n" + " Day $dayOfMonth \n" + " Month : $month \n" + " Year : $year"
                    )

                    mDay = dayOfMonth.toString()
                    mMonth = (month).toString()
                    mYear = year.toString()
                    selectedDate = (mDay + mMonth + mYear).toInt()
                    binding.etStart.setText("$mDay / $mMonth /$mYear")
                    isDateValid = true
                }

            val datePickerDialog: DatePickerDialog = DatePickerDialog(
                this,
                datetimepickerListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.datePicker.minDate = calendar.timeInMillis

            datePickerDialog.show()
        }

        binding.etTime.setOnClickListener {
            val hour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val minute: Int = Calendar.getInstance().get(Calendar.MINUTE)
            val clTime: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                "${LocalTime.now().hour}${LocalTime.now().minute}"
            } else {
                TODO("VERSION.SDK_INT < O")
            }

            val timePickerDialog: TimePickerDialog = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    selectedHour = hourOfDay
                    selectedMinute = minute
                    val selectedTime = "$selectedHour$selectedMinute"
                    binding.etTime.setText("$selectedHour : $selectedMinute")
                    isTimeValid = true
                },
                hour,
                minute,
                false
            )

            timePickerDialog.show()
        }

        binding.btnDaysUp.setOnClickListener {
            if (binding.etDays.text.toString().isNullOrEmpty()) {
                binding.etDays.setText("0")
            }
            var count = binding.etDays.text.toString().toInt()
            count++
            binding.etDays.setText(count.toString())
        }

        binding.btnDaysDown.setOnClickListener {
            if (binding.etDays.text.toString().isNullOrEmpty()) {
                binding.etDays.setText("1")
            }
            var count = binding.etDays.text.toString().toInt()
            count--
            binding.etDays.setText(count.toString())

            if (binding.etDays.text.toString().toInt() < 1) {
                binding.etDays.setText("1")
            }
        }

        binding.btnSubmit.setOnClickListener {
            if (checkNotificationPermission()){
                if (checkSetAlarmPermission()){
                    if (isDateValid and isTimeValid) {
                        val count: Int = binding.etDays.text.toString().toInt()
                        setAlarm(mDay.toInt(), mMonth.toInt(), mYear.toInt(), selectedHour, selectedMinute, count)
                    } else {
                        Toast.makeText(this, "Please enter date & time", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnChangeRingtone.setOnClickListener {

            val intent = Intent()
            intent.type = "audio/*"
            intent.action = Intent.ACTION_OPEN_DOCUMENT
//            //startActivityForResult(Intent.createChooser(intent,"Select Audio"),1)

            val getAudioFile = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ){

            }

            getAudioFile.launch(intent)



        }

    }

    private fun checkNotificationPermission() : Boolean{
        if(ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            return true
        }else{
            Log.d("permission", "checkSelfPermission: Not Granted")
            ActivityCompat.requestPermissions(this, arrayOf(POST_NOTIFICATIONS),1)
            return false


        }

    }

    private fun checkSetAlarmPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()){
            return true
            }else{
                Toast.makeText(this, "Please Allowed Alarm & Reminder", Toast.LENGTH_SHORT).show()
                val intent = Intent()
                intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                startActivity(intent)
                return false
            }
        }else{
            return true
        }
    }

    private fun setAlarm(day: Int, month: Int, year: Int, hour: Int, minute: Int, daysCount: Int) {

        Log.d(
            "date",
            "setAlarm: \n Day $day \n Month : $month \n Year : $year \n Hour : $hour \n Minute : $minute"
        )

        for (i in 0 until daysCount) {

            val intent = Intent(this, AlarmBroadCastReceiver::class.java)
            intent.putExtra("id", "${day + i}")
            val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getBroadcast(
                    this,
                    i,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            } else {
                PendingIntent.getBroadcast(this, i, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }

            val calendar = Calendar.getInstance()
            //for date
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day + i)

            //for time
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

            Log.d("alarm", "setAlarm: Alarms is Setted for Date : ${day + i}/$month/$year $hour:$minute")

        }
            Toast.makeText(this, "Alarms Set for Date : ${day}/$month/$year $hour:$minute", Toast.LENGTH_SHORT).show()

    }


}

